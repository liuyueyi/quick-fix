package com.git.hui.fix.spring;

import com.git.hui.fix.core.FixEngine;
import com.git.hui.fix.spring.endpoint.WebFixEndPoint;
import com.git.hui.fix.spring.loader.BeanServerLoader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by @author yihui in 17:17 18/12/29.
 */
@Configuration
@ComponentScan("com.git.hui.fix.spring.endpoint")
public class FixerAutoConfig {

    @Bean
    public BeanServerLoader beanServerLoader(ApplicationContext applicationContext) {
        return new BeanServerLoader(applicationContext);
    }

}
