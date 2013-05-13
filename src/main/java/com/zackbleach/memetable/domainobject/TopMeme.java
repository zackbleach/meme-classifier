package com.zackbleach.memetable.domainobject;

public class TopMeme {

	private int rank;
	private String name;
	private float certainty;
	private int instances;
	
	public TopMeme(int rank, String name, float certainty, int instances) {
		this.rank = rank;
		this.name = name;
		this.certainty = certainty;
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
	public float getCertainty() {
		return certainty;
	}

	public void setCertainty(float certainty) {
		this.certainty = certainty;
	}
}
