package com.github.opensharing.javabase.jvm.classloading;

import org.openjdk.jol.info.ClassLayout;

/**
 * 对象的内存布局
 *
 * maven 依赖
 *
 * <dependency>
 *   <groupId>org.openjdk.jol</groupId>
 *   <artifactId>jol-core</artifactId>
 *   <version>0.8</version>
 * </dependency>
 *
 * @author jwen
 * Date 2020-11-01
 */
public class JavaObjectLayoutObjectSizeDemo {

    public static void main(String[] args) {
        Object o = new Object();

        System.out.println(ClassLayout.parseInstance(o).toPrintable());
    }
}
