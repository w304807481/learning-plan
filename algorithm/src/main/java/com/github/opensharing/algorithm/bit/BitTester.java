package com.github.opensharing.algorithm.bit;

public class BitTester {
    public static void main(String[] args) {
        printBit(10);
        printBit(Integer.MAX_VALUE);
        printBit(Integer.MIN_VALUE);
    }

    public static void printBit(int n) {
        for (int i = 31; i >=0; i--) {
            System.out.print((n & 1<<i) > 0 ? "1" : "0");
        }
        System.out.println();
    }
}
