package com.git.hui.fix.springmvc.endpoint;

import com.git.hui.fix.api.constants.EndPoint;
import com.git.hui.fix.api.modal.OgnlReqDTO;
import com.git.hui.fix.api.modal.ReflectReqDTO;
import com.git.hui.fix.api.spi.FixEndPoint;
import com.git.hui.fix.core.FixEngine;
import com.google.gson.Gson;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by @author yihui in 17:41 18/12/29.
 */
@RestController
@RequestMapping(path = "inject-fixer-endpoint")
@EndPoint(instance = false)
public class WebFixEndPoint implements FixEndPoint {

    private Gson gson = new Gson();

    @Override
    @PostMapping(path = "call")
    public String call(@RequestBody ReflectReqDTO reqDTO) {
        try {
            Object ans = FixEngine.instance().execute(reqDTO);
            return gson.toJson(ans);
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    @Override
    @PostMapping(path = "ognl")
    public String ognl(@RequestBody OgnlReqDTO reqDTO) {
        try {
            Object ans = FixEngine.instance().execute(reqDTO);
            return gson.toJson(ans);
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}
