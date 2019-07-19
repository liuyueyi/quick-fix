package com.git.hui.fix.core.parser;

import com.git.hui.fix.api.exception.IllegalInvokeArgumentException;
import com.git.hui.fix.api.modal.ImmutablePair;
import com.git.hui.fix.core.reflect.ArgumentParser;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * Created by @author yihui in 14:39 19/7/19.
 */
public class EnumParser extends AbstractArgParser {
    private static final String LONG_TAG = "Enum";

    /**
     * 字符串转枚举
     *
     * @param type  固定为 enum
     * @param value 形如 net.finbtc.doraemon.inject.test.GenicTest.InnerName#DEF， 前面为类，后面为枚举name
     * @return
     */
    @Override
    @SuppressWarnings("unchecked")
    public ImmutablePair<Type, Object> parse(String type, String value) {
        if (!LONG_TAG.equalsIgnoreCase(type)) {
            return null;
        }


        String[] kv = StringUtils.split(value, "#");
        if (kv.length != 2) {
            throw new IllegalInvokeArgumentException("illegal value for enum!");
        }

        try {
            Class clz = ArgumentParser.class.getClassLoader().loadClass(kv[0]);
            Method method = clz.getDeclaredMethod("valueOf", String.class);
            method.setAccessible(true);
            return ImmutablePair.of(clz, method.invoke(null, kv[1].toUpperCase()));
        } catch (Exception e) {
            throw new IllegalInvokeArgumentException("failed to parse enum: " + value);
        }
    }
}
