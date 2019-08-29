package com.git.hui.fix.core.binder;

import com.git.hui.fix.api.loader.ServerLoader;
import com.git.hui.fix.api.spi.ServerLoaderBinder;
import com.git.hui.fix.core.loader.StaticServerLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by @author yihui in 16:15 18/12/29.
 */
public class DefaultServerBinder implements ServerLoaderBinder {

    @Override
    public List<ServerLoader> getBeanLoader() {
        List<ServerLoader> list = new ArrayList<>(1);
        list.add(new StaticServerLoader());
        return list;
    }

}
