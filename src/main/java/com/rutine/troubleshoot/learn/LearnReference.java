package com.rutine.troubleshoot.learn;

public class LearnReference {

    public static void main(String[] args) throws Exception {
        LearnReference.test();
    }

    public static void test() {
        //A没有输出, 因为编译ClassA的常量自动编译到ClassB, 所以不会触发ClassA加载
        System.out.println(ClassB.A);
    }

    public static class ClassA {
        static final int A = 1;

         static {
            System.out.println("--------A--------");
        }
    }

    public static class ClassB {
        static int A = ClassA.A;

       static {
            System.out.println("--------B--------");
        }
    }
}
