package com.zackbleach.memetable.util;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.google.common.collect.ImmutableList;

@Component
public class ImageUtils {

    public Image getImageFromUrl(String url) throws IOException {
        URL imageUrl = new URL(url);
        Image image = ImageIO.read(imageUrl);
        return image;
    }

    public List<BufferedImage> scale(BufferedImage image1,
            BufferedImage image2) {
        int height = 0;
        if (image1.getHeight() > image2.getHeight()) {
            height = image2.getHeight();
        } else {
            height = image1.getHeight();
        }
        BufferedImage image1Scaled = net.semanticmetadata.lire.utils.ImageUtils
                .scaleImage(image1, height);
        BufferedImage image2Scaled = net.semanticmetadata.lire.utils.ImageUtils
                .scaleImage(image2, height);
        return ImmutableList.of(image1Scaled, image2Scaled);
    }

    public boolean isImage(String path) throws IOException {
        return checkContentType(path) || checkExtension(path);
    }

    private boolean checkExtension(String path) {
        boolean validImageExtension = false;
        for (String s : ImageIO.getReaderFormatNames()) {
            if (path.endsWith(s)) {
                validImageExtension = true;
                break;
            }
        }
        return validImageExtension;
    }

    private boolean checkContentType(String path)
            throws MalformedURLException, IOException, ProtocolException {
        URL url = new URL(path);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("HEAD");
        connection.connect();
        return connection.getContentType().contains("image");
    }

    public BufferedImage toBufferedImage(Image img) {
        if (img instanceof BufferedImage) {
            return (BufferedImage) img;
        }

        // Create a buffered image with transparency
        BufferedImage bimage = new BufferedImage(img.getWidth(null),
                img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        // Draw the image on to the buffered image
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();

        // Return the buffered image
        return bimage;
    }


}
