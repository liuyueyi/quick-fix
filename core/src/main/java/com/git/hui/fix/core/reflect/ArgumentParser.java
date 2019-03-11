package com.git.hui.fix.core.reflect;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.util.ParameterizedTypeImpl;
import com.git.hui.fix.api.exception.IllegalInvokeArgumentException;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;

/**
 * 根据传入的参数来解析为对应的do对象
 * Created by @author yihui in 15:32 18/12/13.
 */
public class ArgumentParser {
    /**
     * default empty arguments
     */
    private static final Object[] EMPTY_ARGS = new Object[]{};

    public static Object[] parse(String[] args) {
        if (args == null || args.length == 0) {
            return EMPTY_ARGS;
        }

        Object[] result = new Object[args.length];
        for (int i = 0; i < args.length; i++) {
            result[i] = buildArgObj(args[i]);
        }
        return result;
    }

    /**
     * 将传入的String类型参数封装为目标对象
     *
     * @param arg 以#分割，根据我们的定义，
     *            第一个#前为目标对象类型，
     *            最后一个#后为目标对象值（如果为JOPO，则采用json方式进行反序列化）
     *            中间的作为泛型的参数类型传入
     *
     *            几个常见的case如:
     *
     *            "Hello World"  返回 "Hello Word"
     *            "int#10" 返回 10
     *            "com.git.hui.fix.core.binder.DefaultServerBinder#{}" 返回的是对象 defaultServerBinder
     *            "java.util.List#java.lang.String#["ads","bcd"]  返回的是List集合, 相当于  Arrays.asList("asd", "bcd")
     * @return
     */
    private static Object buildArgObj(String arg) {
        String[] typeValue = arg.split("#");
        if (typeValue.length == 1) {
            // 没有 #，把参数当成String
            return arg;
        } else if (typeValue.length == 2) {
            // 标准的kv参数, 前面为参数类型，后面为参数值
            return parseStrToObj(typeValue[0], typeValue[1]);
        } else if (typeValue.length >= 3) {
            // 对于包含泛型的参数类型
            // java.util.List#java.lang.String#["ads","bcd"]
            String[] reflectTypes = new String[typeValue.length - 2];
            System.arraycopy(typeValue, 1, reflectTypes, 0, typeValue.length - 2);
            return parseStr2GenericObj(typeValue[typeValue.length - 1], typeValue[0], reflectTypes);
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
            } else if ("short".equals(type) || "Short".equals(type)) {
                return Short.parseShort(value);
            } else if ("BigDecimal".equals(type)) {
                return new BigDecimal(value);
            } else if ("BigInteger".equals(type)) {
                return new BigInteger(type);
            } else if ("String".equals(type)) {
                return value;
            } else if ("Class".equalsIgnoreCase(type)) {
                return ArgumentParser.class.getClassLoader().loadClass(type);
            } else {
                Class clz = ArgumentParser.class.getClassLoader().loadClass(type);
                return JSON.parseObject(value, clz);
            }
        } catch (Exception e) {
            throw new IllegalInvokeArgumentException(
                    "Pare Argument to Object Error! type: " + type + " value: " + value, e);
        }
    }

    /**
     * 将value转换为包含泛型的参数类型
     *
     * @param value   对象json串
     * @param clzType 对象类型
     * @param tTypes  泛型参数类型
     * @return
     */
    private static Object parseStr2GenericObj(String value, String clzType, String... tTypes) {
        try {
            Type[] paramsType = new Type[tTypes.length];
            int count = 0;
            for (String t : tTypes) {
                paramsType[count++] = getType(t);
            }

            // 这里借助fastjson指定精确的Type来实现反序列化
            Type type = new ParameterizedTypeImpl(paramsType, null, getType(clzType));
            return JSONObject.parseObject(value, type);
        } catch (Exception e) {
            throw new IllegalInvokeArgumentException(
                    "Pare Argument to Object Error! type: " + clzType + " # " + Arrays.asList(tTypes) + " value: " +
                            value, e);
        }
    }

    /**
     * 获取参数类型
     *
     * @param type
     * @return
     * @throws ClassNotFoundException
     */
    private static Type getType(String type) throws ClassNotFoundException {
        return ArgumentParser.class.getClassLoader().loadClass(type);
    }
}
