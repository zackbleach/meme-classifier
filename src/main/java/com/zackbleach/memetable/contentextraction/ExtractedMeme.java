package com.zackbleach.memetable.contentextraction;

import java.awt.image.BufferedImage;

public class ExtractedMeme {

	private String name;
	private BufferedImage image;
	private String text;
	
	public String getName() {
		return name;
	}
	
	public BufferedImage getImage() {
		return image;
	}
	
	public String getText() {
		return text;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setImage(BufferedImage image) {
		this.image = image;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	
}

