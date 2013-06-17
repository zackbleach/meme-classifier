package com.zackbleach.memetable.clustering;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.zackbleach.memetable.imagerecognition.Meme;
import com.zackbleach.memetable.imagerecognition.Result;
import com.zackbleach.memetable.util.ClassificationUtils;
import com.zackbleach.memetable.util.ImageUtils;

public class Clusterer {

	public final static String IMAGE_PATH = "TestMemes/";
	
	public void cluster() throws JsonParseException, JsonMappingException, IOException, URISyntaxException {
		List<Cluster> clusters = createInitialClusters(OpponentHistogram.class);
		Set<ClusterPair> pairings = getPairings(clusters);
		HierarchyBuilder h = new HierarchyBuilder(clusters, pairings);
		Cluster c = h.buildHeirarchy();
		printHeirarchy(c);
	}
	
	public void printHeirarchy(Cluster root) {
		Deque<Cluster> q = new LinkedList<Cluster>();
		if (root == null) {
			return;
		}
		q.add(root);
		while(!q.isEmpty()) {
			Cluster c =  q.remove();
			System.out.println(c);
			if (!c.isLeafNode()) {
				q.add(c.getChildren()[0]);
				q.add(c.getChildren()[1]);
			}
		}
	}

	public List<BufferedImage> getImages() throws JsonParseException, JsonMappingException, IOException {
		List<BufferedImage> results = new ArrayList<BufferedImage>();
		results.add(ImageUtils.readImagesFromFolder(IMAGE_PATH+Meme.GOOD_GUY_GREG.getDirectory()).get(0));
		results.add(ImageUtils.readImagesFromFolder(IMAGE_PATH+Meme.GOOD_GUY_GREG.getDirectory()).get(1));
		results.add(ImageUtils.readImagesFromFolder(IMAGE_PATH+Meme.BUSINESS_CAT.getDirectory()).get(0));
		results.add(ImageUtils.readImagesFromFolder(IMAGE_PATH+Meme.BUSINESS_CAT.getDirectory()).get(1));
		results.add(ImageUtils.readImagesFromFolder(IMAGE_PATH+Meme.ADVICE_MALLARD.getDirectory()).get(0));
		results.add(ImageUtils.readImagesFromFolder(IMAGE_PATH+Meme.ADVICE_MALLARD.getDirectory()).get(1));
		results.add(ImageUtils.readImagesFromFolder(IMAGE_PATH+Meme.BAD_ADVICE_MALLARD.getDirectory()).get(0));
		results.add(ImageUtils.readImagesFromFolder(IMAGE_PATH+Meme.BAD_ADVICE_MALLARD.getDirectory()).get(1));
		return results;
	}
	
	public List<Cluster> createInitialClusters(Class<? extends MemeFeature> memeFeatureClass)
			throws JsonParseException, JsonMappingException, IOException, URISyntaxException {
		List<BufferedImage> images = getImages();
		List<Cluster> clusters = new ArrayList<Cluster>();
		for (BufferedImage image : images) {
			MemeFeature cedd;
			Result classification = ClassificationUtils.classifyMemeFromImage(image);
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
	
	public Set<ClusterPair> getPairings(List<Cluster> clusters) {
		Set<ClusterPair> pairings = new HashSet<ClusterPair>();
		for (int i = 0; i < clusters.size(); i++) {
			Cluster cluster = clusters.get(i);
			for (int j = i; j < clusters.size(); j ++) {
				Cluster otherCluster = clusters.get(j);
				if (cluster.getFeature().getDistance(otherCluster.getFeature()) != 0 ) {
					pairings.add(new ClusterPair(cluster, otherCluster));
				}
			}
		}
		return pairings;
	}
}
