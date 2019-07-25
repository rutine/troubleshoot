package com.rutine.troubleshoot.container.bean;

import com.rutine.troubleshoot.container.config.EnableConfigX;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Map;

/**
 * 选择导入配置类
 *
 * @author rutine
 * @date 2019/7/2 17:39
 */
public class ConfigBeanImportSelector implements ImportSelector {
    //根据@EnableConfigX 注解的value属性值确定导入那个配置类
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        if (!importingClassMetadata.hasAnnotation(EnableConfigX.class.getName())) {
            throw new RuntimeException(String.format("主动类: %s 必须添加注解: @EnableConfigX", importingClassMetadata.getClassName()));
        }

        //获取@EnableConfigX 注解的 属性 -> 值 Map
        Map<String, Object> attributes = importingClassMetadata.getAnnotationAttributes(EnableConfigX.class.getName());
        //获取 value 属性的值
        Class<?> clazz = (Class<?>) attributes.get("value");

        return new String[] {clazz.getName()};
    }
}
