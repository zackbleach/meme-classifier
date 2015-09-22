package com.zackbleach.meme.classifier.scraper;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import net.semanticmetadata.lire.DocumentBuilder;
import net.semanticmetadata.lire.impl.GenericDocumentBuilder;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.StoredField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zackbleach.meme.classifier.cache.Meme;
import com.zackbleach.meme.classifier.cache.MemeCache;
import com.zackbleach.meme.classifier.featureextraction.FeatureExtractor;
import com.zackbleach.meme.classifier.index.Index;
import com.zackbleach.meme.scraper.ScrapedImage;

@Component
public class Scraper {

    private static final Log logger =
        LogFactory.getLog(Scraper.class);

    @Autowired
    private MemeCache cache;

    @Autowired
    private FeatureExtractor featureExtractor;

    @Resource(name="scrapers")
    private List<com.zackbleach.meme.scraper.Scraper> scrapers;

    private Set<String> seenMemes = new HashSet<String>();

    public Set<ScrapedImage> getNewMemes() {
        Set<ScrapedImage> newMemes = new HashSet<ScrapedImage>();
        for (com.zackbleach.meme.scraper.Scraper scraper : scrapers) {
            List<ScrapedImage> scrapedImages = scraper.scrape();
            logger.info("Finished scraping images for: " + scraper.getClass().toString());
            newMemes.addAll(scrapedImages);
            logger.info("Finished adding memes to new memes");
        }
        logger.info("Updating seen meme cache");
        for (ScrapedImage image : newMemes) {
            if (seenMemes.contains(image.getSourceUrl())) {
                newMemes.remove(image);
            } else {
                seenMemes.add(image.getSourceUrl());
            }
        }
        logger.info("Finished updating cache");
        return newMemes;
    }

    public List<com.zackbleach.meme.scraper.Scraper> getScrapers() {
        return scrapers;
    }

    public void setScrapers(List<com.zackbleach.meme.scraper.Scraper> scrapers) {
        this.scrapers = scrapers;
    }

}
