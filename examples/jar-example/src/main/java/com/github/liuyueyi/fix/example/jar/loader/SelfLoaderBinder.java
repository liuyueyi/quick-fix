package com.github.liuyueyi.fix.example.jar.loader;

import com.github.liuyueyi.fix.api.loader.ServerLoader;
import com.github.liuyueyi.fix.api.spi.ServerLoaderBinder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by @author yihui in 22:37 19/1/3.
 */
public class SelfLoaderBinder implements ServerLoaderBinder {
    @Override
    public List<ServerLoader> getBeanLoader() {
        List<ServerLoader> list = new ArrayList<>(1);
        list.add(SelfServerLoader.getLoader());
        return list;
    }
}
