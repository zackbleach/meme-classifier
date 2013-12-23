package com.zackbleach.memetable.clustering.test;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonParseException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.zackbleach.memetable.clustering.CEDD;
import com.zackbleach.memetable.clustering.Cluster;
import com.zackbleach.memetable.clustering.HierarchyBuilder;
import com.zackbleach.memetable.clustering.MemeFeature;
import com.zackbleach.memetable.clustering.OpponentHistogram;
import com.zackbleach.memetable.imagerecognition.Result;
import com.zackbleach.memetable.util.ClassificationUtils;
import com.zackbleach.memetable.util.ClusteringUtils;
import com.zackbleach.memetable.util.ImageUtils;

public class ClusteringTest {

    private HierarchyBuilder hierarchyBuilder = new HierarchyBuilder();
    private ClusteringUtils clusteringUtils = new ClusteringUtils();

    @Test
    public void cluster() throws JsonParseException, JsonMappingException,
            IOException, URISyntaxException {
        List<Cluster> clusters = createInitialClusters(CEDD.class,
                ImageUtils.readImagesFromFolder("TestCluster/"));
        Cluster c = hierarchyBuilder.buildHeirarchy(clusters);
        clusteringUtils.printHeirarchy(c);
    }
    // TODO: WHY THE EXCEPTIONS?!
    // TODO: SHOULD THIS TAKE A LIST OF IMAGES OR A LIST OF EXTRACTED MEMES?
    List<Cluster> createInitialClusters(
            Class<? extends MemeFeature> memeFeatureClass,
            List<BufferedImage> images) throws JsonParseException,
            JsonMappingException, IOException, URISyntaxException {
        List<Cluster> clusters = new ArrayList<Cluster>();
        for (BufferedImage image : images) {
            MemeFeature cedd;
            Result classification = ClassificationUtils
                    .classifyMemeFromImage(image);
            Cluster cluster = new Cluster();
            try {
                cedd = memeFeatureClass.newInstance();
            } catch (InstantiationException e) {
                cedd = new OpponentHistogram();
            } catch (IllegalAccessException e) {
                cedd = new OpponentHistogram();
            }
            cedd.extract(image);
            cluster.setFeature(cedd);
            clusters.add(cluster);
            cluster.setName(classification.getMeme().getIdentifier());
        }
        return clusters;
    }
}
