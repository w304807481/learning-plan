package com.github.opensharing.algorithm.p;

public class FunctionPxTransferTester {

    public static void main(String[] args) {
        //给定0,1不等概率函数f, 求0，1等概率函数g
        int[] count = new int[2];
        int round = 100_0000;

        for (int i = 0; i < round; i++) {
            count[g1()]++;
        }

        for (int i = 0; i < count.length; i++) {
            System.out.println(i + ": " + count[i]);
        }
    }

    /**
     * 0,1不等概率函数
     *
     * @return
     */
    public static int f() {
        return Math.random() < 0.7 ? 0 : 1;
    }

    /**
     * 求等概率
     * 00 不等概率
     * 01 等概率
     * 10 等概率
     * 11 不等概率
     *
     * @return
     */
    public static int g() {
        int result;
        do {
            result = (f() << 1) + f();
        } while ((result == 0) || (result == 3));
        return result == 1 ? 0 : 1;
    }

    /**
     * 优化版
     *
     * @return
     */
    public static int g1() {
        int result;
        do {
            result = f();
        } while (result == f());
        return result;
    }
}
