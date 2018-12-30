package com.git.hui.fix.api.spi;

import com.git.hui.fix.api.constants.LoaderOrder;
import com.git.hui.fix.api.loader.ServerLoader;

import java.util.List;

/**
 * Created by @author yihui in 15:05 18/12/29.
 */
@LoaderOrder
public interface ServerLoaderBinder {
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

    /**
     * 获取框架所有支持的ServerLoader，用于获取不同场景下获取应用中的内存实例
     *
     * @return
     */
    List<ServerLoader> getBeanLoader();

}
