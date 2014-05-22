package com.zackbleach.memetable.controller;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.TreeMap;

import net.semanticmetadata.lire.imageanalysis.CEDD;

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
import com.zackbleach.memetable.templatescraper.Template;
import com.zackbleach.memetable.util.ImageUtils;

@Controller
@RequestMapping("/meme")
public class TopMemeController {

    private final Logger log = Logger.getLogger(TopMemeController.class);

    @Autowired
    Classifier classifier;

    @Autowired
    Bucketer bucketer;

    @Autowired
    ImageUtils imageUtils;

    @RequestMapping(value = "/classify", method = RequestMethod.GET)
    public ResponseEntity<Classification> classifyMeme(String path)
            throws IOException, JsonParseException, JsonMappingException {
        // Could use response body here as well
        Classification classification = classifier.classify(path);
        return new ResponseEntity<Classification>(classification, HttpStatus.OK);
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ResponseEntity<Map<String, String>> getMemeNames()
            throws IOException, JsonParseException, JsonMappingException {
        Map<String, String> memeNames = new TreeMap<String, String>();
        for (Bucket bucket : bucketer.getBuckets()) {
            String prettyName = (WordUtils.capitalize(bucket.getName()));
            String sourceUrl = bucket.getSourceUrl();
            memeNames.put(prettyName, sourceUrl);
        }
        return new ResponseEntity<Map<String, String>>(memeNames, HttpStatus.OK);
    }

    @RequestMapping(value = "/addMeme", method = RequestMethod.GET)
    public ResponseEntity<String> addBucket(String name, String url) throws IOException,
            URISyntaxException {
        Template template = new Template();
        template.setName(name);
        template.setSourceUrl(url);
        template.setImage(imageUtils.getImageFromUrl(url));
        Bucket bucket = new Bucket(template, new CEDD());
        bucketer.addBucket(bucket);
        return new ResponseEntity<String>(url, HttpStatus.OK);
    }

}
