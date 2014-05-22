package com.zackbleach.memetable.redditscraper;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.apache.lucene.util.IOUtils;
import org.json.simple.JSONObject;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.zackbleach.memetable.contentextraction.extractor.MemeExtractor;

public class RedditScraper implements Scraper {

    private static final Logger log = Logger.getLogger(RedditScraper.class);
    private static final String ADVICE_ANIMALS = "http://www.reddit.com/r/adviceanimals.json";

    public RedditScraper() {
    }

   @Override
    public List<Post> scrape() throws JsonParseException, JsonMappingException,
            IOException {
        String json = getPage();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readValue(json, JsonNode.class);
        JsonNode data = rootNode.get("data");
        JsonNode children = data.get("children");
        List<Post> posts = new ArrayList<Post>();
        for (JsonNode n : children) {
            JsonNode submission = n.get("data");
            String url = submission.get("url").asText();
            int score = submission.get("score").asInt();
            posts.add(new Post(url, score));
            log.info("Found URL: " + url);
        }
        return posts;
    }

    private String getPage() throws IOException {
        URL url = new URL(ADVICE_ANIMALS);
        URLConnection con = url.openConnection();
        Pattern p = Pattern.compile("text/html;\\s+charset=([^\\s]+)\\s*");
        Matcher m = p.matcher(con.getContentType());
        /*
         * If Content-Type doesn't match choose default and hope for the best.
         */
        String charset = m.matches() ? m.group(1) : "ISO-8859-1";
        Reader r = new InputStreamReader(con.getInputStream(), charset);
        StringBuilder buf = new StringBuilder();
        while (true) {
            int ch = r.read();
            if (ch < 0)
                break;
            buf.append((char) ch);
        }
        String str = buf.toString();
        return str;
    }
}
