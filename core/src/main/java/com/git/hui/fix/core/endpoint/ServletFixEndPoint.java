package com.git.hui.fix.core.endpoint;

import com.alibaba.fastjson.JSON;
import com.git.hui.fix.api.constants.EndPoint;
import com.git.hui.fix.api.modal.FixReqDTO;
import com.git.hui.fix.api.spi.FixEndPoint;
import com.git.hui.fix.core.FixEngine;
import com.google.gson.Gson;

/**
 * Created by @author yihui in 17:10 18/12/29.
 */
@EndPoint(order = Integer.MAX_VALUE)
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

    private Gson gson = new Gson();

    @Override
    public String call(FixReqDTO reqDTO) {
        //  fixme 这里改成gson进行序列化，使用fastjson序列化时，如果key为int，不会包含在双引号中
        return gson.toJson(FixEngine.instance().execute(reqDTO));
    }

    public static void main(String[] args) throws InterruptedException {
        ServletFixEndPoint.getInstance();
        Thread.sleep(20 * 3600);
    }
}
