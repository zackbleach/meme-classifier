package com.zackbleach.memetable.util;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URISyntaxException;

import com.zackbleach.memetable.contentextraction.MemeExtractor;
import com.zackbleach.memetable.imagerecognition.Searcher;

public class ClassificationUtils {

	public static BufferedImage getImageFromSite(String path) throws IOException, URISyntaxException {
		MemeExtractor scraper = new MemeExtractor();
		BufferedImage meme = scraper.extractMeme(path);
		return meme;
	}
	
	public static String classifyMeme(String path) throws IOException, URISyntaxException {
		Searcher searcher = new Searcher();
		BufferedImage meme = getImageFromSite(path);
		searcher.search(meme);
		return searcher.getSimplifiedResult();
	}
	
	
}
