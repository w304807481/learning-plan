package com.github.opensharing.javabase.thread;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * 线程创建方法
 *
 * @author jwen
 * Date 2023/12/28
 */
public class ThreadCreateWay {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //1. Thread
        Thread t1 = new MyThread();
        t1.setName("线程1");
        t1.start();

        //2. Runnable
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName() + "执行");
            }
        });
        t2.setName("线程2");
        t2.start();

        //2. Callable
        FutureTask ft = new FutureTask(new Callable() {
            @Override
            public Object call() throws Exception {
                System.out.println(Thread.currentThread().getName() + "执行");
                return 1;
            }
        });
        Thread t3 = new Thread(ft);
        t3.setName("线程3");
        t3.start();
        System.out.println(ft.get());
    }
}

class MyThread extends Thread {
    @Override
    public void run() {
        System.out.println(this.getName() + "执行");
    }
}
