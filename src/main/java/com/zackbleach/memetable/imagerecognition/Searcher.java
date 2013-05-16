package com.zackbleach.memetable.imagerecognition;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import net.semanticmetadata.lire.DocumentBuilder;
import net.semanticmetadata.lire.ImageSearchHits;
import net.semanticmetadata.lire.ImageSearcher;
import net.semanticmetadata.lire.ImageSearcherFactory;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.store.FSDirectory;

import com.zackbleach.memetable.contentextraction.MemeExtractor;
import com.zackbleach.memetable.imagerecognition.Result;

public class Searcher {
	
	private static final Logger log = Logger.getLogger(MemeExtractor.class);
	
	private ImageSearchHits results;
	
	//possible results
	public static final String INSANITY_WOLF = "Insanity Wolf";
	public static final String PHILOSORAPTOR = "Philosoraptor";
	public static final String INTERNET_WIFE = "Internet Wife";
	public static final String SCUMBAG_STEVE = "Scumbag Steve";
	public static final String FOUL_BACHELOR = "Foul Bachelor";
	public static final String ADVICE_MALLARD = "Actual Advice Mallard";
	public static final String SUCCESS_KID = "Success Kid";
	public static final String BAD_ADVICE_MALLARD = "Bad Advice Mallard";
	public static final String GOOD_GUY_GREG = "Good Guy Greg";
	
	private Map<String, String> memes;
	
	public Searcher() {
		DOMConfigurator.configure("src/main/webapp/WEB-INF/log4j.xml");
		memes = new HashMap<String, String>();
		memes.put(Indexer.MEME_LOCATION+Indexer.INSANITY_WOLF_DIR, INSANITY_WOLF);
		memes.put(Indexer.MEME_LOCATION+Indexer.PHILOSORAPTOR_DIR, PHILOSORAPTOR);
		memes.put(Indexer.MEME_LOCATION+Indexer.INTERNETWIFE_DIR, INTERNET_WIFE);
		memes.put(Indexer.MEME_LOCATION+Indexer.SCUMBAG_STEVE_DIR, SCUMBAG_STEVE);
		memes.put(Indexer.MEME_LOCATION+Indexer.FOUL_BACHELOR_DIR, FOUL_BACHELOR);
		memes.put(Indexer.MEME_LOCATION+Indexer.ADVICE_MALLARD_DIR, ADVICE_MALLARD);
		memes.put(Indexer.MEME_LOCATION+Indexer.SUCCESS_KID_DIR, SUCCESS_KID);
		memes.put(Indexer.MEME_LOCATION+Indexer.BAD_ADVICE_MALLARD_DIR, BAD_ADVICE_MALLARD);
		memes.put(Indexer.MEME_LOCATION+Indexer.GOOD_GUY_GREG_DIR, GOOD_GUY_GREG);
	}
	
	public void search(BufferedImage img) throws IOException {
		IndexReader ir = DirectoryReader.open(FSDirectory.open(new File(Indexer.INDEX_PATH)));
		ImageSearcher searcher = ImageSearcherFactory.createCEDDImageSearcher(10);
		ImageSearchHits hits = searcher.search(img, ir);
		results = hits;
	}
	
	public void search(String path) throws IOException {
		BufferedImage img = null;
		File f = new File(path);
		img = ImageIO.read(f);
		search(img);
	}
	
	public Result getSimplifiedResult() {
		Result result = new Result();
		result.setCertainty(results.score(0));
		log.info("Score = " + results.score(0));
		if (results.score(0) < 0.2) {
			result.setMeme("Cats");
			return result;
		}
		String closestFilePath = results.doc(0).getValues(DocumentBuilder.FIELD_NAME_IDENTIFIER)[0];
		String memeName = memes.get(removeFileFromPath(closestFilePath));
		log.info("Found instance of: " + memeName);
		result.setMeme(memeName);
		return result;
	}
	
	private String removeFileFromPath(String path) {
		String[] parts = path.split("/");
		String newPath = StringUtils.join(Arrays.copyOfRange(parts, 0, parts.length-1), "/");
		return newPath;
	}

	public ImageSearchHits getResults() {
		return results;
	}
}
