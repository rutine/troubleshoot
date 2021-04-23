package com.rutine.troubleshoot.data.structure;

import java.util.Arrays;

/**
 * 堆排序, 每个子树的根节点不大于左右节点
 *
 * @author rutine
 * @date 2021/4/19 11:29
 */
public class HeapSort {

    public static void main(String[] args) {
        int[] arr = {8, 2, 4, 3, 6, 7, 5, 9, 1};
        sort(arr);
        System.out.println(Arrays.toString(arr));
    }

    static void sort(int[] arr) {
        int n = arr.length;
        //初建堆
        for (int i = (n / 2 - 1); i >= 0; i--) {
            sift(arr, i, n - 1);
        }

        for (int i = 0; i < (n - 1); i++) {
            //交换第一个和第(n-i-1)个
            int tmp = arr[0];
            arr[0] = arr[n - i - 1];
            arr[n - i - 1] = tmp;

            //从0 -> n-i-2重新筛选
            sift(arr, 0, n - i - 2);
        }
    }

    //筛
    static void sift(int[] arr, int start, int end) {
       int i = start;
       int j = 2 * i + 1;
       int k = arr[i];
       while (j <= end) {
           if (j < end && k <= arr[j]) {
               j++;
           }

           if (k > arr[j]) {
               arr[i] = arr[j];
               i = j;
               j = 2 * i + 1;
           } else {
               break;
           }
       }

       arr[i] = k;
    }
}
