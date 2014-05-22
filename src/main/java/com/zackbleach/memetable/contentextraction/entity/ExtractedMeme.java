package com.zackbleach.memetable.contentextraction.entity;

import java.awt.image.BufferedImage;

public class ExtractedMeme {

    private String name;
    private BufferedImage image;

    public String getName() {
        return name;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }
}
