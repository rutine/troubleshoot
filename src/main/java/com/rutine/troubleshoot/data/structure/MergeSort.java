package com.rutine.troubleshoot.data.structure;

import java.util.Arrays;

/**
 * 归并排序
 *
 * @author rutine
 * @date 2021/4/25 11:00
 */
public class MergeSort {

    public static void main(String[] args) {
        int[] source = {6, 8, 7, 1, 2, 4, 10, 14, 20, 15, 3, 5, 9, 21, 11, 18, 17};
        int[] dest = new int[source.length];
        dest = sort(source, dest);
        System.out.println(Arrays.toString(dest));
    }

    // w <= 2 * j
    //合并两组有序数据
    static void merge(int[] source, int i, int j, int w, int[] dest) {
        int iLen = j;
        int jLen = w;
        int h = i;
        while (i < iLen && j < jLen) {
            dest[h++] = source[i] <= source[j] ? source[i++] : source[j++];
        }
        while (i < iLen) {
            dest[h++]  = source[i++];
        }
        while (j < jLen) {
            dest[h++]  = source[j++];
        }
    }

    //多组有序数据
    static void mergeTrip(int[] source, int[] dest, int w) {
        int i = 0;
        int len = source.length;
        while ((i + 2 * w) <= len) {
            merge(source, i, i + w, i + 2 * w, dest);
            i = i + 2 * w;
        }

        if ((i + w) <= len) {
            merge(source, i, i + w, len, dest);
        } else {
            merge(source, i, len, 0, dest);
        }
    }

    static int[] sort(int[] source, int[] dest) {
        int i = 1;
        while ((2 * i) < source.length) {
            mergeTrip(source, dest, i);
            i = 2 * i;

            mergeTrip(dest, source, i);
            i = 2 * i;
        }

        if (i < source.length) {
            mergeTrip(source, dest, i);
        } else {
            dest = source;
        }

        return dest;
    }

}
