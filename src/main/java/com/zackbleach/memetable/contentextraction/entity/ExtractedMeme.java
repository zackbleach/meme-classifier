package com.zackbleach.memetable.contentextraction.entity;

import java.awt.image.BufferedImage;

public class ExtractedMeme implements ExtractedEntity {

    private String name;
    private BufferedImage image;
    private String text;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public BufferedImage getImage() {
        return image;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setImage(BufferedImage image) {
        this.image = image;
    }

    @Override
    public void setText(String text) {
        this.text = text;
    }

}
