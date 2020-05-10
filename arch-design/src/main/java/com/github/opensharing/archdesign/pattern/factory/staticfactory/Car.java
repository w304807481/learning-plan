package com.github.opensharing.archdesign.pattern.factory.staticfactory;


import java.util.ArrayList;
import java.util.List;

/**
 * @author jwen
 */
public class Car implements Movable {
    private static Movable car = new Car();
    private static List<Car> cars = new ArrayList<Car>();

    private Car() {
    }

    public static Movable getInstance() {
        return car;
    }

    @Override
    public void move() {
        System.out.println("奔驰的汽车滴滴滴......");
    }
}
