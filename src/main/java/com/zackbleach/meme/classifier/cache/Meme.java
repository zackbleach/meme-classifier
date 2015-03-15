package com.zackbleach.meme.classifier.cache;

import java.awt.image.BufferedImage;

public class Meme {

    private BufferedImage image;
    private String name;
    private String sourceUrl;

    public Meme(String name, String sourceUrl, BufferedImage image) {
        this.name = name;
        this.sourceUrl = sourceUrl;
        this.image = image;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

}
