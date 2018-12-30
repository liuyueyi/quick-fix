package com.git.hui.fix.core.reflect;

import com.alibaba.fastjson.JSON;
import com.git.hui.fix.api.exception.IllegalInvokeArgumentException;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * 根据传入的参数来解析为对应的do对象
 * Created by @author yihui in 15:32 18/12/13.
 */
public class ArgumentParser {
    private static final Object[] emptyArgs = new Object[]{};

    public static Object[] parse(String[] args) {
        if (args == null || args.length == 0) {
            return emptyArgs;
        }

        Object[] result = new Object[args.length];
        for (int i = 0; i < args.length; i++) {
            result[i] = buildArgObj(args[i]);
        }
        return result;
    }

    private static Object buildArgObj(String arg) {
        String[] typeValue = arg.split("#");
        if (typeValue.length == 1) {
            // 没有 #，把参数当成String
            return arg;
        } else if (typeValue.length == 2) {
            // 标准的kv参数
            return parseStrToObj(typeValue[0], typeValue[1]);
        } else {
            throw new IllegalInvokeArgumentException("Illegal invoke arg: " + arg);
        }
    }

    private static Object parseStrToObj(String type, String value) {
        try {
            if ("int".equals(type) || "Integer".equals(type)) {
                return Integer.parseInt(value);
            } else if ("long".equals(type) || "Long".equals(type)) {
                return Long.parseLong(value);
            } else if ("float".equals(type) || "Float".equals(type)) {
                return Float.parseFloat(value);
            } else if ("double".equals(type) || "Double".equals(type)) {
                return Double.parseDouble(value);
            } else if ("byte".equals(type) || "Character".equals(type)) {
                return Byte.parseByte(value);
            } else if ("boolean".equals(type) || "Boolean".equals(type)) {
                return Boolean.parseBoolean(value);
            } else if ("BigDecimal".equals(type)) {
                return new BigDecimal(value);
            } else if ("BigInteger".equals(type)) {
                return new BigInteger(type);
            } else if ("String".equals(type)) {
                return value;
            } else {
                Class clz = ArgumentParser.class.getClassLoader().loadClass(type);
                return JSON.parseObject(value, clz);
            }
        } catch (Exception e) {
            throw new IllegalInvokeArgumentException(
                    "Pare Argument to Object Error! type: " + type + " value: " + value, e);
        }
    }
}
