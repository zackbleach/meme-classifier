package com.zackbleach.memetable.clustering;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class HierarchyBuilder {

	private List<Cluster> clusters;
	private Set<ClusterPair> pairs;
	
	public HierarchyBuilder(List<Cluster> clusters, Set<ClusterPair> distances) {
		this.clusters = clusters;
		this.pairs = distances;
	}
	
	public Cluster buildHeirarchy() {
		while (pairs.size() > 0) {
			ClusterPair[] orderedPairs = pairs.toArray(new ClusterPair[pairs.size()]);
			Arrays.sort(orderedPairs);
			ClusterPair minDistance = orderedPairs[0];
			Cluster newCluster = minDistance.agglomerate();
			clusters.removeAll(minDistance.getClusters());
			Iterator<ClusterPair> it = pairs.iterator(); 
			while (it.hasNext()) {
				ClusterPair p = it.next();
				if (!Collections.disjoint(p.getClusters(), minDistance.getClusters())) {
					it.remove();
				}
			}
			for (Cluster c : clusters) {
				pairs.add(new ClusterPair(newCluster, c));
			}
			clusters.add(newCluster);
		}
		return clusters.get(0);
	}
}
