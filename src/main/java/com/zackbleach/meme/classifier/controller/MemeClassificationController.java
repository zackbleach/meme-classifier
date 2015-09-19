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
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.zackbleach.meme.classifier.index.BuildingIndexException;
import com.zackbleach.meme.classifier.index.Index;
import com.zackbleach.meme.classifier.utils.ImageUtils;

@Controller
@Api(value = "Meme Classifier API", description = "Endpoints for classifying memes.")
@RequestMapping("/meme")
public class MemeClassificationController {
    private static final Log log = LogFactory
            .getLog(MemeClassificationController.class);

    @Autowired
    private Index index;

    @Autowired
    private ImageUtils imageUtils;

    @ApiOperation(value = "Classifies a Meme", notes = "Takes a URL which points to an image, then attempts to classify that image as a meme. Returns the name of the meme, as well as the distance from the archetype. You could use the distance as a rough measure of confidence")
    @RequestMapping(value = "/classify", method = RequestMethod.GET)
    public ResponseEntity<Result> classifyMeme(String url) throws IOException,
            JsonParseException, JsonMappingException, URISyntaxException,
            ExecutionException, BuildingIndexException {
        BufferedImage image = imageUtils.getImageFromUrl(url);
        log.info("Classifying: " + url);
        ImageSearchHits hits = index.search(image);
        return new ResponseEntity<Result>(presentHits(hits).iterator().next(),
                HttpStatus.OK);
    }

    @ExceptionHandler(BuildingIndexException.class)
    public ResponseEntity<String> buildingIndexException(
            BuildingIndexException e) {
        return new ResponseEntity<String>(e.getMessage(),
                HttpStatus.SERVICE_UNAVAILABLE);
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
