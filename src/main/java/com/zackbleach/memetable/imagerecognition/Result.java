package com.zackbleach.memetable.imagerecognition;

import java.awt.image.BufferedImage;

public class Result {

	private Meme meme;
	private float certainty;
	private BufferedImage extractedImage;
	
	public Meme getMeme() {
		return meme;
	}
	public void setMeme(Meme meme) {
		this.meme = meme;
	}
	public float getCertainty() {
		return certainty;
	}
	public void setCertainty(float certainty) {
		this.certainty = certainty;
	}
	public BufferedImage getExtractedImage() {
		return extractedImage;
	}
	public void setExtractedImage(BufferedImage extractedImage) {
		this.extractedImage = extractedImage;
	}
	
}
