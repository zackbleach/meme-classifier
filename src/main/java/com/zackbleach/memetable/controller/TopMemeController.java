package com.zackbleach.memetable.controller;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ExecutionException;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.google.common.base.Functions;
import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.Ordering;
import com.zackbleach.memetable.cache.MemeCache;
import com.zackbleach.memetable.domainobject.TopMeme;
import com.zackbleach.memetable.domainobject.TopMemes;
import com.zackbleach.memetable.imagerecognition.Indexer;
import com.zackbleach.memetable.imagerecognition.Result;
import com.zackbleach.memetable.scraper.Post;
import com.zackbleach.memetable.scraper.RedditScraper;
import com.zackbleach.memetable.util.ClassificationUtils;
import com.zackbleach.memetable.util.ImageScrapeUtils;

@Controller
@RequestMapping("api/memes")
public class TopMemeController {
	
	private static final Logger log = Logger.getLogger(TopMemeController.class);

	/**
	 * TODO: This should really be a unit test / should be logged ? 
	 * @param downloadUnknown
	 * @return
	 * @throws IOException
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 */
    @RequestMapping(value = "/frontpage", method = RequestMethod.GET)
    public ResponseEntity<List <TopMeme>> getMemesTest(boolean downloadUnknown) 
    		throws IOException, JsonParseException, JsonMappingException {
    	//Could use response body here as well
    	Indexer.index();
    	RedditScraper scraper = new RedditScraper();
    	List<Post> posts = scraper.scrape();
    	List<TopMeme> results = getTopMemesList(posts, downloadUnknown);
        return new ResponseEntity<List <TopMeme>>(results, HttpStatus.OK);
    }
    
    
    @RequestMapping(value = "/top", method = RequestMethod.GET)
    public ResponseEntity<TopMemes> getMemesTest2(boolean downloadUnknown) 
    		throws IOException, JsonParseException, JsonMappingException {
    	//Could use response body here as well
    	Indexer.index();
    	RedditScraper scraper = new RedditScraper();
    	List<Post> posts = scraper.scrape();
    	List<TopMeme> results = getTopMemesMap(posts, downloadUnknown);
    	TopMemes top = new TopMemes();
    	top.setTopMemes(results);
        return new ResponseEntity<TopMemes>(top, HttpStatus.OK);
    }
    

	private List<TopMeme> getTopMemesMap(List<Post> posts,
			boolean downloadUnknown) {

		Map<TopMeme, Integer> results = new HashMap<TopMeme, Integer>();

		for (Post post : posts) {
			boolean downloaded = false;
			Result r = attemptToRetrieveFromCache(post.getImageUrl());
			if (null != r) {
				if (r.getCertainty() < 0.5 && downloadUnknown) {
					downloaded = ImageScrapeUtils.saveImage(
							r.getExtractedImage(), post.getImageUrl(),
							"downloadedMemes/");
				}
				TopMeme meme = new TopMeme(r.getMeme().identifier(),
						r.getCertainty(), post.getScore(), downloaded);
				if (results.containsKey(meme)) {
					results.put(meme, results.get(meme) + meme.getScore());
				} else {
					results.put(meme, meme.getScore());
				}
			}
		}
		List<TopMeme> meme = new ArrayList<TopMeme>();
		for (TopMeme m : results.keySet()) {
			m.setScore(results.get(m));
			meme.add(m);
		}
		
		Collections.sort(meme);

		return meme;
	}
		

	private List<TopMeme> getTopMemesList(List<Post> posts, boolean downloadUnknown) {
		List<TopMeme> results = new ArrayList<TopMeme>();
		for (Post post : posts) {
			boolean downloaded = false;
			Result r = attemptToRetrieveFromCache(post.getImageUrl());
			if (null != r) {
				if (r.getCertainty() < 0.5 && downloadUnknown) {
					downloaded = ImageScrapeUtils.saveImage(
							r.getExtractedImage(), post.getImageUrl(),
							"downloadedMemes/");
				}
				results.add(new TopMeme(r.getMeme().identifier(), r
						.getCertainty(), post.getScore(), downloaded));
			}
		}
		return results;
	}

	private Result attemptToRetrieveFromCache(String path) {
		Result r = null;
		try {
			r = MemeCache.getInstance().getCache().get(path);
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return r;
	}
}
