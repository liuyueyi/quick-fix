package com.git.hui.fix.core.endpoint;

import com.alibaba.fastjson.JSON;
import com.git.hui.fix.api.modal.FixReqDTO;
import com.git.hui.fix.api.spi.FixEndPoint;
import com.git.hui.fix.core.FixEngine;

/**
 * Created by @author yihui in 17:10 18/12/29.
 */
public class ServletFixEndPoint implements FixEndPoint {
    private static volatile ServletFixEndPoint instance;

    public ServletFixEndPoint() {
        init(this);
    }

    public static ServletFixEndPoint getInstance() {
        return init(null);
    }

    private static ServletFixEndPoint init(ServletFixEndPoint servletFixEndPoint) {
        if (instance == null) {
            synchronized (ServletFixEndPoint.class) {
                if (instance == null) {
                    instance = servletFixEndPoint == null ? new ServletFixEndPoint() : servletFixEndPoint;
                    BasicHttpServer.startHttpServer();
                }
            }
        }

        return instance;
    }

    @Override
    public String call(FixReqDTO reqDTO) {
        return JSON.toJSONString(FixEngine.instance().execute(reqDTO));
    }

    public static void main(String[] args) throws InterruptedException {
        ServletFixEndPoint.getInstance();
        Thread.sleep(20 * 3600);
    }
}
