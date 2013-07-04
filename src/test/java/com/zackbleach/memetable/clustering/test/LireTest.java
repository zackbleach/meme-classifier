package com.zackbleach.memetable.clustering.test;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.context.ApplicationContext;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.zackbleach.memetable.clustering.Clusterer;
import com.zackbleach.memetable.clustering.HierarchyBuilder;
import com.zackbleach.memetable.imagerecognition.Meme;
import com.zackbleach.memetable.util.ImageUtils;

public class LireTest {

	public static final String TEST_FOLDER_PATH = "TestMemes/";
	public final static String IMAGE_PATH = "TestMemes/";
	
	@Test
	public void clusterTest() throws JsonParseException, JsonMappingException, IOException, URISyntaxException {
		Clusterer c = new Clusterer();
		c.setHierarchyBuilder(new HierarchyBuilder());
		
		c.cluster();
	}
	
	//TODO: This should be in a test class 
	public List<BufferedImage> getImages() throws JsonParseException, JsonMappingException, IOException {
		List<BufferedImage> results = new ArrayList<BufferedImage>();
		results.add(ImageUtils.readImagesFromFolder(IMAGE_PATH+Meme.GOOD_GUY_GREG.getDirectory()).get(0));
		results.add(ImageUtils.readImagesFromFolder(IMAGE_PATH+Meme.GOOD_GUY_GREG.getDirectory()).get(1));
		results.add(ImageUtils.readImagesFromFolder(IMAGE_PATH+Meme.BUSINESS_CAT.getDirectory()).get(0));
		results.add(ImageUtils.readImagesFromFolder(IMAGE_PATH+Meme.BUSINESS_CAT.getDirectory()).get(1));
		return results;
	}
}
