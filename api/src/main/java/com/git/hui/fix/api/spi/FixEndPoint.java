package com.git.hui.fix.api.spi;

import com.git.hui.fix.api.modal.FixReqDTO;

/**
 * 对外暴露的服务端点，接收外部的请求数据，并返回执行后的结果
 * - 一个常见的case就是开一个server服务，接收外部的json post请求
 *
 * Created by @author yihui in 17:08 18/12/29.
 */
public interface FixEndPoint {
    String call(FixReqDTO reqDTO);
}
