package com.git.hui.fix.core.reflect;

import com.alibaba.fastjson.JSON;
import com.git.hui.fix.api.exception.ServerInvokedException;
import com.git.hui.fix.api.exception.ServerNotFoundException;
import com.git.hui.fix.core.util.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by @author yihui in 14:45 18/12/13.
 */
public class ReflectUtil {

    /**
     * 遍历类信息，获取成员属性；静态类的调用时，bean应该为null； 实例调用时，为实例本身
     *
     * @param bean
     * @param clz
     * @param fieldName
     * @return
     * @throws IllegalAccessException
     */
    public static Object getField(Object bean, Class clz, String fieldName) throws IllegalAccessException {
        if (clz == Object.class) {
            throw new ServerNotFoundException(
                    "can't find field by fieldName: " + fieldName + " for clz:" + clz.getName());
        }

        for (Field field : clz.getDeclaredFields()) {
            if (field.getName().equals(fieldName)) {
                field.setAccessible(true);
                return field.get(bean);
            }
        }

        return getField(bean, clz.getSuperclass(), fieldName);
    }


    /**
     * 从当前类和父类中查找对应的方法
     *
     * @param clz
     * @param method
     * @param argNum
     * @return
     */
    public static Method getMethod(Class clz, String method, int argNum) {
        if (clz == Object.class) {
            throw new ServerNotFoundException(
                    "can't find method by methodName: " + method + " argNum: " + argNum + " for clz:" + clz.getName());
        }


        for (Method m : clz.getDeclaredMethods()) {
            if (!m.getName().equals(method)) {
                continue;
            }

            if (m.getParameterCount() == argNum) {
                m.setAccessible(true);
                return m;
            }
        }

        return getMethod(clz.getSuperclass(), method, argNum);
    }

    public static String execute(Object bean, Class clz, String method, Object[] args) {
        if (StringUtils.isEmpty(method)) {
            // 获取类的成员属性值时，不传method，直接返回属性值
            return JSON.toJSONString(bean);
        }

        Method chooseMethod = getMethod(clz, method, args.length);

        if (chooseMethod == null) {
            throw new ServerNotFoundException("can't find server's method: " + clz.getName() + "#" + method);
        }

        try {
            chooseMethod.setAccessible(true);
            Object result = chooseMethod.invoke(bean, args);
            return JSON.toJSONString(result);
        } catch (Exception e) {
            throw new ServerInvokedException(
                    "unexpected server invoked " + clz.getName() + "#" + method + " args: " + JSON.toJSONString(args),
                    e);
        }
    }

}
