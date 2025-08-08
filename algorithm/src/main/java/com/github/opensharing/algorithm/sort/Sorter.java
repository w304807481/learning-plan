package com.github.opensharing.algorithm.sort;

/**
 * 排序器
 *
 * @author jwen
 * Date 2025/8/7
 */
public class Sorter {

    /**
     * 选择排序
     *
     * 通俗理解: 内循环一轮选出剩余待排序的元素中最小的，跟待排序第一个元素交换
     *
     * @param arr
     * @return
     */
    public static int[] selectSort(int[] arr) {

        for (int i = 0; i < arr.length; i++) {
            int minIndex = i;

            //寻找最新index
            for (int j = i+1; j < arr.length; j++) {
                minIndex = arr[j] < arr[minIndex] ? j : minIndex;
            }

            swap(arr, i, minIndex);
            //System.out.println(Arrays.toString(arr));
        }

        return arr;
    }

    /**
     * 冒泡排序
     *
     * 通俗理解: 内循环一次，0-(i-1)中大的元素跟右侧交互， 直到冒泡到数组最右边
     *
     * @param arr
     * @return
     */
    public static int[] bubbleSort(int[] arr) {
        for (int i = arr.length-1; i >= 0; i--) {

            for (int j = 0; j < i; j++) {

                if (arr[j] > arr[j+1]) {
                    swap(arr, j, i);
                    //swap(arr, j, j+1);
                }

                //System.out.println(Arrays.toString(arr));
            }
        }

        return arr;
    }

    /**
     * 插入排序
     *
     * 通俗理解: 内循环i次，保障从左到右的i个元素是有序
     *
     * @param arr
     * @return
     */
    public static int[] insertSort(int[] arr) {

        for (int i = 0; i < arr.length; i++) {

            for (int j = i; j > 0; j--) {

                if (arr[j] < arr[j-1]) {
                    swap(arr, j , j-1);
                }
                //System.out.println( i + " " + Arrays.toString(arr));
            }
        }

        return arr;
    }

    private static void swap(int[] arr, int x, int y) {
        int temp = arr[x];
        arr[x] = arr[y];
        arr[y] = temp;
    }
}
