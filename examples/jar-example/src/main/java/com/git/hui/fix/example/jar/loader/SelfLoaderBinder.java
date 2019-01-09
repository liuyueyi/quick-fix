package com.git.hui.fix.example.jar.loader;

import com.git.hui.fix.api.loader.ServerLoader;
import com.git.hui.fix.api.spi.ServerLoaderBinder;

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
