package com.zackbleach.meme.classifier.cache;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.io.BaseEncoding;
import com.zackbleach.meme.classifier.utils.ImageUtils;
import com.zackbleach.meme.scraper.ScrapedImage;

@Repository
public class MemeCache {
    private static final Log log = LogFactory.getLog(MemeCache.class);

    @Autowired
    private JedisPool jedisPool;

    @Autowired
    private ImageUtils imageUtils;

    private final String MEME_STORAGE_DIR = "memes/";
    private final String PATH = "path";
    private final String NAME = "name";

    public Meme getMeme(ScrapedImage image) throws IOException,
            URISyntaxException {
        Jedis jedis = jedisPool.getResource();
        try {
            String url = image.getSourceUrl();
            if (!jedis.exists(url)) {
                return saveToCache(url, image.getName());
            } else {
                return getFromCache(url);
            }
        } finally {
            jedisPool.returnResource(jedis);
        }
    }

    private Meme getFromCache(String url) throws IOException {
        Jedis jedis = jedisPool.getResource();
        try {
            String path = (String) jedis.hget(url, PATH);
            log.info("Getting from path: " + path);
            String name = (String) jedis.hget(url, NAME);
            BufferedImage image = imageUtils.readFromDisk(path);
            return new Meme(name, url, image);
        } finally {
            jedisPool.returnResource(jedis);
        }
    }

    // TODO: there's a chance that names will overlap here. Fix.
    private Meme saveToCache(String url, String name) throws IOException,
            URISyntaxException {
        Jedis jedis = jedisPool.getResource();
        try {
            BufferedImage image = imageUtils.getImageFromUrl(url);
            String filename = name.replaceAll(" ", "-") + "_"
                    + FilenameUtils.getBaseName(url) + "."
                    + FilenameUtils.getExtension(url);
            String path = MEME_STORAGE_DIR + filename;
            imageUtils.saveToDisk(path, image);
            jedis.hset(url, PATH, path);
            jedis.hset(url, NAME, name);
            return new Meme(name, url, image);
        } finally {
            jedisPool.returnResource(jedis);
        }
    }
}
