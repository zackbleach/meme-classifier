package com.zackbleach.memetable.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.google.common.collect.ImmutableList;
import com.zackbleach.memetable.contentextraction.extractor.Extractor;
import com.zackbleach.memetable.contentextraction.extractor.MemeExtractor;

public class ImageUtils {

    private static final Logger log = Logger.getLogger(ImageUtils.class);

    public static BufferedImage getImageFromSite(String path)
            throws IOException, URISyntaxException {
        Extractor ext = new MemeExtractor();
        return ext.extractEntity(path).getImage();
    }

    public static boolean saveImage(String path, String folder) {
        Extractor ext = new MemeExtractor();
        try {
            saveImage(ext.extractEntity(path).getImage(), path, folder);
        } catch (IOException ioe) {
            log.warn("Problem saving image: " + path, ioe);
        } catch (URISyntaxException urie) {
            log.warn("Problem saving image: " + path, urie);
        }
        return false;
    }

    public static boolean saveImage(BufferedImage image, String path,
            String folder) {
        if (image == null) {
            return false;
        }
        try {
            String[] parts = path.split("/");
            File file = new File(folder + parts[parts.length - 1]);
            if (!file.exists()) {
                ImageIO.write(image, "jpg", file);
                log.warn("Saved file to disk: " + file.getName());
                return true;
            } else {
                // already saved
                return true;
            }
        } catch (IOException ioe) {
            log.warn("Problem saving image: " + path, ioe);
        }
        return false;
    }

    public static List<BufferedImage> readImagesFromFolder(String path)
            throws IOException {
        List<BufferedImage> images = new ArrayList<BufferedImage>();
        log.info("Reading images from: " + path);
        File folder = new File(path);
        if (folder.listFiles() == null) {
            return images;
        }
        for (File file : folder.listFiles()) {
            if (file.isDirectory()) {
                readImagesFromFolder(file.getAbsolutePath());
            } else if (!file.isHidden()) {
                images.add(ImageIO.read(file));
            }
        }
        return images;
    }

    public static List<BufferedImage> scale(BufferedImage image1,
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

    public static boolean isImage(String path) throws IOException {
        return checkContentType(path) || checkExtension(path);
    }

    private static boolean checkExtension(String path) {
        boolean isImage = false;
        for (String s : ImageIO.getReaderFormatNames()) {
            if (path.endsWith(s)) {
                isImage = true;
                break;
            }
        }
        return isImage;
    }

    private static boolean checkContentType(String path)
            throws MalformedURLException, IOException, ProtocolException {
        URL url = new URL(path);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("HEAD");
        connection.connect();
        return connection.getContentType().contains("image");
    }
}
