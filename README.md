## Quick-Fix

[![Builder](https://travis-ci.org/liuyueyi/quick-fix.svg?branch=master)](https://travis-ci.org/liuyueyi/quick-fix)
[![mvn-repository](https://jitpack.io/v/liuyueyi/quick-fix.svg)](https://jitpack.io/#liuyueyi/quick-fix)
[![Average time to resolve an issue](http://isitmaintained.com/badge/resolution/liuyueyi/quick-fix.svg)](http://isitmaintained.com/project/liuyueyi/quick-fix "Average time to resolve an issue")
[![Percentage of issues still open](http://isitmaintained.com/badge/open/liuyueyi/quick-fix.svg)](http://isitmaintained.com/project/liuyueyi/quick-fix "Percentage of issues still open")

## I. 背景说明

### case1: 程序出bug了

在我们的实际工作中，当我们遇到别人反馈代码出问题了吧，怎么返回的数据不对？

当应用持续跑了一段时间之后，这个时候我们的第一个反应基本是确认能复现么？如果能复现，那么调用的姿势是不是对的？如果确认姿势没问题，那么就是请求参数不对了!!! 如果请求参数还没有问题，卧槽，这下完了，真可能有bug了，这下怎么办？

接下来，一般的讨论是在测试环境复现一下，如果能复现，那么开启debug（或者远程debug），一行行调试，相信很快就能搞定了；

但是，最怕的就是但是，测试环境没法复现，至于线上环境才有问题，这下怎么搞？

### case2: 缓存数据有问题

另外一个场景就是为了提升服务性能，缓存基本上会被大量的使用在各个系统之间；有缓存，那么就会有缓存不一致的问题，如果缓存用的是外部的如(redis/memcache)之类的，那么缓存数据的查询和订正，就相对简单了；但是，如果我们使用了内存作为数据的缓存，比如（hashmap, guava)，这种时候，我想知道这个内存中的数据怎么办？我想修改这个内存的中的数据怎么办？

### 3. 小结

上面两个场景，归纳一下主要是两个问题

- 如何知道线上应用中，某个服务的方法的执行结果；
- 如何知道线上应用中，某些内存数据的结果

## II. 方案设计

为了解决上面抛出的两个问题，我们要怎么做呢？

### 1. 设计

如何访问应用中的方法、数据，首先想到的就是反射；通过反射来执行某个实例的方法，或者获取实例的属性值，并没有太多的难度，有问题的是如何做到无侵入，如何与外部通信，如何做到通用

首先我们需要注入一个EndPoint，用于实现应用于外界的通信，这个是一切开始的基本条件，Fixer的Endpoint负责接收外部请求，并将请求转发给内部的解析器，执行应用内服务访问，并将结果输出给外部使用者

![IMAGE](http://blog.hhui.top/hexblog/imgs/190102/00.jpg)

上图给出了EndPoint的结构设计，因为目前的java应用，直接以jar方式跑的不太多了，更常见的是将服务跑在其他的容器中，比如我们常见的tomcat应用，Spring应用等；不同的容器，对外暴露的方式不一样，怎么样才可以做到在不同的容器中，进行优雅的支持呢？

接下来请求到应用内之后，首先定位到访问的服务，其次则进行服务调用执行，其实现流程如下

![IMAGE](https://blog.hhui.top/hexblog/imgs/190102/01.jpg)

### 2. 技术

从上面的结构设计出发，找到这个项目实现的关键点，然后看下可以怎么实现

#### a. 服务定位

如何通过传入的请求参数来定位需要执行的服务方法，一般来将，应用中提供的服务可以分为两种情况

- 以实例的形式提供服务
  - 如Spring中以Bean的形式，一个Service就是一个Bean；我们可以借助Spring的ApplicationContext获取对应的bean
  - 这种类型的服务，要求应用本身持有所有服务，然后我们可以通过这个ServiceHolder来定位具体的服务
- 一个类对应一个服务
  - 这种常见的是静态类或者单例，这个是以ClassLoader维度进行区分的；
  - 因此我们可以直接通过ClassLoader方式来加载对应的类

然后我们主要目标需要集中在第一种方式，不同的应用方式，获取ServiceHolder不一样，让我们自己去全部支持，显然是不太现实的，因此我们需要设计一个方案，让使用者，自己来根据应用中的ServiceHolder，来选择具体的Service方法

这种，就可以通过SPI机制来支持

#### b. EndPoint支持

提供与外部的交互，最常见的方案就是暴露一个http接口，然后接收外部的请求；非web服务怎么办？也可以开一个socket走tcp通信，那么问题来了

- 对于web服务
  - 直接在已有的web服务上新增一个端点，并加上权限控制？
  - 另开一个端口提供服务
- 对于非web服务
  - 新开端口提供服务
  
所以再具体的实现中，我们需要考虑以下几点

- 如何复用已有的web服务？
- 没有web服务时，自己怎么支持web服务？
- 如何支持绑定端口的配置？
- 当项目中引入了多种EndPoint支持方式时，怎么保证只有一个生效？

针对上面的问题，具体实现时，会用到下面一些机制

- 引入优先级
- 通过SPI来实现自定义扩展
- 解析JVM参数，来获取对应的配置

## III. 相关博文

从设计到实现，下面博文分别进行详细介绍说明

**技术文档**

- [190102-Quick-Fix 从0到1构建一个应用内服务/数据访问订正工具包](https://liuyueyi.github.io/hexblog/2019/01/02/190102-Quick-Fix-%E4%BB%8E0%E5%88%B01%E6%9E%84%E5%BB%BA%E4%B8%80%E4%B8%AA%E5%BA%94%E7%94%A8%E5%86%85%E6%9C%8D%E5%8A%A1-%E6%95%B0%E6%8D%AE%E8%AE%BF%E9%97%AE%E8%AE%A2%E6%AD%A3%E5%B7%A5%E5%85%B7%E5%8C%85/)
- [190108-Quick-Fix 如何优雅的实现应用内外交互之接口设计篇](https://liuyueyi.github.io/hexblog/2019/01/08/190108-Quick-Fix-%E5%A6%82%E4%BD%95%E4%BC%98%E9%9B%85%E7%9A%84%E5%AE%9E%E7%8E%B0%E5%BA%94%E7%94%A8%E5%86%85%E5%A4%96%E4%BA%A4%E4%BA%92%E4%B9%8B%E6%8E%A5%E5%8F%A3%E8%AE%BE%E8%AE%A1%E7%AF%87/)
- [190311-Quick-Fix 通过反射执行任意类目标方法的实现全程实录（上篇）](https://liuyueyi.github.io/hexblog/2019/03/11/190311-Quick-Fix-%E9%80%9A%E8%BF%87%E5%8F%8D%E5%B0%84%E6%89%A7%E8%A1%8C%E4%BB%BB%E6%84%8F%E7%B1%BB%E7%9B%AE%E6%A0%87%E6%96%B9%E6%B3%95%E7%9A%84%E5%AE%9E%E7%8E%B0%E5%85%A8%E7%A8%8B%E5%AE%9E%E5%BD%95%EF%BC%88%E4%B8%8A%E7%AF%87%EF%BC%89/)
- [2019/03/15 190315-Quick-Fix 通过反射执行任意类目标方法的实现全程实录（中篇）](https://liuyueyi.github.io/hexblog/2019/03/15/190315-Quick-Fix-%E9%80%9A%E8%BF%87%E5%8F%8D%E5%B0%84%E6%89%A7%E8%A1%8C%E4%BB%BB%E6%84%8F%E7%B1%BB%E7%9B%AE%E6%A0%87%E6%96%B9%E6%B3%95%E7%9A%84%E5%AE%9E%E7%8E%B0%E5%85%A8%E7%A8%8B%E5%AE%9E%E5%BD%95%EF%BC%88%E4%B8%AD%E7%AF%87%EF%BC%89/)
- [2019/03/17 190317-Quick-Fix 通过反射执行任意类目标方法的实现全程实录（下篇）](https://liuyueyi.github.io/hexblog/2019/03/17/190317-Quick-Fix-%E9%80%9A%E8%BF%87%E5%8F%8D%E5%B0%84%E6%89%A7%E8%A1%8C%E4%BB%BB%E6%84%8F%E7%B1%BB%E7%9B%AE%E6%A0%87%E6%96%B9%E6%B3%95%E7%9A%84%E5%AE%9E%E7%8E%B0%E5%85%A8%E7%A8%8B%E5%AE%9E%E5%BD%95%EF%BC%88%E4%B8%8B%E7%AF%87%EF%BC%89/)

**使用文档**

- [190104-Quick-Fix 纯Jar应用及扩展手册](https://liuyueyi.github.io/hexblog/2019/01/04/190104-Quick-Fix-%E7%BA%AFJar%E5%BA%94%E7%94%A8%E5%8F%8A%E6%89%A9%E5%B1%95%E6%89%8B%E5%86%8C/)

## IV. 使用说明

### 1. 依赖管理

首先添加仓库，两种方式，一个是github的release版本的引入，优势是稳定；确定是更新及时问题；

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

另一个是我个人的仓库

```xml
<repositories>
    <repository>
        <id>yihui-maven-repo</id>
        <url>https://raw.githubusercontent.com/liuyueyi/maven-repository/master/repository</url>
    </repository>
</repositories>
```

### 2. 引入依赖包

根据实际的应用场景，引入对应的依赖包，

#### a. 纯jar应用

```xml
<dependency>
    <groupId>com.git.hui.fix</groupId>
    <artifactId>fix-core</artifactId>
    <version>1.4.2</version>
</dependency>
```

注意事项：

- fix-core 内置了一个http服务器，默认绑定端口号 9999, 可以通过jvm参数 `-Dquick.fix.port` 来覆盖
- 在应用的入口出，需要主动执行 `FixEngine.instance();` 进行初始化
- 因为fix-core 只提供了静态类的ServerLoader, 对于实例的加载，需要业务方自己来实现


使用姿势如下：

```bash
curl -X POST -H "Content-Type:application/json" http://127.0.0.1:9999/fixer/call -d '{"service": "CalculateServer", "method": "getCache", "params": ["init"], "type":"static"}'
```

实例demo:

[jar-example](examples/jar-example)

#### b. 纯Spring应用

如我是一个纯Spring应用，没有使用SpringMVC，可以引入

```xml
<dependency>
    <groupId>com.git.hui.fix</groupId>
    <artifactId>spring-fixer</artifactId>
    <version>1.4.2</version>
</dependency>
```

注意事项：

spring-fixer提供了访问Spring容器内bean的服务方式，因此除了获取默认提供的静态类之外，还可以访问bean；

- 使用默认的http服务器，端口号为 9999, 通过jvm参数 `-Dquick.fix.port` 来覆盖
- 与前面不同，不需要主动调用`FixEngine.instance()`
- 内部提供bean的加载ServerLoader，可以直接通过beanName或者Bean的完整类名访问其内部方法/数据

使用姿势如下:


```bash
# 执行bean的某个方法
curl -X POST -H "Content-Type:application/json" http://127.0.0.1:8080/inject-fixer-endpoint/call -d '{"service": "demoBean", "method": "randName"}'
# 查看bean的属性值
curl -X POST -H "Content-Type:application/json" http://127.0.0.1:8080/inject-fixer-endpoint/call -d '{"service": "demoBean", "field": "name"}'
# 执行bean的属性的某个方法
curl -X POST -H "Content-Type:application/json" http://127.0.0.1:8080/inject-fixer-endpoint/call -d '{"service": "demoBean", "field": "values", "method":"add", "params": ["autoInsertByQuickFixer!"]}'

# 测试静态类的静态成员的方法调用
curl -X POST -H "Content-Type:application/json" http://127.0.0.1:9999/fixer/call -d '{"service": "com.git.hui.fix.example.spring.server.StaticBean", "method": "getCache", "params": ["init"], "type":"static"}'

# 单例类的使用姿势
curl -X POST -H "Content-Type:application/json" http://127.0.0.1:8080/inject-fixer-endpoint/call -d '{"service": "SingletonBean", "method": "getInstance", "secondMethod": "sayHello", "secondParams": ["init"], "type":"static"}'
```

实例demo:


- [spring-example](examples/spring-example)

#### c. SpringMVC应用

如是一个SpringMVC应用，可以引入

```xml
<dependency>
    <groupId>com.git.hui.fix</groupId>
    <artifactId>spring-mvc-fixer</artifactId>
    <version>1.4.2</version>
</dependency>
```

使用说明：

- 利用mvc本身提供的http服务，访问路径为`/inject-fixer-endpoint/call`； 因此需要做好安全校验


使用姿势&实例：

- 使用和前面的类似
- [spring-mvc-example](examples/spring-mvc-example)


#### d. SpringCloud应用

如果是一个SpringCloud服务，且开启了 actuator 应用监测，可以引入

```xml
<dependency>
    <groupId>com.git.hui.fix</groupId>
    <artifactId>spring-cloud-fixer</artifactId>
    <version>1.4.2</version>
</dependency>
```

使用说明：

- 将FixEndPoint端口集成在SpringCloud的Actuator中，因此在实际使用时，需要在配置中开启，设置参数 `management.endpoints.web.exposure.include`
- 访问路径为：`/actuator/inject-fixer-endpoint/call`， 前面的 `actuator`路径与应用监测配置的路径一致 


使用姿势&实例:

- [spring-cloud-example](examples/spring-cloud-example)

## V. End

### 版本说明

**v1.1**

- 完成quick-fix 基础功能，实现应用内服务\数据访问
- 集成基于socket的http服务器，作为默认的应用内外交互通道
- 支持Spring Jar 应用直接使用
- 支持Spring MVC 应用直接使用
- 支持Spring Cloud 应用直接使用

**v1.2**

- 支持ServerLoader优先级指定
- 使用gson替换fastjson, 解决序列化对应的的key没有双引号导致json格式非法的问题

**v1.3**

- 新增单例反射调用支持；支持二级链式方法调用

**v1.4**

- [issues #4](https://github.com/liuyueyi/quick-fix/issues/4) 支持参数解析的扩展
    - 默认提供八种基本数据类型，BigInteger, BigDecimal, Class, Enum, Json格式POJO对象的参数转换
    - 通过JDK SPI方式，加载自定义的继承自`IArgParser`的参数解析器 
- [issues #5](https://github.com/liuyueyi/quick-fix/issues/5) 解决传参为null的场景支持
- 项目[spring-mvc-example](examples/spring-mvc-example) 新增枚举类传参的示例demo

**v1.4.1**

- 修复`HttpMessageParser`请求头在不同环境下可能大小写不一致的问题

**v1.4.2**

- 添加日志埋点

 
### 其他

拒绝单机，欢迎start或者加好友支持

### 声明

尽信书则不如，已上内容，一家之言，因个人能力有限，难免有疏漏和错误之处，如发现bug或者有更好的建议，欢迎批评指正，不吝感激

- 微博地址: 小灰灰Blog
- QQ： 一灰灰/3302797840； 交流群： 864706093
- WeChat: 一灰/liuyueyi25

### 扫描关注

公众号&博客

![QrCode](https://gitee.com/liuyueyi/Source/raw/master/img/info/blogInfoV2.png)


打赏码

![pay](https://gitee.com/liuyueyi/Source/raw/master/img/pay/pay.png)