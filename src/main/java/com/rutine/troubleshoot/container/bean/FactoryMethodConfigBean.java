package com.rutine.troubleshoot.container.bean;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author rutine
 * @date 2019/6/19 9:08
 */
@Configuration
public class FactoryMethodConfigBean {

    interface ParentBean {}

    class ChildBean implements ParentBean { }

    /**
     * factoryMethod的返回类型是: ParentBean, 实例类型是: ChildBean
     */
    @Bean("childBean")
    public ParentBean factoryMethodBean() {
        return new ChildBean();
    }

    @Component
    @Order(1)
    class EarylyDependencyBean implements InitializingBean {
        @Autowired
        private ParentBean parentBean;

        @Override
        public void afterPropertiesSet() throws Exception {
            System.out.println(parentBean);
        }
    }

    @Component
    @Order(2)
    class DependencyBean implements InitializingBean {
        /**
         * 属性依赖的类型是: ChildBean, 虽然factoryMethod的实例类型是: ChildBean,
         * 但在解决属性依赖时是匹配factoryMethod的返回类型, 因为不匹配所以抛出依赖不满足异常
         */
        @Autowired
        private ChildBean childBean;

        @Override
        public void afterPropertiesSet() throws Exception {
            System.out.println(childBean);
        }
    }
}
