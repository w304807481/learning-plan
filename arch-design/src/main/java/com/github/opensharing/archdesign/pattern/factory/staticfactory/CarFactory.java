package com.github.opensharing.archdesign.pattern.factory.staticfactory;

/**
 * @author jwen
 */
public class CarFactory extends VehicleFactory {

    @Override
    public Movable create() {
        return Car.getInstance();
    }

}
