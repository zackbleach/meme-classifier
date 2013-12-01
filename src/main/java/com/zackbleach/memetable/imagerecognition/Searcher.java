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

import com.zackbleach.memetable.contentextraction.extractor.MemeExtractor;
import com.zackbleach.memetable.imagerecognition.Result;

public class Searcher {
	
	private static final Logger log = Logger.getLogger(MemeExtractor.class);
	
	private ImageSearchHits results;
	
	public Searcher() {
	
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
	
	public Result getTopResult(BufferedImage img) throws IOException {
		search(img);
		Result result = new Result();
		result.setExtractedImage(img);
		result.setCertainty(results.score(0));
		log.info("Score = " + results.score(0));
		if (results.score(0) < 0.2) {
			result.setMeme(Meme.CATS);
			return result;
		}
		String closestFilePath = results.doc(0).getValues(DocumentBuilder.FIELD_NAME_IDENTIFIER)[0];
		Meme meme = Meme.typeByPath(closestFilePath);
		log.info("Found instance of: " + meme.getIdentifier());
		result.setMeme(meme);
		return result;
	}

	public ImageSearchHits getResults() {
		return results;
	}
}
