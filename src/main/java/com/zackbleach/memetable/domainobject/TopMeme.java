package com.zackbleach.memetable.domainobject;

public class TopMeme {

	private int rank;
	private String name;
	private int instances;
	
	public TopMeme(int rank, String name, int instances) {
		this.rank = rank;
		this.name = name;
		this.instances = instances;
	}
	
	public int getRank() {
		return rank;
	}
	public void setRank(int rank) {
		this.rank = rank;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getInstances() {
		return instances;
	}
	public void setInstances(int instances) {
		this.instances = instances;
	}
	
}
