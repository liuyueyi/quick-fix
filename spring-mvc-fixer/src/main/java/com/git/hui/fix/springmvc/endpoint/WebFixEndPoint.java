package com.git.hui.fix.springmvc.endpoint;

import com.git.hui.fix.api.constants.EndPoint;
import com.git.hui.fix.api.modal.FixReqDTO;
import com.git.hui.fix.api.spi.FixEndPoint;
import com.git.hui.fix.core.FixEngine;
import com.google.gson.Gson;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by @author yihui in 17:41 18/12/29.
 */
@RestController
@EndPoint(instance = false)
public class WebFixEndPoint implements FixEndPoint {

    private Gson gson = new Gson();

    @Override
    @PostMapping(path = "inject-fixer-endpoint/call")
    public String call(@RequestBody FixReqDTO reqDTO) {
        Object ans = FixEngine.instance().execute(reqDTO);
        return gson.toJson(ans);
    }
}
