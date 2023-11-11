package com.github.opensharing.framework.spring.beans;

/**
 * 封装配置文件bean标签对Bean的定义
 *
 * @author jwen
 * Date 2023/11/9
 */
public class BeanDefinition {

    private String id;

    private String clazzName;

    private MutablePropertyValues mutablePropertyValues;

    public BeanDefinition() {
        System.out.printf("BeanDefinition 初始化了。。。。。");
        mutablePropertyValues = new MutablePropertyValues();
    }

    public BeanDefinition(String id, String clazzName) {
        this.id = id;
        this.clazzName = clazzName;
        mutablePropertyValues = new MutablePropertyValues();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClazzName() {
        return clazzName;
    }

    public void setClazzName(String clazzName) {
        this.clazzName = clazzName;
    }

    public MutablePropertyValues getMutablePropertyValues() {
        return mutablePropertyValues;
    }

    public void setMutablePropertyValues(MutablePropertyValues mutablePropertyValues) {
        this.mutablePropertyValues = mutablePropertyValues;
    }

    @Override
    public String toString() {
        return "BeanDefinition{" +
                "id='" + id + '\'' +
                ", clazzName='" + clazzName + '\'' +
                ", mutablePropertyValues=" + mutablePropertyValues +
                '}';
    }
}
