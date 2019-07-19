package com.git.hui.fix.core.parser;

import com.git.hui.fix.api.modal.ImmutablePair;

import java.lang.reflect.Type;

/**
 * Created by @author yihui in 14:39 19/7/19.
 */
public class ShortParser extends AbstractArgParser {
    private static final String SIMPLE_TAG = "short";
    private static final String LONG_TAG = "Short";
    private static final String LONG_FULL_TAG = "java.lang.Short";

    @Override
    public ImmutablePair<Type, Object> parse(String type, String value) {
        if (SIMPLE_TAG.equalsIgnoreCase(type)) {
            return ImmutablePair.of(short.class, NULL_TAG.equalsIgnoreCase(value) ? null : Short.parseShort(value));
        }

        if (LONG_TAG.equalsIgnoreCase(type) || LONG_FULL_TAG.equalsIgnoreCase(type)) {
            return ImmutablePair.of(Short.class, NULL_TAG.equalsIgnoreCase(value) ? null : Short.valueOf(value));
        }

        return null;
    }
}
