package com.zackbleach.memetable.bucket;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Level;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class QuickMemeScraper {
    private static final Log log =
        LogFactory.getLog(QuickMemeScraper.class);


    //The url of the website. This is just an example
    private static final String webSiteURL = "http://www.quickmeme.com/caption";

    //The path of the folder that you want to save the images to
    private static final String folderPath = "DownloadedTemplates/";

    public static void scrape() {
        try {
            //Connect to the website and get the html
            Document doc = Jsoup.connect(webSiteURL).get();
            //Get all elements with img tag
            Elements img = doc.getElementsByTag("img");
            for (Element el : img) {
                //for each element get the srs url
                String src = el.absUrl("src");
                String name = el.attr("title");
                log.warn("Image Found!");
                log.warn("src attribute is : "+src);
                getImages(src, name);
            }
        } catch (IOException ex) {
            log.warn("Unable to download meme, " + ex);
        }
    }

    private static void getImages(String src, String name) throws IOException {
        log.warn("Found an image" + name);

        //Open a URL Stream
        URL url = new URL(src);
        InputStream in = url.openStream();

        OutputStream out = new BufferedOutputStream(new FileOutputStream( folderPath+ name));

        for (int b; (b = in.read()) != -1;) {
            out.write(b);
        }
        out.close();
        in.close();
    }
}
