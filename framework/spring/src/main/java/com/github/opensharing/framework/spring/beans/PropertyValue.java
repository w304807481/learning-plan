package com.github.opensharing.framework.spring.beans;

/**
 * 封装bean配置文件中的property属性
 *
 * @author jwen
 * Date 2023/11/9
 */
public class PropertyValue {

    private String name;

    private String ref;

    private String value;

    public PropertyValue() {
        System.out.printf("PropertyValue 初始化了。。。。。");
    }

    public PropertyValue(String name, String ref, String value) {
        this.name = name;
        this.ref = ref;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
