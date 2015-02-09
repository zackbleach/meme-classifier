package com.zackbleach.memetable.controller;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.when;

import java.awt.image.BufferedImage;

import net.semanticmetadata.lire.imageanalysis.LireFeature;

import org.junit.Before;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.zackbleach.memetable.bucketer.Index;
import com.zackbleach.memetable.featureextraction.FeatureExtractor;

public class MemeClassificationControllerTest {

    @Mock
    Index bucketer;

    @Mock
    FeatureExtractor featureExtractor;

    @Mock
    LireFeature feature;

    @InjectMocks
    MemeClassificationController controller;

    @Before
    public void setup() {
       when(featureExtractor.extractFeatures( (BufferedImage) anyObject())).thenReturn(feature);
    }

}
