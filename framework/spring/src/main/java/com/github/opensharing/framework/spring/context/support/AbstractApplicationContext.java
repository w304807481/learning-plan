package com.github.opensharing.framework.spring.context.support;

import java.util.HashMap;
import java.util.Map;

import com.github.opensharing.framework.spring.beans.BeanDefinition;
import com.github.opensharing.framework.spring.beans.factory.support.BeanDefinitionReader;
import com.github.opensharing.framework.spring.beans.factory.support.BeanDefinitionRegistry;
import com.github.opensharing.framework.spring.context.ApplicationContext;

/**
 * 加载配置并初始化bean
 *
 * @author jwen
 * Date 2023/11/10
 */
public abstract class AbstractApplicationContext implements ApplicationContext {

    /**
     * 配置文件路径
     */
    protected String configLocation;

    /**
     * 配置文件读取类
     */
    protected BeanDefinitionReader beanDefinitionReader;

    /**
     * 缓存bean对象
     */
    protected Map<String, Object> beanMap = new HashMap<>();

    protected Map<String, BeanDefinition> beanDefinitionMap;

    @Override
    public void refresh() {

        try {
            beanDefinitionReader.loadBeanDefinitions(configLocation);

            doInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void doInit() throws Exception {
        BeanDefinitionRegistry registry = beanDefinitionReader.getBeanDefinitionRegistry();
        String[] beanDefinitionNames = registry.getBeanDefinitionNames();

        for (String beanName : beanDefinitionNames) {
            getBean(beanName);
        }
    }
}
