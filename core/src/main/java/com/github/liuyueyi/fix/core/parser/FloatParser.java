package com.github.liuyueyi.fix.core.parser;

import com.github.liuyueyi.fix.api.modal.ImmutablePair;

import java.lang.reflect.Type;

/**
 * Created by @author yihui in 14:39 19/7/19.
 */
public class FloatParser extends AbstractArgParser {
    private static final String SIMPLE_TAG = "float";
    private static final String LONG_TAG = "Float";
    private static final String LONG_FULL_TAG = "java.lang.Float";

    @Override
    public ImmutablePair<Type, Object> parse(String type, String value) {
        if (SIMPLE_TAG.equalsIgnoreCase(type)) {
            return ImmutablePair.of(float.class, NULL_TAG.equalsIgnoreCase(value) ? null : Float.parseFloat(value));
        }

        if (LONG_TAG.equalsIgnoreCase(type) || LONG_FULL_TAG.equalsIgnoreCase(type)) {
            return ImmutablePair.of(Float.class, NULL_TAG.equalsIgnoreCase(value) ? null : Float.valueOf(value));
        }

        return null;
    }
}
