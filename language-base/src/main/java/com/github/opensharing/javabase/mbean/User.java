package com.github.opensharing.javabase.mbean;

/**
 * 用户对象
 *
 * @author wenzongwei
 * Date 2020-09-08
 */
public class User implements UserMBean {

    private String name = "wenzongwei";

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void updateName(String name) {
        this.name = name;
    }
}
