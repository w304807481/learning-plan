package com.github.opensharing.framework.netty.hander;

import java.net.SocketAddress;
import java.util.concurrent.atomic.AtomicInteger;

import com.github.opensharing.framework.netty.MessageProtocol;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * MyServerHandler
 *
 * @author jwen
 * Date 2023/11/14
 */
@Slf4j
public class MyServerHandler extends SimpleChannelInboundHandler<MessageProtocol> {
    private final AtomicInteger msgCounter;
    private final AtomicInteger clientCounter;

    public MyServerHandler(AtomicInteger clientCounter, AtomicInteger msgCounter) {
        this.clientCounter = clientCounter;
        this.msgCounter = msgCounter;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageProtocol msg) throws Exception {
        //接收到数据，并处理
        int seq = msgCounter.incrementAndGet();
        int len = msg.getLen();
        byte[] content = msg.getContent();

        log.info("服务器接收到信息[序号={}, 长度={}]",  seq, len);

        //构建一个回复消息协议包
        //MessageProtocol messageProtocol = new MessageProtocol("["+ msgCounter.get() + "]" + UUID.randomUUID().toString());
        MessageProtocol messageProtocol = msg;
        ctx.writeAndFlush(msg);

        log.info("服务端开始回复消息[长度={}]", messageProtocol.getLen());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
        SocketAddress socketAddress = ctx.channel().remoteAddress();
        log.info(socketAddress + " 已关闭");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
        log.info(ctx.channel().remoteAddress() + " 已连接, 目前累计{}", clientCounter.incrementAndGet());
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        super.channelUnregistered(ctx);
        log.info(ctx.channel().remoteAddress() + " 已断开连接, 目前累计{}", clientCounter.decrementAndGet());
    }
}
