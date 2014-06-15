package com.zackbleach.memetable.classification.classifier;

import java.awt.Image;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zackbleach.memetable.classification.bucketer.Bucket;
import com.zackbleach.memetable.classification.bucketer.Bucketer;
@Component
public class Classifier {

    @Autowired
    Bucketer bucketer;

    public Classification classify(String path) throws IOException {
        URL url = new URL(path);
        Image image = ImageIO.read(url);
        return classify(image);
    }

    public Classification classify(Image image) {
        Classification classification = new Classification();
        Bucket closest = null;
        float minDistance = Float.MAX_VALUE;
        for (Bucket b : bucketer.getBuckets()) {
            float distance = b.getDistance(image);
            if (distance < minDistance) {
                closest = b;
                minDistance = distance;
            }
        }
        classification.setMemeName(closest.getName());
        classification.setDistance(minDistance);
        return classification;
    }
}
