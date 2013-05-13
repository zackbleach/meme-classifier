package com.zackbleach.memetable.controller;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.zackbleach.memetable.domainobject.TopMeme;
import com.zackbleach.memetable.domainobject.TopMemes;
import com.zackbleach.memetable.imagerecognition.Indexer;
import com.zackbleach.memetable.imagerecognition.Result;
import com.zackbleach.memetable.scraper.RedditScraper;
import com.zackbleach.memetable.util.ClassificationUtils;

@Controller
@RequestMapping("api/memes")
public class TopMemeController {

    @RequestMapping(value = "/top", method = RequestMethod.GET)
    public ResponseEntity<TopMemes> getEffective(UriComponentsBuilder builder) 
    		throws JsonParseException, JsonMappingException, IOException, URISyntaxException {
    	//Could use response body here as well
    	Indexer.index();
    	RedditScraper scraper = new RedditScraper();
    	List<String> paths = scraper.scrape();
    	List<Result> myMemes = new ArrayList<Result>();
    	for (String path : paths) {
    		myMemes.add(ClassificationUtils.classifyMeme(path));
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

    @RequestMapping(value = "/effective", method = RequestMethod.POST)
    public void someUpdate(Example example) {
        //No-op
    }
}
