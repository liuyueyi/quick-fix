package com.git.hui.fix.example.jar.loader;

import com.git.hui.fix.api.constants.LoaderOrder;
import com.git.hui.fix.api.exception.ServerNotFoundException;
import com.git.hui.fix.api.modal.FixReqDTO;
import com.git.hui.fix.api.modal.ImmutablePair;
import com.git.hui.fix.core.loader.ServerLoaderTemplate;
import com.git.hui.fix.core.util.StringUtils;
import com.git.hui.fix.example.jar.holder.ServerHolder;

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
    public boolean enable(FixReqDTO reqDTO) {
        return StringUtils.isBlank(reqDTO.getType()) || "server".equals(reqDTO.getType());
    }


    public static SelfServerLoader getLoader() {
        return new SelfServerLoader();
    }
}
