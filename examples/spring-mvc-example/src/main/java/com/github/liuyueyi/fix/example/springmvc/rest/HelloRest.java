package com.github.liuyueyi.fix.example.springmvc.rest;

import com.alibaba.fastjson.JSON;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by @author yihui in 18:24 18/12/29.
 */
@RestController
public class HelloRest {

    private Map<String, Object> context = new HashMap<>();

    @GetMapping(path = "query")
    public String query(HttpServletRequest req) {
        return JSON.toJSONString(context.getOrDefault(req.getParameter("key"), "no-arg"));
    }

    @GetMapping(path = "post")
    public String post(String key, String value) {
        context.put(key, value);
        return "over";
    }

    @GetMapping(path = "cache")
    public String getCache(String key) {
        return String.valueOf(StaticBean.getCache(key));
    }
}
