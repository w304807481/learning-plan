package com.github.opensharing.archdesign.pattern.factory.staticfactory;

/**
 * @author jwen
 */
public class Train implements Movable {

    @Override
    public void move() {
        System.out.println("奔驰的火车呜呜呜.....");
    }

}
