package com.zackbleach.memetable.controller;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
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
import com.zackbleach.memetable.cache.MemeCache;
import com.zackbleach.memetable.domainobject.TopMeme;
import com.zackbleach.memetable.domainobject.TopMemes;
import com.zackbleach.memetable.imagerecognition.Indexer;
import com.zackbleach.memetable.imagerecognition.Result;
import com.zackbleach.memetable.scraper.RedditScraper;
import com.zackbleach.memetable.util.ClassificationUtils;
import com.zackbleach.memetable.util.ImageScrapeUtils;

@Controller
@RequestMapping("api/memes")
public class TopMemeController {
	
	private static final Logger log = Logger.getLogger(TopMemeController.class);

    @RequestMapping(value = "/top", method = RequestMethod.GET)
    public ResponseEntity<TopMemes> getEffective(boolean downloadUnknown) 
    		throws IOException, JsonParseException, JsonMappingException {
    	//Could use response body here as well
    	Indexer.index();
    	RedditScraper scraper = new RedditScraper();
    	List<String> paths = scraper.scrape();
    	List<TopMeme> results = getTopMemes(paths, downloadUnknown);
    	TopMemes top = new TopMemes();
    	top.setTopMemes(results);
        return new ResponseEntity<TopMemes>(top, HttpStatus.OK);
    }

	private List<TopMeme> getTopMemes(List<String> paths, boolean downloadUnknown) {
    	List<TopMeme> results = new ArrayList<TopMeme>();
    	int index = 1;
		for (String path : paths) {
	    	boolean downloaded = false;
    	    Result r = attemptToRetrieveFromCache(path);
    			if (r.getCertainty() < 0.5 && downloadUnknown ) {
    					downloaded = ImageScrapeUtils.saveImage(path, "downloadedMemes/");
    			}
    		results.add(new TopMeme(index, r.getMeme(), r.getCertainty(), 9001, downloaded));
    		index++;
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
