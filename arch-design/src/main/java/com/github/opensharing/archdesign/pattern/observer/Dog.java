package com.github.opensharing.archdesign.pattern.observer;

/**
 * @author jwen
 */
public class Dog implements WakenUpListener {
    public Dog() {
    }

    @Override
    public void actionToWakenUP(WakenUpEvent wakenUpEvent) {
        System.out.println("Wang wang....");
    }
}