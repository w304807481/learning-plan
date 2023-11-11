package com.github.opensharing.framework.spring.beans.factory.support;

/**
 * 定义根据配置文件解析成BeanDefinition的规则
 *
 * @author jwen
 * Date 2023/11/9
 */
public interface BeanDefinitionReader {

    /**
     * 获取BeanDefinition的注册表
     * @return
     */
    BeanDefinitionRegistry getBeanDefinitionRegistry();

    /**
     * 根据配置文件解析并加载成BeanDefinition
     * @param configLocation
     */
    void loadBeanDefinitions(String configLocation) throws Exception;
}
