package com.github.opensharing.javabase.reference;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/**
 * 引用类型在缓存中的应用演示
 * 展示如何使用软引用和弱引用实现内存友好的缓存
 *
 * @author jwen
 * Date 2025-09-06
 */
public class ReferenceCacheDemo {

    public static void main(String[] args)  {
        System.out.println("========== 引用类型在缓存中的应用演示 ==========\n");

        // 1. 软引用缓存演示
        demonstrateSoftReferenceCache();

        // 2. 弱引用缓存演示
        demonstrateWeakReferenceCache();

        // 3. 对比不同引用类型的缓存行为
        compareReferenceCaches();
    }

    /**
     * 软引用缓存演示
     * 内存不足时自动清理，适合内存敏感的缓存
     */
    private static void demonstrateSoftReferenceCache() {
        System.out.println("------------------- 软引用缓存 (SoftReference Cache) -------------------");
        
        SoftReferenceCache<String, byte[]> cache = new SoftReferenceCache<>();
        
        // 添加缓存数据
        cache.put("data1", new byte[5 * 1024 * 1024]); // 5MB
        cache.put("data2", new byte[5 * 1024 * 1024]); // 5MB
        
        System.out.println("缓存数据1: " + (cache.get("data1") != null ? "存在" : "已被回收"));
        System.out.println("缓存数据2: " + (cache.get("data2") != null ? "存在" : "已被回收"));
        
        // 模拟内存压力
        try {
            byte[] largeData = new byte[50 * 1024 * 1024]; // 50MB
            System.out.println("分配大内存后:");
            System.out.println("缓存数据1: " + (cache.get("data1") != null ? "存在" : "已被回收"));
            System.out.println("缓存数据2: " + (cache.get("data2") != null ? "存在" : "已被回收"));
        } catch (OutOfMemoryError e) {
            System.out.println("缓存数据1: " + (cache.get("data1") != null ? "存在" : "已被回收"));
            System.out.println("缓存数据2: " + (cache.get("data2") != null ? "存在" : "已被回收"));
            System.out.println("内存不足，软引用缓存数据已被回收");
        }
        
        System.out.println();
    }

    /**
     * 弱引用缓存演示
     * GC时自动清理，适合临时缓存
     */
    private static void demonstrateWeakReferenceCache()  {
        System.out.println("------------------- 弱引用缓存 (WeakReference Cache) -------------------");
        
        WeakReferenceCache<String, Object> cache = new WeakReferenceCache<>();
        
        // 添加缓存数据 - 使用new String()避免字符串常量池问题
        cache.put("key1", new String("弱引用值1"));
        cache.put("key2", new String("弱引用值2"));
        
        System.out.println("GC前 - key1: " + cache.get("key1"));
        System.out.println("GC前 - key2: " + cache.get("key2"));
        
        // 强制垃圾回收
        System.gc();
        
        // 等待一下，确保GC完成
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        System.out.println("GC后 - key1: " + cache.get("key1"));
        System.out.println("GC后 - key2: " + cache.get("key2"));
        System.out.println();
    }

    /**
     * 对比不同引用类型的缓存行为
     */
    private static void compareReferenceCaches() {
        System.out.println("------------------- 引用类型缓存行为对比 -------------------");
        
        // 创建三种缓存
        Map<String, Object> strongCache = new HashMap<>();
        SoftReferenceCache<String, byte[]> softCache = new SoftReferenceCache<>();
        WeakReferenceCache<String, Object> weakCache = new WeakReferenceCache<>();
        
        // 添加数据 - 弱引用使用new String()避免字符串常量池问题
        strongCache.put("strong", new byte[1024 * 1024]);
        softCache.put("soft", new byte[1024 * 1024]);
        weakCache.put("weak", new String("弱引用测试数据"));
        
        System.out.println("初始状态:");
        System.out.println("强引用缓存: " + (strongCache.get("strong") != null ? "存在" : "不存在"));
        System.out.println("软引用缓存: " + (softCache.get("soft") != null ? "存在" : "不存在"));
        System.out.println("弱引用缓存: " + (weakCache.get("weak") != null ? "存在" : "不存在"));
        
        // 执行GC
        System.gc();
        
        System.out.println("\n执行GC后:");
        System.out.println("强引用缓存: " + (strongCache.get("strong") != null ? "存在" : "不存在"));
        System.out.println("软引用缓存: " + (softCache.get("soft") != null ? "存在" : "不存在"));
        System.out.println("弱引用缓存: " + (weakCache.get("weak") != null ? "存在" : "不存在"));
    }
}

/**
 * 软引用缓存实现
 */
class SoftReferenceCache<K, V> {
    private final Map<K, SoftReference<V>> cache = new HashMap<>();
    private final ReferenceQueue<V> queue = new ReferenceQueue<>();

    public void put(K key, V value) {
        cleanUp();
        cache.put(key, new SoftReference<>(value, queue));
    }

    public V get(K key) {
        cleanUp();
        SoftReference<V> ref = cache.get(key);
        if (ref != null) {
            V value = ref.get();
            if (value == null) {
                cache.remove(key);
            }
            return value;
        }
        return null;
    }

    private void cleanUp() {
        Reference<? extends V> ref;
        while ((ref = queue.poll()) != null) {
            final Reference<? extends V> finalRef = ref;
            cache.values().removeIf(softRef -> softRef == finalRef);
        }
    }
}

/**
 * 弱引用缓存实现
 */
class WeakReferenceCache<K, V> {
    private final Map<K, WeakReference<V>> cache = new HashMap<>();
    private final ReferenceQueue<V> queue = new ReferenceQueue<>();

    public void put(K key, V value) {
        cleanUp();
        cache.put(key, new WeakReference<>(value, queue));
    }

    public V get(K key) {
        cleanUp();
        WeakReference<V> ref = cache.get(key);
        if (ref != null) {
            V value = ref.get();
            if (value == null) {
                cache.remove(key);
            }
            return value;
        }
        return null;
    }

    private void cleanUp() {
        Reference<? extends V> ref;
        while ((ref = queue.poll()) != null) {
            final Reference<? extends V> finalRef = ref;
            cache.values().removeIf(weakRef -> weakRef == finalRef);
        }
    }
}