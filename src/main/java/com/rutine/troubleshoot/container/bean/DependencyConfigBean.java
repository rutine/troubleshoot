package com.rutine.troubleshoot.container.bean;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import javax.annotation.Priority;
import java.util.List;

/**
 * @author rutine
 * @date 2019/6/12 15:30
 */
@Configuration
public class DependencyConfigBean {

    //定义接口
    interface AutowireInterface { }

    //依赖装配Bean 1, 依赖高优先, 排序在最后
    @Component
    @Priority(1)
    public static class FirstAutowire implements AutowireInterface, Ordered {
        @Override
        public int getOrder() {
            return 3;
        }
    }

    //依赖装配Bean 2, 依赖低优先, 排序在最前
    @Component
    @Priority(2)
    public static class SecondAutowire implements AutowireInterface, Ordered {
        @Override
        public int getOrder() {
            return 2;
        }
    }

    //依赖Bean
    @Component
    public static class DependencyBean implements InitializingBean {
        @Autowired
        private AutowireInterface autowire;
        @Autowired
        private List<AutowireInterface> autowires;


        @Override
        public void afterPropertiesSet() throws Exception {
            System.out.println(autowire);
            System.out.println(autowires);
        }
    }
}
