package com.github.liuyueyi.fix.core.parser;

import com.github.liuyueyi.fix.api.modal.ImmutablePair;

import java.lang.reflect.Type;

/**
 * Created by @author yihui in 14:39 19/7/19.
 */
public class BooleanParser extends AbstractArgParser {
    private static final String SIMPLE_TAG = "boolean";
    private static final String LONG_TAG = "Boolean";
    private static final String LONG_FULL_TAG = "java.lang.Boolean";

    @Override
    public ImmutablePair<Type, Object> parse(String type, String value) {
        if (SIMPLE_TAG.equalsIgnoreCase(type)) {
            return ImmutablePair
                    .of(boolean.class, NULL_TAG.equalsIgnoreCase(value) ? null : Boolean.parseBoolean(value));
        }

        if (LONG_TAG.equalsIgnoreCase(type) || LONG_FULL_TAG.equalsIgnoreCase(type)) {
            return ImmutablePair.of(Boolean.class, NULL_TAG.equalsIgnoreCase(value) ? null : Boolean.valueOf(value));
        }

        return null;
    }
}
