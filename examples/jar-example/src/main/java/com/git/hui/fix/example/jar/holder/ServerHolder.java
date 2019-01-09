package com.git.hui.fix.example.jar.holder;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by @author yihui in 22:19 19/1/3.
 */
public class ServerHolder {

    public static Map<String, Object> serverCache;

    static {
        serverCache = new HashMap<>();
    }

    public static void addServer(String name, Object server) {
        serverCache.put(name, server);
    }

    public static Object getServer(String name) {
        return serverCache.get(name);
    }

}
