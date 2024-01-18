package com.github.opensharing.javabase.thread;

/**
 * 线程常用方法：setName（设置线程名）setPriority（设置优先级） setDaemon(守护线程)  yield（让步） sleep(休眠)  join（强占）
 *
 * @author jwen
 * Date 2023/12/28
 */
public class ThreadMethod {

    public static void main(String[] args) throws InterruptedException {
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

        Thread.sleep(1000);
    }
}
