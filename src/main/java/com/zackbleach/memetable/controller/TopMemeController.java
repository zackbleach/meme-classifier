package com.zackbleach.memetable.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.zackbleach.memetable.cache.MemeCache;
import com.zackbleach.memetable.domainobject.TopMeme;
import com.zackbleach.memetable.domainobject.TopMemes;
import com.zackbleach.memetable.imagerecognition.Indexer;
import com.zackbleach.memetable.imagerecognition.Result;
import com.zackbleach.memetable.scraper.Post;
import com.zackbleach.memetable.scraper.RedditScraper;
import com.zackbleach.memetable.scraper.Scraper;
import com.zackbleach.memetable.util.ImageUtils;

@Controller
@RequestMapping("api/memes")
public class TopMemeController {

    @Autowired
    MemeCache memeCache;

    private static final Logger log = Logger.getLogger(TopMemeController.class);

    @RequestMapping(value = "/frontpage", method = RequestMethod.GET)
    public ResponseEntity<TopMemes> getMemesTest(boolean downloadUnknown)
            throws IOException, JsonParseException, JsonMappingException {
        // Could use response body here as well
        return getTopMemes(downloadUnknown, false);
    }

    @RequestMapping(value = "/top", method = RequestMethod.GET)
    public ResponseEntity<TopMemes> getMemesTest2(boolean downloadUnknown)
            throws IOException, JsonParseException, JsonMappingException {
        // Could use response body here as well
        return getTopMemes(downloadUnknown, true);
    }

    private ResponseEntity<TopMemes> getTopMemes(boolean downloadUnknown,
            boolean collateMemes) throws IOException, JsonParseException,
            JsonMappingException {
        Indexer.index();
        Scraper scraper = new RedditScraper();
        List<Post> posts = scraper.scrape();
        List<TopMeme> results = getTopMemesList(posts, downloadUnknown);
        TopMemes top = new TopMemes();
        if (collateMemes) {
            results = collateList(results);
        }
        top.setTopMemes(results);
        return new ResponseEntity<TopMemes>(top, HttpStatus.OK);
    }

    private List<TopMeme> getTopMemesList(List<Post> posts,
            boolean downloadUnknown) {
        List<TopMeme> results = new ArrayList<TopMeme>();
        int index = 0;
        for (Post post : posts) {
            boolean downloaded = false;
            Result r = attemptToRetrieveFromCache(post.getImageUrl());
            if (null != r) {
                if (r.getCertainty() < 0.5 && downloadUnknown) {
                    downloaded = ImageUtils.saveImage(r.getExtractedImage(),
                            post.getImageUrl(), "downloadedMemes/");
                }
                results.add(new TopMeme(r.getMeme().getIdentifier(), r
                        .getCertainty(), post.getScore(), downloaded, index));
            }
            index++;
        }
        return results;
    }

    private List<TopMeme> collateList(List<TopMeme> memes) {
        Map<TopMeme, Integer> results = new HashMap<TopMeme, Integer>();
        for (TopMeme meme : memes) {
            if (results.containsKey(meme)) {
                results.put(meme, results.get(meme) + meme.getScore());
            } else {
                results.put(meme, meme.getScore());
            }
        }
        List<TopMeme> meme = new ArrayList<TopMeme>();
        for (TopMeme m : results.keySet()) {
            m.setScore(results.get(m));
            meme.add(m);
        }
        Collections.sort(meme);
        int index = 0;
        for (TopMeme m : meme) {
            m.setRank(index);
            index++;
        }
        return meme;
    }

    /**
     * Will try to get from cache - if unavailable will classify
     */
    private Result attemptToRetrieveFromCache(String path) {
        Result r = null;
        try {
            r = memeCache.getCache().get(path);
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return r;
    }
}
