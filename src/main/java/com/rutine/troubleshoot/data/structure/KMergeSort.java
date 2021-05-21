package com.rutine.troubleshoot.data.structure;

/**
 * k路归并 - 败者树
 *
 * @author rutine
 * @date 2021/4/27 16:14
 */
public class KMergeSort {

    public static void main(String[] args) {
        int[][] arr = {{1, 4}, {5, 7, 11}, {2, 6, 9, 21, 35}, {3, 10, 15, 22}, {8, 12, 16, 25}};

        sort(arr);
    }

    static void sort(int[][] arr) {
        int k = 5;
        int[] losers = new int[k];
        int[] pos = new int[k];
        for (int i = k - 1; i >= 0; i--) {
            adjustLoser(losers, k, i, arr, pos);
        }

        int v = losers[0];
        while(pos[v] < arr[v].length) {
            System.out.print(arr[v][pos[v]]);
            System.out.print(' ');

            pos[v]++; //胜利者数组位置往前移一位
            adjustLoser(losers, k, v, arr, pos);
            v = losers[0];
        }
    }

    static void adjustLoser(int[] losers, int k, int v, int[][] arr, int[] pos) {
        int p = (k + v) / 2;  //父节点
        while (p > 0) {
            int loser = losers[p];
            if (pos[loser] >= arr[loser].length) { //k路已完成所有输出

            }
            else if (pos[v] >= arr[v].length //k路已完成所有输出, 记为失败
                    || arr[v][pos[v]] > arr[loser][pos[loser]]) {
                losers[p] = v;
                v = loser;
            }
            p = p / 2;
        }

        //0位置指向胜利者
        losers[p] = v;
    }

//    static void sort(int[][] arr) {
//        int k = 5;
//        Loser[] losers = new Loser[k];
//        for (int i = 0; i < k; i++) {
//            losers[i] = new Loser();
//        }
//        for (int i = k - 1; i >= 0; i--) {
//            adjustLoser(losers, arr, k, i);
//        }
//
//        int i = losers[0].k;
//        while(losers[i].pos < arr[i].length) {
//            System.out.print(arr[i][losers[i].pos]);
//            System.out.print(' ');
//
//            losers[i].pos++; //胜利者数组位置往前移一位
//            adjustLoser(losers, arr, k, i);
//            i = losers[0].k;
//        }
//    }
//
//    static void adjustLoser(Loser[] losers, int[][] arr, int k, int v) {
//        int p = (k + v) / 2;  //父节点
//        while (p > 0) {
//            if (losers[p].k == -1) { //初始化
//                losers[p].k = v;
//                break;
//            }
//
//            int f = losers[p].k;
//            int fi = losers[f].pos; //k路数组下标
//            int vi = losers[v].pos;
//            if (fi >= arr[f].length) { //k路已完成所有输出
//
//            }
//            else if (vi >= arr[v].length //k路已完成所有输出, 记为失败
//                    || arr[v][vi] > arr[f][fi]) {
//                losers[p].k = v;
//                v = f;
//            }
//            p = p / 2;
//        }
//
//        //0位置指向胜利者
//        losers[p].k = v;
//    }
//
//    static class Loser {
//        int k = -1; //失败者编号
//        int pos = 0; //失败者数组位置
//    }
}
