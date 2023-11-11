package com.github.opensharing.framework.spring.beans.factory.support;

import com.github.opensharing.framework.spring.beans.BeanDefinition;

/**
 * Bean定义的注册中心（注册表）
 *
 * @author jwen
 * Date 2023/11/9
 */
public interface BeanDefinitionRegistry {

    /**
     * 根据名称注册BeanDefinition到注册表中
     * @param beanName
     */
    void registerBeanDefinition(String beanName, BeanDefinition beanDefinition);

    /**
     * 根据名称删除注册表中的BeanDefinition
     * @param beanName
     */
    void removeBeanDefinition(String beanName);

    /**
     * 根据名称h获取注册表中的BeanDefinition
     * @param beanName
     * @return
     */
    BeanDefinition getBeanDefinition(String beanName);

    /**
     * 根据名称判断注册表是否包含BeanDefinition
     * @param beanName
     * @return
     */
    boolean containsBeanDefinition(String beanName);

    /**
     * 获取所有注册表中BeanDefinition的name数组
     * @return
     */
    String[] getBeanDefinitionNames();

    /**
     * 获取注册表中BeanDefinition的个数
     * @return
     */
    int getBeanDefinitionCount();

}
