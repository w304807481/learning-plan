package com.github.opensharing.javabase.reference;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * ThreadLocal内存泄露演示
 * 展示不正确使用ThreadLocal如何导致内存泄露以及正确的使用方法
 *
 * @author jwen
 * Date 2025-09-06
 */
public class ThreadLocalMemoryLeakDemo {

    private static final int MB = 1024 * 1024;
    
    // 错误的ThreadLocal使用方式 - 会导致内存泄露
    private static final ThreadLocal<byte[]> BAD_THREAD_LOCAL = new ThreadLocal<>();
    
    // 正确的ThreadLocal使用方式
    private static final ThreadLocal<byte[]> GOOD_THREAD_LOCAL = new ThreadLocal<>();

    public static void main(String[] args) {
        System.out.println("========== ThreadLocal内存泄露演示 ==========\n");

        // 1. 演示ThreadLocal内存泄露问题
        demonstrateMemoryLeak();

        // 2. 演示正确的ThreadLocal使用方法
        demonstrateCorrectUsage();

        // 3. 演示ThreadLocal内存泄露的检测方法
        demonstrateMemoryLeakDetection();

        // 4. 提供ThreadLocal使用最佳实践
        demonstrateBestPractices();
    }

    /**
     * 演示ThreadLocal内存泄露问题
     * 在线程池环境中，线程不会销毁，ThreadLocal变量会持续存在
     */
    private static void demonstrateMemoryLeak() {
        System.out.println("------------------- ThreadLocal内存泄露演示 -------------------");
        
        // 模拟线程池环境
        List<Thread> threadPool = new ArrayList<>();
        
        // 创建5个线程模拟线程池
        for (int i = 0; i < 5; i++) {
            final int threadId = i;
            Thread thread = new Thread(() -> {
                System.out.println("线程 " + threadId + " 开始执行");
                
                // 错误的使用方式：设置大对象但不清理
                byte[] largeData = new byte[10 * MB]; // 10MB
                BAD_THREAD_LOCAL.set(largeData);
                
                System.out.println("线程 " + threadId + " 设置了ThreadLocal数据: " + largeData.length + " bytes");
                
                // 模拟业务处理
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                
                // 注意：这里没有调用remove()，导致内存泄露
                System.out.println("线程 " + threadId + " 执行完成，但没有清理ThreadLocal");
            });
            
            threadPool.add(thread);
            thread.start();
        }
        
        // 等待所有线程执行完成
        for (Thread thread : threadPool) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        
        System.out.println("所有线程执行完成");
        System.out.println("由于线程可能被重用，ThreadLocal中的数据可能导致内存泄露");
        System.out.println();
    }

    /**
     * 演示正确的ThreadLocal使用方法
     */
    private static void demonstrateCorrectUsage() {
        System.out.println("------------------- 正确的ThreadLocal使用方法 -------------------");
        
        // 模拟线程池环境
        List<Thread> threadPool = new ArrayList<>();
        
        // 创建5个线程
        for (int i = 0; i < 5; i++) {
            final int threadId = i;
            Thread thread = new Thread(() -> {
                try {
                    System.out.println("线程 " + threadId + " 开始执行");
                    
                    // 正确的使用方式：设置数据
                    byte[] data = new byte[5 * MB]; // 5MB
                    GOOD_THREAD_LOCAL.set(data);
                    
                    System.out.println("线程 " + threadId + " 设置了ThreadLocal数据: " + data.length + " bytes");
                    
                    // 使用数据
                    byte[] localData = GOOD_THREAD_LOCAL.get();
                    System.out.println("线程 " + threadId + " 使用ThreadLocal数据: " + (localData != null ? "存在" : "不存在"));
                    
                    // 模拟业务处理
                    Thread.sleep(100);
                    
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    // 关键：在finally块中清理ThreadLocal
                    GOOD_THREAD_LOCAL.remove();
                    System.out.println("线程 " + threadId + " 执行完成，已清理ThreadLocal");
                }
            });
            
            threadPool.add(thread);
            thread.start();
        }
        
        // 等待所有线程执行完成
        for (Thread thread : threadPool) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        
        System.out.println("所有线程执行完成，ThreadLocal已正确清理");
        System.out.println();
    }

