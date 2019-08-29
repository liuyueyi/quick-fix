package com.git.hui.fix.core.endpoint;

import com.alibaba.fastjson.JSONObject;
import com.git.hui.fix.api.modal.FixReqDTO;
import lombok.extern.slf4j.Slf4j;


import java.io.*;
import java.net.Socket;


/**
 * Created by @author yihui in 10:35 18/12/30.
 */
@Slf4j
public class HttpTask implements Runnable {
    private Socket socket;

    public HttpTask(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        if (socket == null) {
            throw new IllegalArgumentException("socket can't be null.");
        }

        try {
            OutputStream outputStream = socket.getOutputStream();
            PrintWriter out = new PrintWriter(outputStream);

            HttpMessageParser.Request httpRequest = HttpMessageParser.parse2request(socket.getInputStream());
            FixReqDTO request = parseRequest(httpRequest);
            try {
                String result = ServletFixEndPoint.getInstance().call(request);
                String httpRes = HttpMessageParser.buildResponse(httpRequest, result);
                out.print(httpRes);
            } catch (Exception e) {
                String httpRes = HttpMessageParser.buildResponse(httpRequest, e.toString());
                out.print(httpRes);
                log.warn("Failed to execute Inject method! req:{}, e: {}", request, e);
            }
            out.flush();
        } catch (IOException e) {
            log.error("Failed to parse/execute Inject method! e: {}", e);
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                log.error("socket close error! e: {}", e);
            }
        }
    }

    /**
     * @param httpRequest
     * @return
     */
    private FixReqDTO parseRequest(HttpMessageParser.Request httpRequest) {
        FixReqDTO request = JSONObject.parseObject(httpRequest.getMessage(), FixReqDTO.class);
        if (log.isDebugEnabled()) {
            log.debug("current request: {}", request);
        }
        return request;
    }
}

