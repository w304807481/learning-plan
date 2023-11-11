package com.github.opensharing.framework.spring.beans;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 封装been的配置中的多个property
 *
 * @author jwen
 * Date 2023/11/9
 */
public class MutablePropertyValues implements Iterable<PropertyValue> {

    private final List<PropertyValue> propertyValueList;

    public MutablePropertyValues() {
        propertyValueList = new ArrayList<>();
    }

    public MutablePropertyValues(List<PropertyValue> propertyValueList) {
        if (propertyValueList == null) {
            this.propertyValueList = new ArrayList<>();
        } else {
            this.propertyValueList = propertyValueList;
        }
    }

    @Override
    public Iterator<PropertyValue> iterator() {
        return propertyValueList.iterator();
    }


    public PropertyValue[] getPropertyValues() {
        return propertyValueList.toArray(new PropertyValue[0]);
    }

    public PropertyValue getPropertyValue(String propertyName) {

        for (PropertyValue propertyValue : propertyValueList) {
            if (propertyValue.getName().equals(propertyName)) {
                return propertyValue;
            }
        }
        return null;
    }

    public boolean isEmpty() {
        return propertyValueList.isEmpty();
    }

    public MutablePropertyValues addPropertyValue(PropertyValue propertyValueTarget) {
        for (int i = 0; i < propertyValueList.size(); i++) {
            PropertyValue propertyValuePre = propertyValueList.get(i);
            if (propertyValuePre.getName().equals(propertyValueTarget.getName())) {
                propertyValueList.set(i, propertyValueTarget);
                return this;
            }
        }

        //全新的配置
        propertyValueList.add(propertyValueTarget);
        return this;
    }

    public boolean contains(String propertyName) {
        return getPropertyValue(propertyName) != null;
    }
}
