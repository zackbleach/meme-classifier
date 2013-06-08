package com.zackbleach.memetable.clustering;

public class ClusterPair implements Comparable<ClusterPair>{
	
	private Cluster left;
	private Cluster right;
	private float distance;
	
	public ClusterPair(Cluster left, Cluster right) {
		this.left = left;
		this.right = right;
		distance = left.getCedd().getDistance(right.getCedd());
	}
	
	public Cluster getLeftCLuster() {
		return left;
	}
	public Cluster getRightCluster() {
		return right;
	}
	public float getDistance() {
		return distance;
	}
	public void setLeftCLuster(Cluster leftCLuster) {
		this.left = leftCLuster;
	}
	public void setRightCluster(Cluster rightCluster) {
		this.right = rightCluster;
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
	
		return null;
	}
}