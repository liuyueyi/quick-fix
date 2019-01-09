package com.git.hui.fix.example.jar;

import com.git.hui.fix.core.FixEngine;
import com.git.hui.fix.example.jar.holder.ServerHolder;
import com.git.hui.fix.example.jar.server.CalculateServer;
import com.git.hui.fix.example.jar.server.HelloServer;

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
                HelloServer helloServer = new HelloServer("小灰灰blog");
                ServerHolder.addServer("helloServer", helloServer);
                CalculateServer.updateCache("init", new BigDecimal(12.3f));
            }
        }).start();

        try {
            Thread.sleep(2 * 3600 * 1000);
        } catch (Exception e) {
            Thread.currentThread().interrupt();
        }
    }
}
