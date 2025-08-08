package com.github.opensharing.algorithm.sort;

import java.util.Arrays;

/**
 * Sorter测试类
 *
 * @author jwen
 * Date 2025/8/7
 */
public class SorterTester {

    public static void main(String[] args) {
        int[] arr1 = new int[]{5,7,3,8,0,1};
        System.out.println(Arrays.toString(Sorter.selectSort(arr1)));

        int[] arr2 = new int[]{5,7,3,8,0,1};
        System.out.println(Arrays.toString(Sorter.bubbleSort(arr2)));

        int[] arr3 = new int[]{5,7,3,8,0,1};
        System.out.println(Arrays.toString(Sorter.insertSort(arr3)));
    }
}
