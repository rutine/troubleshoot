package com.rutine.troubleshoot.container.bean;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author rutine
 * @date 2019/6/26 8:52
 */
@Configuration
public class LazyDependencyConfigBean {

    //多例Bean
    @Component
    @Scope("prototype")
    class PrototypeBean {
        public PrototypeBean getThis() {
            return this;
        }
    }

    @Component
    class LazyDependencyBean implements InitializingBean {
        //懒属性依赖
        @Lazy
        @Autowired
        private PrototypeBean prototypeBean;

        @Override
        public void afterPropertiesSet() throws Exception {
            PrototypeBean a = prototypeBean.getThis();
            PrototypeBean b = prototypeBean.getThis();
            System.out.println(prototypeBean);
            System.out.println(a);
            System.out.println(b);
        }
    }
}
