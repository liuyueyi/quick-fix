package com.git.hui.fix.core.parser;

import com.git.hui.fix.api.modal.ImmutablePair;

import java.lang.reflect.Type;
import java.math.BigDecimal;

/**
 * Created by @author yihui in 14:39 19/7/19.
 */
public class BitDecimalParser extends AbstractArgParser {
    private static final String LONG_TAG = "BigDecimal";
    private static final String LONG_FULL_TAG = "java.math.BigDecimal";

    @Override
    public ImmutablePair<Type, Object> parse(String type, String value) {
        if (LONG_TAG.equalsIgnoreCase(type) || LONG_FULL_TAG.equalsIgnoreCase(type)) {
            return ImmutablePair.of(BigDecimal.class, NULL_TAG.equalsIgnoreCase(value) ? null : new BigDecimal(value));
        }

        return null;
    }
}
