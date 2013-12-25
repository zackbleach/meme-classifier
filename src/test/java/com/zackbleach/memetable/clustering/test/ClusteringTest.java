package com.zackbleach.memetable.clustering.test;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.JsonParseException;
import org.junit.Test;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.zackbleach.memetable.cache.MemeCache;
import com.zackbleach.memetable.clustering.CEDD;
import com.zackbleach.memetable.clustering.Cluster;
import com.zackbleach.memetable.clustering.HierarchyBuilder;
import com.zackbleach.memetable.clustering.MemeFeature;
import com.zackbleach.memetable.clustering.OpponentHistogram;
import com.zackbleach.memetable.contentextraction.entity.ExtractedEntity;
import com.zackbleach.memetable.contentextraction.entity.ExtractedMeme;
import com.zackbleach.memetable.contentextraction.extractor.MemeExtractor;
import com.zackbleach.memetable.imagerecognition.Result;
import com.zackbleach.memetable.util.ClassificationUtils;
import com.zackbleach.memetable.util.ClusteringUtils;
import com.zackbleach.memetable.util.ImageUtils;

public class ClusteringTest {
    private static final Log log = LogFactory.getLog(ClusteringTest.class);

    private HierarchyBuilder hierarchyBuilder = new HierarchyBuilder();
    private ClusteringUtils clusteringUtils = new ClusteringUtils();
    private MemeCache memeCache = MemeCache.getInstance();

    @Test
    // TODO: this can use mocking? or maybe local html, although content
    // extraction should be tested elsewhere
    // TODO: at the moment this won't work because I'm switching the
    // createInitialClusters method to depend on
    // the extraction pipeline
    public void cluster() throws JsonParseException, JsonMappingException,
            IOException, URISyntaxException {
        String path = "TestCluster/";
        List<ExtractedEntity> entities = getExtractedMemes(path);
        log.info("Memes extracted: " + entities.size());
        List<Cluster> clusters = createInitialClusters(CEDD.class, entities);
        Cluster c = hierarchyBuilder.buildHeirarchy(clusters);
        clusteringUtils.printHeirarchy(c);
    }

    private List<ExtractedEntity> getExtractedMemes(String path) {
        List<ExtractedEntity> entities = new ArrayList<ExtractedEntity>();
        try {
            List<BufferedImage> images = ImageUtils
                    .readImagesFromFolder("TestCluster/");
            for (BufferedImage i : images) {
                ExtractedMeme entity = new ExtractedMeme();
                entity.setImage(i);
                Result result;
                result = ClassificationUtils.classifyMemeFromImage(i);
                entity.setName(result.getMeme().getIdentifier().toLowerCase());
                entities.add(entity);
            }
        } catch (IOException e) {
            log.error("Failed to classify meme");
        }
        return entities;
    }

    // TODO: Why the exceptions?!
    List<Cluster> createInitialClusters(
            Class<? extends MemeFeature> memeFeatureClass,
            List<ExtractedEntity> memes) throws JsonParseException,
            JsonMappingException, IOException, URISyntaxException {
        List<Cluster> clusters = new ArrayList<Cluster>();
        for (ExtractedEntity meme : memes) {
            MemeFeature cedd;
            Cluster cluster = new Cluster();
            try {
                cedd = memeFeatureClass.newInstance();
            } catch (InstantiationException e) {
                cedd = new OpponentHistogram();
            } catch (IllegalAccessException e) {
                cedd = new OpponentHistogram();
            }
            cedd.extract(meme.getImage());
            cluster.setFeature(cedd);
            cluster.setName(meme.getName());
            clusters.add(cluster);
        }
        return clusters;
    }
}
