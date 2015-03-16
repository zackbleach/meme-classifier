package com.zackbleach.meme.classifier.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

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
import com.zackbleach.meme.classifier.index.Index;
import com.zackbleach.meme.classifier.utils.ImageUtils;
import com.zackbleach.meme.scraper.ScrapedImage;

@Controller
@RequestMapping("/meme")
public class MemeClassificationController {
    private static final Log log = LogFactory
            .getLog(MemeClassificationController.class);

    @Autowired
    private Index index;

    @Autowired
    private ImageUtils imageUtils;

    @RequestMapping(value = "/classify", method = RequestMethod.GET)
    public ResponseEntity<Result> classifyMeme(String path) throws IOException,
            JsonParseException, JsonMappingException, URISyntaxException,
            ExecutionException {
        BufferedImage image = imageUtils.getImageFromUrl(path);
        log.info("Classifying: " + path);
        ImageSearchHits hits = index.search(image);
        return new ResponseEntity<Result>(presentHits(hits).iterator().next(),
                HttpStatus.OK);
    }

    @RequestMapping(value = "/getAllDocs", method = RequestMethod.GET)
    public ResponseEntity<List<ScrapedImage>> getAllDocs() throws IOException,
            JsonParseException, JsonMappingException, URISyntaxException,
            ExecutionException {
        return new ResponseEntity<List<ScrapedImage>>(index.getAllDocs(), HttpStatus.OK);
    }

    @RequestMapping(value = "/getDistance", method = RequestMethod.GET)
    public ResponseEntity<Double>getDistance(String urlOne, String urlTwo) throws IOException,
            JsonParseException, JsonMappingException, URISyntaxException,
            ExecutionException {
                BufferedImage imageOne = imageUtils.getImageFromUrl(urlOne);
                BufferedImage imageTwo = imageUtils.getImageFromUrl(urlTwo);
                double distance = imageUtils.getDistance(imageOne, imageTwo);
        return new ResponseEntity<Double>(distance, HttpStatus.OK);
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
            Result result = new Result(hits.score(i), doc.get(Index.MEME_TYPE));
            results.add(result);
            log.info("Result URL: "
                    + doc.get(DocumentBuilder.FIELD_NAME_IDENTIFIER));
            log.info("Distance: " + hits.score(i));
        }
        return results;
    }
}
