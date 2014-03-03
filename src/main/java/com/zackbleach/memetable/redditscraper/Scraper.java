package com.zackbleach.memetable.redditscraper;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

public interface Scraper {

    /**
     * Grabs content we're interested in from defined URL
     */
    public abstract List<Post> scrape() throws JsonParseException,
            JsonMappingException, IOException;

}
