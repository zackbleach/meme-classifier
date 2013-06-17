package com.zackbleach.memetable.clustering.test;

import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.zackbleach.memetable.clustering.Clusterer;

public class LireTest {

	public static final String TEST_FOLDER_PATH = "TestMemes/";
	
	@Test
	public void clusterTest() throws JsonParseException, JsonMappingException, IOException, URISyntaxException {
		Clusterer c = new Clusterer();
		c.cluster();
	}
}
