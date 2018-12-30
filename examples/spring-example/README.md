# spring测试工程

## 测试实例

### 1. bean实例调用方式

调用bean的方法，或者直接查询bean的属性值

```bash
curl -X POST -H "Content-Type:application/json" http://127.0.0.1:8080/fixer/call -d '{"service": "demoBean", "method": "randName"}'
curl -X POST -H "Content-Type:application/json" http://127.0.0.1:8080/fixer/call -d '{"service": "demoBean", "field": "name"}'
```


### 2. 静态类调用方式

测试静态类的静态成员的方法调用

```bash
curl -X POST -H "Content-Type:application/json" http://127.0.0.1:8080/fixer/call -d '{"service": "com.git.hui.fix.example.spring.rest.StaticBean", "method": "put","field":"localCache", "params": ["init","BigDecimal#10"], "type":"static"}'
curl -X POST -H "Content-Type:application/json" http://127.0.0.1:8080/fixer/call -d '{"service": "com.git.hui.fix.example.spring.rest.StaticBean", "method": "get","field":"localCache", "params": ["init"], "type":"static"}'
```


测试静态类的静态方法调用

```bash
curl -X POST -H "Content-Type:application/json" http://127.0.0.1:8080/fixer/call -d '{"service": "com.git.hui.fix.example.spring.rest.StaticBean", "method": "updateCache", "params": ["init","BigDecimal#3"], "type":"static"}'
curl -X POST -H "Content-Type:application/json" http://127.0.0.1:8080/fixer/call -d '{"service": "com.git.hui.fix.example.spring.rest.StaticBean", "method": "getCache", "params": ["init"], "type":"static"}'
```

测试静态类的静态成员的父类方法

```bash
curl -X POST -H "Content-Type:application/json" http://127.0.0.1:8080/fixer/call -d '{"service": "com.git.hui.fix.example.spring.rest.StaticBean", "method": "invalidateAll","field":"localCache", "type":"static"}'
curl -X POST -H "Content-Type:application/json" http://127.0.0.1:8080/fixer/call -d '{"service": "com.git.hui.fix.example.spring.rest.StaticBean", "method": "getCache", "params": ["init"], "type":"static"}'
```