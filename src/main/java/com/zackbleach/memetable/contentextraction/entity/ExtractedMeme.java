package com.zackbleach.memetable.contentextraction.entity;

import java.awt.image.BufferedImage;

public class ExtractedMeme implements ExtractedEntity {

	private String name;
	private BufferedImage image;
	private String text;
	
	/* (non-Javadoc)
	 * @see com.zackbleach.memetable.contentextraction.ExtractedEntity#getName()
	 */
	@Override
	public String getName() {
		return name;
	}
	
	/* (non-Javadoc)
	 * @see com.zackbleach.memetable.contentextraction.ExtractedEntity#getImage()
	 */
	@Override
	public BufferedImage getImage() {
		return image;
	}
	
	/* (non-Javadoc)
	 * @see com.zackbleach.memetable.contentextraction.ExtractedEntity#getText()
	 */
	@Override
	public String getText() {
		return text;
	}
	
	/* (non-Javadoc)
	 * @see com.zackbleach.memetable.contentextraction.ExtractedEntity#setName(java.lang.String)
	 */
	@Override
	public void setName(String name) {
		this.name = name;
	}
	
	/* (non-Javadoc)
	 * @see com.zackbleach.memetable.contentextraction.ExtractedEntity#setImage(java.awt.image.BufferedImage)
	 */
	@Override
	public void setImage(BufferedImage image) {
		this.image = image;
	}
	
	/* (non-Javadoc)
	 * @see com.zackbleach.memetable.contentextraction.ExtractedEntity#setText(java.lang.String)
	 */
	@Override
	public void setText(String text) {
		this.text = text;
	}
	
	
}

