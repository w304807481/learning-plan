package com.github.opensharing.framework.spring.beans.factory;

/**
 * IOC的顶层接口
 *
 * @author jwen
 * Date 2023/11/10
 */
public interface BeanFactory {

    Object getBean(String beanName) throws Exception;

    <T> T getBean(String beanName, Class<? extends T> clazz) throws Exception;
}
