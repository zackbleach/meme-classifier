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
public class Searcher {
	
	private static final Logger log = Logger.getLogger(MemeExtractor.class);
	
	private ImageSearchHits results;
	
	//possible results
	public static final String INSANITY_WOLF = "Insanity Wolf";
	public static final String PHILOSORAPTOR = "Philosoraptor";
	public static final String INTERNET_WIFE = "Internet Wife";
	
	private Map<String, String> memes;
	
	public Searcher() {
		DOMConfigurator.configure("src/main/webapp/WEB-INF/log4j.xml");
		memes = new HashMap<String, String>();
		memes.put(Indexer.MEME_LOCATION+Indexer.INSANITY_WOLF_DIR, INSANITY_WOLF);
		memes.put(Indexer.MEME_LOCATION+Indexer.PHILOSORAPTOR_DIR, PHILOSORAPTOR);
		memes.put(Indexer.MEME_LOCATION+Indexer.INTERNETWIFE_DIR, INTERNET_WIFE);
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
	
	public String getSimplifiedResult() {
		String closestFilePath = results.doc(0).getValues(DocumentBuilder.FIELD_NAME_IDENTIFIER)[0];
		String memeName = memes.get(removeFileFromPath(closestFilePath));
		log.info("Found instance of: " + memeName);
		return memeName;
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
