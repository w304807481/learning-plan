package com.github.opensharing.algorithm.sum;

/**
 * 斐波那契数列
 */
public class FactorialTester {

    public static void main(String[] args) {
        System.out.println(factorial(4));
        System.out.println(factorial(10));
    }

    private static int factorial(int n) {
        int total = 0;
        int current = 1;

        for (int j = 1; j <= n; j++) {
            current = current * j;
            total += current;
        }

        return total;
    }
}
