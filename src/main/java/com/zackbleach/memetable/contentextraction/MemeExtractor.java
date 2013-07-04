package com.zackbleach.memetable.contentextraction;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.validator.routines.UrlValidator;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.jsoup.Jsoup;
import org.jsoup.UnsupportedMimeTypeException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.zackbleach.memetable.util.ImageUtils;
import com.zackbleach.memetable.util.URLUtils;

public class MemeExtractor {

	private static final Logger log = Logger.getLogger(MemeExtractor.class);
	
	/**
	 * Attempts to extract a Meme from a valid URL
	 * @param path Path to extract Meme from 
	 * @return {@link BufferedImage} Meme.
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public ExtractedMeme extractMeme(String path) throws IOException, URISyntaxException {
		URLUtils.validateUrl(path);
		log.info("Beginning meme extraction from: " + path);
		ExtractedMeme meme = new ExtractedMeme();
		if (ImageUtils.isImage(path)) {
			log.info("Found path to image");
			meme.setImage(downloadImage(path));
		} else {
			log.info("Found path to HTML page");
			meme = retrieveMemeFromHtml(path);
		}
		if (meme != null) {
			log.info("Rertrieved meme.... Great success");
		}
		return meme;
	}
	
	/**
	 * If an extraction pattern for the passed in URL exists
	 * uses that to extract image. If it doesn't hands URL to 
	 * extractFromUnknownSite() method
	 * @param path URL to extract meme from
	 * @return {@link BufferedImage} Meme
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	private ExtractedMeme retrieveMemeFromHtml(String path) throws IOException, URISyntaxException {
		Document doc = getDocumentFromPath(path);
		String domain = URLUtils.getDomainName(path);
		ExtractedMeme meme = new ExtractedMeme();
		Site s = Site.siteFromDomain(domain);
		if (s != null) {
			meme.setImage(extractImageFromKnownSite(s, doc));
			meme.setName(extractNameFromKnownSite(s, doc));
		} else {
			meme.setImage(extractFromUnknownSite(doc));
		}
		return meme;
	}

	/**
	 * Returns the HTML of a given path enclosed in a {@link Document}
	 * @param path URL to get document from
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
	 * Downloads the HTML source and then uses the provided pattern
	 * so retrieve the URL to the image. The image is then downloaded
	 * and returned.
	 * @param path URL to extract meme from
	 * @param pattern RegEx used to match meme location
	 * @return {@link BufferedImage} representing the extracted meme
	 * @throws IOException
	 */
	private BufferedImage extractImageFromKnownSite(Site site, Document doc) throws IOException {
		BufferedImage meme = null;
		for (String s : site.getImageExtractionPatterns()) {
			Element image = doc.select(s).first();
			//TODO: tidy up
			if (image == null) {
				return extractFromUnknownSite(doc);
			}
			String url = image.absUrl("src");
			try {
				log.info("Downloading image: " + url);
				meme = downloadImage(url);
				break;
			} catch (IOException e) {
				log.warn("Failed to get image from url: " + url);
			}
		}
		return meme;
	}
	
	private String extractNameFromKnownSite(Site site, Document doc) {
		String name = null;
		for (String s: site.getImageNameExtractionPatterns()) {
			Element e = doc.getElementById("meme_name");		
			name = e.ownText();
			if (StringUtils.isNotEmpty(name)) {
				break;
			}
		}
		return name;
	}

	/**
	 * If we find a site we don't have an extraction pattern for first check to see
	 * that it is an HTML document. If it is, just get the largest image on the page. 
	 * @param path URL to extract meme from
	 * @return {@link BufferedImage} representing the extracted meme
	 * @throws IOException
	 */
	private BufferedImage extractFromUnknownSite(Document doc) throws IOException {
		List<ImageIcon> images = getAllImages(doc);
		return getLargestImage(images);
	}

	
	/**
	 * Given a {@link Document} returns a list of images representing
	 * all images on the site
	 * @param doc HTML Document
	 * @return All images from the document
	 * @throws IOException
	 */
	private List<ImageIcon> getAllImages(Document doc) throws IOException {
		log.info("Unknown site, downloading all images and using biggest one");
		List<ImageIcon> images = new ArrayList<ImageIcon>();
		for (Element e : doc.select("img")) {
			try {
				images.add(new ImageIcon(downloadImage(e.attr("src"))));
			} catch (MalformedURLException mue) {
				log.info("Couldn't get image :" + e.attr("src") + "\nSkipping...");
			}
		}
		return images;
	}
	
	/**
	 * Given a list of {@link BufferedImage}, returns the largest one
	 * @param images List of images
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

	private BufferedImage downloadImage(String path) throws IOException {
		BufferedImage image = null;
		URL url = new URL(path);
		image = ImageIO.read(url);
		return image;
	}
}
