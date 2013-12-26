package com.zackbleach.memetable.cache;

import java.io.IOException;
import java.net.URISyntaxException;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.zackbleach.memetable.imagerecognition.Result;
import com.zackbleach.memetable.util.ClassificationUtils;

@Component
public class MemeCache {
    private static final Log log =
        LogFactory.getLog(MemeCache.class);

    private LoadingCache<String, Result> cache = CacheBuilder.newBuilder()
                   .maximumSize(50)
                   .build(
                       new CacheLoader<String, Result>() {
                         public Result load(String key) throws IOException, URISyntaxException {
                             //TODO: can I make this Spring managed?
                           return ClassificationUtils.classifyMemeFromPath(key);
                         }
                       });

    public LoadingCache<String, Result> getCache() {
        return cache;
    }
}
