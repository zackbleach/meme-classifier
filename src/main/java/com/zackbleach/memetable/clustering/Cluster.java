package com.zackbleach.memetable.clustering;

import java.util.List;

import net.semanticmetadata.lire.imageanalysis.CEDD;

public class Cluster {

	private String name;
	//Distance measurement
	private CEDD cedd;
	private Cluster parent;
	private Cluster[] children = new Cluster[2];
	
	public String getName() {
		return name;
	}
	public Cluster getParent() {
		return parent;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setParent(Cluster parent) {
		this.parent = parent;
	}
	public CEDD getCedd() {
		return cedd;
	}
	public void setCedd(CEDD cedd) {
		this.cedd = cedd;
	}
	public Cluster[] getChildren() {
		return children;
	}
	public void setChildren(Cluster[] children) {
		this.children = children;
	}
}
