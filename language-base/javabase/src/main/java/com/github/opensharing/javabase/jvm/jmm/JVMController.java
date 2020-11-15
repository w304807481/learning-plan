package com.github.opensharing.javabase.jvm.jmm;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * JVM
 * VM options：-Xms50M -Xmx50M
 * 请单线程模拟操作
 *
 * @author jwen
 * Date 2020-09-06
 */
@RestController
@RequestMapping("/jvm")
public class JVMController {

    /**
     * 模拟OOM
     */
    public static Map<Integer, Byte[]> heapObjects = new LinkedHashMap<Integer, Byte[]>() {
        /**
         * 调整大小， 及时释放对象，使得对象可回收
         */
        private int lru = 10;

        @Override
        protected boolean removeEldestEntry(Map.Entry<Integer, Byte[]> eldest) {
            System.out.println("removeEldestEntry=========heapObjects.size:" + this.size());
            //LRU
            return this.size() >= lru;
        }
    };

    /**
     * 模拟创建对象，消耗heap
     *
     * @param loop
     * @return
     * @throws Exception
     */
    @GetMapping("/object/new/{loop}")
    public String start(@PathVariable("loop") int loop) throws Exception {
        int size = heapObjects.size();
        while (loop > 0) {
            System.out.println("object/new=========heapObjects.size:" + heapObjects.size());
            Byte[] heap1MObj = new Byte[1024 * 1024];
            heapObjects.put(++size, heap1MObj);
            Thread.sleep(50L);
            loop--;
        }
        return "object/new=========heapObjects.size:" + heapObjects.size();
    }

    /**
     * 模拟对象使用完毕，主动断开引用关系
     *
     * @param loop
     * @return
     * @throws Exception
     */
    @GetMapping("/object/gc/{loop}")
    public String gc(@PathVariable("loop") int loop) throws Exception {
        int size = heapObjects.size();
        while (loop > 0) {
            System.out.println("object/gc=========heapObjects.size:" + heapObjects.size());
            heapObjects.remove(size--);
            loop--;
        }
        return "object/gc=========heapObjects.size:" + heapObjects.size();
    }
}
