package com.git.hui.fix.core.parser;

import com.git.hui.fix.api.exception.IllegalInvokeArgumentException;
import com.git.hui.fix.api.modal.ImmutablePair;
import com.git.hui.fix.core.reflect.ArgumentParser;

import java.lang.reflect.Type;

/**
 * Created by @author yihui in 14:39 19/7/19.
 */
public class ClassParser extends AbstractArgParser {
    private static final String LONG_TAG = "class";
    private static final String LONG_FULL_TAG = "java.lang.Class";

    @Override
    public ImmutablePair<Type, Object> parse(String type, String value) {
        if (LONG_TAG.equalsIgnoreCase(type) || LONG_FULL_TAG.equalsIgnoreCase(type)) {
            try {
                return ImmutablePair.of(Class.class, NULL_TAG.equalsIgnoreCase(value) ? null :
                        ArgumentParser.class.getClassLoader().loadClass(value));
            } catch (ClassNotFoundException e) {
                throw new IllegalInvokeArgumentException(
                        "Pare Argument to Object Error! type: " + type + " value: " + value, e);
            }
        }

        return null;
    }
}
