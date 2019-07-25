package com.rutine.troubleshoot.container.bean;

import org.springframework.beans.factory.BeanNameAware;

/**
 * @author rutine
 * @date 2019/7/2 16:17
 */
public class BeanB implements BeanNameAware  {
    protected BeanA beanA;

    @Override
    public void setBeanName(String beanName) {
        System.out.println("---> instance: " + getShortName(this) + "  beanName: " + beanName);
    }

    private static String getShortName(Object obj) {
        String str = obj instanceof String ? (String) obj : obj.toString();
        String[] arr = str.split("\\.");
        return arr[arr.length - 1];
    }
}
