package com.github.opensharing.algorithm.random;

/**
 * 根据Math.random()，求x，x2等概率函数
 */
public class RandomPxTester {
    public static void main(String[] args) {
        testRandomPx();
        testRandomPx2();
        testRandomP1_$1_x$2();
    }

    /**
     * P:x
     */
    public static void testRandomPx() {
        double limit = 0.3;
        int count = 0;
        int round = 100_0000;
        for (int i = 0; i < round; i++) {

            if (Math.random() < limit) {
                count++;
            }
        }

        System.out.print(limit + " P: " + limit);
        System.out.print(" ---> ");
        System.out.println(limit + " count: " + (count / (round * 1.0)));
    }

    /**
     * P:x2
     */
    public static void testRandomPx2() {
        double limit = 0.4;
        int count = 0;
        int round = 100_0000;

        for (int i = 0; i < round; i++) {

            double xpower2 = Math.max(Math.random(), Math.random());

            if (xpower2 < limit) {
                count++;
            }
        }

        System.out.print(limit + " P: " + Math.pow(limit, 2));
        System.out.print(" ---> ");
        System.out.println("count: " + (count / (round * 1.0)));
    }

    /**
     * P:1-(1-x)2
     */
    public static void testRandomP1_$1_x$2() {
        double limit = 0.4;
        int count = 0;
        int round = 100_0000;

        for (int i = 0; i < round; i++) {

            double p1_$1_x$2 = Math.min(Math.random(), Math.random());

            if (p1_$1_x$2 < limit) {
                count++;
            }
        }

        System.out.print(limit + " P: " + (1 - Math.pow((1 - limit), 2)));
        System.out.print(" ---> ");
        System.out.print("count: " + (count / (round * 1.0)));
    }
}
