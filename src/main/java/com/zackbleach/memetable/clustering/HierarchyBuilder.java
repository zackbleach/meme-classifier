package com.zackbleach.memetable.clustering;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class HierarchyBuilder {
	
	private final static int MAGICAL_MEME_DISTANCE = 10; 
	
	public HierarchyBuilder() {
	}
	
	public Cluster buildHeirarchy(List<Cluster> clusters, Set<ClusterPair> pairs) {
		while (pairs.size() > 0) {
			ClusterPair[] orderedPairs = pairs.toArray(new ClusterPair[pairs.size()]);
			Arrays.sort(orderedPairs);
			ClusterPair minDistance = orderedPairs[0];
			if (minDistance.getDistance() > MAGICAL_MEME_DISTANCE) {
				break;
			}
            System.out.println("Hello");

			Cluster newCluster = minDistance.agglomerate();
			clusters.removeAll(Arrays.asList(minDistance.getClusters()));
			Iterator<ClusterPair> it = pairs.iterator(); 
			while (it.hasNext()) {
				ClusterPair p = it.next();
				if (!Collections.disjoint(Arrays.asList(p.getClusters()), Arrays.asList(minDistance.getClusters()))) {
					it.remove();
				}
			}
			for (Cluster c : clusters) {
				pairs.add(new ClusterPair(newCluster, c));
			}
			clusters.add(newCluster);
		}
		return finalCluster(clusters);
	}
	
	private Cluster finalCluster(List<Cluster> cs) {
		Cluster root = new Cluster();
		Cluster[] children = cs.toArray(new Cluster[cs.size()]);
		root.setName("Clusters");
		root.setChildren(children);
		return root;
	}
}
