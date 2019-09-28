package com.rutine.troubleshoot.learn;

import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.classreading.SimpleMetadataReaderFactory;

import java.io.IOException;

/**
 * 通过字节码读取class文件到内存结构
 *
 * @author rutine
 * @date 2019/7/25 16:13
 */
public class LearnMetadataReader {

    public static void main(String[] args) throws Exception {
        LearnMetadataReader.test();
    }

    public static void test() throws IOException {
        ResourceLoader loader = new DefaultResourceLoader();
        MetadataReaderFactory factory = new SimpleMetadataReaderFactory();
        MetadataReader reader = factory.getMetadataReader(loader.getResource("ConfigA.class"));
        AnnotationMetadata metadata = reader.getAnnotationMetadata();
        System.out.println(metadata);
    }
}
