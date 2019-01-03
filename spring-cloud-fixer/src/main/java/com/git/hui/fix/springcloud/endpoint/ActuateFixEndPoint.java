package com.git.hui.fix.springcloud.endpoint;

import com.alibaba.fastjson.JSON;
import com.git.hui.fix.api.constants.EndPoint;
import com.git.hui.fix.api.modal.FixReqDTO;
import com.git.hui.fix.api.spi.FixEndPoint;
import com.git.hui.fix.core.FixEngine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.endpoint.web.annotation.RestControllerEndpoint;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Created by @author yihui in 09:53 19/1/3.
 */
@Slf4j
@RestControllerEndpoint(id = "inject-fixer-endpoint")
@EndPoint(order = 0, instance = false)
public class ActuateFixEndPoint implements FixEndPoint {

    @Override
    @PostMapping(path = "call")
    public String call(@RequestBody FixReqDTO reqDTO) {
        try {
            Object obj = FixEngine.instance().execute(reqDTO);
            return JSON.toJSONString(obj);
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}
