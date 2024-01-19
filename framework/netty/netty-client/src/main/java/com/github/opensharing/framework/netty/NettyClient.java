package com.github.opensharing.framework.netty;

import java.util.concurrent.TimeUnit;

import com.github.opensharing.framework.netty.hander.MyClientHandler;
import com.github.opensharing.framework.netty.hander.MyMessageDecoder;
import com.github.opensharing.framework.netty.hander.MyMessageEncoder;

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
    /**
     * 消息发送间隔时间
     */
    private int msgIntervalTime = 1000;

    /**
     * 停止消息的限制
     */
    private int msgStopLimit = 10;

    private final EventLoopGroup group = new NioEventLoopGroup(1);

    public NettyClient(){

    }

    public NettyClient(int msgIntervalTime, int msgStopLimit) {
        this.msgIntervalTime = msgIntervalTime;
        this.msgStopLimit = msgStopLimit;
    }

    public void start() throws InterruptedException {
        this.start("127.0.0.1", 5102);
    }

    public void start(int port) throws InterruptedException {
        this.start("127.0.0.1", port);
    }

    public void start(String host, int port) throws InterruptedException {

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
                            channel.pipeline()
                                    .addLast(new MyMessageEncoder())
                                    .addLast(new MyMessageDecoder())
                                    /* // 20s 内如果没有向服务器写数据，会触发一个 IdleState#WRITER_IDLE 事件
                                    .addLast(new IdleStateHandler(0, 20, 0))
                                    .addLast(new ChannelDuplexHandler() {
                                        // 用来触发特殊事件
                                        @Override
                                        public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception{
                                            IdleStateEvent event = (IdleStateEvent) evt;
                                            // 触发了写空闲事件
                                            if (event.state() == IdleState.WRITER_IDLE) {
                                                ctx.writeAndFlush(new MessageProtocol("Ping Server"));
                                                log.info(ctx.channel().remoteAddress() + " 20s 没有写数据了，发送一个心跳包");
                                            }
                                        }
                                    })*/
                                    .addLast(new MyClientHandler(msgIntervalTime, msgStopLimit));
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
                    }, 5, TimeUnit.SECONDS);
                }
            });

            //future.channel().closeFuture().sync();
        } catch (Exception e) {
            log.info("服务端异常", e);

            if (group != null) {
                group.shutdownGracefully();
            }
        }
    }

    public void stop() {
        if (group != null) {
            group.shutdownGracefully();
        }
        log.info("Netty Client 已停止");
    }

    public static void main(String[] args) throws InterruptedException {
        new NettyClient().start();
    }
}
