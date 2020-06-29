package com.github.liuyueyi.fix.example.springmvc.rest;

/**
 * Created by @author yihui in 19:45 19/7/5.
 */
public class SingletonBean {

    private static class InnerClz {
        private static final SingletonBean INSTANCE = new SingletonBean();
    }

    public static SingletonBean getInstance() {
        return InnerClz.INSTANCE;
    }

    public String sayHello(String hello) {
        return hello;
    }
}
