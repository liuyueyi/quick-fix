package com.git.hui.fix.example.jar.server;

/**
 * Created by @author yihui in 22:32 19/1/3.
 */
public class HelloServer {
    private String title;

    public HelloServer(String title) {
        this.title = title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String sayHello() {
        return title;
    }

}
