package com.zackbleach.meme.classifier.index;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

import net.semanticmetadata.lire.DocumentBuilder;
import net.semanticmetadata.lire.ImageSearchHits;
import net.semanticmetadata.lire.ImageSearcher;
import net.semanticmetadata.lire.ImageSearcherFactory;
import net.semanticmetadata.lire.imageanalysis.LireFeature;
import net.semanticmetadata.lire.impl.GenericDocumentBuilder;
import net.semanticmetadata.lire.impl.GenericFastImageSearcher;
import net.semanticmetadata.lire.impl.ParallelImageSearcher;
import net.semanticmetadata.lire.utils.LuceneUtils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.FSDirectory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.google.common.collect.ImmutableList;
import com.zackbleach.meme.classifier.cache.Meme;
import com.zackbleach.meme.classifier.cache.MemeCache;
import com.zackbleach.meme.scraper.ScrapedImage;
import com.zackbleach.meme.scraper.Scraper;

@Component
public class Index {

    @Autowired
    private Class<? extends LireFeature> featureExtractionMethod;

    @Autowired
    private MemeCache cache;

    @Resource(name="scrapers")
    private List<Scraper> scrapers;

    private static final Log logger = LogFactory.getLog(Index.class);
    private static final String INDEX_LOCATION = "meme-index/";
    private static final int ONE_HOUR = 3600000;

    private DocumentBuilder builder;
    private IndexReader indexReader;
    private IndexWriter indexWriter;

    public static String MEME_TYPE = "meme-type";

    public boolean buildingIndex = false;

    private List<ScrapedImage> getScrapedImages() {
        List<ScrapedImage> images = new ArrayList<ScrapedImage>();
        for (Scraper scraper : scrapers) {
            images.addAll(scraper.scrape());
        }
        return images;
    }

    @Async
    @Scheduled(fixedDelay = ONE_HOUR * 12)
    private void update() throws URISyntaxException, IOException {
        rebuildIndex();
    }

    private void rebuildIndex() throws URISyntaxException, IOException {
        buildingIndex = true;
        List<Document> documents = new ArrayList<Document>();
        for (ScrapedImage example : getScrapedImages()) {
            Document document;
            try {
                Meme meme = cache.getMeme(example);
                document = createDocument(meme);
            } catch (IOException e) {
                logger.warn("Skipping: " + example.getSourceUrl());
                continue;
            }
            logger.warn("Adding meme to index: " + example.getName()
                    + ". From: " + example.getSourceUrl());
            documents.add(document);
        }
        logger.warn("Total memes in index: " + indexReader.numDocs());
        add(documents);
        buildingIndex = false;
    }

    @PostConstruct
    private void create() throws IOException, URISyntaxException {
        builder = new GenericDocumentBuilder(featureExtractionMethod);
        File index = new File(INDEX_LOCATION);
        if (!index.exists()) {
            rebuildIndex();
        }
        indexWriter = getIndexWriter();
        indexReader = DirectoryReader.open(indexWriter, true);
    }

    @PreDestroy
    private void shutdown() throws IOException {
        indexWriter.close();
    }

    private IndexWriter getIndexWriter() throws IOException {
        IndexWriterConfig conf = new IndexWriterConfig(
                LuceneUtils.LUCENE_VERSION, new WhitespaceAnalyzer(
                        LuceneUtils.LUCENE_VERSION));
        conf.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
        return new IndexWriter(FSDirectory.open(new File(INDEX_LOCATION)), conf);
    }

    public void add(String imageUrl, String name) throws IOException,
            URISyntaxException {
        Meme meme = cache.getMeme(new ScrapedImage(name, imageUrl));
        Document document = createDocument(meme);
        add(ImmutableList.of(document));
    }

    public void add(List<Document> documents) throws IOException {
        for (Document document : documents) {
            logger.info("Adding meme to index: " + document.getField(MEME_TYPE));
            String name = document.get(DocumentBuilder.FIELD_NAME_IDENTIFIER);
            logger.info("Name is: " + name);
            Term term = new Term(DocumentBuilder.FIELD_NAME_IDENTIFIER, name);
            indexWriter.updateDocument(term, document);
        }
        indexWriter.commit();
        indexReader = DirectoryReader.open(indexWriter, true);
        logger.info(indexReader.numDocs() + " Memes in index");
    }

    public int getNumDocs() {
        return indexReader.numDocs();
    }

    public ImageSearchHits search(BufferedImage image) throws IOException,
            BuildingIndexException {
        if (buildingIndex) {
            throw new BuildingIndexException("Building Index! Please wait");
        }
        ImageSearcher imageSearcher = new GenericFastImageSearcher(10,
                featureExtractionMethod);
        ImageSearchHits hits = imageSearcher.search(image, indexReader);
        return hits;
    }

    public List<ScrapedImage> getAllDocs() {
        List<ScrapedImage> docs = new ArrayList<ScrapedImage>();
        for (int i=0; i< indexReader.maxDoc(); i++) {
            Document doc;
            try {
                doc = indexReader.document(i);
                String url = doc.getField(GenericDocumentBuilder.FIELD_NAME_IDENTIFIER).stringValue();
                String name = doc.getField(MEME_TYPE).stringValue();
                ScrapedImage image = new ScrapedImage(name, url);
                docs.add(image);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return docs;
    }

        private Document createDocument(Meme meme) throws FileNotFoundException {
            Document document = builder.createDocument(meme.getImage(),
                    meme.getSourceUrl());
            document.add(new StoredField(MEME_TYPE, meme.getName()));
            return document;
        }
}
