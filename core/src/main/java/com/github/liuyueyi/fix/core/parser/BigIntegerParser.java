package com.github.liuyueyi.fix.core.parser;

import com.github.liuyueyi.fix.api.modal.ImmutablePair;

import java.lang.reflect.Type;
import java.math.BigInteger;

/**
 * Created by @author yihui in 14:39 19/7/19.
 */
public class BigIntegerParser extends AbstractArgParser {
    private static final String LONG_TAG = "BigInteger";
    private static final String LONG_FULL_TAG = "java.math.BigInteger";

    @Override
    public ImmutablePair<Type, Object> parse(String type, String value) {
        if (LONG_TAG.equalsIgnoreCase(type) || LONG_FULL_TAG.equalsIgnoreCase(type)) {
            return ImmutablePair.of(BigInteger.class, NULL_TAG.equalsIgnoreCase(value) ? null : new BigInteger
                    (value));
        }

        return null;
    }
}
