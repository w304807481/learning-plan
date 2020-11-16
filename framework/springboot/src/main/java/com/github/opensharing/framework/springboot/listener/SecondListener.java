package com.github.opensharing.framework.springboot.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * FirstListener
 * <p>
 * 3.1 配置方式一 通过注解@WebListener 配置
 *
 * @author jwen
 * Date 2020-09-10
 */
public class SecondListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("SecondListener init.......");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("SecondListener destroy.......");
    }
}
