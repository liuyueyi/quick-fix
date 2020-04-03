# spring测试工程

## I. Spring-MVC-Fixer 使用说明

### 1. 配置相关

引入依赖，下面二选一

```xml
<repositories>
    <repository>
        <id>yihui-maven-repo</id>
        <url>https://raw.githubusercontent.com/liuyueyi/maven-repository/master/repository</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>com.github.liuyueyi.fix</groupId>
        <artifactId>spring-mvc-fixer</artifactId>
        <version>1.3</version>
    </dependency>
</dependencies>
```

或者

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>com.github.liuyueyi.fix</groupId>
        <artifactId>spring-mvc-fixer</artifactId>
        <version>1.3</version>
    </dependency>
</dependencies>
```

### 2. 使用

引入上面依赖之后，如无问题，会暴露一个http接口 `/inject-fixer-endpoint/call`，用于接收外部请求，执行内部服务方法；在实际的使用过程中，需要注意加上安全校验相关的逻辑

## II. 测试实例

### 0. 请求方式说明

| 标题 | 值 | 解释 | 
| --- | --- | --- |
| 请求方法 | POST | 只支持POST请求 | 
| 请求头 | application/json | 请求参数以json串方式提交 | 
| 请求参数 | service |  需要执行的服务，可以是完成路径，可以是beanName | 
| - | field | 需要访问的服务内部成员属性，值为属性名；为空时，表示执行的服务的某个方法 | 
| - | method | 方法名，需要执行的方法；为空时，表示访问某个服务的成员属性值 |
| - | type | static 表示访问静态类；其他表示访问Spring Bean | 
| - | params | 请求参数，数组，可以不存在，格式为 `类型#值`，对于基本类型，可以省略类型的前缀包 |   
| - | secondMethod | 链式请求方法名 |   
| - | secondField | 链式请求属性 |   
| - | secondParams | 链式请求参数，数组，可以不存在，格式为 `类型#值`，对于基本类型，可以省略类型的前缀包 |   


### 1. bean实例调用方式

调用bean的方法，或者直接查询bean的属性值

```bash
# 执行bean的某个方法
curl -X POST -H "Content-Type:application/json" http://127.0.0.1:8080/inject-fixer-endpoint/call -d '{"service": "demoBean", "method": "randName"}'
# 查看bean的属性值
curl -X POST -H "Content-Type:application/json" http://127.0.0.1:8080/inject-fixer-endpoint/call -d '{"service": "demoBean", "field": "name"}'
# 执行bean的属性的某个方法
curl -X POST -H "Content-Type:application/json" http://127.0.0.1:8080/inject-fixer-endpoint/call -d '{"service": "demoBean", "field": "values", "method":"add", "params": ["autoInsertByQuickFixer!"]}'

curl -X POST -H "Content-Type:application/json" http://127.0.0.1:8080/inject-fixer-endpoint/call -d '{"service": "com.github.liuyueyi.fix.example.springmvc.rest.DemoBean", "field": "values", "method":"get", "params": ["int#0"]}'


# ongl方式访问
curl -X POST -H "Content-Type:application/json" http://127.0.0.1:8080/inject-fixer-endpoint/ognl -d '{"expression": "#demoBean.randName()"}'
```


### 2. 静态类调用方式

测试静态类的静态成员的方法调用

```bash
curl -X POST -H "Content-Type:application/json" http://127.0.0.1:8080/inject-fixer-endpoint/call -d '{"service": "com.github.liuyueyi.fix.example.springmvc.rest.StaticBean", "method": "put","field":"localCache", "params": ["init","BigDecimal#10"], "type":"static"}'
curl -X POST -H "Content-Type:application/json" http://127.0.0.1:8080/inject-fixer-endpoint/call -d '{"service": "com.github.liuyueyi.fix.example.springmvc.rest.StaticBean", "method": "get","field":"localCache", "params": ["init"], "type":"static"}'


# ognl方式访问
curl -X POST -H "Content-Type:application/json" http://127.0.0.1:8080/inject-fixer-endpoint/ognl -d '{"expression": "@com.github.liuyueyi.fix.example.springmvc.rest.StaticBean@localCache.put(\"dd\", new java.math.BigDecimal(12.1))"}'
curl -X POST -H "Content-Type:application/json" http://127.0.0.1:8080/inject-fixer-endpoint/ognl -d '{"expression": "@com.github.liuyueyi.fix.example.springmvc.rest.StaticBean@localCache.get(\"dd\")"}'
```


测试静态类的静态方法调用

