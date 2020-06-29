package com.github.liuyueyi.fix.core.parser;

import com.github.liuyueyi.fix.api.modal.ImmutablePair;

import java.lang.reflect.Type;

/**
 * Created by @author yihui in 14:39 19/7/19.
 */
public class IntegerParser extends AbstractArgParser {
    private static final String SIMPLE_TAG = "int";
    private static final String LONG_TAG = "Integer";
    private static final String LONG_FULL_TAG = "java.lang.Integer";

    @Override
    public ImmutablePair<Type, Object> parse(String type, String value) {
        if (SIMPLE_TAG.equalsIgnoreCase(type)) {
            return ImmutablePair.of(int.class, NULL_TAG.equalsIgnoreCase(value) ? null : Integer.parseInt(value));
        }

        if (LONG_TAG.equalsIgnoreCase(type) || LONG_FULL_TAG.equalsIgnoreCase(type)) {
            return ImmutablePair.of(Integer.class, NULL_TAG.equalsIgnoreCase(value) ? null : Integer.valueOf(value));
        }

        return null;
    }
}
