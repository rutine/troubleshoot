package com.rutine.troubleshoot.data.structure;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Queue;

/**
 * @author rutine
 * @date 2020/6/15 9:07
 */
public class FourFundamentalOperation {
    public static void main(String[] args) throws Exception {
        String str = Integer.toBinaryString( 0x80000001);
        System.out.println(str.length());
        System.out.println(str);
        System.out.println(0x7fffffff);
        System.out.println(0x80000000);
        int q = 10;
        System.out.println(((q << 6) + (q << 5) + (q << 2)));
        System.out.println(numberZeroLeading(1));

        calc("42+2*(3-1+(5+2*(2+2)))+3+1*2-3/1");
    }

    public static int numberZeroLeading(int a) {
        if (a == 0) {
            return 32;
        }
        int n = 1;
        int x = a;
        if ((x >>> 16) == 0) { n += 16; x <<= 16; }
        if ((x >>> 24) == 0) { n += 8; x <<= 8; }
        if ((x >>> 28) == 0) { n += 4; x <<= 4; }
        if ((x >>> 30) == 0) { n += 2; x <<= 2; }
        n -= (x >>> 31);

        return n;
    }

    public static void calc(String express) {
        Queue<Object> queue = new ArrayDeque<>();
        parse(express, queue);

        Deque<Double> operands = new ArrayDeque<>();
        while (!queue.isEmpty()) {
            Object obj = queue.poll();
            if (obj instanceof Double) {
                operands.push((Double) obj);
            } else {
                Double first = operands.pop();
                Double second = operands.pop();
                switch (((Character) obj)) {
                    case '+':
                        operands.push(first + second);
                        break;
                    case '-':
                        operands.push(second - first);
                        break;
                    case '*':
                        operands.push(first * second);
                        break;
                    case '/':
                        operands.push(second / first);
                        break;
                }
            }
        }

        System.out.println(operands.peek());
        System.out.println(42+2*(3-1+5+2*(2+2))+3+1*2-3/1);
    }

    static void parse(String exp, Queue<Object> queue) {
        Deque<Character> stack = new ArrayDeque<>();
        StringBuilder operand = new StringBuilder();
        for (int i = 0; i < exp.length(); i++) {
            char ch = exp.charAt(i);
            if (Character.isDigit(ch)) {
                operand.append(ch);
            }
            else if (ch == ' ') {
                continue;
            }
            else if (ch == '+' || ch == '-' || ch == '*' || ch == '/' || ch == '(') {
                if (operand.length() > 0) {
                    queue.add(toNumber(operand.toString()));
                    operand.setLength(0);
                }
                else if (priority(ch) != 2) {
                    //连着两个操作符
                }

                while (true) {
                    if (stack.isEmpty()) {
                        stack.push(ch);
                        break;
                    }

                    char top = stack.peek();
                    if (priority(ch) > priority(top) || priority(top) == 2) {
                        stack.push(ch);
                        break;
                    }

                    queue.add(top);
                    stack.pop();
                }
            }
            else if (ch == ')') {
                if (operand.length() > 0) {
                    queue.add(toNumber(operand.toString()));
                    operand.setLength(0);
                }

                while (!stack.isEmpty()) {
                    char top = stack.pop();
                    if (priority(top) == 2) {
                        if (priority(top) != priority(ch)) {
                            //illegal pair
                        }

                        break;
                    }
                    queue.add(top);
                }
            }
            else {
                //illegal char
            }
        }

        if (operand.length() > 0) {
            queue.add(toNumber(operand.toString()));
            operand.setLength(0);
        }
        while (!stack.isEmpty()) {
            queue.add(stack.pop());
        }
    }

    static int priority(char operator) {
        if (operator == '+' || operator == '-') {
            return 0;
        } else if (operator == '*' || operator == '/') {
            return 1;
        } else if (operator == '(' || operator == ')') {
            return 2;
        }

        return -1;
    }
    static double toNumber(Object obj) {
        if (obj instanceof String) {
            return Double.valueOf((String) obj);
        }

        return ((Number) obj).doubleValue();
    }
}
