package com.zackbleach.memetable.classification.bucketer;

import java.awt.Image;
import java.awt.image.BufferedImage;

import net.semanticmetadata.lire.imageanalysis.LireFeature;

import com.zackbleach.memetable.classification.featureextraction.MemeFeature;
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
    private int volume;
    private MemeFeature memeFeature;

    public Bucket(Template template, MemeFeature memeFeature) {
        this.name = template.getName();
        this.memeFeature = memeFeature;
        memeFeature.extract((BufferedImage) template.getImage());
    }

    public float getDistance(Image image) {
        MemeFeature newFeature;
        try {
            newFeature = (MemeFeature) memeFeature.getClass().newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
            return Float.MAX_VALUE;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return Float.MAX_VALUE;
        }
        newFeature.extract((BufferedImage)image);
        return memeFeature.getDistance((LireFeature)newFeature);
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

    @Override
    // TODO: could add name to this as well? - If I do
    // remember to change the hashcoded method
    // TODO: probably change this - not sure if protoype
    // comparison is correct
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Bucket other = (Bucket) obj;
        if (memeFeature == null) {
            if (other.memeFeature != null)
                return false;
        } else if (!memeFeature.getDoubleHistogram().equals(other.memeFeature.getDoubleHistogram()))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((memeFeature.getDoubleHistogram() == null) ? 0 : memeFeature.getDoubleHistogram().hashCode());
        return result;
    }

}
