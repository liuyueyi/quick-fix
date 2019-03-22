package com.git.hui.fix.core.reflect;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.util.ParameterizedTypeImpl;
import com.git.hui.fix.api.exception.IllegalInvokeArgumentException;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * 根据传入的参数来解析为对应的do对象
 * Created by @author yihui in 15:32 18/12/13.
 */
public class ArgumentParser {
    /**
     * default empty arguments
     */
    private static final Object[] EMPTY_ARGS = new Object[0];
    private static final Type[] EMPTY_TYPE = new Type[0];

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
                return ArgumentParser.class.getClassLoader().loadClass(value);
            } else {
                Type paramType = genParameterType(type);
                return JSON.parseObject(value, paramType);
            }
        } catch (Exception e) {
            throw new IllegalInvokeArgumentException(
                    "Pare Argument to Object Error! type: " + type + " value: " + value, e);
        }
    }

    private static Type genParameterType(String express) throws ClassNotFoundException {
        int start = express.indexOf("<");
        if (start < 0) {
            // 非泛型对象，普通的POJO
            return Class.forName(express);
        }
        int end = express.lastIndexOf(">");
        return genParameterType(express.substring(0, start), express.substring(start + 1, end));
    }

    /**
     * java.util.List | java.util.Map<java.lang.String, java.util.List<java.util.Map<java.lang.String, java.lang.Integer>>>
     * java.util.Map | java.lang.String, java.util.List<java.util.Map<java.lang.String, java.lang.Integer>>
     * java.util.List | java.util.Map<java.lang.String, java.lang.Integer>
     * java.util.Map | java.lang.String, java.lang.Integer
     *
     * @param rawType
     * @param actualType
     * @return
     */
    @SuppressWarnings("unchecked")
    private static ParameterizedType genParameterType(String rawType, String actualType) throws ClassNotFoundException {
        if (!actualType.contains("<")) {
            // 普通的对象，直接解析即可
            Type[] actualTypeArguments = parse2simpleType(actualType);
            return new ParameterizedTypeImpl(actualTypeArguments, null, Class.forName(rawType));
        }

        // 泛型起始坐标
        int genericStart = actualType.indexOf("<");
        // 泛型结束坐标
        int genericEnd = actualType.lastIndexOf(">");
        String subActualType = actualType.substring(genericStart + 1, genericEnd);

        // 解决嵌套的泛型类型获取
        int subRawTypeStart = getRawTypeIndex(actualType, genericStart);
        Type subGenType = genParameterType(actualType.substring(subRawTypeStart, genericStart), subActualType);
        Type[] beforeArg = parse2simpleType(actualType, 0, subRawTypeStart);
        Type[] endArg = parse2simpleType(actualType, genericEnd + 1, actualType.length());
        return new ParameterizedTypeImpl(mergeType(beforeArg, subGenType, endArg), null, Class.forName(rawType.trim()));
    }

    /**
     * 解析泛型前面的包装类起始坐标
     *
     * 如： java.lang.String, java.util.List<java.util.Map<java.lang.String, java.lang.Integer>>
     * 返回的就是 java.util.List 这个字符串在整个type的起始位置
     *
     * @param type              字符串
     * @param genericStartIndex 泛型的坐标，如上例中 '<' 的位置
     * @return
     */
    private static int getRawTypeIndex(String type, int genericStartIndex) {
        char tmp;
        int rawTypeIndex = genericStartIndex - 1;
        while (--rawTypeIndex > 0) {
            tmp = type.charAt(rawTypeIndex);
            if (tmp == '<' || tmp == ',' || tmp == ' ') {
                break;
            }
        }

        return rawTypeIndex;
    }

    /**
     * java.util.Map<java.lang.String, java.lang.Integer>
     *
     * @param type
     * @return
     */
    private static Type[] parse2simpleType(String type, int start, int end) throws ClassNotFoundException {
        if (start >= end || end > type.length()) {
            return EMPTY_TYPE;
        }

        return parse2simpleType(type.substring(start, end));
    }

    private static Type[] parse2simpleType(String type) throws ClassNotFoundException {
        if (StringUtils.isBlank(type)) {
            return EMPTY_TYPE;
        }

        String[] arguments = StringUtils.split(type, ",");
        Type[] result = new Type[arguments.length];
        int index = 0;
        for (String s : arguments) {
            result[index++] = Class.forName(s.trim());
        }

        return result;
    }

    private static Type[] mergeType(Type[] before, Type sub, Type[] end) {
        Type[] result = new Type[before.length + end.length + 1];

        int offset = before.length;
        if (offset > 0) {
            System.arraycopy(before, 0, result, 0, offset);
        }
        result[offset] = sub;
        if (end.length > 0) {
            System.arraycopy(end, 0, result, offset + 1, end.length);
        }
        return result;
    }
}
