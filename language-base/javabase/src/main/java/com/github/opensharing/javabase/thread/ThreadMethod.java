package com.github.opensharing.javabase.thread;

/**
 * 线程常用方法：Thread.setName（设置线程名）
 *             Thread.setPriority（设置优先级）
 *             Thread.setDaemon(守护线程)
 *             Thread.yield（让步）
 *             Thread.sleep(休眠)
 *             Thread.join（强占）
 *             Object.wait(等待)
 *             Object.notify(随机唤醒)
 *             Object.notifyAll(唤醒全部)
 *
 *
 * @author jwen
 * Date 2023/12/28
 */
public class ThreadMethod {

    public static void main(String[] args) throws InterruptedException {
        baseMethod();

        Thread.sleep(5000);

        waitNotify();

        Thread.sleep(5000);

        stop();
        stopWaitThread();
        stopSleepThread();
        stopBlockedThreadInvalid();
    }

    private static void stop() throws InterruptedException {
        Thread t5 = new Thread(() -> {
            int i = 1;
            //通过状态为来停止线程
            while (!Thread.currentThread().isInterrupted()) {
                if (i == 0) {
                    System.out.println(Thread.currentThread().getName() + "执行了" + i);
                }
                i++;
            }
            System.out.println(Thread.currentThread().getName() + "即将结束");
        });
        t5.setName("平台-模块-服务-线程号" + 5);
        t5.start();

        Thread.sleep(1000);
        System.out.println("t5 state: " + t5.getState());
        //改变状态位
        t5.interrupt();
    }

    private static void stopWaitThread() throws InterruptedException {
        Thread t6 = new Thread(() -> {
            try {
                //WAITING 状态线程被中断
                synchronized (ThreadMethod.class) {
                    ThreadMethod.class.wait();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.out.println(Thread.currentThread().getName() + "即将结束");
                return;
            }
        });
        t6.setName("平台-模块-服务-线程号" + 6);
        t6.start();

        Thread.sleep(500);
        System.out.println("t6 state: " + t6.getState());
        //改变状态位
        t6.interrupt();
    }

    private static void stopSleepThread() throws InterruptedException {
        Thread t7 = new Thread(() -> {
            try {
                //TIME_WAITING 状态线程被中断
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.out.println(Thread.currentThread().getName() + "即将结束");
                return;
            }
        });
        t7.setName("平台-模块-服务-线程号" + 6);
        t7.start();

        Thread.sleep(500);
        System.out.println("t7 state: " + t7.getState());
        //改变状态位
        t7.interrupt();
    }

    private static void stopBlockedThreadInvalid() throws InterruptedException {
        Object obj = new Object();

        Thread t8 = new Thread(() -> {
            synchronized (obj) {

            }
            System.out.println(Thread.currentThread().getName() + "即将结束");
        });
        t8.setName("平台-模块-服务-线程号" + 8);

        synchronized (obj) {
            t8.start();
            Thread.sleep(500);
            System.out.println("t8 state: " + t8.getState());
            //改变状态位
            t8.interrupt();
            Thread.sleep(500);
            System.out.println("t8 interrupt 无效: " + t8.getState());
            Thread.sleep(5000);
        }
    }

    /**
     * Object.wait(等待)
     * Object.notify(随机唤醒)
     * Object.notifyAll(唤醒全部)
     *
     * @throws InterruptedException
     */
    private static void waitNotify() throws InterruptedException {
        Thread t3 = new Thread(()  -> {
            sync();
        });
        t3.setName("平台-模块-服务-线程号" + 3);
        Thread t4 = new Thread(()  -> {
            sync();
        });
        t4.setName("平台-模块-服务-线程号" + 4);
        t3.start();
        t4.start();

        Thread.sleep(3000);
        synchronized (ThreadMethod.class) {
            System.out.println(Thread.currentThread().getName() + "执行中");
            ThreadMethod.class.notifyAll();
        }
    }

    /**
     * Thread.setName（设置线程名）
     * Thread.setPriority（设置优先级）
     * Thread.setDaemon(守护线程)
     * Thread.yield（让步）
     * Thread.sleep(休眠)
     * Thread.join（强占）
     *
     * @throws InterruptedException
     */
    private static void baseMethod() throws InterruptedException {
        Thread t1 = new Thread(() -> {
            for (int i = 1; i <= 100; i++) {

                System.out.println(Thread.currentThread().getName() + "执行了 " + i);
                if (i % 10 == 0) {
                    System.out.println(Thread.currentThread().getName() + "==========让出CPU");
                    //让出CPU
                    Thread.yield();

                }
            }
        });
        t1.setName("平台-模块-服务-线程号" + 1);
        t1.setPriority(5);//1~10
        t1.setDaemon(false);
        t1.start();


        Thread t2 = new Thread(() -> {
            for (int i = 1; i <= 100; i++) {

                System.out.println(Thread.currentThread().getName() + "执行了 " + i);
                if (i % 20 == 0) {
                    System.out.println(Thread.currentThread().getName() + "==========让出CPU");
                    //让出CPU
                    Thread.yield();
                }
            }
        });
        t2.setName("平台-模块-服务-线程号" + 2);
        t2.setPriority(5);//1~10
        t2.start();

        System.out.println(Thread.currentThread().getName() + "执行中");
        t1.join();
        System.out.println(Thread.currentThread().getName() + "执行中");
    }

    public static synchronized void sync() {
        try {
            for (int i = 0; i < 10; i++) {
                if (i == 5) {
                    ThreadMethod.class.wait();
                }
                Thread.sleep(200);
                System.out.println(Thread.currentThread().getName() + "执行中");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
