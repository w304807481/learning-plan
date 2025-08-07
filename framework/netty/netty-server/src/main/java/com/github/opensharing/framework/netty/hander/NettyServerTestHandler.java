package com.github.opensharing.framework.netty.hander;

import java.net.SocketAddress;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 请编写类注释
 *
 * @author jwen
 * Date 2023/11/12
 */
@Slf4j
public class NettyServerTestHandler extends ChannelInboundHandlerAdapter {

    // 读取信息调用
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 和NIO一样有缓冲区 ByteBuf就是对ByteBuffer做了一层封装
        ByteBuf msg1 = (ByteBuf) msg;
        log.info("客户端信息：" + msg1.toString(CharsetUtil.UTF_8));
    }

    // 连接事件 连接成功调用
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        SocketAddress socketAddress = ctx.channel().remoteAddress();
        log.info(socketAddress + " 已连接");

        // 发送数据
        ctx.writeAndFlush(Unpooled.copiedBuffer("Hello Client", CharsetUtil.UTF_8));
        //ctx.write(Unpooled.copiedBuffer("Hello Client", CharsetUtil.UTF_8));
    }

    // 断开连接调用
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info(ctx.channel().remoteAddress() + " 已断开连接");
    }

    // 读取信息完成事件  信息读取完成后调用
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
//        if(!ctx.channel().isWritable()){
//            Channel channel = ctx.channel();
//            log.info("channel is unwritable turn off AutoRead");
//            channel.config().setAutoRead(false);
//        }
    }

    // 异常处理  发生异常调用
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // 异常后 关闭与客户端连接
        ctx.close();
    }
}
