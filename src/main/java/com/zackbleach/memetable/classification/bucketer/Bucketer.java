package com.zackbleach.memetable.classification.bucketer;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

import net.semanticmetadata.lire.imageanalysis.CEDD;
import net.semanticmetadata.lire.imageanalysis.FuzzyColorHistogram;
import net.semanticmetadata.lire.imageanalysis.LireFeature;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.zackbleach.memetable.templatescraper.QuickMemeTemplateScraper;
import com.zackbleach.memetable.templatescraper.Template;

@Component
public class Bucketer {
    private static final Log logger = LogFactory.getLog(Bucketer.class);

    //TODO: persist template - use file system and reddis?

    @Autowired
    QuickMemeTemplateScraper quickMemeScraper;

    private Set<Bucket> buckets = new HashSet<Bucket>();

    private static final int ONE_HOUR = 3600000;

    public Bucketer() {
    }

    @PostConstruct
    @Scheduled(initialDelay = ONE_HOUR * 12, fixedDelay = ONE_HOUR * 12)
    private void createBuckets() {
        List<Template> templates = quickMemeScraper.scrape();
        logger.debug("Creating Buckets");
        for (Template template : templates) {
            Bucket bucket = new Bucket(template, new CEDD());
            buckets.add(bucket);
        }
    }

    public Set<Bucket> getBuckets() {
        return buckets;
    }

    public void addBucket(Bucket bucket) {
        buckets.add(bucket);
    }
}
