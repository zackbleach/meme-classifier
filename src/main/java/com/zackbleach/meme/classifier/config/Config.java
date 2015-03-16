package com.zackbleach.meme.classifier.config;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.semanticmetadata.lire.imageanalysis.ColorLayout;
import net.semanticmetadata.lire.imageanalysis.LireFeature;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.scheduling.annotation.EnableScheduling;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;

import com.zackbleach.meme.scraper.MemegurScraper;
import com.zackbleach.meme.scraper.Scraper;

@Configuration
@EnableScheduling
public class Config {

    @Bean
    Class<? extends LireFeature> featureExtractionMethod() {
        return ColorLayout.class;
    }

    @Bean
    JedisConnectionFactory jedisConnectionFactory() {
        return new JedisConnectionFactory();
    }

    @Bean
    RedisTemplate<String, Map<String, String>> redis() {
        final RedisTemplate<String, Map<String, String>> redisTemplate = new RedisTemplate<String, Map<String, String>>();
        redisTemplate.setConnectionFactory(jedisConnectionFactory());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        return redisTemplate;
    }

    @Bean
    public JedisPool jedisPool() {
        try {
            URI redisURI = new URI(System.getenv("REDISCLOUD_URL"));
            return new JedisPool(new JedisPoolConfig(),
                    redisURI.getHost(),
                    redisURI.getPort(),
                    Protocol.DEFAULT_TIMEOUT,
                    redisURI.getUserInfo().split(":",2)[1]);
        } catch (URISyntaxException e) {
            throw new RuntimeException("Redis couldn't be configured from URL in REDISTOGO_URL env var: "+
                    System.getenv("REDISTOGO_URL"));
        }
    }

    @Bean(name="scrapers")
    List<Scraper> scrapers() {
        List<Scraper> scrapers = new ArrayList<Scraper>();
        // scrapers.add(new ImgflipScraper());
        // scrapers.add(new QuickMemeScraper());
        scrapers.add(new MemegurScraper());
        return scrapers;
    }
}
