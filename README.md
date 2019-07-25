# troubleshoot
主要释疑java相关难点用例

### MyTest
程序测试主入口类

```
  public static void main(String[] args) throws Exception {
    LearnAnnotationUtils.test();
  }
```

所有用例都有 `public static void test()` 方法, 每个具体用例测试入口, 每个用例使用注释方式说明需要解决的问题

### com.rutine.troubleshoot.container 包及子包
这个包名下的所有类都是学习**Spring**容器相关  
启动类：`SpringContainer`  
配置类主入口: `ConfigContext`  