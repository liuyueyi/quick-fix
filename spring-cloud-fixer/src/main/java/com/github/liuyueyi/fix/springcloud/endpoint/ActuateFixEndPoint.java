package com.github.liuyueyi.fix.springcloud.endpoint;

import com.github.liuyueyi.fix.api.constants.EndPoint;
import com.github.liuyueyi.fix.api.modal.ReflectReqDTO;
import com.github.liuyueyi.fix.api.modal.OgnlReqDTO;
import com.github.liuyueyi.fix.api.spi.FixEndPoint;
import com.github.liuyueyi.fix.core.FixEngine;
import com.google.gson.Gson;
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
    private Gson gson = new Gson();

    @Override
    @PostMapping(path = "call")
    public String call(@RequestBody ReflectReqDTO reqDTO) {
        try {
            Object obj = FixEngine.instance().execute(reqDTO);
            return gson.toJson(obj);
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    @Override
    @PostMapping(path = "ognl")
    public String ognl(OgnlReqDTO reqDTO) {
        try {
            Object obj = FixEngine.instance().execute(reqDTO);
            return gson.toJson(obj);
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}
