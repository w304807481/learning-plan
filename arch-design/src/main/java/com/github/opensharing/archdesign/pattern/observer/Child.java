package com.github.opensharing.archdesign.pattern.observer;

import java.util.ArrayList;
import java.util.List;

/**
 * 事件触发对象：小孩， 也是被观察者
 *
 * @author jwen
 */
public class Child implements Runnable {

    /**
     * 持有小孩观察者集合
     */
    List<WakenUpListener> wakenUpListeners = new ArrayList<WakenUpListener>();

    public Child() {
    }

    public void addWakenUpListener(WakenUpListener l) {
        wakenUpListeners.add(l);
    }

    public void wakenUp() {
        //化对象主动守候监听事件发生 为 事件发生后被动激发会响应此事件的对象的处理事件
        for (int i = 0; i < wakenUpListeners.size(); i++) {
            WakenUpListener l = wakenUpListeners.get(i);
            l.actionToWakenUP(new WakenUpEvent(System.currentTimeMillis(), "bed", null));
        }
    }

    @Override
    public void run() {

        //模拟小孩熟睡
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //小孩醒了
        wakenUp();
    }
}