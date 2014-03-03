package com.zackbleach.memetable.scraper.test;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.zackbleach.memetable.redditscraper.Post;
import com.zackbleach.memetable.redditscraper.RedditScraper;
import com.zackbleach.memetable.redditscraper.Scraper;

public class RedditScraperTest {

    @Test
    public void retrieveUrlsFromReddit() throws Exception {
        Scraper scraper = new RedditScraper();
        List<Post> urls = scraper.scrape();
        for (Post post : urls) {
            if (post.getImageUrl() == null) {
                Assert.fail();
            }
        }
    }
}
