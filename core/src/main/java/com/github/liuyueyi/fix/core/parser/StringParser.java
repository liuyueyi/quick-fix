package com.github.liuyueyi.fix.core.parser;

import com.github.liuyueyi.fix.api.modal.ImmutablePair;
import com.github.liuyueyi.fix.core.util.StringUtils;

import java.lang.reflect.Type;

/**
 * Created by @author yihui in 14:39 19/7/19.
 */
public class StringParser extends AbstractArgParser {
    private static final String LONG_TAG = "String";
    private static final String LONG_FULL_TAG = "java.lang.String";

    @Override
    public ImmutablePair<Type, Object> parse(String type, String value) {
        if (StringUtils.isBlank(type) || LONG_TAG.equalsIgnoreCase(type) || LONG_FULL_TAG.equalsIgnoreCase(type)) {
            return ImmutablePair.of(String.class, NULL_TAG.equalsIgnoreCase(value) ? null : value);
        }

        return null;
    }

    /**
     * 根据个人经验，将优先级设置为比其他的几个转换高一点
     *
     * @return
     */
    @Override
    public int order() {
        return 9;
    }
}
