package com.zackbleach.memetable.domainobject;

public class TopMeme {

	private int rank;
	private String name;
	private float certainty;
	private int score;
	private boolean downloaded;
	
	public TopMeme(int rank, String name, float certainty, int instances, boolean downloaded) {
		this.rank = rank;
		this.name = name;
		this.certainty = certainty;
		this.score = instances;
		this.downloaded = downloaded;
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
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public float getCertainty() {
		return certainty;
	}
	public void setCertainty(float certainty) {
		this.certainty = certainty;
	}
	public boolean isDownloaded() {
		return downloaded;
	}
	public void setDownloaded(boolean downloaded) {
		this.downloaded = downloaded;
	}
}
