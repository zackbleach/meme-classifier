package com.zackbleach.memetable.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import net.semanticmetadata.lire.DocumentBuilder;
import net.semanticmetadata.lire.ImageSearchHits;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.document.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.zackbleach.memetable.bucketer.Index;
import com.zackbleach.memetable.utils.ImageUtils;

@Controller
@RequestMapping("/meme")
public class MemeClassificationController {
    private static final Log log = LogFactory
            .getLog(MemeClassificationController.class);

    @Autowired
    Index index;

    @Autowired
    ImageUtils imageUtils;

    private LoadingCache<String, BufferedImage> graphs = CacheBuilder.newBuilder()
            .maximumSize(1000).expireAfterWrite(60, TimeUnit.MINUTES)
            .build(new CacheLoader<String, BufferedImage>() {
                public BufferedImage load(String path) throws IOException,
                        URISyntaxException {
                 return imageUtils.getImageFromUrl(path);
                }
            });

    @RequestMapping(value = "/classify/ranked", method = RequestMethod.GET)
    public ResponseEntity<List<Result>> classifyMemeRanked(String path)
            throws IOException, JsonParseException, JsonMappingException,
            URISyntaxException, ExecutionException {
        BufferedImage image = graphs.get(path);
        ImageSearchHits hits = index.search(image);
        return new ResponseEntity<List<Result>>(presentHits(hits),
                HttpStatus.OK);
    }

    @RequestMapping(value = "/classify", method = RequestMethod.GET)
    public ResponseEntity<Result> classifyMeme(String path) throws IOException,
            JsonParseException, JsonMappingException, URISyntaxException,
            ExecutionException {
        BufferedImage image = graphs.get(path);
        ImageSearchHits hits = index.search(image);
        return new ResponseEntity<Result>(presentHits(hits).iterator().next(),
                HttpStatus.OK);
    }

    @RequestMapping(value = "/addMeme", method = RequestMethod.GET)
    public ResponseEntity<String> addMeme(String path, String name)
            throws IOException, JsonParseException, JsonMappingException,
            URISyntaxException, ExecutionException {
        index.add(imageUtils.getImageFromUrl(path), name);
        return new ResponseEntity<String>("Added Meme: " + name, HttpStatus.OK);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> illegalArgumentException(
            IllegalArgumentException e) {
        return new ResponseEntity<String>(e.getMessage(),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> exception(Exception e) {
        log.error("Uncaught exception:", e);
        return new ResponseEntity<String>("Internal Server Error",
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private List<Result> presentHits(ImageSearchHits hits) {
        List<Result> results = new ArrayList<Result>();
        for (int i = 0; i < hits.length(); i++) {
            Document doc = hits.doc(i);
            log.warn(doc.getFields());
            Result result = new Result(hits.score(i),
                    doc.get(DocumentBuilder.FIELD_NAME_IDENTIFIER));
            results.add(result);
        }
        return results;
    }
}
