package com.rutine.troubleshoot.data.structure;

/**
 * 最大子串, 滑动窗口原理
 *
 * @author rutine
 * @date 2020/7/2 16:28
 */
public class MaxSubstring {

    public static void main(String[] args) throws Exception {
        System.out.println(find("abc 13 abcd 1b abcde"));
    }

    public static String find(String src) {
        int len = src.length();
        int preWinStart = 0;
        int preWinEnd = 0;
        int curWinStart = 0;
        for (int i = 1; i < len; i++) {
            for (int j = i - 1; j >= curWinStart; j--) {
                //找到重复的
                if (src.charAt(j) == src.charAt(i)) {
                    //当前滑动窗口比之前的大, 替换
                    if ((preWinEnd - preWinStart) < (i - curWinStart)) {
                        preWinStart = curWinStart;
                        preWinEnd = i;
                    }
                    curWinStart = j + 1;
                    break;
                }
            }
        }
        if ((preWinEnd - preWinStart) < (len - curWinStart)) {
            preWinStart = curWinStart;
            preWinEnd = len;
        }

        return src.substring(preWinStart, preWinEnd);
    }
}
