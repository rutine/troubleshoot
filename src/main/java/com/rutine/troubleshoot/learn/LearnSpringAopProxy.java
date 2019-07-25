package com.rutine.troubleshoot.learn;

import org.springframework.aop.AfterReturningAdvice;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.lang.Nullable;

import java.lang.reflect.Method;

/**
 * @author rutine
 * @date 2019/4/22 11:56
 */
public class LearnSpringAopProxy {

    public static void main(String[] args) throws Exception {
        LearnSpringAopProxy.test();
    }

    public static void test() {
        ProxyFactory factory = new ProxyFactory(new B());
        factory.addAdvice(new MyMethodBeforeAdvice());
        factory.addAdvice(new MyAfterReturningAdvice());
        A proxy = (A) factory.getProxy();
        proxy.go();
    }


    interface A {
        Integer go();
    }

    static class B implements A {
        @Override
        public Integer go() {
            return 1;
        }
    }

    static class MyMethodBeforeAdvice implements MethodBeforeAdvice {
        @Override
        public void before(Method method, Object[] objects, @Nullable Object o) throws Throwable {
            System.out.println("----before----");
        }
    }

    static class MyAfterReturningAdvice implements AfterReturningAdvice {
        @Override
        public void afterReturning(@Nullable Object o, Method method, Object[] objects, @Nullable Object o1) throws Throwable {
            System.out.println("----after----");
        }
    }
}
