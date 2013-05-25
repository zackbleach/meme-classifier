package com.zackbleach.memetable.domainobject;

public class TopMeme implements Comparable<TopMeme>{

	private String name;
	private float certainty;
	private int score;
	private boolean downloaded;
	private int rank;
	
	public TopMeme(String name, float certainty, int instances, boolean downloaded, int rank) {
		this.name = name;
		this.certainty = certainty;
		this.score = instances;
		this.downloaded = downloaded;
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
	public int getRank() {
		return rank;
	}
	public void setRank(int rank) {
		this.rank = rank;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		TopMeme other = (TopMeme) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return name;
	}

	public int compareTo(TopMeme arg0) {
		// TODO Auto-generated method stub
		int result= arg0.getScore() - getScore();
		if (result !=0) {
			return result;
		}
		 
		else return 1;
		 
	}
}
