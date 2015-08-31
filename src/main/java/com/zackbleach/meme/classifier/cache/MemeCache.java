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
    private ImageUtils imageUtils;

    //TODO: Tidy
    public Meme getMeme(ScrapedImage image) throws URISyntaxException,
            IOException {
            BufferedImage meme = imageUtils.getImageFromUrl(image.getSourceUrl());
            return new Meme(image.getName(), image.getSourceUrl(), meme);
    }
}
