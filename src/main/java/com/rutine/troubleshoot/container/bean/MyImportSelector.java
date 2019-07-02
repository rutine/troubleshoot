package com.rutine.troubleshoot.container.bean;

import com.rutine.troubleshoot.container.config.ConfigSelect;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Map;

/**
 * 选择导入配置类
 *
 * @author rutine
 * @date 2019/7/2 17:39
 */
public class MyImportSelector implements ImportSelector {

    //根据注解@ConfigSelect的value属性值确定导入那个配置类
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        if (!importingClassMetadata.hasAnnotation(ConfigSelect.class.getName())) {
            throw new RuntimeException(String.format("主动导入类: %s 必须添加注解: @ConfigSelect", importingClassMetadata.getClassName()));
        }

        Map<String, Object> attributes = importingClassMetadata.getAnnotationAttributes(ConfigSelect.class.getName());
        Class<?> clazz = (Class<?>) attributes.get("value");
        return new String[] {clazz.getName()};
    }
}
