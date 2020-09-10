package com.github.opensharing.framework.springboot.listener;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;

/**
 * Spingboot 整合 listener
 * <p>
 * 1.增加依赖
 * <parent>
 * <groupId>org.springframework.boot</groupId>
 * <artifactId>spring-boot-starter-parent</artifactId>
 * <version>2.3.3.RELEASE</version>
 * <relativePath/> <!-- lookup parent from repository -->
 * </parent>
 * <p>
 * 2.启动类加关键注解@SpringBootApplication
 * <p>
 * 3.编写Listener, 实现相应的接口， 比如ServletContextListener
 * 3.1 配置方式一 通过注解@WebListener 配置
 * 3.2 配置方式二 通过@Bean ServletListenerRegistrationBean 配置
 * <p>
 *
 * @author jwen
 * Date 2020-09-09
 */
@SpringBootApplication
@ServletComponentScan
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    /**
     * 3.2 配置方式二 通过@Bean ServletListenerRegistrationBean 配置
     *
     * @return
     */
    @Bean
    public ServletListenerRegistrationBean getServletRegistrationBean() {
        ServletListenerRegistrationBean bean = new ServletListenerRegistrationBean(new SecondListener());
        return bean;
    }
}
