package com.github.opensharing.framework.netty;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * 监听Spring容器启动完成，完成后启动Netty服务器
 * @author Gjing
 **/
@Component
public class NettyStartListener implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) throws Exception {
        new NettyClient().start();
    }
}