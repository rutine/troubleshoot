package com.rutine.troubleshoot.container.config;

import com.rutine.troubleshoot.container.bean.BeanB;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author rutine
 * @date 2019/7/2 18:12
 */
@Configuration
public class ConfigC {
    @Bean("configC#BeanB")
    public BeanB beanB() {
        return new BeanB();
    }
}
