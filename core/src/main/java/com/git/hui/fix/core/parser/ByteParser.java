package com.git.hui.fix.core.parser;

import com.git.hui.fix.api.modal.ImmutablePair;

import java.lang.reflect.Type;

/**
 * Created by @author yihui in 14:39 19/7/19.
 */
public class ByteParser extends AbstractArgParser {
    private static final String SIMPLE_TAG = "byte";
    private static final String LONG_TAG = "Byte";
    private static final String LONG_FULL_TAG = "java.lang.Byte";

    @Override
    public ImmutablePair<Type, Object> parse(String type, String value) {
        if (SIMPLE_TAG.equalsIgnoreCase(type)) {
            return ImmutablePair.of(byte.class, NULL_TAG.equalsIgnoreCase(value) ? null : Byte.parseByte(value));
        }

        if (LONG_TAG.equalsIgnoreCase(type) || LONG_FULL_TAG.equalsIgnoreCase(type)) {
            return ImmutablePair.of(Byte.class, NULL_TAG.equalsIgnoreCase(value) ? null : Byte.valueOf(value));
        }

        return null;
    }
}
