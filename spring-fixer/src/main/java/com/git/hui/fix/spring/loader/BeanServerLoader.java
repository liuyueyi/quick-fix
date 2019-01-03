package com.git.hui.fix.spring.loader;

import com.git.hui.fix.api.exception.ServerNotFoundException;
import com.git.hui.fix.api.loader.ServerLoader;
import com.git.hui.fix.api.modal.FixReqDTO;
import com.git.hui.fix.api.modal.ImmutablePair;
import com.git.hui.fix.core.loader.ServerLoaderTemplate;
import com.git.hui.fix.core.reflect.ReflectUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;

/**
 * Created by @author yihui in 17:21 18/12/29.
 */
public class BeanServerLoader extends ServerLoaderTemplate {
    private static final String BEAN_TYPE = "bean";

    private static ApplicationContext applicationContext;

    public BeanServerLoader(ApplicationContext applicationContext) {
        BeanServerLoader.applicationContext = applicationContext;
    }

    @Override
    public boolean enable(FixReqDTO reqDTO) {
        return StringUtils.isEmpty(reqDTO.getType()) || BEAN_TYPE.equalsIgnoreCase(reqDTO.getType().trim());
    }

    private boolean beanName(String server) {
        return !server.contains(".");
    }

    @Override
    public ImmutablePair<Object, Class> loadServicePair(String server) {
        Object invokeBean = null;
        if (beanName(server)) {
            // 表示传入的是beanName，通过beanName来查找对应的bean
            invokeBean = applicationContext.getBean(server.trim());
        } else {
            // 表示传入的是完整的服务名，希望通过class来查找对应的bean
            try {
                Class clz = this.getClass().getClassLoader().loadClass(server.trim());
                if (clz != null) {
                    invokeBean = applicationContext.getBean(clz);
                }
            } catch (Exception e) {
                throw new ServerNotFoundException("Failed to load Server: " + server);
            }
        }

        if (invokeBean == null) {
            throw new ServerNotFoundException("Server not found: " + server);
        }

        return ImmutablePair.of(invokeBean, invokeBean.getClass());
    }

    public static BeanServerLoader getLoader() {
        return applicationContext.getBean(BeanServerLoader.class);
    }
}
