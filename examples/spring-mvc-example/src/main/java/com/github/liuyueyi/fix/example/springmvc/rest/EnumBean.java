package com.github.liuyueyi.fix.example.springmvc.rest;

import com.github.liuyueyi.fix.example.springmvc.constant.DemoType;
import org.springframework.stereotype.Component;

/**
 * 测试传参为枚举的case
 * Created by @author yihui in 16:07 19/7/19.
 */
@Component
public class EnumBean {
    /**
     * 测试 curl -X POST -H "Content-Type:application/json" http://127.0.0.1:8080/inject-fixer-endpoint/call -d '{"service": "enumBean", "method": "showMethod", "params": ["enum#com.git.hui.fix.example.springmvc.constant.DemoType#GET", "int#20"]}'
     *
     * @param type
     * @param rand
     * @return
     */
    public String showMethod(DemoType type, Integer rand) {
        return type + "|" + rand;
    }

}
