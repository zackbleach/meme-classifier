package com.zackbleach.memetable.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;

import javax.imageio.ImageIO;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.zackbleach.memetable.contentextraction.MemeExtractor;

public class ImageScrapeUtils {
	
	private static final Logger log = Logger.getLogger(ImageScrapeUtils.class);

	public static BufferedImage getImageFromSite(String path) throws IOException, URISyntaxException {
		MemeExtractor scraper = new MemeExtractor();
		BufferedImage meme = scraper.extractMeme(path);
		return meme;
	}
	
	public static void saveImage(String path, String folder) throws IOException, URISyntaxException {
		BufferedImage image = getImageFromSite(path);
		String[] parts = path.split("/");
		ImageIO.write(image, "jpg" ,
				new File(folder + parts[parts.length-1]));
	}
	
	
}
