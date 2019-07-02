package com.rutine.troubleshoot.container.bean;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.Nullable;

/**
 * @author rutine
 * @date 2019/7/1 10:49
 */
@Configuration
public class BeanMethodCircleDependencyConfigBean {

    @Bean
    public FactoryBean<BeanA> beanAFactory() {
        FactoryBean<BeanA> factory = new FactoryBean<BeanA>() {
            @Nullable
            @Override
            public BeanA getObject() throws Exception {
                //创建新对象
                return new BeanA();
            }

            @Nullable
            @Override
            public Class<?> getObjectType() {
                return BeanA.class;
            }
        };

        return factory;
    }

    @Bean
    public BeanA beanA() {
        BeanA a = new BeanA();
        a.beanB = beanB();

        return a;
    }

    @Bean
    public BeanB beanB() {
        try {
            /**
             * 虽然调用了工厂方法及工厂的getObject()方法两次,
             * 看似工厂创建了两次, 工厂的getObject()方法每次都是重新new对象,
             * 但是打印的都是相同的对象
             */
            System.out.println("---> factoryBean" + beanAFactory().getObject());
            System.out.println("---> factoryBean" + beanAFactory().getObject());
        } catch (Exception e) {
            e.printStackTrace();
        }
        BeanB b = new BeanB();
//        b.beanA = beanA();

        return b;
    }
}