    /**
     * 演示ThreadLocal内存泄露的检测方法
     */
    private static void demonstrateMemoryLeakDetection() {
        System.out.println("------------------- ThreadLocal内存泄露检测 -------------------");
        
        // 创建一个自定义的ThreadLocal类，便于监控
        ThreadLocal<LargeObject> leakingThreadLocal = new ThreadLocal<>();
        
        Thread thread = new Thread(() -> {
            System.out.println("检测线程开始执行");
            
            // 创建大对象
            LargeObject largeObject = new LargeObject(20 * MB);
            leakingThreadLocal.set(largeObject);
            
            System.out.println("设置了大型对象: " + largeObject.size + " bytes");
            
            // 获取并检查对象
            LargeObject retrieved = leakingThreadLocal.get();
            System.out.println("检索到的对象: " + (retrieved != null ? "存在" : "不存在"));
            
            // 模拟内存泄露：不清理ThreadLocal
            // 在实际应用中，可以通过内存监控工具检测这种泄露
            
            System.out.println("线程执行完成，但未清理ThreadLocal（模拟内存泄露）");
        });
        
        thread.start();
        
        try {
            thread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // 强制垃圾回收，演示对象可能仍然存在
        System.gc();
        
        System.out.println("垃圾回收完成，但ThreadLocal中的对象可能仍然存在");
        System.out.println("在实际应用中，需要使用内存分析工具（如VisualVM、MAT）来检测这类泄露");
        System.out.println();
    }

    /**
     * ThreadLocal使用最佳实践演示
     */
    private static void demonstrateBestPractices() {
        System.out.println("------------------- ThreadLocal最佳实践 -------------------");
        
        // 1. 使用try-finally模式
        ThreadLocal<String> threadLocal = new ThreadLocal<>();
        
        Thread thread1 = new Thread(() -> {
            try {
                threadLocal.set("重要数据");
                System.out.println("线程1处理数据: " + threadLocal.get());
                // 业务逻辑...
            } finally {
                threadLocal.remove(); // 确保清理
                System.out.println("线程1: ThreadLocal已清理");
            }
        });
        
        // 2. 使用静态工厂方法初始化
        ThreadLocal<Integer> counter = ThreadLocal.withInitial(() -> 0);
        
        Thread thread2 = new Thread(() -> {
            try {
                counter.set(counter.get() + 1);
                System.out.println("线程2计数器: " + counter.get());
            } finally {
                counter.remove();
                System.out.println("线程2: 计数器已清理");
            }
        });
        
        // 3. 使用WeakReference避免强引用
        ThreadLocal<WeakReference<Object>> weakThreadLocal = new ThreadLocal<>();
        
        Thread thread3 = new Thread(() -> {
            try {
                Object data = new Object();
                weakThreadLocal.set(new WeakReference<>(data));
                System.out.println("线程3使用弱引用存储数据");
            } finally {
                weakThreadLocal.remove();
                System.out.println("线程3: 弱引用ThreadLocal已清理");
            }
        });
        
        // 启动所有线程
        thread1.start();
        thread2.start();
        thread3.start();
        
        try {
            thread1.join();
            thread2.join();
            thread3.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        System.out.println("\nThreadLocal最佳实践总结:");
        System.out.println("1. 始终在finally块中调用remove()方法");
        System.out.println("2. 使用ThreadLocal.withInitial()提供初始值");
        System.out.println("3. 避免存储大对象，考虑使用弱引用");
        System.out.println("4. 在线程池环境中特别小心内存泄露");
        System.out.println("5. 定期使用内存分析工具检查泄露");
        System.out.println();
    }

    /**
     * 大对象类，用于演示内存占用
     */
    static class LargeObject {
        private final int size;
        private final byte[] data;
        
        public LargeObject(int size) {
            this.size = size;
            this.data = new byte[size];
        }
        
        // 注意：finalize()方法已过时，这里仅用于演示目的
        // 在实际应用中，应该使用Cleaner或AutoCloseable等现代机制
    }
}