package com.github.opensharing.framework.springboot.filter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;

/**
 * Spingboot 整合 filter
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
 * 3.编写Servlet, 继承HttpServlet
 * 3.1 通过注解@WebFilter 配置
 * 3.2 通过@Bean ServletRegistrationBean 配置
 */
@SpringBootApplication
@ServletComponentScan
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    /**
     * 配置方式二
     *
     * @return
     */
    @Bean
    public FilterRegistrationBean getServletRegistrationBean() {
        FilterRegistrationBean bean = new FilterRegistrationBean(new ServletSecondFilter());
        bean.addUrlPatterns("/first");
        return bean;
    }
}
