package com.zackbleach.memetable.cache;

import java.io.IOException;
import java.net.URISyntaxException;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.zackbleach.memetable.imagerecognition.Result;
import com.zackbleach.memetable.util.ClassificationUtils;

public class MemeCache {
    
    private LoadingCache<String, Result> cache;
    private static MemeCache instance = new MemeCache();
    
    private MemeCache() {
        cache = CacheBuilder.newBuilder()
                   .maximumSize(50)
                   .build(
                       new CacheLoader<String, Result>() {
                         public Result load(String key) throws IOException, URISyntaxException {
                           return ClassificationUtils.classifyMemeFromPath(key);
                         }
                       });
    }

    public LoadingCache<String, Result> getCache() {
        return cache;
    }
    
    public static MemeCache getInstance() {
        return instance;
    }
    
}
