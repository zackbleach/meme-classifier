package com.zackbleach.memetable.scraper;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

public interface Scraper {

	//TODO: comment
	public abstract List<Post> scrape() throws JsonParseException,
			JsonMappingException, IOException;

}