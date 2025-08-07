package com.github.opensharing.framework.netty;

import java.util.ArrayList;
import java.util.List;

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

    private final List<NettyClient> nettyClients = new ArrayList<>();

    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<String> argsList = args.getNonOptionArgs();
        System.out.println("ApplicationArguments: " + argsList);

        String host = "127.0.0.1";
        if (argsList.size() >= 1) {
            host = argsList.get(0);
        }

        int port = 5102;
        if (argsList.size() >= 2) {
            port = Integer.valueOf(argsList.get(1));
        }

        int clients = 1;
        if (argsList.size() >= 3) {
            clients = Integer.valueOf(argsList.get(2));
        }

        int msgIntervalTime = 1000;
        int msgStopLimit = 10;
        if (argsList.size() >= 5) {
            msgIntervalTime = Integer.valueOf(argsList.get(3));
            msgStopLimit = Integer.valueOf(argsList.get(4));
        }

        boolean allPorts = false;
        if (argsList.size() >=6) {
            allPorts = Boolean.valueOf(argsList.get(5));
        }

        log.info("host={}, port={}, clients={}, msgIntervalTime={}, msgStopLimit={}, allPorts={}, ", host, port, clients, msgIntervalTime, msgStopLimit, allPorts);

        for (int i = 0; i < clients; i++) {
            NettyClient nettyClient = new NettyClient(msgIntervalTime, msgStopLimit);
            nettyClient.start(host,port);
            nettyClients.add(nettyClient);
        }

        if (allPorts) {
            for (int i = 0; i < clients; i++) {
                NettyClient nettyClient1 = new NettyClient(msgIntervalTime, msgStopLimit);
                NettyClient nettyClient2 = new NettyClient(msgIntervalTime, msgStopLimit);
                nettyClient1.start(host,5103);
                nettyClient1.start(host,5104);
                nettyClients.add(nettyClient1);
                nettyClients.add(nettyClient2);
            }
        }
    }

    @Override
    public void destroy() throws Exception {
        for (NettyClient client : nettyClients) {
            client.stop();
        }
    }
}