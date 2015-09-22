package com.zackbleach.meme.classifier.config;

import java.util.ArrayList;
import java.util.List;

import net.semanticmetadata.lire.imageanalysis.ColorLayout;
import net.semanticmetadata.lire.imageanalysis.LireFeature;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.zackbleach.meme.scraper.ImgflipScraper;
import com.zackbleach.meme.scraper.MemegurScraper;
import com.zackbleach.meme.scraper.QuickMemeScraper;
import com.zackbleach.meme.scraper.Scraper;

@Configuration
@EnableScheduling
public class Config {

    @Bean
    Class<? extends LireFeature> featureExtractionMethod() {
        return ColorLayout.class;
    }

    @Bean(name="scrapers")
    List<Scraper> scrapers() {
        List<Scraper> scrapers = new ArrayList<Scraper>();
        // scrapers.add(new ImgflipScraper());
        // scrapers.add(new QuickMemeScraper());
        // scrapers.add(new MemegurScraper());
        return scrapers;
    }
}
