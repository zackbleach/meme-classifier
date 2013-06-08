package com.zackbleach.memetable.contentextraction;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import org.apache.commons.validator.routines.UrlValidator;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.UnsupportedMimeTypeException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class MemeExtractor {
	
	//TODO: Take out references to ImageIcon?
	//TODO: Get more clues (Title DIV on QuickMeme?)
	//TODO: Extraction patterns should be in an Enum?

	private static final Logger log = Logger.getLogger(MemeExtractor.class);

	private static final String QUICKMEME_MEME_PATTERN = "img[id$=img]";
	private static final String IMGUR_MEME_PATTERN = "div.image img";
	private static final String LIVEMEME_MEME_PATTERN = "img[id$=memeImage]";
	private static final String MEMECREATOR_MEME_PATTERN = "div.left img";

	public MemeExtractor() {
	}

	public BufferedImage extractMeme(String path) throws IOException,
			URISyntaxException {
		validateUrl(path);
		log.info("Beginning meme extraction from: " + path);
		boolean isImage = isImage(path);
		BufferedImage image = null;
		if (isImage) {
			log.info("Found path to image");
			image = downloadImage(path);
		} else {
			log.info("Found path to HTML page");
			image = retrieveImageFromHtml(path);
		}
		log.info("Rertrieved meme.... Great success");
		return image;
	}

	private BufferedImage retrieveImageFromHtml(String path)
			throws IOException, URISyntaxException {
		String domain = getDomainName(path);
		BufferedImage meme = null;
		if (domain.equals("quickmeme") || domain.equals("qkme")) {
			meme = extractFromKnownSite(path, QUICKMEME_MEME_PATTERN);
		} else if (domain.equals("imgur")) {
			meme = extractFromKnownSite(path, IMGUR_MEME_PATTERN);
		} else if (domain.equals("livememe")) {
			meme = extractFromKnownSite(path, LIVEMEME_MEME_PATTERN);
		} else if (domain.equals("memecreator")) {
			meme = extractFromKnownSite(path, MEMECREATOR_MEME_PATTERN);
		} else {
			meme = extractFromUnknownSite(path);
		}
		return meme;
	}

	private BufferedImage extractFromKnownSite(String path, String pattern) throws IOException {
		Document doc = Jsoup.connect(path).execute().parse();
		Element image = doc.select(pattern).first();
		String url = image.absUrl("src");
		log.info("Downloading image: " + url);
		return downloadImage(url);
	}

	/**
	 * If we find a site we don't have an extraction pattern for first check to see
	 * that it is an HTML document. If it isn't, try to download it as an image. If
	 * it is, just get the largest image on the page. 
	 */
	private BufferedImage extractFromUnknownSite(String path) throws IOException {
		Document doc = null;
		try {
			doc = Jsoup.connect(path).execute().parse();
		} catch (UnsupportedMimeTypeException e) {
			return downloadImage(path);
		}
		if (doc == null) {
			throw new IOException("Could not load document");
		}
		List<ImageIcon> images = getAllImages(doc);
		return getLargestImage(images);
	}

	private List<ImageIcon> getAllImages(Document doc)
			throws IOException {
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

	private BufferedImage downloadImage(String path) throws IOException {
		BufferedImage image = null;
		URL url = new URL(path);
		image = ImageIO.read(url);
		return image;
	}

	private void validateUrl(String path) throws URISyntaxException {
		String[] schemes = { "http", "https" };
		UrlValidator urlValidator = new UrlValidator(schemes);
		if (!urlValidator.isValid(path)) {
			log.warn("Invalid URL: " + path);
			throw new URISyntaxException(path, "Invalid URL");
		}
	}

	private String getDomainName(String url) throws URISyntaxException {
		URI uri = new URI(url);
		String domain = uri.getHost();
		domain = domain.startsWith("www.") ? domain.substring(4) : domain;
		String[] parts = domain.split("\\.");
		return parts[0];
	}

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
	
	private String getContentType(String path) throws IOException{
		URL url = new URL(path);
		HttpURLConnection connection = (HttpURLConnection)  url.openConnection();
		connection.setRequestMethod("HEAD");
		connection.connect();
		return connection.getContentType();
		
	}
	
	private boolean isImage(String path) {
	    return isImageFromContentTypeHeader(path) 
	    		|| isImageFromExtension(path);
	}

	private boolean isImageFromExtension(String path) {
		for (String s : ImageIO.getReaderFormatNames()) {
			if (path.endsWith(s)) {
				return true;
			}
		}
		return false;
	}

	private boolean isImageFromContentTypeHeader(String path) {
		try {
			String contentType = getContentType(path);
			if (contentType.toLowerCase().contains("image")) {
				return true;
			}
		} catch (IOException e) {
			log.error("Failed to retrieve content type for URL: " + path);
		}
		return false;
	}
}
