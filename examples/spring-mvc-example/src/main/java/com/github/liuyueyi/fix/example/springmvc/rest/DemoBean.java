package com.github.liuyueyi.fix.example.springmvc.rest;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * Created by @author yihui in 23:23 18/12/30.
 */
@Service
public class DemoBean {

    private static final String INIT_NAME = "一灰灰Blog";
    private List<String> values = new ArrayList<>();
    private AtomicInteger cnt = new AtomicInteger(0);
    private String name;

    public int randName() {
        name = INIT_NAME + ((int) (Math.random() * 100));
        cnt.addAndGet(1);
        values.add(name);
        return cnt.get();
    }

}
