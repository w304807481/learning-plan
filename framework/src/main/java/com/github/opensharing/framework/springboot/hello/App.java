package com.github.opensharing.framework.springboot.hello;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spingboot
 * <p>
 * 1.增加依赖
 * <parent>
 * <groupId>org.springframework.boot</groupId>
 * <artifactId>spring-boot-starter-parent</artifactId>
 * <version>2.3.3.RELEASE</version>
 * <relativePath/> <!-- lookup parent from repository -->
 * </parent>
 * <p>
 * 2.启动类加关键注解
 *
 * @author wenzongwei
 * Date 2020-09-09
 * @SpringBootApplication 3.编写Rest
 * 在同级目录或子目录编写。否则需要其他注解配合
 */
@SpringBootApplication
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
