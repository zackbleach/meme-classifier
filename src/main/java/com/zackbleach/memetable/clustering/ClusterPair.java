package com.zackbleach.memetable.clustering;

import java.util.Set;

import org.springframework.util.StringUtils;

public class ClusterPair implements Comparable<ClusterPair>{
	
	private Set<Cluster> clusters;
	private float distance;
	
	public ClusterPair(Cluster left, Cluster right) {
		clusters.add(left);
		clusters.add(right);
		distance = left.getCedd().getDistance(right.getCedd());
	}
	
	public float getDistance() {
		return distance;
	}
	
	public void setDistance(float distance) {
		this.distance = distance;
	}
	
	@Override
	public int compareTo(ClusterPair otherClusterPair) {
	    if (otherClusterPair.getDistance() == this.getDistance()) {
	    	return 0;
	    }
		return (this.getDistance() > otherClusterPair.getDistance()) ? 1 : -1;
	}
	
	public Cluster agglomerate() {
		//if first clusters name is not null use that, otherwise use cluster 2
		Cluster[] leftAndRight = clusters.toArray(new Cluster[2]);
		Cluster left = leftAndRight[0];
		Cluster right = leftAndRight[1];
		
		Cluster cluster = new Cluster();
		if (!StringUtils.isEmpty(left.getName()) && !StringUtils.isEmpty(right.getName())) {
			cluster.setName(left.getName() + "+" + right.getName());
		} else if (left.getName() != null) {
			cluster.setName(left.getName());
		} else if (right.getName() != null) {
			cluster.setName(right.getName());
		}
		//set children as left and right clusters 
		Cluster[] children = new Cluster[2];
		children[0] = left;
		children[1] = right;
		cluster.setChildren(children);
		//merge CEDD 
		CEDD averageHistogram = new CEDD();
		double[] clusterSmash = new double[144];
		for (int i = 0; i < left.getCedd().getDoubleHistogram().length; i++) {
			clusterSmash[i] = (left.getCedd().getDoubleHistogram()[i] + right
					.getCedd().getDoubleHistogram()[i]) / 2;
		}
		averageHistogram.setDoubleHistogram(clusterSmash);
		cluster.setCedd(averageHistogram);
		return cluster;
 	}
}