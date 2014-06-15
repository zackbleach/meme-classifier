package com.zackbleach.memetable.templatescraper;

import java.awt.Image;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

@Component
public class QuickMemeTemplateScraper {
    private static final Log log = LogFactory.getLog(QuickMemeTemplateScraper.class);

    private final static String quickMemeUrl = "http://www.quickmeme.com/caption";

    public List<Template> scrape() {
        List<Template> templates = new ArrayList<Template>();
        try {
            // Connect to the website and get the html
            Document doc = Jsoup.connect(quickMemeUrl).get();
            // Get all elements with img tag
            Elements img = doc.getElementsByTag("img");
            for (Element el : img) {
                // for each element get the src url
                String src = el.absUrl("src");
                String name = el.attr("title");
                log.info("Found image at : " + src + " \n name is: " + name);
                // create template
                Template template = getTemplate(src, name);
                templates.add(template);
            }
        } catch (IOException ex) {
            log.warn("Unable to download meme, " + ex);
        }
        return templates;
    }

    private Template getTemplate(String src, String name) throws IOException {
        Template template = new Template();
        template.setImage(getImageFromUrl(src));
        template.setName(name);
        template.setSourceUrl(src);
        return template;
    }

    private Image getImageFromUrl(String url) throws IOException {
        URL imageUrl = new URL(url);
        Image image = ImageIO.read(imageUrl);
        return image;
    }
}
