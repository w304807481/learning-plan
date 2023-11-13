package com.github.opensharing.framework.netty;

import java.util.concurrent.TimeUnit;

import com.github.opensharing.framework.netty.hander.NettyClientTestHandler;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

/**
 * Netty客户端
 *
 * @author jwen
 * Date 2023/11/12
 */
@Slf4j
public class NettyClient {
    private final EventLoopGroup group = new NioEventLoopGroup();
    private final int port = 5103;
    private final String host = "127.0.0.1";

    public void start() throws InterruptedException {
        try {
            Bootstrap bootstrap = new Bootstrap();
            // 客户端不需要处理连接 所以一个线程组就够了
            bootstrap.group(group)
                    // 连接通道
                    .channel(NioSocketChannel.class)
                    .remoteAddress(host, port)
                    .option(ChannelOption.TCP_NODELAY, true)
                    // 数据处理
                    .handler(new ChannelInitializer<Channel>() {
                        @Override
                        protected void initChannel(Channel channel) throws Exception {
                            channel.pipeline().addLast(new NettyClientTestHandler());
                        }
                    });
            ChannelFuture future = bootstrap.connect();
            //客户端断线重连逻辑
            future.addListener((ChannelFutureListener) future1 -> {
                if (future1.isSuccess()) {
                    log.info("连接Netty服务端成功");
                } else {
                    log.info("连接失败，进行断线重连");
                    future1.channel().eventLoop().schedule(() -> {
                        try {
                            start();
                        } catch (InterruptedException e) {
                            log.error("连接失败", e);
                        }
                    }, 20, TimeUnit.SECONDS);
                }
            });
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            log.info("服务端异常", e);
        } finally {
            group.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        new NettyClient().start();
    }
}
