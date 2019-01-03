package com.git.hui.fix.springmvc;

import com.git.hui.fix.springmvc.endpoint.WebFixEndPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by @author yihui in 09:29 19/1/3.
 */
@Configuration
public class MvcFixerAutoConfig {
    @Bean
    public WebFixEndPoint webFixEndPoint() {
        return new WebFixEndPoint();
    }

}
