package com.github.opensharing.framework.spring.context.support;

import java.lang.reflect.Method;

import com.github.opensharing.framework.spring.beans.BeanDefinition;
import com.github.opensharing.framework.spring.beans.MutablePropertyValues;
import com.github.opensharing.framework.spring.beans.PropertyValue;
import com.github.opensharing.framework.spring.beans.factory.support.BeanDefinitionRegistry;
import com.github.opensharing.framework.spring.beans.factory.xml.XmlBeanDefinitionReader;
import com.github.opensharing.framework.spring.utils.SpringUtils;

/**
 * IOC容器的子类，负责通过xml配置加载并初始化bean
 *
 * @author jwen
 * Date 2023/11/10
 */
public class ClassXmlApplicationContext extends AbstractApplicationContext {


    public ClassXmlApplicationContext(String configLocation) {
        this.configLocation = configLocation;
        this.beanDefinitionReader = new XmlBeanDefinitionReader();
        this.refresh();
    }

    @Override
    public Object getBean(String beanName) throws Exception {
        //缓存检查
        Object bean = beanMap.get(beanName);

        if(bean != null) {
            return bean;
        }

        //初始创建
        BeanDefinitionRegistry registry = this.beanDefinitionReader.getBeanDefinitionRegistry();
        BeanDefinition beanDefinition = registry.getBeanDefinition(beanName);
        bean = this.generateBean(beanDefinition);
        beanMap.put(beanName, bean);
        return bean;
    }

    @Override
    public <T> T getBean(String beanName, Class<? extends T> clazz) throws Exception {
        Object bean = this.getBean(beanName);

        if (bean == null) {
            return null;
        }
        return clazz.cast(bean);
    }

    private Object generateBean(BeanDefinition beanDefinition) throws Exception {
        //根据反射生产bean
        String clazzName = beanDefinition.getClazzName();
        Class clazz = Class.forName(clazzName);
        Object bean = clazz.newInstance();
        //设置属性
        this.populateProperties(beanDefinition, clazz, bean);
        return bean;
    }

    private void populateProperties(BeanDefinition beanDefinition, Class clazz, Object bean) throws Exception {
        MutablePropertyValues mutablePropertyValues = beanDefinition.getMutablePropertyValues();
        PropertyValue[] propertyValues = mutablePropertyValues.getPropertyValues();

        if (propertyValues == null) {
            return;
        }

        for(PropertyValue propertyValue : propertyValues) {
            this.populateOneProperty(clazz, bean, propertyValue);
        }
    }

    private void populateOneProperty(Class clazz, Object bean, PropertyValue propertyValue) throws Exception {
        String pName = propertyValue.getName();
        String pRef = propertyValue.getRef();
        String pValue = propertyValue.getValue();
        String methodName = SpringUtils.getMethodName(pName);
        Object methodParam = pValue;

        //获取ref对象
        if ((pRef != null) && (!"".equals(pRef))) {
            methodParam = this.getBean(pRef);
        }

        //setRef
        Method[] methods = clazz.getMethods();
        for (Method m : methods) {
            if (m.getName().equals(methodName)) {
                System.out.println("methodName: " + methodName + " methodParam: " + methodParam);
                m.invoke(bean, methodParam);
            }
        }
    }
}
