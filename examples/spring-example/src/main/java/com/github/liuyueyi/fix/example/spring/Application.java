package com.github.liuyueyi.fix.example.spring;

import com.github.liuyueyi.fix.core.FixEngine;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by @author yihui in 17:49 18/12/29.
 */
@SpringBootApplication
public class Application implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Application.class);
        app.run(args);
        registerHook();

        try {
            Thread.sleep(2 * 3600 * 1000);
        } catch (Exception e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
    }

    @Override
    public void run(String... strings) throws Exception {
        FixEngine.instance();
    }

    /**
     * 注册一个程序关闭的钩子, 用于回收现场
     */
    private static void registerHook() {
        Runtime.getRuntime().addShutdownHook(new Thread("shutdown-thread") {
            @Override
            public void run() {
                // 回收
                System.out.println("==========over===============");
            }
        });
    }
}
