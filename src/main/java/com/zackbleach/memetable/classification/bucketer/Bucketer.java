package com.zackbleach.memetable.classification.bucketer;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.zackbleach.memetable.classification.featureextraction.CEDD;
import com.zackbleach.memetable.templatescraper.QuickMemeScraper;
import com.zackbleach.memetable.templatescraper.Template;

@Component
public class Bucketer {
    private static final Log logger = LogFactory.getLog(Bucketer.class);

    @Autowired
    QuickMemeScraper quickMemeScraper;

    // TODO: there will be another app in the background
    // that temporarily scrapes templates from popular
    // meme sites - those will be saved in to a database
    // and used to populate the initial buckets
    //
    // There may have to be a different type of storage for
    // clusters found that don't currently have a matching
    // template

    // @Autowire
    // TemplateStorage templateStorage;

    private Set<Bucket> buckets = new HashSet<Bucket>();

    private static final int ONE_HOUR = 3600000;
    private static final int FIVE_MINUTES = 300000;

    public Bucketer() {
    }

    @PostConstruct
    @Scheduled(initialDelay=FIVE_MINUTES, fixedDelay=FIVE_MINUTES)
    private void createBuckets() {
        List<Template> templates = quickMemeScraper.scrape();
        for (Template template : templates) {
            logger.debug("Creating Buckets");
            Bucket bucket = new Bucket(template, new CEDD());
            buckets.add(bucket);
        }
    }

    public Set<Bucket> getBuckets() {
        return buckets;
    }

}
