package com.github.liuyueyi.fix.core.parser;


import com.github.liuyueyi.fix.api.modal.ImmutablePair;

import java.lang.reflect.Type;

/**
 * Created by @author yihui in 14:39 19/7/19.
 */
public class CharParser extends AbstractArgParser {
    private static final String SIMPLE_TAG = "char";
    private static final String LONG_TAG = "Character";
    private static final String LONG_FULL_TAG = "java.lang.Character";

    @Override
    public ImmutablePair<Type, Object> parse(String type, String value) {
        if (SIMPLE_TAG.equalsIgnoreCase(type)) {
            return ImmutablePair.of(char.class, NULL_TAG.equalsIgnoreCase(value) ? null : value.charAt(0));
        }

        if (LONG_TAG.equalsIgnoreCase(type) || LONG_FULL_TAG.equalsIgnoreCase(type)) {
            return ImmutablePair.of(Character.class, NULL_TAG.equalsIgnoreCase(value) ? null : value.charAt(0));
        }

        return null;
    }
}
