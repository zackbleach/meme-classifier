package com.zackbleach.memetable.scraper.test;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.zackbleach.memetable.scraper.Post;
import com.zackbleach.memetable.scraper.RedditScraper;

public class RedditScraperTest {
	
	@Test
	public void retrieveUrlsFromReddit() throws Exception {
		RedditScraper scraper = new RedditScraper();
		List<Post> urls = scraper.scrape();
		for (Post post : urls) {
			if (post.getImageUrl()==null){
				Assert.fail();
			}
		}
	}
}
