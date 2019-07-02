package com.rutine.troubleshoot.container.bean;

/**
 * @author rutine
 * @date 2019/7/2 16:17
 */
public class BeanB {
    protected BeanA beanA;

    public BeanB() {
        System.out.println("---> bean b: " + this);
    }
}
