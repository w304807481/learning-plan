package com.github.opensharing.framework.springboot.view.jsp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spingboot
 * <p>
 * 1.增加依赖
 * <parent>
 *   <groupId>org.springframework.boot</groupId>
 *   <artifactId>spring-boot-starter-parent</artifactId>
 *   <version>2.3.3.RELEASE</version>
 *   <relativePath/> <!-- lookup parent from repository -->
 * </parent>
 * <dependency>
 *   <groupId>javax.servlet</groupId>
 *   <artifactId>jstl</artifactId>
 *   </dependency>
 * <dependency>
 *   <groupId>org.apache.tomcat.embed</groupId>
 *   <artifactId>tomcat-embed-jasper</artifactId>
 *   <scope>provided</scope>
 * </dependency>
 * <p>
 * 2.启动类加关键注解@SpringBootApplication
 * <p>
 * 3.编写Controller 注意必须是@Controller， 不能是@RestController
 * 在同级目录或子目录编写。否则需要其他注解配合
 * <p>
 * 4.增加配置参数
 *   spring.mvc.view.prefix=/WEB-INF/jsp/
 *   spring.mvc.view.suffix=.jsp
 * <p>
 * 5.编写JSP
 *
 * @author jwen
 * Date 2020-09-09
 */
@SpringBootApplication
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
