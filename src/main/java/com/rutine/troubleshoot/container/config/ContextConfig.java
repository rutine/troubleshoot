package com.rutine.troubleshoot.container.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * 配置类主入口
 *
 * @author rutine
 * @date 2019/5/20 15:28
 */
@Configuration
//@ComponentScan(basePackages = "com.rutine.troubleshoot.container.bean")
@Import(ConfigSelector.class)
public class ContextConfig {

}
