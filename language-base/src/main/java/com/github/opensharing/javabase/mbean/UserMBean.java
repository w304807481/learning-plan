package com.github.opensharing.javabase.mbean;

/**
 * User暴露出的管理接口
 * <p>
 * 标准MBEAN是有侵入性的，他要管理的对象是符合JAVA BEAN规范的对象。
 * 但是要作为标准MBEAN而被管理，就需要实现一个接口。这个接口的名称必须是类名加上MBean。
 *
 * @author jwen
 * Date 2020-09-07
 */
public interface UserMBean {

    String getName();

    /**
     * 注意命名为setName无法暴露， 改为SetName或updateName后可以
     *
     * @param name
     */
    void updateName(String name);
}
