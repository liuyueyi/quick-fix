package com.github.liuyueyi.fix.example.jar.loader;

import com.github.liuyueyi.fix.api.constants.LoaderOrder;
import com.github.liuyueyi.fix.api.exception.ServerNotFoundException;
import com.github.liuyueyi.fix.api.modal.ImmutablePair;
import com.github.liuyueyi.fix.api.modal.ReflectReqDTO;
import com.github.liuyueyi.fix.core.loader.ServerLoaderTemplate;
import com.github.liuyueyi.fix.core.util.StringUtils;
import com.github.liuyueyi.fix.example.jar.holder.ServerHolder;

/**
 * Created by @author yihui in 22:21 19/1/3.
 */
@LoaderOrder(order = 0)
public class SelfServerLoader extends ServerLoaderTemplate {
    @Override
    public ImmutablePair<Object, Class> loadServicePair(String service) {
        Object server = ServerHolder.getServer(service);
        if (server == null) {
            throw new ServerNotFoundException("not server:" + service + " found!");
        }

        return ImmutablePair.of(server, server.getClass());
    }

    @Override
    public boolean enable(ReflectReqDTO reqDTO) {
        return StringUtils.isBlank(reqDTO.getType()) || "server".equals(reqDTO.getType());
    }

    @Override
    public Object getInvokeObject(String key) {
        return ServerHolder.getServer(key);
    }


    public static SelfServerLoader getLoader() {
        return new SelfServerLoader();
    }
}
