package com.zackbleach.memetable.featureextraction;

import java.awt.image.BufferedImage;

import net.semanticmetadata.lire.imageanalysis.ColorLayout;
import net.semanticmetadata.lire.imageanalysis.LireFeature;

import org.springframework.stereotype.Component;

import com.zackbleach.memetable.bucketer.Index;

@Component
public class FeatureExtractor {

    public LireFeature extractFeatures(BufferedImage image)
            throws InstantiationException, IllegalAccessException {
        LireFeature feature = Index.FEATURE_EXTRACTION_METHOD.newInstance();
        feature.extract(image);
        return feature;
    }
}
