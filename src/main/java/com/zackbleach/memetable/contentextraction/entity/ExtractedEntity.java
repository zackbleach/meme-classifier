package com.zackbleach.memetable.contentextraction.entity;

import java.awt.image.BufferedImage;

public interface ExtractedEntity {

	public abstract String getName();

	public abstract BufferedImage getImage();

	public abstract String getText();

	public abstract void setName(String name);

	public abstract void setImage(BufferedImage image);

	public abstract void setText(String text);

}
