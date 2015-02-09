package com.zackbleach.memetable.scraper;

import java.awt.image.BufferedImage;

public class ScrapedImage {

    private BufferedImage image;
    private String name;
    private String sourceUrl;

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
