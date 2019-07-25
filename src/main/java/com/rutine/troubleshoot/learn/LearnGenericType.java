package com.rutine.troubleshoot.learn;

import org.springframework.core.ResolvableType;
import org.springframework.stereotype.Service;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 理解泛型
 *
 * @author rutine
 * @date 2017年3月18日
 */
public class LearnGenericType {

    public static void main(String[] args) throws Exception {
        LearnGenericType.test();
    }


    public static void test() throws NoSuchMethodException, SecurityException {
        Type type = B.class.getGenericSuperclass();
        System.out.println(type);
        System.out.println(((ParameterizedType) type).getRawType()); //A

        Type[] types = ((ParameterizedType) type).getActualTypeArguments();
        System.out.println(types[0]); //String

        Method method = A.class.getDeclaredMethod("get", new Class[]{Object.class, String.class});
        System.out.println(method.getGenericParameterTypes()[0]); //PK
        System.out.println(ResolvableType.forClass(A.class).getGenerics()[0].getType()); //TypeVariable

        Annotation[] ans = B.class.getAnnotations();
        for (Annotation an : ans) {
            Method[] methods = an.annotationType().getDeclaredMethods();
            for (Method method1 : methods) {
                System.out.println(method1.getName());
            }
        }
    }

    static class A<T, PK> {
        public int get(PK id, String a) {
            return 1;
        }
    }

    @Service
    static class B extends A<String, Long> {
        private C c;

        public void setC(C c) {
            this.c = c;
        }

        public C getC() {
            return this.c;
        }
    }

    class C {
        private int c = 2;

        public void setC(int c) {
            this.c = c;
        }

        public int getC() {
            return this.c;
        }
    }
}
