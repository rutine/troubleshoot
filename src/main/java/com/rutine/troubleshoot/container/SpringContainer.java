package com.rutine.troubleshoot.container;

import com.rutine.troubleshoot.container.config.ContextConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author rutine
 * @date 2019/5/20 14:45
 */
public class SpringContainer {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(ContextConfig.class);
//        context.scan("com.rutine.troubleshoot.container");
        context.refresh();
        context.start();
    }
}
