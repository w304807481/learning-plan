package com.github.opensharing.archdesign.pattern.factory.staticfactory.singleton;

import com.github.opensharing.archdesign.pattern.factory.staticfactory.Movable;

/**
 * @author jwen
 */
public class Train implements Movable {

    @Override
    public void move() {
        System.out.println("奔驰的火车呜呜呜.....");
    }

    private static class TrainFactory {
        private static final Train train = new Train();
    }

    public static Train getInstance() {
        /**
         * 兼顾懒加载，线程安全，代码简洁
         */
        return TrainFactory.train;
    }

}
