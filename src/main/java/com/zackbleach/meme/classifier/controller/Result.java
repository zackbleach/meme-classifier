package com.zackbleach.meme.classifier.controller;

public class Result {

    private float distance;
    private String name;

    public Result(float distance, String name) {
        this.distance = distance;
        this.name = name;
    }

    public float getDistance() {
        return distance;
    }

    public String getName() {
        return name;
    }
}
