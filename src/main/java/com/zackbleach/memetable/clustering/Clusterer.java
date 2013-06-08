package com.zackbleach.memetable.clustering;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import net.semanticmetadata.lire.imageanalysis.CEDD;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.zackbleach.memetable.contentextraction.MemeExtractor;
import com.zackbleach.memetable.scraper.Post;
import com.zackbleach.memetable.scraper.RedditScraper;

public class Clusterer {

	public List<BufferedImage> getImages() throws JsonParseException, JsonMappingException, IOException, URISyntaxException {
		RedditScraper scraper = new RedditScraper();
		MemeExtractor memeExtractor = new MemeExtractor();
		List<Post> posts = scraper.scrape();
		List<BufferedImage> results = new ArrayList<BufferedImage>();
		for (Post post : posts) {
			results.add(memeExtractor.extractMeme(post.getImageUrl()));
		}
		return results;
	}
	
	public void createInitialClusters() throws JsonParseException, JsonMappingException, IOException, URISyntaxException {
		List<BufferedImage> images = getImages();
		List<Cluster> clusters = new ArrayList<Cluster>();
		for (BufferedImage image : images) {
			Cluster cluster = new Cluster();
			CEDD cedd = new CEDD();
			cedd.extract(image);
			cluster.setCedd(cedd);
			clusters.add(cluster);
		}
	}
	
	public List<ClusterPair> getPairings(List<Cluster> clusters) {
		List<ClusterPair> pairings = new ArrayList<ClusterPair>();
		for (Cluster cluster : clusters) {
			for (Cluster otherCluster : clusters) {
				pairings.add(new ClusterPair(cluster, otherCluster));
			}
		}
		return pairings;
	}
	
	
	
}
