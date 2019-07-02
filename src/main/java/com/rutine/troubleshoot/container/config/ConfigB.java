package com.rutine.troubleshoot.container.config;

import com.rutine.troubleshoot.container.bean.BeanB;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author rutine
 * @date 2019/7/2 17:27
 */
@Configuration
public class ConfigB {

    @Bean("configB#BeanB")
    public BeanB beanB() {
        return new BeanB();
    }
}
