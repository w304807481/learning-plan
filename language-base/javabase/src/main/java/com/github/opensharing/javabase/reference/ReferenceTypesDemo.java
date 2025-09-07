package com.github.opensharing.javabase.reference;

import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Java四种引用类型演示
 * 强引用(Strong Reference) - 默认的引用类型，不会被垃圾回收
 * 软引用(Soft Reference) - 内存不足时会被回收
 * 弱引用(Weak Reference) - 下次垃圾回收时会被回收
 * 虚引用(Phantom Reference) - 随时可能被回收，主要用于跟踪对象被回收的状态
 *
 * @author jwen
 * Date 2025-09-06
 */
public class ReferenceTypesDemo {

    private static final int MB = 1024 * 1024;

    public static void main(String[] args) {
        System.out.println("========== Java四种引用类型演示 ==========\n");

        // 1. 强引用演示
        demonstrateStrongReference();

        // 2. 软引用演示
        demonstrateSoftReference();

        // 3. 弱引用演示
        demonstrateWeakReference();

        // 4. 虚引用演示
        demonstratePhantomReference();
    }

    /**
     * 强引用演示 - 默认引用类型
     * 特点：只要强引用存在，对象永远不会被垃圾回收
     */
    private static void demonstrateStrongReference() {
        System.out.println("------------------- 强引用 (Strong Reference) -------------------");
        
        // 创建强引用
        Object strongRef = new Object();
        System.out.println("创建强引用对象: " + strongRef);
        
        // 强引用存在时，对象不会被回收
        System.gc();
        System.out.println("执行GC后，强引用对象仍然存在: " + strongRef);
        
        // 断开强引用后，对象可以被回收
        strongRef = null;
        System.gc();
        System.out.println("断开强引用并执行GC后，对象被回收");
        System.out.println();
    }

    /**
     * 软引用演示
     * 特点：内存不足时会被回收，适合做缓存
     */
    private static void demonstrateSoftReference() {
        System.out.println("------------------- 软引用 (Soft Reference) -------------------");
        
        // 创建软引用
        SoftReference<byte[]> softRef = new SoftReference<>(new byte[10 * MB]);
        System.out.println("创建软引用对象，大小: 10MB");
        System.out.println("软引用对象: " + softRef.get());
        
        // 内存充足时，软引用不会被回收
        System.gc();
        System.out.println("内存充足时执行GC，软引用仍然存在: " + (softRef.get() != null));
        
        // 模拟内存不足的情况
        try {
            List<byte[]> memoryList = new ArrayList<>();
            while (true) {
                memoryList.add(new byte[10 * MB]);
                if (softRef.get() == null) {
                    System.out.println("内存不足，软引用对象被回收！");
                    break;
                }
            }
        } catch (OutOfMemoryError e) {
            System.out.println("内存溢出，软引用对象已被回收");
        }
        System.out.println();
    }

    /**
     * 弱引用演示
     * 特点：只要发生垃圾回收就会被回收
     */
    private static void demonstrateWeakReference() {
        System.out.println("------------------- 弱引用 (Weak Reference) -------------------");
        
        // 创建弱引用
        String strongRef = new String("弱引用测试");
        WeakReference<String> weakRef = new WeakReference<>(strongRef);
        System.out.println("创建弱引用对象: " + weakRef.get());
        
        // 执行垃圾回收
        strongRef = null;
        System.out.println("强应用断开，垃圾回收没触发: " + weakRef.get());
        System.gc();
        System.out.println("执行GC后，弱引用对象: " + weakRef.get());
        
        // 再次创建弱引用并立即回收
        weakRef = new WeakReference<>(new String("新的弱引用"));
        System.out.println("新弱引用对象: " + weakRef.get());

        // 没有强引用指向String对象
        // 强制垃圾回收
        System.gc();
        
        System.out.println("再次执行GC后，弱引用对象: " + weakRef.get());
        System.out.println();
    }

    /**
     * 虚引用演示
     * 特点：随时可能被回收，主要用于跟踪对象被回收的状态
     */
    private static void demonstratePhantomReference() {
        System.out.println("------------------- 虚引用 (Phantom Reference) -------------------");
        
        // 创建引用队列
        ReferenceQueue<Object> referenceQueue = new ReferenceQueue<>();
        
        // 创建虚引用
        Object obj = new Object();
        PhantomReference<Object> phantomRef = new PhantomReference<>(obj, referenceQueue);
        
        System.out.println("创建虚引用对象");
        System.out.println("虚引用get()始终返回null: " + phantomRef.get());
        
        // 创建监控线程
        Thread monitorThread = new Thread(() -> {
            try {
                System.out.println("监控线程启动，等待对象被回收...");
                Reference<?> ref = referenceQueue.remove();
                if (ref == phantomRef) {
                    System.out.println("虚引用对象已被垃圾回收器回收！");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        monitorThread.setDaemon(true);
        monitorThread.start();
        
        // 断开强引用
        obj = null;
        
        // 强制垃圾回收
        System.gc();
        
        // 等待监控线程执行
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        System.out.println("虚引用演示完成");
        System.out.println();
    }
}