package com.github.liuyueyi.fix.springcloud;

import com.github.liuyueyi.fix.springcloud.endpoint.ActuateFixEndPoint;
import org.springframework.boot.actuate.endpoint.web.annotation.RestControllerEndpoint;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by @author yihui in 09:47 19/1/3.
 */
@Configuration
@ConditionalOnClass(RestControllerEndpoint.class)
public class CloudFixerAutoConfig {
    @Bean
    public ActuateFixEndPoint actuateFixEndPoint() {
        return new ActuateFixEndPoint();
    }
}
