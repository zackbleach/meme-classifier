package com.zackbleach.memetable.classification.bucketer;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.List;

import net.semanticmetadata.lire.imageanalysis.LireFeature;

import com.google.common.collect.ImmutableList;
import com.zackbleach.memetable.templatescraper.Template;

/**
 * Represents a bucket which an instance of a meme can be put in to
 * A bucket is choosen based on the memefeature it has labelled itself with, in this case it
 * will be an agglomerated memefeature which is added to when a new meme is
 * thrown in
 *
 * Buckets should be ordered volumously
 */
public class Bucket {

    // Should I keep a list of all features that are in here?
    private int id;
    private String name;
    private String sourceUrl;
    private int volume;
    private BufferedImage image;
    private LireFeature memeFeature;

    public Bucket(Template template, LireFeature memeFeature) {
        this.name = template.getName();
        this.sourceUrl = template.getSourceUrl();
        this.image = toBufferedImage(template.getImage());
        this.memeFeature = memeFeature;
    }

    private BufferedImage toBufferedImage(Image img) {
        if (img instanceof BufferedImage) {
            return (BufferedImage) img;
        }

        // Create a buffered image with transparency
        BufferedImage bimage = new BufferedImage(img.getWidth(null),
                img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        // Draw the image on to the buffered image
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();

        // Return the buffered image
        return bimage;
    }

    public float getDistance(Image image) {
        BufferedImage bufferedImage = toBufferedImage(image);
        List<BufferedImage> scaledImages = scale(bufferedImage, this.image);
        LireFeature myFeature;
        LireFeature theirFeature;
        try {
            myFeature = (LireFeature) memeFeature.getClass().newInstance();
            theirFeature = (LireFeature) memeFeature.getClass().newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
            return Float.MAX_VALUE;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return Float.MAX_VALUE;
        }
        myFeature.extract(scaledImages.get(0));
        theirFeature.extract(scaledImages.get(1));
        return myFeature.getDistance(theirFeature);
    }

    private List<BufferedImage> scale(BufferedImage image1,
            BufferedImage image2) {
        int height = 0;
        if (image1.getHeight() > image2.getHeight()) {
            height = image2.getHeight();
        } else {
            height = image1.getHeight();
        }
        BufferedImage image1Scaled = net.semanticmetadata.lire.utils.ImageUtils
                .scaleImage(image1, height);
        BufferedImage image2Scaled = net.semanticmetadata.lire.utils.ImageUtils
                .scaleImage(image2, height);
        return ImmutableList.of(image1Scaled, image2Scaled);
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }
}
