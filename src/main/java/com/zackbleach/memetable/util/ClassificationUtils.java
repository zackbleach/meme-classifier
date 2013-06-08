package com.zackbleach.memetable.util;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.log4j.Logger;

import net.semanticmetadata.lire.imageanalysis.CEDD;

import com.google.common.collect.ImmutableList;
import com.zackbleach.memetable.cache.MemeCache;
import com.zackbleach.memetable.contentextraction.MemeExtractor;
import com.zackbleach.memetable.imagerecognition.Result;
import com.zackbleach.memetable.imagerecognition.Searcher;
import static com.zackbleach.memetable.util.ImageUtils.getImageFromSite;
public class ClassificationUtils {
	
	private static final Logger log = Logger.getLogger(ClassificationUtils.class);

	//TODO: Test sensible level for this. 
	public static final int THRESHOLD = 10;
	
	public static Result classifyMemeFromPath(String path) throws IOException, URISyntaxException {
		BufferedImage meme = getImageFromSite(path);
		return classifyMemeFromImage(meme);
	}
	
	public static Result classifyMemeFromImage(BufferedImage image) throws IOException {
		Searcher searcher = new Searcher();
		return searcher.getTopResult(image);
	}
	
	public static float getCEDDDistance(BufferedImage image1, BufferedImage image2) {
		CEDD cedd = new CEDD();
		cedd.extract(image1);
		CEDD newCedd = new CEDD();
		newCedd.extract(image2);
		return cedd.getDistance(newCedd);
	}
	
	/**
	 * Checks in {@link MemeCache} first, if it's not there 
	 * downloads image from path and queries Lucene index.
	 * @param path Path to the image to be classified
	 * @return Classification {@link Result}
	 */
	public static Result classify(String path) {
		Result r = null;
		try {
			r = MemeCache.getInstance().getCache().get(path);
		} catch (ExecutionException e) {
			log.error("Failed to classify meme from path: " + path);
			e.printStackTrace();
		}
		return r;
	}
}
