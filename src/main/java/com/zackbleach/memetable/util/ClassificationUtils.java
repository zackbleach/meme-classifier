package com.zackbleach.memetable.util;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URISyntaxException;

import com.zackbleach.memetable.imagerecognition.Result;
import com.zackbleach.memetable.imagerecognition.Searcher;
import static com.zackbleach.memetable.util.ImageScrapeUtils.getImageFromSite;
public class ClassificationUtils {
	
	public static Result classifyMeme(String path) throws IOException, URISyntaxException {
		Searcher searcher = new Searcher();
		BufferedImage meme = getImageFromSite(path);
		return searcher.getTopResult(meme);
	}
	
	
}
