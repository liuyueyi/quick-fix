package com.github.liuyueyi.fix.core;

import com.alibaba.fastjson.JSON;
import com.github.liuyueyi.fix.api.exception.ServerNotFoundException;
import com.github.liuyueyi.fix.api.loader.ServerLoader;
import com.github.liuyueyi.fix.api.modal.ImmutablePair;
import com.github.liuyueyi.fix.api.modal.OgnlReqDTO;
import com.github.liuyueyi.fix.api.modal.ReflectReqDTO;
import com.github.liuyueyi.fix.api.spi.ServerLoaderBinder;
import com.github.liuyueyi.fix.core.endpoint.EndPointLoader;
import com.github.liuyueyi.fix.core.ognl.OgnlFacade;
import com.github.liuyueyi.fix.core.reflect.ReflectUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ServiceLoader;

/**
 * Created by @author yihui in 16:21 18/12/29.
 */
@Slf4j
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
        initOgnlContext();
        selectFixEndPoint();
    }

    private void initAllServerLoads() {
        ServiceLoader<ServerLoaderBinder> binders = ServiceLoader.load(ServerLoaderBinder.class);

        // 选择一个优先级最高的binder，用于加载所有的ServerLoader
        List<ServerLoader> loaderList = new ArrayList<>(10);
        for (ServerLoaderBinder binder : binders) {
            loaderList.addAll(binder.getBeanLoader());
            if (log.isDebugEnabled()) {
                for (ServerLoader loader : binder.getBeanLoader()) {
                    log.debug("register ServerLoader: {} by {}", loader.getClass(), binder.getClass());
                }
            }
        }

        serverLoaders = loaderList;
        // 为所有加载的ServerLoader进行优先级排序，这里主要是为了避免第三方的 ServerLoaderBinder 没有实现排序而加上的
        serverLoaders.sort(Comparator.comparingInt(ServerLoader::order));
    }

    private void initOgnlContext() {
        OgnlFacade.instance().init(serverLoaders);
    }

    /**
     * 选择服务侵入端点
     */
    private void selectFixEndPoint() {
        EndPointLoader.autoLoadEndPoint();
    }

    /**
     * 反射表达式执行
     *
     * @param req
     * @return
     */
    public Object execute(ReflectReqDTO req) {
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

    private Object doExecute(Object invokeObject, Class invokeClass, ReflectReqDTO req) {
        return ReflectUtil.execute(invokeObject, invokeClass, req);
    }

    /**
     * ognl表达式执行
     *
     * @param req
     * @return
     */
    public Object execute(OgnlReqDTO req) {
        return OgnlFacade.instance().execute(req);
    }
}
