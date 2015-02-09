package com.zackbleach.memetable.utils;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import javax.imageio.ImageIO;

import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.stereotype.Component;

@Component
public class ImageUtils {

    public BufferedImage getImageFromUrl(String path) throws IOException,
            URISyntaxException {
        URL url = getUrlFromString(path);
        BufferedImage image = ImageIO.read(url);
        if (image == null) {
            throw new IllegalArgumentException(
                    "Could not read an image from the specified location");
        }
        return convertToBgr(image);
    }

    public static BufferedImage convertToBgr(BufferedImage image) {
        if (image.getType() == BufferedImage.TYPE_3BYTE_BGR) {
            return image;
        }
        BufferedImage convertedImg = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_BGR);
        convertedImg.getGraphics().drawImage(image, 0, 0, null);
        return convertedImg;
    }

    private URL getUrlFromString(String path) throws URISyntaxException,
            MalformedURLException {
        UrlValidator urlValidator = new UrlValidator();
        if (!urlValidator.isValid(path)) {
            throw new URISyntaxException(path, "URL not valid");
        }
        return new URL(path);
    }



}
