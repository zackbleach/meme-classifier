package com.zackbleach.meme.classifier.index;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import net.semanticmetadata.lire.DocumentBuilder;
import net.semanticmetadata.lire.ImageSearchHits;
import net.semanticmetadata.lire.ImageSearcher;
import net.semanticmetadata.lire.imageanalysis.LireFeature;
import net.semanticmetadata.lire.impl.GenericDocumentBuilder;
import net.semanticmetadata.lire.impl.GenericFastImageSearcher;
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

import com.zackbleach.meme.classifier.cache.Meme;
import com.zackbleach.meme.classifier.cache.MemeCache;
import com.zackbleach.meme.classifier.scraper.Scraper;
import com.zackbleach.meme.scraper.ScrapedImage;

@Component
public class Index {

    public static String MEME_TYPE = "meme-type";

    // Initialised to true so that it blocks calls just after booting when it's scraping the first set of memes
    public boolean buildingIndex = true;

    @Autowired
    public Class<? extends LireFeature> featureExtractionMethod;

    @Autowired
    private Scraper scraper;

    @Autowired
    private MemeCache cache;

    private static final int ONE_HOUR = 3600000;

    private static final Log logger = LogFactory.getLog(Index.class);

    private String indexLocation = "meme-index/";
    private IndexReader indexReader;
    private IndexWriter indexWriter;

    @PostConstruct
    public void create() throws IOException, URISyntaxException {
        indexWriter = getIndexWriter();
        indexReader = DirectoryReader.open(indexWriter, true);
    }

    @PreDestroy
    public void shutdown() throws IOException {
        indexWriter.close();
    }

    @Async
    @Scheduled(fixedDelay = ONE_HOUR * 12)
    public void update() throws URISyntaxException, IOException {
        logger.warn("Total memes in index: " + indexReader.numDocs());
        List<Document> documents = convertImagesToDocuments(scraper.getNewMemes());
        buildingIndex = true;
        add(documents);
        buildingIndex = false;
    }

    private List<Document> convertImagesToDocuments(Set<ScrapedImage> scrapedImages) {
        List<Document> documents = new ArrayList<Document>();
        DocumentBuilder builder = new GenericDocumentBuilder(featureExtractionMethod);
        for (ScrapedImage scrapedImage : scrapedImages) {
            logger.info("Converting: " + scrapedImage.getSourceUrl() + " to document");
            Document document;
            try {
                Meme meme = cache.getMeme(scrapedImage);
                document = createDocument(meme, builder);
            } catch (IOException | URISyntaxException e) {
                logger.warn("Skipping: " + scrapedImage.getSourceUrl());
                continue;
            }
            documents.add(document);
        }
        return documents;
    }

    private Document createDocument(Meme meme, DocumentBuilder builder) throws FileNotFoundException {
        Document document = builder.createDocument(meme.getImage(),
                meme.getSourceUrl());
        document.add(new StoredField(Index.MEME_TYPE, meme.getName()));
        return document;
    }
    private IndexWriter getIndexWriter() throws IOException {
        IndexWriterConfig conf = new IndexWriterConfig(
                LuceneUtils.LUCENE_VERSION, new WhitespaceAnalyzer(
                        LuceneUtils.LUCENE_VERSION));
        conf.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
        return new IndexWriter(FSDirectory.open(new File(indexLocation)), conf);
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


    public int getNumDocs() {
        return indexReader.numDocs();
    }

    public List<ScrapedImage> getAllDocs() {
        List<ScrapedImage> docs = new ArrayList<ScrapedImage>();
        for (int i = 0; i < indexReader.maxDoc(); i++) {
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

}
