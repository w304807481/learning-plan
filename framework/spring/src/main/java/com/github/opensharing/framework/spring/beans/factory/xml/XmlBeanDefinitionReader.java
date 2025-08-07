package com.github.opensharing.framework.spring.beans.factory.xml;

import java.io.InputStream;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.github.opensharing.framework.spring.beans.BeanDefinition;
import com.github.opensharing.framework.spring.beans.PropertyValue;
import com.github.opensharing.framework.spring.beans.factory.support.BeanDefinitionReader;
import com.github.opensharing.framework.spring.beans.factory.support.BeanDefinitionRegistry;
import com.github.opensharing.framework.spring.beans.factory.support.SimpleBeanDefinitionRegistry;

/**
 * 封装通过XML配置文件定义的Bean的解析过程
 *
 * @author jwen
 * Date 2023/11/9
 */
public class XmlBeanDefinitionReader implements BeanDefinitionReader {

    private final BeanDefinitionRegistry beanDefinitionRegistry;

    public XmlBeanDefinitionReader() {
        beanDefinitionRegistry = new SimpleBeanDefinitionRegistry();
    }

    @Override
    public BeanDefinitionRegistry getBeanDefinitionRegistry() {
        return beanDefinitionRegistry;
    }

    @Override
    public void loadBeanDefinitions(String configLocation) throws Exception {
        //通过DOM4J来解析xml
        SAXReader reader = new SAXReader();
        InputStream inputStream =  this.getClass().getClassLoader().getResourceAsStream(configLocation);
        Document document = reader.read(inputStream);
        Element rootElement = document.getRootElement();
        this.parseBeans(rootElement);
    }

    /**
     * 解析XML文件中的beans标签，构建BeanDefinition并注册
     * @param rootElement
     */
    private void parseBeans(Element rootElement) {
        //获取多有bean标签
        List<Element> elementList =  rootElement.elements();

        if ((elementList == null) || (elementList.isEmpty())) {
            return;
        }

        for (Element beanElement : elementList) {
            BeanDefinition beanDefinition = this.parseOneBean(beanElement);
            beanDefinitionRegistry.registerBeanDefinition(beanDefinition.getId(), beanDefinition);
        }
    }

    /**
     * 根据bean标签，解析构建BeanDefinition
     * @param beanElement
     * @return
     */
    private BeanDefinition parseOneBean(Element beanElement) {
        String beanId = beanElement.attributeValue("id");
        String clazzName = beanElement.attributeValue("class");
        BeanDefinition beanDefinition = new BeanDefinition(beanId, clazzName);

        List<Element> propertyElements = beanElement.elements("property");
        this.parseBeanPrrperty(beanDefinition, propertyElements);
        return beanDefinition;
    }

    /**
     * 根据property标签配置装配BeanDefinition
     * @param beanDefinition
     * @param propertyElements
     */
    private void parseBeanPrrperty(BeanDefinition beanDefinition, List<Element> propertyElements) {
        if((propertyElements == null) || (propertyElements.isEmpty())) {
            return;
        }

        for (Element proElement : propertyElements) {
            String name = proElement.attributeValue("name");
            String ref = proElement.attributeValue("ref");
            String value = proElement.attributeValue("value");

            PropertyValue propertyValue = new PropertyValue(name, ref, value);
            beanDefinition.getMutablePropertyValues().addPropertyValue(propertyValue);
        }
    }

    public static void main(String[] args) throws Exception {
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader();
        reader.loadBeanDefinitions("beans.xml");

        String[] beanDefinitionNames = reader.getBeanDefinitionRegistry().getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            System.out.println("bean: " + beanDefinitionName);}

    }
}
