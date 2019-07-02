package com.rutine.troubleshoot.container.config;

import com.rutine.troubleshoot.container.bean.BeanA;
import com.rutine.troubleshoot.container.bean.MyImportBeanDefinitionRegistrar;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;

/**
 * @author rutine
 * @date 2019/7/2 17:26
 */
@Configuration
@ImportResource("classpath:myImport.xml")
@PropertySource("classpath:config.properties")
@Import({ConfigC.class, MyImportBeanDefinitionRegistrar.class})
public class ConfigA {

    @Bean("configA#BeanA")
    public BeanA beanA() {
        return new BeanA();
    }
}
