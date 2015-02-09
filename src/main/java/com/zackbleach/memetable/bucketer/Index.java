package com.zackbleach.memetable.bucketer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import net.semanticmetadata.lire.DocumentBuilder;
import net.semanticmetadata.lire.ImageSearchHits;
import net.semanticmetadata.lire.ImageSearcher;
import net.semanticmetadata.lire.imageanalysis.ColorLayout;
import net.semanticmetadata.lire.imageanalysis.LireFeature;
import net.semanticmetadata.lire.impl.GenericDocumentBuilder;
import net.semanticmetadata.lire.impl.GenericFastImageSearcher;
import net.semanticmetadata.lire.utils.LuceneUtils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
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
import com.zackbleach.memetable.scraper.QuickMemeScraper;
import com.zackbleach.memetable.scraper.ScrapedImage;

@Component
public class Index {

    public  static final Class<? extends LireFeature> FEATURE_EXTRACTION_METHOD = ColorLayout.class;

    private static final Log logger = LogFactory.getLog(Index.class);
    private static final String INDEX_LOCATION = "meme-index/";
    private static final int ONE_HOUR = 3600000;

    private final DocumentBuilder builder = new GenericDocumentBuilder(
            FEATURE_EXTRACTION_METHOD);

    private IndexReader indexReader;
    private IndexWriter indexWriter;

    @Autowired
    private QuickMemeScraper quickMemeScraper;

    @Async
    @Scheduled(fixedDelay = ONE_HOUR * 12)
    private void update() throws IOException {
        List<ScrapedImage> templates = quickMemeScraper.scrapeTemplates();
        List<Document> documents = new ArrayList<Document>();
        for (ScrapedImage template : templates) {
            documents.add(builder.createDocument(template.getImage(),
                    template.getName()));
        }
        add(documents);
    }

    @PostConstruct
    private void create() throws IOException {
        File index = new File(INDEX_LOCATION);
        indexWriter = createIndexWriter();
        if (index.exists()) {
            indexReader = DirectoryReader.open(indexWriter, true);
        } else {
            update();
        }
    }

    private IndexWriter createIndexWriter() throws IOException {
        IndexWriterConfig conf = new IndexWriterConfig(
                LuceneUtils.LUCENE_VERSION, new WhitespaceAnalyzer(
                        LuceneUtils.LUCENE_VERSION));
        conf.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
        return new IndexWriter(FSDirectory.open(new File(INDEX_LOCATION)), conf);

    }

    public void add(BufferedImage image, String name) throws IOException {
        Document document = builder.createDocument(image, name);
        add(ImmutableList.of(document));
    }

    public void add(List<Document> documents) throws IOException {
        for (Document document : documents) {
            String name = document.get(DocumentBuilder.FIELD_NAME_IDENTIFIER);
            Term term = new Term(DocumentBuilder.FIELD_NAME_IDENTIFIER, name);
            logger.info("Adding meme to index: " + name);
            indexWriter.updateDocument(term, document);
        }
        indexWriter.commit();
        indexReader = DirectoryReader.open(indexWriter, true);
        logger.info(indexReader.numDocs() + " Memes in index");
    }

    public ImageSearchHits search(BufferedImage image) throws IOException {
        ImageSearcher imageSearcher = new GenericFastImageSearcher(10,
                FEATURE_EXTRACTION_METHOD);
        ImageSearchHits hits = imageSearcher.search(image, indexReader);
        return hits;
    }
}
