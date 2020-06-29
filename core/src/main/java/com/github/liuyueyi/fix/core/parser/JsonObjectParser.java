package com.github.liuyueyi.fix.core.parser;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.util.ParameterizedTypeImpl;
import com.github.liuyueyi.fix.api.exception.IllegalInvokeArgumentException;
import com.github.liuyueyi.fix.api.modal.ImmutablePair;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by @author yihui in 14:39 19/7/19.
 */
public class JsonObjectParser extends AbstractArgParser {
    private static final Type[] EMPTY_TYPE = new Type[0];

    /**
     * json格式对象转换, 默认的兜底类型
     *
     * @param type  固定为 enum
     * @param value 形如 java.util.Map<java.lang.String, java.util.List<java.util.Map<java.lang.String, java.lang.Integer>>>
     * @return
     */
    @Override
    public ImmutablePair<Type, Object> parse(String type, String value) {
        try {
            Type paramType = genParameterType(type);
            if (NULL_TAG.equalsIgnoreCase(value)) {
                return ImmutablePair.of(paramType, null);
            }

            return ImmutablePair.of(paramType, JSON.parseObject(value, paramType));
        } catch (Exception e) {
            throw new IllegalInvokeArgumentException(
                    "Pare Argument to Object Error! type: " + type + " value: " + value);
        }

    }

    @Override
    public int order() {
        return Integer.MAX_VALUE;
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
