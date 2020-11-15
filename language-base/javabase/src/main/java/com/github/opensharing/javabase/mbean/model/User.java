package com.github.opensharing.javabase.mbean.model;

/**
 * 用户对象
 *
 * @author jwen
 * Date 2020-09-08
 */
public class User implements UserMBean {

    private String name = "jwen";

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void updateName(String name) {
        this.name = name;
    }
}
