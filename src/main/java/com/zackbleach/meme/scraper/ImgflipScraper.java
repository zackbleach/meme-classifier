package com.zackbleach.meme.scraper;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ImgflipScraper implements Scraper {
    private static final Log logger = LogFactory.getLog(ImgflipScraper.class);

    public final String IMGFLIP_URL = "http://imgflip.com/memetemplates?page=";

    private Map<String, String> getMemes() {
        Map<String, String> memePages = new HashMap<String, String>();
        int pageNo = 1;
        boolean hasMemes = true;
        try {
            while (hasMemes) {
                Document doc = getPage(IMGFLIP_URL + pageNo);
                Elements memes = doc.select(".mt-img-wrap a ");
                for (Element el : memes) {
                    memePages.put(el.attr("title"), el.absUrl("href"));
                }
                pageNo++;
                hasMemes = !memes.isEmpty();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Found " + memePages.size() + " memes");
        return memePages;
    }

    public List<ScrapedImage> scrape() {
        List<ScrapedImage> images = new ArrayList<ScrapedImage>();
        Map<String, String> memes = getMemes();
        for (String name : memes.keySet()) {
            try {
                Document memePage = getPage(memes.get(name));
                Elements imgl = memePage.getElementsByClass("base-img");
                for (Element img : imgl) {
                    String src = img.absUrl("src");
                    if (!src.isEmpty()) {
                        logger.info(name + " found at: " + src);
                        images.add(getScrapedMeme(src, deleteLastWord(name)));
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
        return images;
    }

    private String deleteLastWord(String sentence) {
        return sentence.replaceAll(" [^ ]+$", "");
    }

    private ScrapedImage getScrapedMeme(String src, String name)
            throws IOException, URISyntaxException {
        ScrapedImage template = new ScrapedImage();
        template.setName(name);
        template.setSourceUrl(src);
        return template;
    }

    private Document getPage(String url) throws IOException {
        logger.warn("Getting URL: " + url);
        Document document = Jsoup
                .connect(url)
                .userAgent(
                        "Mozilla/5.0 (Windows NT 6.0) AppleWebKit/536.5 (KHTML, like Gecko) Chrome/19.0.1084.46 Safari/536.5")
                .ignoreHttpErrors(true).get();
        return document;

    }

    public static void main(String[] args) {
        ImgflipScraper s = new ImgflipScraper();
        s.scrape();
    }
}
