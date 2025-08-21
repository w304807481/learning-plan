package com.github.opensharing.algorithm.random;

/**
 * 根据1-5 等概率函数f()， 求1-7等概率函数
 */
public class RandomTransferTester {

    public static void main(String[] args) {

        //给定1-5等概率函数f，求1-7等概率函数
        int[] count = new int[8];
        int round = 100_0000;

        for (int i = 0; i < round; i++) {
            count[f4()]++;
        }

        for (int i = 0; i < count.length; i++) {
            System.out.println(i + ": " + count[i]);
        }
    }


    /**
     * P: 1-5 等概率
     */
    private static int f() {
        return (int)(Math.random() * 5) + 1;
    }

    /**
     * 求 0,1等概率函数
     */
    public static int f2() {
        int result;
        do {
            result = f();
        } while (result == 3);

        return result <= 2 ? 0 : 1;
    }

    /**
     * 求0~7等概率
     */
    public static int f3() {
        return (f2() << 2) + (f2() << 1) + f2();
    }

    /**
     * 求1-7等概率
     */
    public static int f4() {
        int result;
        do {
            result = f3();
        } while (result == 0);
        return result;
    }
}
