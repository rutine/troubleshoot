package com.rutine.troubleshoot;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class LearnJarFile {
    
    public static void test() throws IOException {
      JarFile jarFile = new JarFile("C:/Users/administrator/.m2/repository/org/springframework/spring-beans/4.2.6.RELEASE/spring-beans-4.2.6.RELEASE.jar");
      for (Enumeration<JarEntry> entries = jarFile.entries(); entries.hasMoreElements();) {
          JarEntry entry = entries.nextElement();
          String entryPath = entry.getName();
          System.out.println(entryPath);
      }
      jarFile.close();

      File url = new File("C:/Users/administrator/.m2/repository/org/springframework/spring-beans/4.2.6.RELEASE/spring-beans-4.2.6.RELEASE.jar!/META-INF/license.txt");
      System.out.println(url.toURL());
    }
    
}
