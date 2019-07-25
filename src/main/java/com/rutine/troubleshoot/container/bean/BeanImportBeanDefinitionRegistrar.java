package com.rutine.troubleshoot.container.bean;

import com.rutine.troubleshoot.container.config.ConfigA;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

/**
 * 导入Bean定义声明注册器 示例
 *
 * @author rutine
 * @date 2019/7/2 16:30
 */
public class BeanImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        //我们限制"主动类"必须是ConfigA才开始注册我们的Bean定义
        if (importingClassMetadata.getClassName().equals(ConfigA.class.getName())) {
            System.out.println("------> do registry");

            //注册我们的Bean定义......
            RootBeanDefinition beanDefinition = new RootBeanDefinition(BeanA.class);
            registry.registerBeanDefinition("registrar#BeanA", beanDefinition);
        } else {
            System.out.println("------> skip registry");
        }
    }
}
