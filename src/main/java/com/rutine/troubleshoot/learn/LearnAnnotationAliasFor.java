package com.rutine.troubleshoot.learn;

import com.rutine.troubleshoot.utils.JsonUtils;
import org.springframework.core.annotation.AliasFor;
import org.springframework.core.annotation.AnnotationConfigurationException;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author rutine
 * @date 2019/4/12 17:24
 */
public class LearnAnnotationAliasFor {

    public static void main(String[] args) throws Exception {
        LearnAnnotationAliasFor.test();
    }

    public static void test()  {
        //获取所有属性别名
        Map<String, List<String>> map = getAttributeAliasMap(Rutinee.class);
        Set<Map.Entry<String, List<String>>> set = map.entrySet();
        for (Map.Entry<String, List<String>> entry : set) {
            System.out.println(entry.getKey() + " -> " + JsonUtils.toJson(entry.getValue()));
        }
    }

    /**
     * @Target @Retention @Documented 都是 @Rutine 的直接元注解
     * 这里不拿这三个来分析, 在这里@Rutine当做一个原始注解
     */
    @Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    public @interface Rutine {
        String source() default "source";

        @AliasFor("source")
        String alias() default "alias";
    }

    /**
     * @Rutine 是 @Rutinee的直接元注解
     */
    @Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @Rutine
    public @interface Rutinee {
        @AliasFor(value = "source", annotation = Rutine.class)
        String alias2() default "";

        @AliasFor(value = "source", annotation = Rutine.class)
        String alias3() default "";
    }



    static Map<String, List<String>> getAttributeAliasMap(Class<? extends Annotation> annotationType) {
        if (annotationType == null) {
            return Collections.emptyMap();
        }

        Map<String, List<String>> map = new LinkedHashMap<String, List<String>>();
        for (Method attribute : getAttributeMethods(annotationType)) {
            List<String> aliasNames = getAttributeAliasNames(attribute);
            if (!aliasNames.isEmpty()) {
                map.put(attribute.getName(), aliasNames);
            }
        }

        return map;
    }
    static List<String> getAttributeAliasNames(Method attribute) {
        Assert.notNull(attribute, "attribute must not be null");
        AliasDescriptor descriptor = AliasDescriptor.from(attribute);
        return (descriptor != null ? descriptor.getAttributeAliasNames() : Collections.<String> emptyList());
    }
    static boolean isAttributeMethod(@Nullable Method method) {
        return method != null && method.getParameterCount() == 0 && method.getReturnType() != Void.TYPE;
    }

    static List<Method> getAttributeMethods(Class<? extends Annotation> annotationType) {
        List<Method> methods = new ArrayList();
        Method[] var2 = annotationType.getDeclaredMethods();
        int var3 = var2.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            Method method = var2[var4];
            if (isAttributeMethod(method)) {
                ReflectionUtils.makeAccessible(method);
                methods.add(method);
            }
        }

