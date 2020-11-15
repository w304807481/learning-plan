package com.github.opensharing.framework.springboot.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * FirstListener
 * <p>
 * 3.1 配置方式一 通过注解@WebListener 配置
 *
 * @author jwen
 * Date 2020-09-10
 */
@WebListener
public class FirstListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("FirstListener init.......");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("FirstListener destroy.......");
    }
}
