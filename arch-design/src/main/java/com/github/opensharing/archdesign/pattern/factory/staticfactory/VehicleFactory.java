package com.github.opensharing.archdesign.pattern.factory.staticfactory;

/**
 * @author jwen
 */
public abstract class VehicleFactory {

    /**
     * 生产
     *
     * @return
     */
    public abstract Movable create();
}
