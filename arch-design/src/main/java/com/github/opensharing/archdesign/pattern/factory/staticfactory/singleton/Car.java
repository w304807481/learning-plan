package com.github.opensharing.archdesign.pattern.factory.staticfactory.singleton;


import com.github.opensharing.archdesign.pattern.factory.staticfactory.Movable;

/**
 * 小汽车
 * @author jwen
 */
public class Car implements Movable {

    /**
     * 1.饿汉式单例：先创建，牺牲内存换取安全
     */
    private static final Movable car = new Car();

    /**
     * 2.对外屏蔽构造方法
     */
    private Car() {
    }

    /**
     * 3. 提供工厂方法
     *
     * @return
     */
    public static Movable getInstance() {
        return car;
    }

    @Override
    public void move() {
        System.out.println("奔驰的汽车滴滴滴......");
    }
}
