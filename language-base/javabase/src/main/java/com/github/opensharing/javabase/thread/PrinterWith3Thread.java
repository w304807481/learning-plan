package com.github.opensharing.javabase.thread;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 3个线程按照顺序交替打印ABC 10次
 *
 * @author jwen
 * Date 2021-08-04
 */
public class PrinterWith3Thread {


    public static void main(String[] args) {
        //方法1: 通过取模的形式
        //new PrinterByMode().print();
        //方法2: 通过 wait/notify范式来实现
        //new PrinterByWaitNotify().print();
        //方法3: 通过 ReentrantLock,Condition来实现
        //new PrinterByCondition().print();
        //方法4: 通过 通过 环形线程 来实现
        //new PrinterByCyclicThread().print();
        //方法5: 通过 阻塞队列 + "击鼓传花" 来实现
        new PrinterByBlockingQueue().print();
    }

}

/**
 * 方法1: 通过取模的形式
 */
class PrinterByMode {
    private static final int COUNT = 10;
    private int threadNum;
    private volatile int index = 0;
    private final Lock lock = new ReentrantLock();

    public void print() {
        threadNum = 3;
        new MyThread(0).start();
        new MyThread(1).start();
        new MyThread(2).start();
    }

    class MyThread extends Thread {

        /**
         * 余数
         */
        private int remainder;

        public MyThread(int remainder) {
            this.remainder = remainder;
        }

        @Override
        public void run() {
            while (true) {

                lock.lock();

                try {
                    if (index >= COUNT) {
                        return;
                    }
                    if (index % threadNum == remainder) {
                        System.out.println(Thread.currentThread().getName() + "ABC");
                        index++;
                    }
                } finally {
                    lock.unlock();
                }
            }
        }
    }

}

/**
 * 方法2: 通过 wait/notify范式来实现
 */
class PrinterByWaitNotify {
    static int COUNT = 10;
    final Lock lock = new Lock();


    public void print() {
        new MyThread(1).start();
        new MyThread(2).start();
        new MyThread(3).start();
    }

    class Lock {
        private int state = 1;

        public int getState() {
            return state;
        }

        public void changeState() {
            //击鼓传花
            switch (state) {
                case 1 :
                    state = 2;
                    break;
                case 2 :
                    state = 3;
                    break;
                case 3 :
                    state = 1;
                    break;
                default:
                    break;
            }
        }
    }

    class MyThread extends Thread {
        int state;

        public MyThread(int state) {
            this.state = state;
        }

        @Override
        public void run() {
            synchronized (lock) {
                try {
                    for (int i = 0; i < COUNT; i++) {

                        if (state == lock.getState()) {
                            System.out.println(Thread.currentThread().getName() + "ABC");

                            lock.changeState();
                            lock.notifyAll();
                        }
                        else {
                            //完成打印，线程停止等待
                            lock.wait(500);
                        }

                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

/**
 * 方法3: 通过 ReentrantLock,Condition,CountDownLatch来实现
 */
class PrinterByCondition {
    static int COUNT = 10;
    CountDownLatch countDownLatch = new CountDownLatch(COUNT);
    ReentrantLock lock = new ReentrantLock();
    final Condition conditionA = lock.newCondition();
    final Condition conditionB = lock.newCondition();
    final Condition conditionC = lock.newCondition();
    volatile int commState = 1;

    public void print() {
        new MyThread(1).start();
        new MyThread(2).start();
        new MyThread(3).start();
    }

    class MyThread extends Thread {
        private int state;

        public MyThread(int state) {
            this.state = state;
        }

        @Override
        public void run() {
            lock.lock();
            try {
                while(countDownLatch.getCount() > 0) {

                    switch (state) {
                        case 1:
                            if (state == commState) {
                                System.out.println(Thread.currentThread().getName() + "ABC");
                                commState = 2;
                                countDownLatch.countDown();
                                conditionB.signal();
                            } else {
                                conditionA.await(500, TimeUnit.MILLISECONDS);
                            }
                            break;
                        case 2:
                            if (state == commState) {
                                System.out.println(Thread.currentThread().getName() + "ABC");
                                commState = 3;
                                countDownLatch.countDown();
                                conditionC.signal();
                            } else {
                                conditionB.await(500, TimeUnit.MILLISECONDS);
                            }
                            break;
                        case 3:
                            if (state == commState) {
                                System.out.println(Thread.currentThread().getName() + "ABC");
                                commState = 1;
                                countDownLatch.countDown();
                                conditionA.signal();
                            } else {
                                conditionC.await(500, TimeUnit.MILLISECONDS);
                            }
                            break;
                        default:
                            break;
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    }
}

/**
 * 方法4: 通过 环形线程 来实现
 */
class PrinterByCyclicThread {
    static int COUNT = 10;
    CountDownLatch countDownLatch = new CountDownLatch(COUNT);


    public void print() {
        MyThread t1 = new MyThread(true);
        MyThread t2 = new MyThread(false);
        MyThread t3 = new MyThread(false);
        t1.setNext(t2).setNext(t3).setNext(t1);
        t1.start();
        t2.start();
        t3.start();

    }

    class MyThread extends Thread {
        boolean state;
        MyThread next;

        public MyThread(boolean state) {
            this.state = state;
        }

        public MyThread setNext(MyThread next) {
            this.next = next;
            return next;
        }

        public void changeState() {
            this.state = !state;
        }

        @Override
        public void run() {
            while (countDownLatch.getCount() > 0) {

                if (state) {
                    System.out.println(Thread.currentThread().getName() + "ABC");
                    countDownLatch.countDown();
                    this.changeState();
                    next.changeState();

                }
            }
        }
    }
}

/**
 * 方法5: 通过 阻塞队列 + "击鼓传花" 来实现
 */
class PrinterByBlockingQueue {
    static int COUNT = 10;
    CountDownLatch countDownLatch = new CountDownLatch(COUNT);

    BlockingQueue<String> blockingQueue1 = new ArrayBlockingQueue(1);
    BlockingQueue<String> blockingQueue2 = new ArrayBlockingQueue(1);
    BlockingQueue<String> blockingQueue3 = new ArrayBlockingQueue(1);

    public void print() {
        new MyThread(1).start();
        new MyThread(2).start();
        new MyThread(3).start();
        blockingQueue1.offer("ABC");
    }

    class MyThread extends Thread {
        int state;

        public MyThread(int state) {
            this.state = state;
        }

        @Override
        public void run() {
            String flower = null;
            while (countDownLatch.getCount() > 0) {
                try {
                    switch (state) {
                        case 1:
                            flower = blockingQueue1.poll(500, TimeUnit.MILLISECONDS);
                            if (countDownLatch.getCount() <= 0) {break;}
                            System.out.println(Thread.currentThread().getName() + flower);
                            countDownLatch.countDown();
                            blockingQueue2.offer(flower);
                            break;
                        case 2:
                            flower = blockingQueue2.poll(500, TimeUnit.MILLISECONDS);
                            if (countDownLatch.getCount() <= 0) {break;}
                            System.out.println(Thread.currentThread().getName() + flower);
                            countDownLatch.countDown();
                            blockingQueue3.offer(flower);
                            break;
                        case 3:
                            flower = blockingQueue3.poll(500, TimeUnit.MILLISECONDS);
                            if (countDownLatch.getCount() <= 0) {break;}
                            System.out.println(Thread.currentThread().getName() + flower);
                            countDownLatch.countDown();
                            blockingQueue1.offer(flower);
                            break;
                        default:
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}