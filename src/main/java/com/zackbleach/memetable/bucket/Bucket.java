package com.zackbleach.memetable.bucket;

import java.awt.image.BufferedImage;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.zackbleach.memetable.classification.featureextraction.MemeFeature;

/**
 * Represents a bucket which an instance of a meme can be put in to A bucket is
 * choosen based on the memefeature it has labelled itself with, in this case it
 * will be an agglomerated memefeature which is added to when a new meme is
 * thrown in
 *
 * Buckets should be ordered volumously
 */
@Entity
@Table(name = "buckets")
public class Bucket {

    // Should I keep a list of all features that are in here?
    private int id;
    private double[] prototype;
    private String name;
    private int volume;
    private String imagePath;

    public Bucket() {

    }

    @Id
    @GeneratedValue
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "prototype", nullable = false)
    public double[] getPrototype() {
        return prototype;
    }

    public void setPrototype(double[] feature) {
        this.prototype = feature;
    }

    @Column(name = "name", nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "volume", nullable = false)
    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    @Column(name = "image_path", nullable = false)
    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
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
        if (prototype == null) {
            if (other.prototype != null)
                return false;
        } else if (!prototype.equals(other.prototype))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((prototype == null) ? 0 : prototype.hashCode());
        return result;
    }

}
