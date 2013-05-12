package com.zackbleach.memetable.util;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URISyntaxException;

import com.zackbleach.memetable.contentextraction.MemeExtractor;

public class ImageScrapeUtils {

	public static BufferedImage getImageFromSite(String path) throws IOException, URISyntaxException {
		MemeExtractor scraper = new MemeExtractor();
		BufferedImage meme = scraper.extractMeme(path);
		return meme;
	}
	
	
}
