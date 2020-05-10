package com.github.opensharing.archdesign.pattern.factory.staticfactory.singleton;

import com.github.opensharing.archdesign.pattern.factory.staticfactory.Movable;

/**
 * 飞机
 *
 * @author jwen
 * Date 2020-05-10
 */
public class Plane implements Movable {

    /**
     * 1. 持有单例
     */
    private static volatile Plane plane;

    /**
     * 2. 对外屏蔽构造方法
     */
    private Plane() {

    }

    /**
     * 3. 懒汉式：提供工厂方法
     *
     * @return
     */
    public static synchronized Plane getInstance() {
        if (plane == null) {
            plane = new Plane();
            System.out.println("新创建飞机。。。");
        } else {
            System.out.println("使用已经创建好的飞机");
        }
        return plane;
    }

    @Override
    public void move() {
        System.out.println("超音速飞机嗡嗡嗡。。。");
    }
}
