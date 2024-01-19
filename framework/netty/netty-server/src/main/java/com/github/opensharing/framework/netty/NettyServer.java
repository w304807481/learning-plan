package com.github.opensharing.framework.netty;

import java.util.Random;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import com.github.opensharing.framework.netty.hander.MyMessageDecoder;
import com.github.opensharing.framework.netty.hander.MyMessageEncoder;
import com.github.opensharing.framework.netty.hander.MyServerHandler;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.WriteBufferWaterMark;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

/**
 * Netty服务端
 *
 * @author jwen
 * Date 2023/11/12
 */
@Slf4j
public class NettyServer {

    private final AtomicInteger msgCounter = new AtomicInteger();
    private final AtomicInteger clientCounter = new AtomicInteger();

    private boolean epollMode = false;

    //1.连接线程：bossGroup
    private final EventLoopGroup bossGroup;
    //2.工作线程组：workGroup
    private final EventLoopGroup workerGroup;
    //3.Server
    private final ServerBootstrap serverBootstrap;

    public NettyServer(boolean epollMode) {
        this.epollMode = epollMode && Epoll.isAvailable();

        if (this.epollMode) {
            log.info("epollMode is setup");
        } else {
            log.info("nioMode is setup");
        }

        this.bossGroup = this.getBossGroup();
        this.workerGroup = this.getWorkerGroup();
        this.serverBootstrap = new ServerBootstrap();
    }

    public void start() throws InterruptedException {
        init();
        bindPorts();
    }

    public void stop() {
        // 优雅的关闭 释放资源
        if (bossGroup != null) {
            bossGroup.shutdownGracefully();
        }
        if (workerGroup != null) {
            workerGroup.shutdownGracefully();
        }
        log.info("Netty Server 停止");
    }

    private void init() {
        // 传入两个线程组
        serverBootstrap.group(bossGroup, workerGroup)
                // 指定Channel 和Epoll/NIO一样是采用Channel通道的方式通信 所以需要指定服务端通道
                .channel(epollMode ? EpollServerSocketChannel.class : NioServerSocketChannel.class)

                //服务端可连接队列数,对应TCP/IP协议listen函数中backlog参数
                .option(ChannelOption.SO_BACKLOG, 1024)
                .option(ChannelOption.SO_RCVBUF, 1024)

                //ByteBuf的分配器(重用缓冲区)，默认值为ByteBufAllocator.DEFAULT
                //.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)

                //用于Channel分配接受Buffer的分配器，默认值为AdaptiveRecvByteBufAllocator.DEFAULT，是一个自适应的接受缓冲区分配器，能根据接受到的数据自动调节大小。可选值为FixedRecvByteBufAllocator，固定大小的接受缓冲区分配器
                //.option(ChannelOption.RCVBUF_ALLOCATOR, AdaptiveRecvByteBufAllocator.DEFAULT);

                //保持连接生命，不因空闲而断开
                //.childOption(ChannelOption.SO_KEEPALIVE, true)
                //防止数据传输延迟 如果false的话会缓冲数据达到一定量在flush,降低系统网络调用（具体场景）
                //.childOption(ChannelOption.TCP_NODELAY, true)

                //.childOption(ChannelOption.SO_RCVBUF, 1024 * 1024)
                //.childOption(ChannelOption.SO_SNDBUF, 1024 * 1024)

                //设置WriteBuffer缓冲区高低水位线
                .childOption(ChannelOption.WRITE_BUFFER_WATER_MARK, this.getWriteBufferWaterMark())

                //设置数据处理器
                .childHandler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel channel) throws Exception {
                        // 在管道中 添加数据处理类
                        channel.pipeline()
                                .addLast(new MyMessageDecoder())
                                .addLast(new MyMessageEncoder())
                                /*//60s 内如果没有收到 channel 的数据，会触发一个 IdleState#READER_IDLE 事件
                                .addLast(new IdleStateHandler(60,0,0))
                                .addLast(new ChannelDuplexHandler() {
                                    // 用来触发特殊事件
                                    @Override
                                    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception{
                                        IdleStateEvent event = (IdleStateEvent) evt;
                                        // 触发了读空闲事件
                                        if (event.state() == IdleState.READER_IDLE) {
                                            ctx.channel().close();
                                            log.info(ctx.channel().remoteAddress() + " 已经 60s 没有读到数据了, 已断开连接, 目前累计{}", clientCounter.get());
                                        }
                                    }
                                })*/
                                .addLast(new MyServerHandler(clientCounter, msgCounter));
                    }
                });

//        // linux平台下支持SO_REUSEPORT特性以提高性能
//        if (epollMode) {
//            log.info("启用了 SO_REUSEPORT");
//            serverBootstrap.option(EpollChannelOption.SO_REUSEPORT, true);
//        }
    }

    private void bindPorts() throws InterruptedException {
        // 同步等待成功
        ChannelFuture future5102 = serverBootstrap.bind(5102).sync();
        log.info("启动 Netty Server (5102) " + (future5102.isSuccess() ? "成功" : "失败"));

        ChannelFuture future5103 = serverBootstrap.bind(5103).sync();
        log.info("启动 Netty Server (5103) " + (future5103.isSuccess() ? "成功" : "失败"));

        ChannelFuture future5104 = serverBootstrap.bind(5104).sync();
        log.info("启动 Netty Server (5104) " + (future5104.isSuccess() ? "成功" : "失败"));

        //等待服务端监听端口关闭 链路关闭后main函数才会结束
//        future5102.channel().closeFuture().sync();
//        future5103.channel().closeFuture().sync();
//        future5104.channel().closeFuture().sync();
    }

    private WriteBufferWaterMark getWriteBufferWaterMark() {
        //32k - 64k
        return new WriteBufferWaterMark(32 * 1024, 64 * 1024);
    }

    private EventLoopGroup getBossGroup() {

        if (epollMode) {
            log.info("BossGroup is setup to EpollEventLoopGroup");
            return new EpollEventLoopGroup(1, new ThreadFactory() {
                @Override
                public Thread newThread(Runnable r) {
                    Thread t = new Thread(r);
                    t.setName("BossGroup" + new Random().nextInt(10));
                    return t;
                }
            });
        }
        else {
            return new NioEventLoopGroup(10, new ThreadFactory() {
                @Override
                public Thread newThread(Runnable r) {
                    Thread t = new Thread(r);
                    t.setName("BossGroup" + new Random().nextInt(10));
                    return t;
                }
            });
        }
    }

    private EventLoopGroup getWorkerGroup() {
        if (epollMode) {
            log.info("WorkerGroup is setup to EpollEventLoopGroup");
            return new EpollEventLoopGroup(20, new ThreadFactory() {
                private final AtomicInteger threadCounter = new AtomicInteger(1);

                @Override
                public Thread newThread(Runnable r) {
                    Thread t = new Thread(r);
                    t.setName("WorkerGroup-" + + threadCounter.getAndIncrement());
                    return t;
                }
            });
        }
        else {
            return new NioEventLoopGroup(20, new ThreadFactory() {
                private final AtomicInteger threadCounter = new AtomicInteger(1);

                @Override
                public Thread newThread(Runnable r) {
                    Thread t = new Thread(r);
                    t.setName("WorkerGroup-" + threadCounter.getAndIncrement());
                    return t;
                }
            });
        }
    }

    public AtomicInteger getClientCounter() {
        return clientCounter;
    }

    public static void main(String[] args) throws InterruptedException {
        new NettyServer(true).start();
    }
}
