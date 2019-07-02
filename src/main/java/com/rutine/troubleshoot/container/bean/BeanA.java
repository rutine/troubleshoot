package com.rutine.troubleshoot.container.bean;

/**
 * @author rutine
 * @date 2019/7/2 16:16
 */
public class BeanA {
    protected BeanB beanB;

    public BeanA() {
        System.out.println("---> bean a: " + this);
    }
}
