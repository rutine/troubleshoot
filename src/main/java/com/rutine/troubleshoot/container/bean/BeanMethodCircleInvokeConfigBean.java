package com.rutine.troubleshoot.container.bean;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.Nullable;

/**
 *
 * @Bean方法之间不支持循环调用, @Bean方法按照定义顺序由容器进行初始化
 *
 * @author rutine
 * @date 2019/8/6 15:00
 */
@Configuration
public class BeanMethodCircleInvokeConfigBean {
    @Bean
    public FactoryBean<BeanA> beanAFactory() {
        FactoryBean<BeanA> factory = new FactoryBean<BeanA>() {
            @Override
            public BeanA getObject() throws Exception {
                //创建新对象
                return new BeanA();
            }
            @Override
            public Class<?> getObjectType() {
                return BeanA.class;
            }
        };
        return factory;
    }

    @Bean
    public BeanA beanA() throws Exception {
        BeanA a = new BeanA();
        //调用beanB(), 被拦截并从容器获取实例, 在单例情况下, 保证永远都是同一实例
        a.beanB = beanB();
        return a;
    }

    @Bean
    public BeanB beanB() throws Exception {
        /**
         * 虽然调用了工厂方法及工厂的getObject()方法两次,
         * 看似工厂创建了两次, 工厂的getObject()方法每次都是重新new对象,
         * 但是打印的都是相同的对象
         * 调用beanAFactory()方法返回的是多例动态代理对象
         */
        System.out.println("---> factoryBean" + beanAFactory().getObject());
        System.out.println("---> factoryBean" + beanAFactory().getObject());

        BeanB b = new BeanB();
        //与beanA方法内调用beanB方法形成循环调用, 异常
//        b.beanA = beanA();

        return b;
    }
}
