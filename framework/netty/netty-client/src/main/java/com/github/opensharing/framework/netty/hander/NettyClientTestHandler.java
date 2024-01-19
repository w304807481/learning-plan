package com.github.opensharing.framework.netty.hander;

import java.net.SocketAddress;

import com.github.opensharing.framework.netty.MessageProtocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * NettyClientTestHandler
 *
 * @author jwen
 * Date 2023/11/12
 */
@Slf4j
public class NettyClientTestHandler extends ChannelInboundHandlerAdapter {


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf msg1 = (ByteBuf) msg;
        log.info("服务端信息：" + msg1.toString(CharsetUtil.UTF_8));
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 连接上 就给服务端发送数据
        //ctx.writeAndFlush(Unpooled.copiedBuffer("Hello Server", CharsetUtil.UTF_8));

        for (int i = 0; i < 1000000; i++) {
            ctx.write(new MessageProtocol("Hello Server" + i + " "));
            Thread.sleep(1);

            if ((i+1)%10000 == 0) {
                System.out.println("====第" + (i+1) + "次 flush");
                ctx.flush();
            }
        }

        SocketAddress socketAddress = ctx.channel().remoteAddress();
        log.info(socketAddress + " 已连接");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info(ctx.channel().remoteAddress() + " 已断开连接");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.info(ctx.channel().remoteAddress() + " 已断开连接", cause);
        ctx.close();
    }
}
