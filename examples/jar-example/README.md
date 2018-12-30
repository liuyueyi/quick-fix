# jar应用测试工程

## 测试实例

默认开启的端口号是 9999, 可以通过设置jvm参数 `-DfixerPort` 来重设对应的端口号


### 静态类调用方式

测试静态类的静态成员的方法调用

```bash
curl -X POST -H "Content-Type:application/json" http://127.0.0.1:9999/fixer/call -d '{"service": "com.git.hui.fix.example.jar.server.CalculateServer", "method": "getCache", "params": ["init"], "type":"static"}'
```

