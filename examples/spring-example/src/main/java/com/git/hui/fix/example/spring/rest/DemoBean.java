package com.git.hui.fix.example.spring.rest;

import org.springframework.stereotype.Service;


/**
 * Created by @author yihui in 23:23 18/12/30.
 */
@Service
public class DemoBean {

    private static final String INIT_NAME = "一灰灰Blog";
    private String name;

    public void randName() {
        name = INIT_NAME + ((int) (Math.random() * 100));
    }


}
