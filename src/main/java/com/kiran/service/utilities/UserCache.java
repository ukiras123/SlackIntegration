package com.kiran.service.utilities;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @author Kiran
 * @since 1/31/18
 */

@Component
public class UserCache {


    static LoadingCache<String, String> userCacheForChannelProp = CacheBuilder.newBuilder()
            .maximumSize(100)
            .expireAfterWrite(24, TimeUnit.HOURS)
            .build( new CacheLoader<String, String>() {
                        @Override
                        public String load(String key) throws Exception {
                            return "empty";
                        }
                    }
            );

    public void addToChannelPropCache(String key, String value) {
        userCacheForChannelProp.put(key, value);
    }

    public String getFromChannelPropCache(String key) throws ExecutionException {
        return userCacheForChannelProp.get(key);
    }
}

