package com.github.opensharing.archdesign.pattern.observer;

/**
 * 观察者：爷爷
 *
 * @author jwen
 */
public class GrandFather implements WakenUpListener {
    public GrandFather() {
    }

    @Override
    public void actionToWakenUP(WakenUpEvent wakenUpEvent) {
        System.out.println("抱抱小孩子，哄一哄");
    }
}