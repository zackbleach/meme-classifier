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
import com.zackbleach.memetable.contentextraction.MemeExtractor;
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
    public ResponseEntity<TopMemes> getEffective(UriComponentsBuilder builder) 
    		throws JsonParseException, JsonMappingException, IOException, URISyntaxException {
    	//Could use response body here as well
    	Indexer.index();
    	RedditScraper scraper = new RedditScraper();
    	List<String> paths = scraper.scrape();
    	List<Result> myMemes = new ArrayList<Result>();
    	for (String path : paths) {
    	    Result r = null;
    		try {
				r = MemeCache.getInstance().getCache().get(path);
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
    		if (r == null) {
    			r = ClassificationUtils.classifyMeme(path);
    		}
    		myMemes.add(r);
    	}
    	TopMemes top = new TopMemes();
    	List<TopMeme> memes = new ArrayList<TopMeme>();
    	int index = 1;
		for (Result meme : myMemes) {
			memes.add(new TopMeme(index, meme.getMeme(), meme.getCertainty(), 9001));
			index++;
		}
    	top.setTopMemes(memes);
        return new ResponseEntity<TopMemes>(top, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/download", method = RequestMethod.GET)
    public ResponseEntity<String> getMemes() throws IOException, URISyntaxException {
    	Indexer.index();
    	RedditScraper scraper = new RedditScraper();
    	List<String> paths = scraper.scrape();
    	int downloads = 0;
    	for (String path : paths) {
    		try {
    			ImageScrapeUtils.saveImage(path, "downloadedMemes/");
    			downloads++;
    		} catch (Exception e) {
    			log.error("Error saving image: " + path, e);
    		}
    	}
        return new ResponseEntity<String>("Downloaded " + downloads + " images", 
        		HttpStatus.OK);
    }

//    @RequestMapping(value = "/hairy", method = RequestMethod.POST)
//    public ResponseEntity<String> someUpdate(String hairy) {
//        return new ResponseEntity<String>("You typed: "+hairy, HttpStatus.OK);
//    }
//    
//    @RequestMapping(value = "/hairy", method = RequestMethod.GET)
//    public ResponseEntity<String> getHairy(UriComponentsBuilder builder) {
//        return new ResponseEntity<String>("Hairy is gay", HttpStatus.NOT_ACCEPTABLE);
//    }

}
