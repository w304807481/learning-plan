package com.github.opensharing.framework.netty;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.extern.slf4j.Slf4j;

/**
 * Spingboot
 *<p>
 * 1.增加依赖
 *   <parent>
 *     <groupId>org.springframework.boot</groupId>
 *     <artifactId>spring-boot-starter-parent</artifactId>
 *     <version>2.3.3.RELEASE</version>
 *     <relativePath/> <!-- lookup parent from repository -->
 *   </parent>
 *<p>
 * 2.启动类加关键注解@SpringBootApplication
 *<p>
 * @author jwen
 * Date 2020-09-09
 */
@SpringBootApplication
@Slf4j
public class NettyClientApp {

    public static void main(String[] args) {
        SpringApplication.run(NettyClientApp.class, args);
    }
}
