package com.zackbleach.meme.classifier.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
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
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection
                .setRequestProperty(
                        "User-Agent",
                        "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_5) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.65 Safari/537.31");
       BufferedImage image = ImageIO.read(connection.getInputStream());
        if (image == null) {
            throw new IllegalArgumentException(
                    "Could not read an image from the specified location");
        }
        return convertToBgr(image);
    }

    public BufferedImage convertToBgr(BufferedImage image) {
        if (image.getType() == BufferedImage.TYPE_3BYTE_BGR) {
            return image;
        }
        BufferedImage convertedImg = new BufferedImage(image.getWidth(),
                image.getHeight(), BufferedImage.TYPE_INT_BGR);
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

    public void saveToDisk(String path, BufferedImage image) throws IOException {
        File file = new File(path);
        file.getParentFile().mkdirs();
        file.createNewFile();
        ImageIO.write(image, "jpg", file);
    }

    public BufferedImage readFromDisk(String path) throws IOException {
        File imageFile = new File(path);
        BufferedImage image = ImageIO.read(imageFile);
        if (image == null) {
            throw new IllegalArgumentException(
                    "Could not read an image from the specified location");
        }
        return convertToBgr(image);

    }

}
