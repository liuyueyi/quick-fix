package com.git.hui.fix.core.endpoint;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * http协议解析器
 *
 * Created by @author yihui in 11:35 18/12/30.
 */
@Slf4j
public class HttpMessageParser {

    @Data
    public static class Request {
        /**
         * 请求方法 GET/POST/PUT/DELETE/OPTION...
         */
        private String method;
        /**
         * 请求的uri
         */
        private String uri;
        /**
         * http版本
         */
        private String version;

        /**
         * 请求头
         */
        private Map<String, String> headers;

        /**
         * 请求参数相关
         */
        private String message;
    }

    @Data
    public static class Response {
        private String version;
        private int code;
        private String status;

        private Map<String, String> headers;

        private String message;
    }

    /**
     * http的请求可以分为三部分
     *
     * 第一行为请求行: 即 方法 + URI + 版本
     * 第二部分到一个空行为止，表示请求头
     * 空行
     * 第三部分为接下来所有的，表示发送的内容,message-body；其长度由请求头中的 Content-Length 决定
     *
     * 几个实例如下
     *
     * @param reqStream
     * @return
     */
    public static Request parse2request(InputStream reqStream) throws IOException {
        BufferedReader httpReader = new BufferedReader(new InputStreamReader(reqStream, "UTF-8"));
        Request httpRequest = new Request();
        decodeRequestLine(httpReader, httpRequest);
        decodeRequestHeader(httpReader, httpRequest);
        decodeRequestMessage(httpReader, httpRequest);
        return httpRequest;
    }

    /**
     * 根据标准的http协议，解析请求行
     *
     * @param reader
     * @param request
     */
    private static void decodeRequestLine(BufferedReader reader, Request request) throws IOException {
        String[] strs = StringUtils.split(reader.readLine(), " ");
        assert strs.length == 3;
        request.setMethod(strs[0]);
        request.setUri(strs[1]);
        request.setVersion(strs[2]);
    }

    /**
     * 根据标准http协议，解析请求头
     *
     * @param reader
     * @param request
     * @throws IOException
     */
    private static void decodeRequestHeader(BufferedReader reader, Request request) throws IOException {
        Map<String, String> headers = new HashMap<>(16);
        String line = reader.readLine();
        String[] kv;
        while (!"".equals(line)) {
            kv = StringUtils.split(line, ":");
            assert kv.length == 2;
            headers.put(kv[0].trim().toLowerCase(), kv[1].trim());
            line = reader.readLine();
        }

        request.setHeaders(headers);
        if (log.isDebugEnabled()) {
            log.info("request header: {}", headers);
        }
    }

    /**
     * 根据标注http协议，解析正文
     *
     * @param reader
     * @param request
     * @throws IOException
     */
    private static void decodeRequestMessage(BufferedReader reader, Request request) throws IOException {
        int contentLen = Integer.parseInt(request.getHeaders().getOrDefault("content-length", "-1"));
        if (contentLen > 0) {
            char[] message = new char[contentLen];
            reader.read(message);
            request.setMessage(new String(message));
            return;
        }

        // 如get/options请求就没有message
        // 表示没有message，直接返回
        if (contentLen == -1) {
            return;
        }

        // fixme 这种时候，可能是通过 chunked 方式发送数据，待验证这种支持方式是否准确
        StringBuilder message = new StringBuilder();
        int ch;
        while (reader.ready()) {
            ch = reader.read();
            if (ch <= 0) {
                break;
            }
            message.append((char) ch);
        }
        request.setMessage(message.toString());
    }

    public static String buildResponse(Request request, String response) {
        Response httpResponse = new Response();
        httpResponse.setCode(200);
        httpResponse.setStatus("ok");
        httpResponse.setVersion(request.getVersion());

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Content-Length", String.valueOf(response.getBytes().length));
        httpResponse.setHeaders(headers);

        httpResponse.setMessage(response);

        StringBuilder builder = new StringBuilder();
        buildResponseLine(httpResponse, builder);
        buildResponseHeaders(httpResponse, builder);
        buildResponseMessage(httpResponse, builder);
        return builder.toString();
    }


    private static void buildResponseLine(Response response, StringBuilder stringBuilder) {
        stringBuilder.append(response.getVersion()).append(" ").append(response.getCode()).append(" ")
                .append(response.getStatus()).append("\n");
    }

    private static void buildResponseHeaders(Response response, StringBuilder stringBuilder) {
        for (Map.Entry<String, String> entry : response.getHeaders().entrySet()) {
            stringBuilder.append(entry.getKey()).append(":").append(entry.getValue()).append("\n");
        }
        stringBuilder.append("\n");
    }

    private static void buildResponseMessage(Response response, StringBuilder stringBuilder) {
        stringBuilder.append(response.getMessage());
    }
}

