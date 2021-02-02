package com.github.opensharing.algorithm.lru;

/**
 * Hello world!
 */
public class LRUTester {

    public static void main(String[] args) {
        LRUCacher cacher = new LRUCacher(3);

        cacher.put("1", "data1");
        System.out.println("\n put(1) cacher: " + cacher);

        cacher.put("2", "data2");
        System.out.println("\n put(2) cacher: " + cacher);

        cacher.put("3", "data3");
        System.out.println("\n put(3) cacher: " + cacher);
        
        cacher.put("4", "data4");
        System.out.println("\n put(4) cacher: " + cacher);

        cacher.put("2", "data2");
        System.out.println("\n put(2) cacher: " + cacher);

        cacher.get("4");
        System.out.println("\n get(4) cacher: " + cacher);

        cacher.get("3");
        System.out.println("\n get(3) cacher: " + cacher);

        cacher.put("5", "data5");
        System.out.println("\n put(5) cacher: " + cacher);
    }
}
