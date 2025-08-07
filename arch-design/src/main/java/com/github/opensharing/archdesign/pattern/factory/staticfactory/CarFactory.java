package com.github.opensharing.archdesign.pattern.factory.staticfactory;

import com.github.opensharing.archdesign.pattern.factory.staticfactory.singleton.Car;

/**
 * @author jwen
 */
public class CarFactory extends VehicleFactory {

    @Override
    public Movable create() {
        return Car.getInstance();
    }

}
