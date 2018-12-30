package com.git.hui.fix.spring.endpoint;

import com.alibaba.fastjson.JSON;
import com.git.hui.fix.api.constants.EndPoint;
import com.git.hui.fix.api.modal.FixReqDTO;
import com.git.hui.fix.api.spi.FixEndPoint;
import com.git.hui.fix.core.FixEngine;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by @author yihui in 17:41 18/12/29.
 */
@RestController
@EndPoint(instance = false)
public class WebFixEndPoint implements FixEndPoint {
    @Override
    @PostMapping(path = "fixer/call")
    public String call(@RequestBody FixReqDTO reqDTO) {
        Object ans = FixEngine.instance().execute(reqDTO);
        return JSON.toJSONString(ans);
    }
}
