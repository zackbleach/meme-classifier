package com.zackbleach.memetable.imagerecognition;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.imageio.ImageIO;

import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;

import net.semanticmetadata.lire.DocumentBuilderFactory;
import net.semanticmetadata.lire.impl.ChainedDocumentBuilder;
import net.semanticmetadata.lire.utils.FileUtils;
import net.semanticmetadata.lire.utils.LuceneUtils;

public class Indexer {
		
	public static final String INDEX_PATH = "index/";
	public static final String MEME_LOCATION = "/Users/zackbleach/Documents/Spring/Memes/";
	
    public static void index() throws IOException {
    	
    	if (new File(INDEX_PATH).getCanonicalFile().isDirectory()) {
    		System.out.println("Index alredy exists");
    		return;
    	}

    	boolean passed = false;
        if (MEME_LOCATION != null) {
            File f = new File(MEME_LOCATION);
            System.out.println("Indexing images in " + MEME_LOCATION);
            if (f.exists() && f.isDirectory()) passed = true;
        }
        if (!passed) {
            throw new IOException("Directory not found");
        }	
        // Getting all images from a directory and its sub directories.
        ArrayList<String> images = FileUtils.getAllImages(new File(MEME_LOCATION), true);
 
        // Use multiple DocumentBuilder instances:
        ChainedDocumentBuilder builder = new ChainedDocumentBuilder();
        builder.addBuilder(DocumentBuilderFactory.getCEDDDocumentBuilder());
        builder.addBuilder(DocumentBuilderFactory.getEdgeHistogramBuilder());
        builder.addBuilder(DocumentBuilderFactory.getOpponentHistogramDocumentBuilder());
 
        // Creating an Lucene IndexWriter
        IndexWriterConfig conf = new IndexWriterConfig(LuceneUtils.LUCENE_VERSION,
                new WhitespaceAnalyzer(LuceneUtils.LUCENE_VERSION));
        IndexWriter iw = new IndexWriter(FSDirectory.open(new File(INDEX_PATH)), conf);
        // Iterating through images building the low level features
        for (Iterator<String> it = images.iterator(); it.hasNext(); ) {
            String imageFilePath = it.next();
            System.out.println("Indexing " + imageFilePath);
            try {
                BufferedImage img = ImageIO.read(new FileInputStream(imageFilePath));
                Document document = builder.createDocument(img, imageFilePath);
                iw.addDocument(document);
            } catch (Exception e) {
                System.err.println("Error reading image or indexing it.");
                e.printStackTrace();
            }
        }
        // closing the IndexWriter
        iw.close();
        System.out.println("Finished indexing.");
    }
}