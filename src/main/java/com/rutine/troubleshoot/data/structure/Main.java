package com.rutine.troubleshoot.data.structure;

import java.util.Scanner;

/**
 * @author rutine
 * @date 2020/6/15 9:04
 */
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        long input = scanner.nextLong();
        int j = (int) Math.sqrt(input) + 1;
        int i = 2;
        while (input != 1 && i < j) {
            if (input % i == 0) {
                System.out.println(i + " ");
                input /= i;
                i = 2;
            } else {
                i++;
            }
        }

        if (i > j) {
            System.out.println(input + " ");
        }
        scanner.close();
    }

    public static int calcRabbit(int month) {
//        if (month < 4) return month == 0 ? 0 : 1;
//        return get(month - 1) + get(month - 3);

        int sum = 0;
        if (month < 4) {
            return month == 0 ? 0 : 1;
        } else {
            //第4个月生兔子，记录新生的兔子的数量
            month -= 3;
            sum = calcRabbit(month);//计录孩子生了多少
            //加回月数，算出最开始那只兔子下一月的生孩子情况
            month += 3;
            return sum + calcRabbit(month - 1);//加上最初的兔子生的数量
        }
        //其实，每个函数，每个大哥或二弟，不是都是只管一只兔子吗？
    }

    public static void lostHalfPerTime(double h, int n) {
        if (n == 1) {
            System.out.println(h);
        }

        double sum = h;
        for (int i = 2; i <= n; i++) {
            h = h / 2D;
            sum += h * 2;
        }

        System.out.println(sum);
        System.out.println(h);
    }
}
