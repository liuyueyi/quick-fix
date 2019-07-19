package com.git.hui.fix.core.parser;



import com.git.hui.fix.api.modal.ImmutablePair;

import java.lang.reflect.Type;

/**
 * 解析传入的参数，然后返回参数类型和value的转换器
 * Created by @author yihui in 14:38 19/7/19.
 */
public interface IArgParser extends Comparable<IArgParser> {


    ImmutablePair<Type, Object> parse(String type, String value);

    /**
     * 排序
     *
     * @return
     */
    default int order() {
        return 10;
    }
}
