package com.zackbleach.memetable.clustering.test;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.zackbleach.memetable.clustering.CEDD;
import com.zackbleach.memetable.clustering.Clusterer;
import com.zackbleach.memetable.imagerecognition.Meme;
import com.zackbleach.memetable.util.ImageUtils;

public class LireTest {

	public static final String TEST_FOLDER_PATH = "TestMemes/";
	
	@Test
	public void clusterTest() throws JsonParseException, JsonMappingException, IOException, URISyntaxException {
		Clusterer c = new Clusterer();
		c.cluster();
	}
	
	public void lireTest() throws IOException {
		
		List<BufferedImage> adviceMallards = 
				ImageUtils.readImagesFromFolder(TEST_FOLDER_PATH+Meme.ADVICE_MALLARD.getDirectory());
		
		List<BufferedImage> insanityWolves = 
				ImageUtils.readImagesFromFolder(TEST_FOLDER_PATH+Meme.INSANITY_WOLF.getDirectory());
		
		CEDD duck = new CEDD();
		CEDD duck2 = new CEDD();
		
		duck.extract(adviceMallards.get(0));
		duck2.extract(adviceMallards.get(1));
		
		CEDD wolf = new CEDD();
		wolf.extract(insanityWolves.get(0));
		
		System.out.println("Distance between duck1 and duck2: " + duck.getDistance(duck2));
		System.out.println("Distance between duck1 and wolf: " + duck.getDistance(wolf));
		System.out.println("Distance between duck2 and wolf" + duck2.getDistance(wolf));
	
		double T0 = (duck.T0 + duck2.T0) / 2;
		double T1 = (duck.T1 + duck2.T1) / 2;
		double T2 = (duck.T2 + duck2.T2) / 2;
		double T3 = (duck.T3 + duck2.T3) / 2;
		boolean Compact = duck.Compact;
		
		com.zackbleach.memetable.clustering.CEDD avDuck = new com.zackbleach.memetable.clustering.CEDD();
		
		double[] duckSmash = new double[144];
		
		for (int i = 0; i < duck.getDoubleHistogram().length; i++) {
			duckSmash[i] = (duck.getDoubleHistogram()[i] + duck2.getDoubleHistogram()[i])/2;
		}
		
		avDuck.setDoubleHistogram(duckSmash);
	
		System.out.println("Av duck distance:" + avDuck.getDistance(wolf));
		System.out.println("Av duck distance from duck1: " + avDuck.getDistance(duck));
		System.out.println("Av duck distance from duck2: " + avDuck.getDistance(duck2));
	
	}
}
