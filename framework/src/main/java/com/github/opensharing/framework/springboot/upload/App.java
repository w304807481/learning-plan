package com.github.opensharing.framework.springboot.upload;

import javax.servlet.MultipartConfigElement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.util.unit.DataSize;

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
 * 2.启动类加关键注解@SpringBootApplication
 * <p>
 * 3.编写UploadController
 * <p>
 * 4.通过Spring配置文件或 MultipartConfigFactory配置
 * spring.http.multipart.maxFileSize=10Mb
 * spring.http.multipart.maxRequestSize=100Mb
 * <p>
 *
 * @author jwen
 * Date 2020-09-09
 */
@SpringBootApplication
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        //单个数据大小
        factory.setMaxFileSize(DataSize.ofMegabytes(10));
        //总上传数据大小
        factory.setMaxRequestSize(DataSize.ofMegabytes(100));
        return factory.createMultipartConfig();
    }
}
