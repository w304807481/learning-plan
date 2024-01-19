package com.github.opensharing.framework.netty.hander;

import java.util.concurrent.atomic.AtomicInteger;

import com.github.opensharing.framework.netty.MessageProtocol;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * MyClientHandler
 *
 * @author jwen
 * Date 2023/11/14
 */
@Slf4j
public class MyClientHandler extends SimpleChannelInboundHandler<MessageProtocol> {
    private final AtomicInteger msgCounter = new AtomicInteger();

    /**
     * 消息发送间隔时间
     */
    private int msgIntervalTime = 1000;

    /**
     * 停止消息的限制
     */
    private int msgStopLimit = 10;

    public MyClientHandler() {
    }

    public MyClientHandler(int msgIntervalTime, int msgStopLimit) {
        this.msgIntervalTime = msgIntervalTime;
        this.msgStopLimit = msgStopLimit;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        if (!ctx.channel().isActive()) {
            log.info("通道关闭，停止发送（channelActive）");
            return;
        }
        ctx.writeAndFlush(new MessageProtocol("Hello Server0 "));
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageProtocol msg) throws Exception {
        //byte[] buffer = msg.getContent();
        //String message = new String(buffer, Charset.forName("utf-8"));
        int seq = msgCounter.incrementAndGet();

        log.info("客户端接收到消息[序号={}, 长度={}]", seq, msg.getLen());
        if (msgCounter.get() % 10 == 0) {
            log.info("客户端接收到消息[序号={}, 长度={}]", seq, msg.getLen());
        }


        Thread.sleep(msgIntervalTime);

        if (!ctx.channel().isActive()) {
            log.info("通道关闭，停止发送（channelRead0）");
            return;
        }
        //MessageProtocol messageProtocol = new MessageProtocol("Hello Server" + seq + " ");
        MessageProtocol messageProtocol = new MessageProtocol();
        ctx.writeAndFlush(messageProtocol);

        log.info("客户端开始回复消息[长度={}]", messageProtocol.getLen());
        if (msgCounter.get() % 10 == 0) {
            log.info("客户端开始回复消息[长度={}]", messageProtocol.getLen());
        }

        if (msgCounter.get() == msgStopLimit) {
            ctx.channel().close();
            log.info("主动关闭------");
        }
    }

    /**
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
        log.error(ctx.channel().remoteAddress() + " 已断开连接（exceptionCaught）", cause);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        ctx.channel().close();
        log.info(ctx.channel().remoteAddress() + " 已断开连接（channelInactive）");
    }
}