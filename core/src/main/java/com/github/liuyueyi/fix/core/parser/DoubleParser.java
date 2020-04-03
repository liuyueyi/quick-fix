package com.github.liuyueyi.fix.core.parser;

import com.github.liuyueyi.fix.api.modal.ImmutablePair;

import java.lang.reflect.Type;

/**
 * Created by @author yihui in 14:39 19/7/19.
 */
public class DoubleParser extends AbstractArgParser {
    private static final String SIMPLE_TAG = "double";
    private static final String LONG_TAG = "Double";
    private static final String LONG_FULL_TAG = "java.lang.Double";

    @Override
    public ImmutablePair<Type, Object> parse(String type, String value) {
        if (SIMPLE_TAG.equalsIgnoreCase(type)) {
            return ImmutablePair.of(double.class, NULL_TAG.equalsIgnoreCase(value) ? null : Double.parseDouble(value));
        }

        if (LONG_TAG.equalsIgnoreCase(type) || LONG_FULL_TAG.equalsIgnoreCase(type)) {
            return ImmutablePair.of(Double.class, NULL_TAG.equalsIgnoreCase(value) ? null : Double.valueOf(value));
        }

        return null;
    }
}
