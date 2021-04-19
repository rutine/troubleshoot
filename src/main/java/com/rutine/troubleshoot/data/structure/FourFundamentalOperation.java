package com.rutine.troubleshoot.data.structure;

import java.util.ArrayDeque;
import java.util.Deque;

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

    public static void firstPriority() {
        String exp = "42+2*{3-1+[5+2*(2+2)]}+3+1*2-3/1";
        Deque<Object> stack = new ArrayDeque<>();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < exp.length(); i++) {
            char ch = exp.charAt(i);
            if (Character.isDigit(ch)) {
                builder.append(ch);
            }
            else if (ch == '+' || ch == '-' || ch == '*' || ch == '/'
                    || ch == '{' || ch == '[' || ch == '(') {
                if (builder.length() > 0) {
                    stack.push(toNumber(builder.toString()));
                    builder.setLength(0);
                }
                stack.push(ch);
            }
            else if (ch == '}' || ch == ']' || ch == ')') {

            }
            else {
                //illegal char
            }
        }
    }
    //3+1*2-3/1
    public static void secondPriority(Deque<Object> stack, boolean fromHead) {
        double total = 0;
        Object object = null;
        while ((object = fromHead ? stack.poll() : stack.pollLast()) != null) {
            if (object instanceof Number) {
                double num = toNumber(object);
                total = ((Number) object).doubleValue();
            } else if (object instanceof Character) {
                Character ch = (Character) object;
                switch (ch) {
                    case '*':

                        break;
                    case '/':

                        break;
                }
            }
        }
    }
    // 1+3-2
    public static double thirdPriority(Deque<Object> stack, boolean fromHead) {
        double total = 0;
        int type = 0;
        Object object = null;
        while ((object = fromHead ? stack.poll() : stack.pollLast()) != null) {
            if (object instanceof Number) {
                double num = toNumber(object);
                total = total + (type == 1 ? num : -num);
            } else if (object instanceof Character) {
                if (type != 0) {
                    //invalid char
                }

                Character ch = (Character) object;
                switch (ch) {
                    case '+':
                        type = 1;
                        break;
                    case '-':
                        type = -1;
                        break;
                    default:
                        object = null;
                }
            }
            if (object == null) {
                //invalid char
            }
        }

        return total;
    }
    private static double toNumber(Object obj) {
        if (obj instanceof String) {
            return Double.valueOf((String) obj);
        }
        return ((Number) obj).doubleValue();
    }
}
