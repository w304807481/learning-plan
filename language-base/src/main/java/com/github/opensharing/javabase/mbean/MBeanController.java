package com.github.opensharing.javabase.mbean;

import java.lang.management.ManagementFactory;

import javax.management.MBeanServer;
import javax.management.ObjectName;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * JMX MBean Controller
 *
 * @author wenzongwei
 * Date 2020-09-08
 */
@RestController
@RequestMapping("/mbean")
public class MBeanController implements InitializingBean {

    @GetMapping("/user/get")
    public Object getUser() throws Exception {
        MBeanServer server = ManagementFactory.getPlatformMBeanServer();
        ObjectName objectName = new ObjectName("com.github.opensharing.javabase.mbean:type=User");
        Object attribute = server.getAttribute(objectName, "Name");
        return attribute;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        //实现MBean注册
        MBeanServer server = ManagementFactory.getPlatformMBeanServer();
        ObjectName objectName = new ObjectName("com.github.opensharing.javabase.mbean:type=User");
        server.registerMBean(new User(), objectName);
        System.err.println("Application Admin MBean registered with name '" + objectName + "'");
    }
}
