package com.rutine.troubleshoot.learn;

import org.springframework.core.annotation.AliasFor;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * AnnotationUtils 中有个内部的AliasDescriptor, 主要用于支持spring内置的@AliasFor注解
 * @AliasFor 可以对一个注解的属性定义仅有一个别名，或多个属性别名于元注解的一个属性
 *
 * @author rutine
 * @date 2018/7/9 9:52
 */
@LearnAliasDescriptor.ErrorRutine
@LearnAliasDescriptor.Rutine
@LearnAliasDescriptor.Rutinee
public class LearnAliasDescriptor {

    public static void main(String[] args) throws Exception {
        LearnAliasDescriptor.test();
    }

    public static void test() {
        try {
            //运行异常
            ErrorRutine ann = LearnAliasDescriptor.class.getAnnotation(ErrorRutine.class);
            AnnotationUtils.getAnnotationAttributes(ann);
        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            //运行正常
            Rutine ann = LearnAliasDescriptor.class.getAnnotation(Rutine.class);
            AnnotationUtils.getAnnotationAttributes(ann);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            //运行正常
            Rutinee ann = LearnAliasDescriptor.class.getAnnotation(Rutinee.class);
            AnnotationUtils.getAnnotationAttributes(ann);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 错误的定义
     * 同一个注解内，属性别名必须成对出现
     */
    @Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    public @interface ErrorRutine {
        @AliasFor("alias")
        String value() default "";

        @AliasFor("value")
        String alias() default "";

        @AliasFor("value")
        String other() default "";
    }

    /**
     * 正确的定义
     *
     */
    @Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    public @interface Rutine {
        @AliasFor("alias")
        String value() default "";

        @AliasFor("value")
        String alias() default "";
    }


    /**
     * 正确继承的定义
     * aliasOne和aliasTwo是元注解@Rutine属性value的两个不同别名
     * @Rutine必须是元注解, @Rutinee属性@AliasFor的annotation必须显示指明
     */
    @Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @Rutine
    public @interface Rutinee {
        @AliasFor(value = "value", annotation = Rutine.class)
        String aliasOne() default "";

        @AliasFor(value = "value", annotation = Rutine.class)
        String aliasTwo() default "";
    }
}
