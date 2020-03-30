package com.git.hui.fix.core.loader;

import com.git.hui.fix.api.exception.ServerNotFoundException;
import com.git.hui.fix.api.modal.ReflectReqDTO;
import com.git.hui.fix.api.modal.ImmutablePair;

/**
 * 静态类服务加载器
 *
 * Created by @author yihui in 15:16 18/12/29.
 */
public class StaticServerLoader extends ServerLoaderTemplate {
    private static final String STATIC_TYPE = "static";

    @Override
    public boolean enable(ReflectReqDTO reqDTO) {
        return STATIC_TYPE.equalsIgnoreCase(reqDTO.getType());
    }

    @Override
    public Object getInvokeObject(String key) {
        return null;
    }

    @Override
    public ImmutablePair<Object, Class> loadServicePair(String service) {
        try {
            Class clz = this.getClass().getClassLoader().loadClass(service);
            return ImmutablePair.of(null, clz);
        } catch (Exception e) {
            throw new ServerNotFoundException("parse " + service + " to bean error: " + e.getMessage());
        }
    }
}
