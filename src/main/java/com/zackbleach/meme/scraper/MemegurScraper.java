
package com.zackbleach.meme.scraper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MemegurScraper implements Scraper {

    private static final Log log = LogFactory.getLog(MemegurScraper.class);

    private final static String MEMEGUR_CODE_URL = "http://imgur.com/memegen";
    private final static String MEMEGUR_EXAMPLE_API_URL = "http://imgur.com/memegen/examples/";
    private final static String MEMEGUR_EXAMPLE_URL = "http://imgur.com/";
    private final static String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:5.0) Gecko/20100101 Firefox/5.0";


    public List<ScrapedImage> scrape() {
        List<ScrapedImage> memes = new ArrayList<ScrapedImage>();
        Document doc;
        try {
            doc = Jsoup.connect(MEMEGUR_CODE_URL).userAgent(USER_AGENT).get();
            Elements img = doc.select("div[class=item center default]");
            for (Element e : img) {
                String code = e.attr("data-value");
                String name = e.attr("data-name");
                List<Example> examples = getExamples(code);
                for (Example example : examples) {
                    String url = MEMEGUR_EXAMPLE_URL+example.getHash()+".png";
                    log.info("Found: " + name + " at url: " + url);
                    memes.add(new ScrapedImage(name, url));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return memes;
    }

    public List<Example> getExamples(String code) {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(MEMEGUR_EXAMPLE_API_URL+code);
        Data data = target.request().get(Data.class);
        return data.getExample();
    }

    @JsonIgnoreProperties(ignoreUnknown=true)
    private static class Data {

        @JsonProperty(value="data")
        private List<Example> example;

        public List<Example> getExample(){
            return example;
        }

        public void setExample(List<Example> example) {
            this.example = example;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown=true)
    private static class Example {

        private String hash;

        public String getHash() {
            return hash;
        }

        public void setHash(String hash) {
            this.hash = hash;
        }
    }
}
