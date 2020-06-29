package com.github.liuyueyi.fix.core.binder;

import com.github.liuyueyi.fix.api.loader.ServerLoader;
import com.github.liuyueyi.fix.api.spi.ServerLoaderBinder;
import com.github.liuyueyi.fix.core.loader.StaticServerLoader;

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