        return methods;
    }
    private static class AliasDescriptor {

        private final Method sourceAttribute; //别名属性

        private final Class<? extends Annotation> sourceAnnotationType; //别名属性注解类型

        private final String sourceAttributeName;

        private final Method aliasedAttribute; //原名属性(被别名的原始属性)

        private final Class<? extends Annotation> aliasedAnnotationType; // 原名属性注解类型

        private final String aliasedAttributeName;

        private final boolean isAliasPair;

        /**
         * Create an {@code AliasDescriptor} <em>from</em> the declaration
         * of {@code @AliasFor} on the supplied annotation attribute and
         * validate the configuration of {@code @AliasFor}.
         * @param attribute the annotation attribute that is annotated with
         * {@code @AliasFor}
         * @return an alias descriptor, or {@code null} if the attribute
         * is not annotated with {@code @AliasFor}
         * @see #validateAgainst
         */
        public static AliasDescriptor from(Method attribute) {
            AliasFor aliasFor = attribute.getAnnotation(AliasFor.class);
            if (aliasFor == null) {
                return null;
            }

            AliasDescriptor descriptor = new AliasDescriptor(attribute, aliasFor);
            descriptor.validate();
            return descriptor;
        }

        @SuppressWarnings("unchecked")
        private AliasDescriptor(Method sourceAttribute, AliasFor aliasFor) {
            Class<?> declaringClass = sourceAttribute.getDeclaringClass();
            Assert.isTrue(declaringClass.isAnnotation(), "sourceAttribute must be from an annotation");

            this.sourceAttribute = sourceAttribute;
            this.sourceAnnotationType = (Class<? extends Annotation>) declaringClass;
            this.sourceAttributeName = sourceAttribute.getName();

            this.aliasedAnnotationType = (Annotation.class == aliasFor.annotation() ?
                    this.sourceAnnotationType : aliasFor.annotation());
            this.aliasedAttributeName = getAliasedAttributeName(aliasFor, sourceAttribute);
            if (this.aliasedAnnotationType == this.sourceAnnotationType &&
                    this.aliasedAttributeName.equals(this.sourceAttributeName)) {
                String msg = String.format("@AliasFor declaration on attribute '%s' in annotation [%s] points to " +
                                "itself. Specify 'annotation' to point to a same-named attribute on a meta-annotation.",
                        sourceAttribute.getName(), declaringClass.getName());
                throw new AnnotationConfigurationException(msg);
            }
            try {
                this.aliasedAttribute = this.aliasedAnnotationType.getDeclaredMethod(this.aliasedAttributeName);
            }
            catch (NoSuchMethodException ex) {
                String msg = String.format(
                        "Attribute '%s' in annotation [%s] is declared as an @AliasFor nonexistent attribute '%s' in annotation [%s].",
                        this.sourceAttributeName, this.sourceAnnotationType.getName(), this.aliasedAttributeName,
                        this.aliasedAnnotationType.getName());
                throw new AnnotationConfigurationException(msg, ex);
            }

            this.isAliasPair = (this.sourceAnnotationType == this.aliasedAnnotationType);
        }

        private void validate() {
            // 1.原名属性(aliasedAnnotation)注解类型是否是别名属性(sourceAnnotation)的元注解类型
            // Target annotation is not meta-present?
            if (!this.isAliasPair && !AnnotationUtils.isAnnotationMetaPresent(this.sourceAnnotationType, this.aliasedAnnotationType)) {
                String msg = String.format("@AliasFor declaration on attribute '%s' in annotation [%s] declares " +
                                "an alias for attribute '%s' in meta-annotation [%s] which is not meta-present.",
                        this.sourceAttributeName, this.sourceAnnotationType.getName(), this.aliasedAttributeName,
                        this.aliasedAnnotationType.getName());
                throw new AnnotationConfigurationException(msg);
            }

            // 2.相同注解类型, 是否原名属性也存在注解<code>AliasFor</code>指向别名属性
            if (this.isAliasPair) {
                AliasFor mirrorAliasFor = this.aliasedAttribute.getAnnotation(AliasFor.class);
                if (mirrorAliasFor == null) {
                    String msg = String.format("Attribute '%s' in annotation [%s] must be declared as an @AliasFor [%s].",
                            this.aliasedAttributeName, this.sourceAnnotationType.getName(), this.sourceAttributeName);
                    throw new AnnotationConfigurationException(msg);
                }

                String mirrorAliasedAttributeName = getAliasedAttributeName(mirrorAliasFor, this.aliasedAttribute);
                if (!this.sourceAttributeName.equals(mirrorAliasedAttributeName)) {
                    String msg = String.format("Attribute '%s' in annotation [%s] must be declared as an @AliasFor [%s], not [%s].",
                            this.aliasedAttributeName, this.sourceAnnotationType.getName(), this.sourceAttributeName,
                            mirrorAliasedAttributeName);
                    throw new AnnotationConfigurationException(msg);
                }
            }

            // 3.返回类型是否相同
            Class<?> returnType = this.sourceAttribute.getReturnType();
            Class<?> aliasedReturnType = this.aliasedAttribute.getReturnType();
            if (returnType != aliasedReturnType &&
                    (!aliasedReturnType.isArray() || returnType != aliasedReturnType.getComponentType())) {
                String msg = String.format("Misconfigured aliases: attribute '%s' in annotation [%s] " +
                                "and attribute '%s' in annotation [%s] must declare the same return type.",
                        this.sourceAttributeName, this.sourceAnnotationType.getName(), this.aliasedAttributeName,
                        this.aliasedAnnotationType.getName());
                throw new AnnotationConfigurationException(msg);
            }

            if (this.isAliasPair) {
                validateDefaultValueConfiguration(this.aliasedAttribute);
            }
        }

        private void validateDefaultValueConfiguration(Method aliasedAttribute) {
            Assert.notNull(aliasedAttribute, "aliasedAttribute must not be null");
            Object defaultValue = this.sourceAttribute.getDefaultValue();
            Object aliasedDefaultValue = aliasedAttribute.getDefaultValue();

            if (defaultValue == null || aliasedDefaultValue == null) {
                String msg = String.format("Misconfigured aliases: attribute '%s' in annotation [%s] " +
                                "and attribute '%s' in annotation [%s] must declare default values.",
                        this.sourceAttributeName, this.sourceAnnotationType.getName(), aliasedAttribute.getName(),
                        aliasedAttribute.getDeclaringClass().getName());
                throw new AnnotationConfigurationException(msg);
            }

            if (!ObjectUtils.nullSafeEquals(defaultValue, aliasedDefaultValue)) {
                String msg = String.format("Misconfigured aliases: attribute '%s' in annotation [%s] " +
                                "and attribute '%s' in annotation [%s] must declare the same default value.",
                        this.sourceAttributeName, this.sourceAnnotationType.getName(), aliasedAttribute.getName(),
                        aliasedAttribute.getDeclaringClass().getName());
                throw new AnnotationConfigurationException(msg);
            }
        }

        /**
         * Validate this descriptor against the supplied descriptor.
         * <p>This method only validates the configuration of default values
         * for the two descriptors, since other aspects of the descriptors
         * are validated when they are created.
         */
        private void validateAgainst(AliasDescriptor otherDescriptor) {
            validateDefaultValueConfiguration(otherDescriptor.sourceAttribute);
        }

        /**
         * Determine if this descriptor represents an explicit override for
         * an attribute in the supplied {@code metaAnnotationType}.
         * @see #isAliasFor
         */
        private boolean isOverrideFor(Class<? extends Annotation> metaAnnotationType) {
            return (this.aliasedAnnotationType == metaAnnotationType);
        }

        /**
         * A -> C
         * B -> C
         * 所以C是A和B的共同别名
         *
         * Determine if this descriptor and the supplied descriptor both
         * effectively represent aliases for the same attribute in the same
         * target annotation, either explicitly or implicitly.
         * <p>This method searches the attribute override hierarchy, beginning
         * with this descriptor, in order to detect implicit and transitively
         * implicit aliases.
         * @return {@code true} if this descriptor and the supplied descriptor
         * effectively alias the same annotation attribute
         * @see #isOverrideFor
         */
        private boolean isAliasFor(AliasDescriptor otherDescriptor) {
            for (AliasDescriptor lhs = this; lhs != null; lhs = lhs.getAttributeOverrideDescriptor()) {
                for (AliasDescriptor rhs = otherDescriptor; rhs != null; rhs = rhs.getAttributeOverrideDescriptor()) {
                    if (lhs.aliasedAttribute.equals(rhs.aliasedAttribute)) {
                        //存在即是
                        return true;
                    }
                }
            }
            return false;
        }

        public List<String> getAttributeAliasNames() {
            // Explicit alias pair?
            if (this.isAliasPair) {
                return Collections.singletonList(this.aliasedAttributeName);
            }

            // Else: search for implicit aliases
            List<String> aliases = new ArrayList<String>();
            for (AliasDescriptor otherDescriptor : getOtherDescriptors()) {
                if (this.isAliasFor(otherDescriptor)) {
                    this.validateAgainst(otherDescriptor);
                    aliases.add(otherDescriptor.sourceAttributeName);
                }
            }
            return aliases;
        }

        private List<AliasDescriptor> getOtherDescriptors() {
            List<AliasDescriptor> otherDescriptors = new ArrayList<AliasDescriptor>();
            for (Method currentAttribute : getAttributeMethods(this.sourceAnnotationType)) {
                if (!this.sourceAttribute.equals(currentAttribute)) {
                    AliasDescriptor otherDescriptor = AliasDescriptor.from(currentAttribute);
                    if (otherDescriptor != null) {
                        otherDescriptors.add(otherDescriptor);
                    }
                }
            }
            return otherDescriptors;
        }

        public String getAttributeOverrideName(Class<? extends Annotation> metaAnnotationType) {
            Assert.notNull(metaAnnotationType, "metaAnnotationType must not be null");
            Assert.isTrue(Annotation.class != metaAnnotationType,
                    "metaAnnotationType must not be [java.lang.annotation.Annotation]");

            // Search the attribute override hierarchy, starting with the current attribute
            for (AliasDescriptor desc = this; desc != null; desc = desc.getAttributeOverrideDescriptor()) {
                if (desc.isOverrideFor(metaAnnotationType)) {
                    return desc.aliasedAttributeName;
                }
            }

            // Else: explicit attribute override for a different meta-annotation
            return null;
        }

        private AliasDescriptor getAttributeOverrideDescriptor() {
            if (this.isAliasPair) {
                return null;
            }
            return AliasDescriptor.from(this.aliasedAttribute);
        }

        /**
         * <pre>
         *     <em>获取AliasFor属性指定的别名, 仅允许value或attribute一个属性指定</em>
         * </pre>
         *
         * Get the name of the aliased attribute configured via the supplied
         * {@link AliasFor @AliasFor} annotation on the supplied {@code attribute},
         * or the original attribute if no aliased one specified (indicating that
         * the reference goes to a same-named attribute on a meta-annotation).
         * <p>This method returns the value of either the {@code attribute}
         * or {@code value} attribute of {@code @AliasFor}, ensuring that only
         * one of the attributes has been declared while simultaneously ensuring
         * that at least one of the attributes has been declared.
         * @param aliasFor the {@code @AliasFor} annotation from which to retrieve
         * the aliased attribute name
         * @param attribute the attribute that is annotated with {@code @AliasFor}
         * @return the name of the aliased attribute (never {@code null} or empty)
         * @throws AnnotationConfigurationException if invalid configuration of
         * {@code @AliasFor} is detected
         */
        private String getAliasedAttributeName(AliasFor aliasFor, Method attribute) {
            String attributeName = aliasFor.attribute();
            String value = aliasFor.value();
            boolean attributeDeclared = StringUtils.hasText(attributeName);
            boolean valueDeclared = StringUtils.hasText(value);

            // Ensure user did not declare both 'value' and 'attribute' in @AliasFor
            if (attributeDeclared && valueDeclared) {
                String msg = String.format("In @AliasFor declared on attribute '%s' in annotation [%s], attribute 'attribute' " +
                                "and its alias 'value' are present with values of [%s] and [%s], but only one is permitted.",
                        attribute.getName(), attribute.getDeclaringClass().getName(), attributeName, value);
                throw new AnnotationConfigurationException(msg);
            }

            // Either explicit attribute name or pointing to same-named attribute by default
            attributeName = (attributeDeclared ? attributeName : value);
            return (StringUtils.hasText(attributeName) ? attributeName.trim() : attribute.getName());
        }

        @Override
        public String toString() {
            return String.format("%s: @%s(%s) is an alias for @%s(%s)", getClass().getSimpleName(),
                    this.sourceAnnotationType.getSimpleName(), this.sourceAttributeName,
                    this.aliasedAnnotationType.getSimpleName(), this.aliasedAttributeName);
        }
    }

}
