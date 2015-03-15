package com.zackbleach.meme.classifier.cache;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.io.BaseEncoding;
import com.zackbleach.meme.classifier.utils.ImageUtils;
import com.zackbleach.meme.scraper.ScrapedImage;

@Repository
public class MemeCache {

    @Autowired
    private RedisTemplate<String, Map<String, String>> redis;

    @Autowired
    private ImageUtils imageUtils;

    private final String MEME_STORAGE_DIR = "memes/";
    private final String PATH = "path";
    private final String NAME = "name";

    public Meme getMeme(ScrapedImage image) throws IOException,
            URISyntaxException {
        String url = image.getSourceUrl();
        if (!redis.hasKey(url)) {
            return saveToCache(url, image.getName());
        } else {
            return getFromCache(url);
        }
    }

    private Meme getFromCache(String url) throws IOException {
           String path = (String) redis.opsForHash().get(url, PATH);
           String name = (String) redis.opsForHash().get(url, NAME);
           BufferedImage image = imageUtils.readFromDisk(path);
           return new Meme(name, url, image);
    }

    private Meme saveToCache(String url, String name) throws IOException,
            URISyntaxException {
        BufferedImage image = imageUtils.getImageFromUrl(url);
        String filename = name.replaceAll(" ", "-") + "_" + FilenameUtils.getBaseName(url)
            + "." + FilenameUtils.getExtension(url);
        String path = MEME_STORAGE_DIR + filename;
        imageUtils.saveToDisk(path, image);
        redis.opsForHash().put(url, PATH, path);
        redis.opsForHash().put(url, NAME, name);
        return new Meme(name, url, image);
    }

}
