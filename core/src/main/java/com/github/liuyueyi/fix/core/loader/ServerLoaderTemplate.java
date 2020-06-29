package com.github.liuyueyi.fix.core.loader;

import com.github.liuyueyi.fix.api.exception.ServerNotFoundException;
import com.github.liuyueyi.fix.api.loader.ServerLoader;
import com.github.liuyueyi.fix.api.modal.ImmutablePair;
import com.github.liuyueyi.fix.api.modal.ReflectReqDTO;
import com.github.liuyueyi.fix.core.reflect.ReflectUtil;
import com.github.liuyueyi.fix.core.util.StringUtils;


/**
 * 服务加载模板类
 * Created by @author yihui in 11:58 19/1/3.
 */
public abstract class ServerLoaderTemplate implements ServerLoader {

    @Override
    public ImmutablePair<Object, Class> getInvokeObject(ReflectReqDTO reqDTO) {
        ImmutablePair<Object, Class> serverPair = loadServicePair(reqDTO.getService());

        if (StringUtils.isEmpty(reqDTO.getField())) {
            return serverPair;
        }

        return loadFieldPair(reqDTO, serverPair);
    }

    /**
     * 返回目标对象
     *
     * @param service
     * @return
     */
    public abstract ImmutablePair<Object, Class> loadServicePair(String service);

    public ImmutablePair<Object, Class> loadFieldPair(ReflectReqDTO reqDTO, ImmutablePair<Object, Class> serverPair) {
        try {
            return ReflectUtil.getField(serverPair.getLeft(), serverPair.getRight(), reqDTO.getField());
        } catch (Exception e) {
            throw new ServerNotFoundException("get server#filed error!", e);
        }
    }
}
