package com.github.opensharing.framework.netty;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * 监听Spring容器启动完成，完成后启动Netty服务器
 * @author Gjing
 **/
@Slf4j
@Component
public class NettyStartListener implements ApplicationRunner, DisposableBean {

    NettyServer nettyServer;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<String> argsList = args.getNonOptionArgs();
        log.info("ApplicationArguments: " + argsList);
        boolean epollMode = false;
        if (argsList.size() > 0) {
            epollMode = Boolean.valueOf(argsList.get(0));
        }

        nettyServer = new NettyServer(epollMode);
        try {
            nettyServer.start();
        } catch (Exception e) {
            log.error("nettyServer 启动异常：" ,e);
            nettyServer.stop();
        }

        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(new Runnable(){
            @Override
            public void run() {
                log.info("目前已连接累计{}", nettyServer.getClientCounter().get());
            }
        }, 10, 10, TimeUnit.SECONDS);
    }

    @Override
    public void destroy() throws Exception {
        nettyServer.stop();
    }
}