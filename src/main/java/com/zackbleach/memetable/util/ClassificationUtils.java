package com.zackbleach.memetable.util;

import java.awt.image.BufferedImage;

import org.springframework.stereotype.Component;

import com.zackbleach.memetable.classification.featureextraction.CEDD;

@Component
public class ClassificationUtils {

    public static float getCEDDDistance(BufferedImage image1,
            BufferedImage image2) {
        CEDD cedd = new CEDD();
        cedd.extract(image1);
        CEDD newCedd = new CEDD();
        newCedd.extract(image2);
        return cedd.getDistance(newCedd);
    }
}
