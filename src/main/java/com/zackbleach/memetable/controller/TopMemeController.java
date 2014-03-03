package com.zackbleach.memetable.controller;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.zackbleach.memetable.domainobject.TopMemes;

@Controller
@RequestMapping("api/memes")
public class TopMemeController {

    private static final Logger log = Logger.getLogger(TopMemeController.class);

    @RequestMapping(value = "/frontpage", method = RequestMethod.GET)
    public ResponseEntity<TopMemes> getMemesTest(boolean downloadUnknown)
            throws IOException, JsonParseException, JsonMappingException {
        // Could use response body here as well
        return null;
    }

    @RequestMapping(value = "/top", method = RequestMethod.GET)
    public ResponseEntity<TopMemes> getMemesTest2(boolean downloadUnknown)
            throws IOException, JsonParseException, JsonMappingException {
        // Could use response body here as well
        return null;
    }
}
