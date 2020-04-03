package com.github.liuyueyi.fix.example.spring.server;


import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.math.BigDecimal;

/**
 * Created by @author yihui in 18:36 18/12/29.
 */
public class StaticBean {

    private static LoadingCache<String, BigDecimal> localCache;

    static {
        localCache = CacheBuilder.newBuilder().build(new CacheLoader<String, BigDecimal>() {
            @Override
            public BigDecimal load(String key) throws Exception {
                return BigDecimal.ZERO;
            }
        });

        localCache.put("init", new BigDecimal(520));
    }


    public static BigDecimal getCache(String key) {
        return localCache.getUnchecked(key);
    }

    public static void updateCache(String key, BigDecimal value) {
        localCache.put(key, value);
    }
}
