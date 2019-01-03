# jar应用测试工程

## I. spring-fixer 使用说明
      
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
        <groupId>com.git.hui.fix</groupId>
        <artifactId>spring-fixer</artifactId>
        <version>1.0</version>
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
        <groupId>com.git.hui.fix</groupId>
        <artifactId>spring-fixer</artifactId>
        <version>1.0</version>
    </dependency>
</dependencies>
```

### 2. 使用

引入上面依赖之后，如无问题，会暴露一个http接口 `/inject-fixer-endpoint/call`，用于接收外部请求，执行内部服务方法；在实际的使用过程中，需要注意加上安全校验相关的逻辑

- **默认开启的端口号是 9999, 可以通过设置jvm参数 `-Dquick.fix.port` 来重设对应的端口号**
- 在项目开头，需要主动调用一下  `FixEngine.instance();` 进行初始化

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


### 1. 静态类调用方式

测试bean的服务调用

```bash
# 执行bean的某个方法
curl -X POST -H "Content-Type:application/json" http://127.0.0.1:8080/inject-fixer-endpoint/call -d '{"service": "demoBean", "method": "randName"}'
# 查看bean的属性值
curl -X POST -H "Content-Type:application/json" http://127.0.0.1:8080/inject-fixer-endpoint/call -d '{"service": "demoBean", "field": "name"}'
# 执行bean的属性的某个方法
curl -X POST -H "Content-Type:application/json" http://127.0.0.1:8080/inject-fixer-endpoint/call -d '{"service": "demoBean", "field": "values", "method":"add", "params": ["autoInsertByQuickFixer!"]}'
```

测试静态类的静态成员的方法调用

```bash
curl -X POST -H "Content-Type:application/json" http://127.0.0.1:9999/fixer/call -d '{"service": "com.git.hui.fix.example.spring.server.StaticBean", "method": "getCache", "params": ["init"], "type":"static"}'
```