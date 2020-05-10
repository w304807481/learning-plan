package com.github.opensharing.archdesign.pattern.observer;

/**
 * 模拟父母亲人监听小孩睡觉醒来的事件处理
 *
 * @author Administrator
 */
public class ChildSleepObserverTest {

    public static void main(String[] args) {
        Child child = new Child();
        //加入第一个监听小孩醒来事件的对象
        child.addWakenUpListener(new Dad());
        child.addWakenUpListener(new GrandFather());
        child.addWakenUpListener(new Dog());
        new Thread(child).start();
    }
}


