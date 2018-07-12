package com.rutine.troubleshoot;

import org.springframework.core.annotation.AnnotationUtils;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 说明spring AnnotationUtils 工具GET和FIND两个不同的语义
 *
 * @author rutine
 * @date 2018/7/11 10:25
 */
@LearnAnnotationUtils.Rutineee
public class LearnAnnotationUtils {

    public static void test()  {
        //GET语义
        //输出@Rutineee
        System.out.println(AnnotationUtils.getAnnotation(LearnAnnotationUtils.class, Rutineee.class));

        //输出@Rutinee
        System.out.println(AnnotationUtils.getAnnotation(LearnAnnotationUtils.class, Rutinee.class));

        //输出null
        System.out.println(AnnotationUtils.getAnnotation(LearnAnnotationUtils.class, Rutine.class));


        //FIND语义
        //输出@Rutineee
        System.out.println(AnnotationUtils.findAnnotation(LearnAnnotationUtils.class, Rutineee.class));

        //输出@Rutinee
        System.out.println(AnnotationUtils.findAnnotation(LearnAnnotationUtils.class, Rutinee.class));

        //输出@Rutine
        System.out.println(AnnotationUtils.findAnnotation(LearnAnnotationUtils.class, Rutine.class));
    }

    /**
     * @Target @Retention @Documented 都是 @Rutine 的直接元注解
     * 这里不拿这三个来分析, 在这里@Rutine当做一个原始注解
     */
    @Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    public @interface Rutine {

    }

    /**
     * @Rutine 是 @Rutinee的直接元注解
     */
    @Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @Rutine
    public @interface Rutinee {
    }


    /**
     * @Rutine 是 @Rutineee的间接元注解
     */
    @Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @Rutinee
    public @interface Rutineee {
    }
}
