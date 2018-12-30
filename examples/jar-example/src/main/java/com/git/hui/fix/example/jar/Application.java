package com.git.hui.fix.example.jar;

import com.git.hui.fix.core.FixEngine;
import com.git.hui.fix.example.jar.server.CalculateServer;

import java.math.BigDecimal;

/**
 * Created by @author yihui in 22:51 18/12/30.
 */
public class Application {

    public static void main(String[] args) {
        System.out.println(" --- ");
        new Thread(new Runnable() {
            @Override
            public void run() {
                FixEngine.instance();
                CalculateServer.updateCache("init", new BigDecimal(12.3f));
            }
        }).start();

        try {
            Thread.sleep(2 * 2600);
        } catch (Exception e) {
            Thread.currentThread().interrupt();
        }
    }
}
