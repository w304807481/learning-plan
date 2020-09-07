package com.github.opensharing.javabase.mbean;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * JMX MBean
 *
 * @author wenzongwei
 * Date 2020-09-08
 */
@SpringBootApplication
public class MBeanDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(MBeanDemoApplication.class, args);
    }
}
