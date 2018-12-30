package com.git.hui.fix.core.endpoint;

import com.git.hui.fix.api.modal.FixReqDTO;


import java.io.*;
import java.net.Socket;


/**
 * Created by @author yihui in 10:35 18/12/30.
 */
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
            }
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param httpRequest
     * @return
     */
    private FixReqDTO parseRequest(HttpMessageParser.Request httpRequest) {
        FixReqDTO request = new FixReqDTO();
        System.out.println("req: " + httpRequest);
        return request;
    }
}

