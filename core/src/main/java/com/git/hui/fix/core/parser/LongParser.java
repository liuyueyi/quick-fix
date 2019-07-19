package com.git.hui.fix.core.parser;

import com.git.hui.fix.api.modal.ImmutablePair;

import java.lang.reflect.Type;

/**
 * Created by @author yihui in 14:39 19/7/19.
 */
public class LongParser extends AbstractArgParser {
    private static final String SIMPLE_TAG = "long";
    private static final String LONG_TAG = "Long";
    private static final String LONG_FULL_TAG = "java.lang.Long";

    @Override
    public ImmutablePair<Type, Object> parse(String type, String value) {
        if (SIMPLE_TAG.equalsIgnoreCase(type)) {
            return ImmutablePair.of(long.class, NULL_TAG.equalsIgnoreCase(value) ? null : Long.parseLong(value));
        }

        if (LONG_TAG.equalsIgnoreCase(type) || LONG_FULL_TAG.equalsIgnoreCase(type)) {
            return ImmutablePair.of(Long.class, NULL_TAG.equalsIgnoreCase(value) ? null : Long.valueOf(value));
        }

        return null;
    }
}
