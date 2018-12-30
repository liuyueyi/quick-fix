package com.git.hui.fix.api.loader;

import com.git.hui.fix.api.constants.LoaderOrder;
import com.git.hui.fix.api.modal.FixReqDTO;
import com.git.hui.fix.api.modal.ImmutablePair;

/**
 * 用来实现加载应用中的实例或静态类的接口
 *
 * Created by @author yihui in 14:57 18/12/29.
 */
@LoaderOrder
public interface ServerLoader {
    /**
     * 返回优先级
     *
     * @return
     */
    default int order() {
        try {
            return this.getClass().getAnnotation(LoaderOrder.class).order();
        } catch (Exception e) {
            return 10;
        }
    }

    boolean enable(FixReqDTO reqDTO);

    ImmutablePair<Object, Class> getInvokeObject(FixReqDTO reqDTO);

}
