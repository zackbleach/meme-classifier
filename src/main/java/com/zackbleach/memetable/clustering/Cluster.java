package com.zackbleach.memetable.clustering;

public class Cluster {

	private String name;
	//Distance measurement
	private CEDD cedd;
	private Cluster parent;
	private Cluster[] children = new Cluster[2];
	private int height = 0;
	
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
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cedd == null) ? 0 : cedd.getDoubleHistogram().hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Cluster other = (Cluster) obj;
		if (cedd == null) {
			if (other.cedd != null)
				return false;
		} else if (!cedd.getDoubleHistogram().equals(other.cedd.getDoubleHistogram()))
			return false;
		return true;
	}
	public boolean isLeafNode() {
		return (null == children[0] && null == children[1]);
	}
	@Override
	public String toString() {
		return "{Name: " + name + " | " + "Height: " + height + "}";
	}
	
}
