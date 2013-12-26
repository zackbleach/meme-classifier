package com.zackbleach.memetable.contentextraction.extractor;

import java.awt.image.BufferedImage;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zackbleach.memetable.cache.MemeCache;
import com.zackbleach.memetable.contentextraction.Site;
import com.zackbleach.memetable.contentextraction.entity.ExtractedEntity;
import com.zackbleach.memetable.contentextraction.entity.ExtractedMeme;
import com.zackbleach.memetable.imagerecognition.Result;
import com.zackbleach.memetable.util.ImageUtils;
import com.zackbleach.memetable.util.URLUtils;

@Component
public class MemeExtractor implements Extractor {

    private static final Logger log = Logger.getLogger(MemeExtractor.class);

    //TODO: Consider refactoring this to take a URL object as a param?
    public ExtractedEntity extractEntity(String path) throws IOException,
            URISyntaxException {
        ExtractedMeme meme = new ExtractedMeme();
        URLUtils.validateUrl(path);
        log.info("Beginning meme extraction from: " + path);
        if (ImageUtils.isImage(path)) {
            meme = retrieveMemeFromImage(path);
        } else {
            meme = retrieveMemeFromHtml(path);
        }
        log.info("Rertrieved meme.... Great success");
        return meme;
    }

    /**
     * If an extraction pattern for the passed in URL exists uses that to
     * extract image. If it doesn't hands URL to extractFromUnknownSite() method
     *
     * @param path
     *            URL to extract meme from
     * @return {@link BufferedImage} Meme
     * @throws IOException
     * @throws URISyntaxException
     */
    private ExtractedMeme retrieveMemeFromImage(String path)
            throws IOException, URISyntaxException {
        log.info("Extracting meme from image");
        ExtractedMeme meme = new ExtractedMeme();
        meme.setImage(downloadImage(path));
        return meme;
    }

    private ExtractedMeme retrieveMemeFromHtml(String path) throws IOException,
            URISyntaxException {
        log.info("Extracting Meme from HTML page");
        Document doc = getDocumentFromPath(path);
        String domain = URLUtils.getDomainName(path);
        ExtractedMeme meme = new ExtractedMeme();
        Site s = Site.siteFromDomain(domain);
        if (s != null) {
            meme.setImage(extractImageFromKnownSite(s, doc));
            meme.setName(extractNameFromKnownSite(s, doc));
        } else {
            meme.setImage(extractImageFromUnknownSite(doc));
        }
        return meme;
    }

    /**
     * Returns the HTML of a given path enclosed in a {@link Document}
     *
     * @param path
     *            URL to get document from
     * @return a {@link Document}
     * @throws IOException
     */
    private Document getDocumentFromPath(String path) throws IOException {
        Document doc = Jsoup.connect(path).get();
        if (doc == null) {
            throw new IOException("Could not load document");
        }
        return doc;
    }

    /**
     * Attempts to get the name of a meme using one of the predefined extraction
     * patterns from a site
     *
     * @param site
     *            Site extraction patterns to use
     * @param doc
     *            Document to extract name from
     * @return Name of Extracted Entity
     */
    private String extractNameFromKnownSite(Site site, Document doc) {
        String name = null;
        for (String extractionPattern : site.getImageNameExtractionPatterns()) {
            Element e = doc.getElementById(extractionPattern);
            name = e.ownText();
            if (StringUtils.isNotEmpty(name)) {
                break;
            }
        }
        return name;
    }

    /**
     * see that it is an HTML document. If it is, just get the largest image on
     * If we find a site we don't have an extraction pattern for first check to
     * the page.
     *
     * @param path
     *            URL to extract meme from
     * @return {@link BufferedImage} representing the extracted meme
     * @throws IOException
     */
    private BufferedImage extractImageFromUnknownSite(Document doc)
            throws IOException {
        List<ImageIcon> images = getAllImages(doc);
        return getLargestImage(images);
    }

    private BufferedImage extractImageFromKnownSite(Site site, Document doc) throws IOException {
        BufferedImage meme = null;
        for (String s : site.getImageExtractionPatterns()) {
            Element image = doc.select(s).first();
            if (image == null) {
                return extractImageFromUnknownSite(doc);
            }
            String url = image.absUrl("src");
            log.info("Downloading image: " + url);
            meme = downloadImage(url);
        }
        return meme;
    }

    /**
     * Given a {@link Document} returns a list of images representing all images
     * on the site
     *
     * @param doc
     *            HTML Document
     * @return All images from the document
     * @throws IOException
     */
    private List<ImageIcon> getAllImages(Document doc) throws IOException {
        log.info("Unknown site, downloading all images and using biggest one");
        List<ImageIcon> images = new ArrayList<ImageIcon>();
        for (Element e : doc.select("img")) {
            images.add(new ImageIcon(downloadImage(e.attr("src"))));
        }
        return images;
    }

    /**
     * Given a list of {@link BufferedImage}, returns the largest one
     *
     * @param images
     *            List of images
     * @return the image with the biggest area
     */
    private BufferedImage getLargestImage(List<ImageIcon> images) {
        int largestImageIndex = 0;
        int largestImageSize = 0;
        int index = 0;
        for (ImageIcon i : images) {
            int currentImageSize = i.getIconHeight() * i.getIconWidth();
            if (largestImageSize < currentImageSize) {
                largestImageIndex = index;
                largestImageSize = currentImageSize;
            }
            index++;
        }
        return (BufferedImage) images.get(largestImageIndex).getImage();
    }

    // TODO: should these exceptions be here?
    private BufferedImage downloadImage(String path) {
        BufferedImage image = null;
        URL url;
        try {
            url = new URL(path);
        } catch (MalformedURLException e1) {
            log.warn("Failed to download image from path: " + path
                    + " ,invalid path");
            return null;
        }
        try {
            image = ImageIO.read(url);
        } catch (IOException e) {
            log.warn("Failed to download image from path: " + path
                    + " ,couldn't read image");
        }
        return image;
    }
}
