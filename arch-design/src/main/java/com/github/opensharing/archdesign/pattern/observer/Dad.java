package com.github.opensharing.archdesign.pattern.observer;

public class Dad implements WakenUpListener {
    public Dad() {
    }

    @Override
    public void actionToWakenUP(WakenUpEvent wakenUpEvent) {
        System.out.println("给小孩子换尿布，喂奶!");
    }
}