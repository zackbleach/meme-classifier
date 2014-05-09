package com.zackbleach.memetable.classification.classifier;

public class Classification {

    private String memeName;
    private float distance;

    public String getMemeName() {
        return memeName;
    }

    public void setMemeName(String memeName) {
        this.memeName = memeName;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float confidence) {
        this.distance = confidence;
    }
}
