package com.zackbleach.memetable.scraper;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zackbleach.memetable.utils.ImageUtils;

@Component
public class QuickMemeScraper {

    @Autowired
    ImageUtils imageUtils;

    private static final Log log = LogFactory.getLog(QuickMemeScraper.class);

    private final static String quickMemeUrl = "http://www.quickmeme.com/caption";

    public List<ScrapedImage> scrapeTemplates() {
        List<ScrapedImage> templates = new ArrayList<ScrapedImage>();
        try {
            Document doc = Jsoup.connect(quickMemeUrl).get();
            Elements img = doc.getElementsByTag("img");
            for (Element el : img) {
                // for each element get the src url
                String src = el.absUrl("src");
                String name = el.attr("title");
                log.info("Found image at : " + src + " \n name is: " + name);
                // create template
                try {
                    ScrapedImage template = getScrapedMeme(src, name);
                    templates.add(template);
                } catch (URISyntaxException e) {
                    log.error("Could not download image from URL: " + src);
                }
            }
        } catch (IOException ex) {
            log.warn("Unable to download meme, " + ex);
        }
        return templates;
    }

    public List<ScrapedImage> scrapeExamples(ScrapedImage scrapedMeme) {
        List<ScrapedImage> examples = new ArrayList<ScrapedImage>();
        String examplesUrlPrefix = "http://www.quickmeme.com/";
        String memeUrl = scrapedMeme.getName().replaceAll(" ", "-");
        String fullUrl = (examplesUrlPrefix + memeUrl);
        try {
            Document doc = Jsoup.connect(fullUrl).get();
            Elements img = doc.getElementsByClass("post-image");
            int counter = 0;
            for (Element el : img) {
                // for each element get the src url
                String src = el.absUrl("src");
                String filename = (scrapedMeme.getName() + " " + counter);
                log.info("Found image at : " + src + " \n name is: " + filename);
                try {
                    ScrapedImage template = getScrapedMeme(src, filename);
                    examples.add(template);
                    counter++;
                } catch (URISyntaxException e) {
                    log.error("Could not download image from URL: " + src);
                }
            }
        } catch (IOException ex) {
            log.warn("Unable to download example, " + ex);
        }
        return examples;
    }

    private ScrapedImage getScrapedMeme(String src, String name)
            throws IOException, URISyntaxException {
        ScrapedImage template = new ScrapedImage();
        template.setImage(imageUtils.getImageFromUrl(src));
        template.setName(name);
        template.setSourceUrl(src);
        return template;
    }
}
