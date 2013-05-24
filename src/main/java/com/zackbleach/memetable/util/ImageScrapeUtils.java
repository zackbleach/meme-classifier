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
	
	public static boolean saveImage(String path, String folder) {
		try {
			String[] parts = path.split("/");
			File file = new File(folder+parts[parts.length - 1]);
			if (!file.exists()) {
				BufferedImage image = getImageFromSite(path);
				ImageIO.write(image, "jpg", file);
				log.warn("Saved file to disk: " + file.getName());
				return true;
			} else {
				return true;
			}
		} catch (IOException ioe) {
			log.warn("Problem saving image: " + path, ioe);
		} catch (URISyntaxException urie) {
			log.warn("Problem saving image: " + path, urie);
		}
		return false;
	}
	
	public static boolean saveImage(BufferedImage image, String path,  String folder) {
		if (image == null) {
			return false;
		}
		try {
			String[] parts = path.split("/");
			File file = new File(folder+parts[parts.length - 1]);
			if (!file.exists()) {
				ImageIO.write(image, "jpg", file);
				log.warn("Saved file to disk: " + file.getName());
				return true;
			} else {
				return true;
			}
		} catch (IOException ioe) {
			log.warn("Problem saving image: " + path, ioe);
		}
		return false;
	}
	
	
}
