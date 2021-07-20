### 注意

1. 在哪个 lib 中引用注解，哪个 lib 的 build.gradle 文件中加入如下配置项，否则注解不会生效

```
dependencies {
    // ...
    implementation project(path: ':mixkit-annotation')
    implementation project(path: ':mixkit-compiler')
    annotationProcessor project(path: ':mixkit-compiler')
    //...
}
```
