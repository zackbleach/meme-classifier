package com.zackbleach.memetable.scraper.test;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.zackbleach.memetable.scraper.RedditScraper;

public class RedditScraperTest {
	
	@Test
	public void retrieveUrlsFromReddit() throws Exception {
		RedditScraper scraper = new RedditScraper();
		List<String> urls = scraper.scrape();
		for (String s : urls) {
			if (s==null){
				Assert.fail();
			}
		}
	}
}
