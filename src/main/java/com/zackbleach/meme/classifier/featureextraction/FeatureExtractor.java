package com.zackbleach.meme.classifier.featureextraction;

import java.awt.image.BufferedImage;

import net.semanticmetadata.lire.imageanalysis.LireFeature;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zackbleach.meme.classifier.index.Index;

@Component
public class FeatureExtractor {

    @Autowired
    public Class<? extends LireFeature> featureExtractionMethod;

    public LireFeature extractFeatures(BufferedImage image) {
        LireFeature feature;
        try {
            feature = featureExtractionMethod.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            // if this happens, we've got issues
            throw new RuntimeException();
        }
        feature.extract(image);
        return feature;
    }
}
