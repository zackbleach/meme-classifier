package com.zackbleach.memetable.clustering.test;

import static com.zackbleach.memetable.util.ImageUtils.readImagesFromFolder;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.JsonParseException;
import org.junit.Test;

import weka.clusterers.Cobweb;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

import weka.filters.Filter;
import weka.filters.unsupervised.attribute.AddID;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.google.common.collect.ImmutableList;
import com.zackbleach.memetable.clustering.CEDD;
import com.zackbleach.memetable.clustering.Cluster;
import com.zackbleach.memetable.clustering.HierarchyBuilder;
import com.zackbleach.memetable.clustering.MemeFeature;
import com.zackbleach.memetable.clustering.OpponentHistogram;
import com.zackbleach.memetable.contentextraction.entity.ExtractedEntity;
import com.zackbleach.memetable.contentextraction.entity.ExtractedMeme;
import com.zackbleach.memetable.imagerecognition.Result;
import com.zackbleach.memetable.util.ClassificationUtils;
import com.zackbleach.memetable.util.ClusteringUtils;
import com.zackbleach.memetable.util.ImageUtils;

public class ClusteringTest {
    private static final Log log = LogFactory.getLog(ClusteringTest.class);

    private HierarchyBuilder hierarchyBuilder = new HierarchyBuilder();
    private ClusteringUtils clusteringUtils = new ClusteringUtils();
    private ClassificationUtils classificationUtils = new ClassificationUtils();

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
                result = classificationUtils.classifyMemeFromImage(i);
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

    @Test
    /**
    * WEKA IS SHIT!
    */
    public void wekaTest() throws Exception {
        List<BufferedImage> images;
        try {
            images = readImagesFromFolder("TestCluster/");
            List<double[]> histograms = new ArrayList<double[]>();
            for (BufferedImage image : images) {
                OpponentHistogram oh = new OpponentHistogram();
                oh.extract(image);
                histograms.add(oh.getDoubleHistogram());
            }

            ArrayList<Attribute> attributes = new ArrayList<Attribute>();
            for (int i = 0; i < 64; i++) {
                Attribute attr = new Attribute("bin" + i);
                attributes.add(attr);
            }
            attributes.add(new Attribute("name"));

            Instances dataset = new Instances("meme_dataset", attributes, 0);
            Instance example = new DenseInstance(65);
            for (double[] histogram : histograms) {
                for (int i = 0; i < attributes.size() - 1; i++) {
                    example.setValue(attributes.get(i), histogram[i]);
                }
                example.setValue(attributes.get(attributes.size()-1), 1234);
                dataset.add(example);
            }

            // train Cobweb
            Cobweb cw = new Cobweb();
            cw.buildClusterer(dataset);
            for (Instance i : dataset) {
                cw.updateClusterer(i);
            }
            cw.updateFinished();
            log.warn(cw);
        } catch (IOException e) {
            log.error("Failed to load test memes");
        }
    }
}
