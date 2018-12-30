package com.git.hui.fix.spring.loader;

import com.git.hui.fix.api.exception.ServerNotFoundException;
import com.git.hui.fix.api.loader.ServerLoader;
import com.git.hui.fix.api.modal.FixReqDTO;
import com.git.hui.fix.api.modal.ImmutablePair;
import com.git.hui.fix.core.reflect.ReflectUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;

/**
 * Created by @author yihui in 17:21 18/12/29.
 */
public class BeanServerLoader implements ServerLoader {
    private static final String BEAN_TYPE = "bean";

    private static ApplicationContext applicationContext;

    public BeanServerLoader(ApplicationContext applicationContext) {
        BeanServerLoader.applicationContext = applicationContext;
    }

    @Override
    public boolean enable(FixReqDTO reqDTO) {
        return StringUtils.isEmpty(reqDTO.getType()) || BEAN_TYPE.equalsIgnoreCase(reqDTO.getType().trim());
    }

    @Override
    public ImmutablePair<Object, Class> getInvokeObject(FixReqDTO reqDTO) {
        String server = reqDTO.getService();
        Object invokeBean;
        if (!server.contains(".")) {
            // 表示传入的是beanName，通过beanName来查找对应的bean
            invokeBean = applicationContext.getBean(server.trim());
        } else {
            // 表示传入的是完整的服务名，希望通过class来查找对应的bean
            try {
                invokeBean = this.getClass().getClassLoader().loadClass(server.trim());
            } catch (Exception e) {
                throw new ServerNotFoundException("Failed to load Server: " + server);
            }
        }

        if (invokeBean == null) {
            throw new ServerNotFoundException("Server not found: " + server);
        }

        if (!StringUtils.isEmpty(reqDTO.getField())) {
            try {
                invokeBean = ReflectUtil.getField(invokeBean, invokeBean.getClass(), reqDTO.getField());
            } catch (Exception e) {
                throw new ServerNotFoundException("Failed to load Server's Field: " + server + "#" + reqDTO.getField());
            }
        }

        return ImmutablePair.of(invokeBean, invokeBean.getClass());
    }

    public static BeanServerLoader getLoader() {
        return applicationContext.getBean(BeanServerLoader.class);
    }
}
