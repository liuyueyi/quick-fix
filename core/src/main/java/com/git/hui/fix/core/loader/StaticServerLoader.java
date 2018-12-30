package com.git.hui.fix.core.loader;

import com.git.hui.fix.api.exception.ServerNotFoundException;
import com.git.hui.fix.api.loader.ServerLoader;
import com.git.hui.fix.api.modal.FixReqDTO;
import com.git.hui.fix.api.modal.ImmutablePair;
import com.git.hui.fix.core.reflect.ReflectUtil;
import com.git.hui.fix.core.util.StringUtils;

/**
 * 静态类服务加载器
 *
 * Created by @author yihui in 15:16 18/12/29.
 */
public class StaticServerLoader implements ServerLoader {
    private static final String STATIC_TYPE = "static";

    @Override
    public boolean enable(FixReqDTO reqDTO) {
        return STATIC_TYPE.equalsIgnoreCase(reqDTO.getType());
    }

    @Override
    public ImmutablePair<Object, Class> getInvokeObject(FixReqDTO reqDTO) {
        String service = reqDTO.getService();
        Class clz;

        try {
            clz = this.getClass().getClassLoader().loadClass(service);
        } catch (Exception e) {
            throw new ServerNotFoundException("parse " + service + " to bean error: " + e.getMessage());
        }

        if (StringUtils.isEmpty(reqDTO.getField())) {
            return ImmutablePair.of(null, clz);
        }

        Object invokeTarget;
        try {
            invokeTarget = ReflectUtil.getField(null, clz, reqDTO.getField());
        } catch (Exception e) {
            throw new ServerNotFoundException("get server#filed error!", e);
        }

        return ImmutablePair.of(invokeTarget, invokeTarget.getClass());
    }
}
