package com.rutine.troubleshoot.container.bean;

import com.rutine.troubleshoot.container.config.ContextConfig;
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
public class MyImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        //我们限制必须是ContextConfig导入的才进行注册我们的Bean定义
        if (importingClassMetadata.getClassName().equals(ContextConfig.class.getName())) {
            System.out.println("------> right import");

            //注册我的Bean定义......
            RootBeanDefinition beanDefinition = new RootBeanDefinition(BeanA.class);
            registry.registerBeanDefinition("importRegistrarBeanA", beanDefinition);
        } else {
            System.out.println("------> error import");
        }
    }
}
