package com.zackbleach.memetable.controller;

import java.io.IOException;
import java.util.ArrayList;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.TreeSet;

import org.apache.commons.lang.WordUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.zackbleach.memetable.classification.bucketer.Bucket;
import com.zackbleach.memetable.classification.bucketer.Bucketer;
import com.zackbleach.memetable.classification.classifier.Classification;
import com.zackbleach.memetable.classification.classifier.Classifier;

@Controller
@RequestMapping("/meme")
public class TopMemeController {

    private final Logger log = Logger.getLogger(TopMemeController.class);

    @Autowired
    Classifier classifier;

    @Autowired
    Bucketer bucketer;

    @RequestMapping(value = "/classify", method = RequestMethod.GET)
    public ResponseEntity<Classification> classifyMeme(String path)
            throws IOException, JsonParseException, JsonMappingException {
        // Could use response body here as well
        Classification classification = classifier.classify(path);
        return new ResponseEntity<Classification>(classification, HttpStatus.OK);
    }

//    @RequestMapping(value = "/list", method = RequestMethod.GET)
//    public ResponseEntity<Collection<String>> getMemeNames()
//            throws IOException, JsonParseException, JsonMappingException {
//            Collection<String> memeNames = new TreeSet<String>();
//            for (Bucket bucket : bucketer.getBuckets()) {
//                String prettyName = (WordUtils.capitalize(bucket.getName()));
//                memeNames.add(prettyName);
//            }
//        return new ResponseEntity<Collection<String>>(memeNames, HttpStatus.OK);
//    }
//
}
