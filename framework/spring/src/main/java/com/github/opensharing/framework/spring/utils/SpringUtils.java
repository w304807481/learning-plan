package com.github.opensharing.framework.spring.utils;

/**
 * SpringUtils
 *
 * @author jwen
 * Date 2023/11/10
 */
public class SpringUtils {

    private SpringUtils(){}

    public static String getMethodName(String propertyName) {
        return "set" + propertyName.substring(0,1).toUpperCase() + propertyName.substring(1);
    }
}
