package com.zackbleach.memetable.clustering;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.zackbleach.memetable.contentextraction.ExtractedMeme;
import com.zackbleach.memetable.contentextraction.MemeExtractor;
import com.zackbleach.memetable.imagerecognition.Meme;
import com.zackbleach.memetable.imagerecognition.Result;
import com.zackbleach.memetable.scraper.Post;
import com.zackbleach.memetable.scraper.RedditScraper;
import com.zackbleach.memetable.scraper.Scraper;
import com.zackbleach.memetable.util.ClassificationUtils;
import com.zackbleach.memetable.util.ImageUtils;

//TODO: Autowire the rest of this app
public class Clusterer {

	private static final Logger log = Logger.getLogger(Clusterer.class);
	
	@Autowired
	public HierarchyBuilder hierarchyBuilder;
	
	//TODO: This should return a cluster?
	public void cluster() throws JsonParseException, JsonMappingException, IOException, URISyntaxException {
		List<Cluster> clusters = createInitialClustersFromReddit(CEDD.class);
		Set<ClusterPair> pairings = getPairings(clusters);
		Cluster c = hierarchyBuilder.buildHeirarchy(clusters, pairings);
		printHeirarchy(c);
	}
	
	//Should this be here? (Maybe in clustering utils?)
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
				for (Cluster child : c.getChildren()) {
					q.add(child);
				}
			}
		}
	}


	
	//this should just take an initial set of images and cluster them 
	public List<Cluster> createInitialClustersFromReddit(Class<? extends MemeFeature> memeFeatureClass)
			throws JsonParseException, JsonMappingException, IOException, URISyntaxException {
		Scraper scraper = new RedditScraper();
		List<Post> posts = scraper.scrape();
		MemeExtractor m = new MemeExtractor();
		List<ExtractedMeme> memes = new ArrayList<ExtractedMeme>();
		for (Post p : posts) {
			try {
				ExtractedMeme extractMeme = m.extractMeme(p.getImageUrl());
				memes.add(extractMeme);
			} catch (SocketTimeoutException s) {
				log.error("Read timed out, skipping...");
			}
		}
		List<Cluster> clusters = new ArrayList<Cluster>();
		for (ExtractedMeme meme : memes) {
			MemeFeature histogram;
			if (meme.getImage() != null) {
				Cluster cluster = new Cluster();
				try {
					histogram = memeFeatureClass.newInstance();
				} catch (InstantiationException e) {
					histogram = new OpponentHistogram();
				} catch (IllegalAccessException e) {
					histogram = new OpponentHistogram();
				}
				histogram.extract(meme.getImage());
				cluster.setFeature(histogram);
				cluster.setName(meme.getName());
				if (cluster.getName() == null) {
					cluster.setName(ClassificationUtils.classifyMemeFromImage(meme.getImage()).getMeme().getIdentifier());
				}
				clusters.add(cluster);
			}
		}
		return clusters;
	}
	
	//TODO: WHY THE EXCEPTIONS?!
	//TODO: SHOULD THIS TAKE A LIST OF IMAGES OR A LIST OF EXTRACTED MEMES?
	List<Cluster> createInitialClusters(Class<? extends MemeFeature> memeFeatureClass, 
			List<BufferedImage> images) throws JsonParseException, JsonMappingException, 
			IOException, URISyntaxException {
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
	
	//TODO: add comment
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
	
	public HierarchyBuilder getHierarchyBuilder() {
		return hierarchyBuilder;
	}

	public void setHierarchyBuilder(HierarchyBuilder hierarchyBuilder) {
		this.hierarchyBuilder = hierarchyBuilder;
	}
}
