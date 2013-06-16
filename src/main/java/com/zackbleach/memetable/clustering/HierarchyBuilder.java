package com.zackbleach.memetable.clustering;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class HierarchyBuilder {

	private List<Cluster> clusters;
	//todo: change pair to set
	private Set<ClusterPair> pairs;
	
	public HierarchyBuilder(List<Cluster> clusters, Set<ClusterPair> distances) {
		this.clusters = clusters;
		this.pairs = distances;
	}
	
	//TODO: Tidy this up
	public Cluster buildHeirarchy() {
		while (pairs.size() > 0) {
			System.out.println("Clusters: " + clusters.size());
			System.out.println("Pairs: " + pairs.size());
			Collections.sort(pairs);
			ClusterPair minDistance = pairs.get(0);
			Cluster newCluster = minDistance.agglomerate();
			System.out.println("Min distance was: " + minDistance.getDistance());
			System.out.println("Other distances were: ");
			for (ClusterPair distance : pairs) {
				System.out.println(distance.getLeftCLuster() + " --> " + distance.getRightCluster() + " + " + 
						distance.getDistance());
			}
			int newHeight = Math.max(minDistance.getLeftCLuster().getHeight(), minDistance.getRightCluster().getHeight()) +1;
			newCluster.setHeight(newHeight);
			clusters.remove(minDistance.getLeftCLuster());
			clusters.remove(minDistance.getRightCluster());
			Iterator<ClusterPair> it = pairs.iterator(); 
			while (it.hasNext()) {
				ClusterPair p = it.next();
				if (p.getLeftCLuster() == minDistance.getRightCluster()
						|| p.getRightCluster() == minDistance.getRightCluster()) {
					it.remove();
					continue;
				}
				else if (p.getLeftCLuster() == minDistance.getLeftCLuster()
						|| p.getRightCluster() == minDistance.getLeftCLuster()) {
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

	public List<Cluster> getClusters() {
		return clusters;
	}

	public List<ClusterPair> getPairs() {
		return pairs;
	}

	public void setClusters(List<Cluster> clusters) {
		this.clusters = clusters;
	}

	public void setPairs(List<ClusterPair> pairs) {
		this.pairs = pairs;
	}
	
	
}
