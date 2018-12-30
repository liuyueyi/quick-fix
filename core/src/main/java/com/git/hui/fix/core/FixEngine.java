package com.git.hui.fix.core;

import com.alibaba.fastjson.JSON;
import com.git.hui.fix.api.exception.ServerNotFoundException;
import com.git.hui.fix.api.loader.ServerLoader;
import com.git.hui.fix.api.modal.FixReqDTO;
import com.git.hui.fix.api.modal.ImmutablePair;
import com.git.hui.fix.api.spi.ServerLoaderBinder;
import com.git.hui.fix.core.reflect.ArgumentParser;
import com.git.hui.fix.core.reflect.ReflectUtil;
import com.git.hui.fix.core.endpoint.EndPointLoader;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ServiceLoader;

/**
 * Created by @author yihui in 16:21 18/12/29.
 */
public class FixEngine {
    private static class InnerClass {
        private static final FixEngine INSTANCE = new FixEngine();
    }

    public static FixEngine instance() {
        return InnerClass.INSTANCE;
    }

    private List<ServerLoader> serverLoaders;

    private FixEngine() {
        initAllServerLoads();
        selectFixEndPoint();
    }

    private void initAllServerLoads() {
        ServiceLoader<ServerLoaderBinder> binders = ServiceLoader.load(ServerLoaderBinder.class);

        // 选择一个优先级最高的binder，用于加载所有的ServerLoader
        List<ServerLoader> loaderList = new ArrayList<>(10);
        for (ServerLoaderBinder binder : binders) {
            loaderList.addAll(binder.getBeanLoader());
        }

        serverLoaders = loaderList;
        // 为所有加载的ServerLoader进行优先级排序，这里主要是为了避免第三方的 ServerLoaderBinder 没有实现排序而加上的
        serverLoaders.sort(Comparator.comparingInt(ServerLoader::order));
    }

    /**
     * 选择服务侵入端点
     */
    private void selectFixEndPoint() {
        EndPointLoader.autoLoadEndPoint();
    }

    public Object execute(FixReqDTO req) {
        ImmutablePair<Object, Class> invokeObjPair = null;
        for (ServerLoader loader : serverLoaders) {
            if (loader.enable(req)) {
                invokeObjPair = loader.getInvokeObject(req);
                break;
            }
        }

        if (invokeObjPair == null) {
            throw new ServerNotFoundException("can't find ServerLoader to invoke this req: " + JSON.toJSON(req));
        }

        return doExecute(invokeObjPair.getLeft(), invokeObjPair.getRight(), req);
    }

    private Object doExecute(Object invokeObject, Class invokeClass, FixReqDTO req) {
        Object[] arguments = ArgumentParser.parse(req.getParams());
        return ReflectUtil.execute(invokeObject, invokeClass, req.getMethod(), arguments);
    }

}