```bash
curl -X POST -H "Content-Type:application/json" http://127.0.0.1:8080/inject-fixer-endpoint/call -d '{"service": "com.github.liuyueyi.fix.example.springmvc.rest.StaticBean", "method": "updateCache", "params": ["init","BigDecimal#3"], "type":"static"}'
curl -X POST -H "Content-Type:application/json" http://127.0.0.1:8080/inject-fixer-endpoint/call -d '{"service": "com.github.liuyueyi.fix.example.springmvc.rest.StaticBean", "method": "getCache", "params": ["init"], "type":"static"}'

# ognl访问方式
curl -X POST -H "Content-Type:application/json" http://127.0.0.1:8080/inject-fixer-endpoint/ognl -d '{"expression": "@com.github.liuyueyi.fix.example.springmvc.rest.StaticBean@getCache(\"dd\")"}'
```

测试静态类的静态成员的父类方法

```bash
curl -X POST -H "Content-Type:application/json" http://127.0.0.1:8080/inject-fixer-endpoint/call -d '{"service": "com.git.hui.fix.example.springmvc.rest.StaticBean", "method": "invalidateAll","field":"localCache", "type":"static"}'
curl -X POST -H "Content-Type:application/json" http://127.0.0.1:8080/inject-fixer-endpoint/call -d '{"service": "com.git.hui.fix.example.springmvc.rest.StaticBean", "method": "getCache", "params": ["init"], "type":"static"}'
```

### 3. 单例调用方式

单例的正常使用，会涉及到两个方法传入，在1.3+版本之后支持，通过制定 `secondMethod`, `secondParams`来实现

一个简单的测试case

```java
package com.github.liuyueyi.fix.example.springmvc.rest;

/**
 * Created by @author yihui in 19:45 19/7/5.
 */
public class SingletonBean {

    private static class InnerClz {
        private static final SingletonBean INSTANCE = new SingletonBean();
    }

    public static SingletonBean getInstance() {
        return InnerClz.INSTANCE;
    }

    public String sayHello(String hello) {
        return hello;
    }
}
```

如果我们需要执行上面的 `sayHello`方法，可以如下

```bash
curl -X POST -H "Content-Type:application/json" http://127.0.0.1:8080/inject-fixer-endpoint/call -d '{"service": "com.github.liuyueyi.fix.example.springmvc.rest.SingletonBean", "method": "getInstance", "secondMethod": "sayHello", "secondParams": ["init"], "type":"static"}'
```

### 4. 调用类的方法的方法

当一次调用某个服务的方法返回的是一个非POJO对象时，我可能需要再次调用这个对象的方法/或者访问它的属性（这种case和上面的单例差不多)，此时在1.3+版本中，支持二次链式调用

针对静态类

```java
package com.github.liuyueyi.fix.example.springmvc.rest;


import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.math.BigDecimal;

/**
 * Created by @author yihui in 18:36 18/12/29.
 */
public class StaticBean {

    private static LoadingCache<String, BigDecimal> localCache;

    static {
        localCache = CacheBuilder.newBuilder().build(new CacheLoader<String, BigDecimal>() {
            @Override
            public BigDecimal load(String key) throws Exception {
                return BigDecimal.ZERO;
            }
        });

        localCache.put("init", new BigDecimal(520));
    }


    public static BigDecimal getCache(String key) {
        return localCache.getUnchecked(key);
    }

    public static void updateCache(String key, BigDecimal value) {
        localCache.put(key, value);
    }


    public static LoadingCache<String, BigDecimal> getLocalCache() {
        return localCache;
    }


    public static class InnerBean {
        private String name;

        public InnerBean() {
            name = "name_" + Thread.currentThread().getId();
        }

        public String getName() {
            return name;
        }
    }

    private static InnerBean innerBean = new InnerBean();

    public static InnerBean getInnerBean() {
        return innerBean;
    }
}
```

如果我希望通过调用 `StaticBean.getLocalCache`来获取内部对象，然后再调用内部对象的方法或属性时，可以如下操作

```bash
curl -X POST -H "Content-Type:application/json" http://127.0.0.1:8080/inject-fixer-endpoint/call -d '{"service": "com.github.liuyueyi.fix.example.springmvc.rest.StaticBean", "method": "getLocalCache", "secondMethod": "get", "secondParams": ["init"], "type":"static"}'
```

针对`innerBean`的访问，可以如下

```bash
curl -X POST -H "Content-Type:application/json" http://127.0.0.1:8080/inject-fixer-endpoint/call -d '{"service": "com.github.liuyueyi.fix.example.springmvc.rest.StaticBean", "method": "getInnerBean", "secondMethod": "getName", "secondParams": [], "type":"static"}'
curl -X POST -H "Content-Type:application/json" http://127.0.0.1:8080/inject-fixer-endpoint/call -d '{"service": "com.github.liuyueyi.fix.example.springmvc.rest.StaticBean", "method": "getInnerBean", "secondField": "name", "secondParams": [], "type":"static"}'
```