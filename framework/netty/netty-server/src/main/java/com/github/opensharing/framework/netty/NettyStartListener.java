package com.github.opensharing.framework.netty;

import java.util.List;

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
        List<String> argsList = args.getNonOptionArgs();
        System.out.println("ApplicationArguments: " + argsList);
        boolean epollMode = false;
        if (argsList.size() > 0) {
            epollMode = Boolean.valueOf(argsList.get(0));
        }
        new NettyServer(epollMode).start();
    }
}