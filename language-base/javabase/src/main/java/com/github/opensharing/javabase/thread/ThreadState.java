package com.github.opensharing.javabase.thread;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 线程状态：NEW，RUNNABLE (READY，RUNNING)，WAITING (BLOCKED, WAITING, TIME_WAITING), TERMINATED
 *
 * @author jwen
 * Date 2023/12/28
 */
public class ThreadState {
    Lock lock = new ReentrantLock();

    public static void main(String[] args) throws InterruptedException {
        ThreadState ts = new ThreadState();

        stateNew();

        ts.stateRunnable();

        Thread t = stateBlocked(ts.lock);
        synchronized (ts.lock) {
            t.start();
            Thread.sleep(500);
            System.out.println(t.getState());
        }

        ts.stateWaiting();

        ts.stateTimeWaiting();

        ts.stateTerminated();
    }


    public static void stateNew() {
        Thread t = new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "执行了");
        });
        t.setName("stateNew");
        System.out.println(t.getState());
    }

    public void stateRunnable() throws InterruptedException {
        Thread t = new Thread(() -> {
            int  i = 1;
            while (i < 100000) {
                i++;
            }
            System.out.println(Thread.currentThread().getName() + "执行了");
        });
        t.setName("stateRunnable");
        t.start();
        System.out.println(t.getState());
    }

    public static Thread stateBlocked(Lock lock) {
        Thread t = new Thread(() -> {
            synchronized (lock) {
                System.out.println(Thread.currentThread().getName() + "执行了");
            }
        });
        t.setName("stateBlocked");
        return t;
    }

    public void stateWaiting() throws InterruptedException {
        Thread t = new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "执行了");

            synchronized (lock) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t.setName("stateWaiting");
        t.start();
        Thread.sleep(500);
        System.out.println(t.getState());

        synchronized (lock) {
            lock.notifyAll();
        }
    }

    public void stateTimeWaiting() throws InterruptedException {
        Thread t = new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "执行了");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        t.setName("stateTimeWaiting");
        t.start();
        Thread.sleep(500);
        System.out.println(t.getState());
    }

    public void stateTerminated() throws InterruptedException {
        Thread t = new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "执行了");
        });
        t.setName("stateTerminated");
        t.start();
        Thread.sleep(500);
        System.out.println(t.getState());
    }
}
