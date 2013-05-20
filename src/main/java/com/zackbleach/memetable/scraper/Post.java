package com.zackbleach.memetable.scraper;

public class Post {
	
	private String imageUrl;
	private int score;
	
	public Post (String imageUrl, int score) {
		this.imageUrl= imageUrl;
		this.score = score;
	}
	
	public String getImageUrl() {
		return imageUrl;
	}
	public int getScore() {
		return score;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public void setScore(int score) {
		this.score = score;
	}
	
}
