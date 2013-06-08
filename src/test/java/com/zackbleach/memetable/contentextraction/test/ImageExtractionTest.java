package com.zackbleach.memetable.contentextraction.test;

import static com.zackbleach.memetable.util.ImageScrapeUtils.getImageFromSite;

import java.awt.Image;
import java.io.IOException;
import java.net.URISyntaxException;

import junit.framework.Assert;

import org.junit.Test;

import com.zackbleach.memetable.contentextraction.MemeExtractor;
import com.zackbleach.memetable.util.ImageViewer;

public class ImageExtractionTest {
	
	final static String IMAGE = "http://resme.me/3ucjh8.jpg";
	final static String QUICKMEME = "http://www.quickmeme.com/meme/3uazvf/#by=ad";
	final static String IMGUR = "http://imgur.com/r/meme/kFA95Ro";
	final static String LIVEMEME = "http://www.livememe.com/509nrs1";
	final static String MEMECREATOR = "http://www.memecreator.org/meme/find-out-theres-a-forever-alone-tiny-chat-way-too-nervous-to-get-in-the-convo-or/";
	final static String UNKNOWN_SITE_1 = "http://pulse2.com/2012/07/15/breaking-bad-meme/22/";
	final static String UNKNOWN_SITE_2 = "http://cheezburger.com/4863560192";
	final static String UNKNOWN_SITE_3 = "http://knowyourmeme.com/memes/y-u-no-guy";
	final static String BAD_URL = "aaaaaaa";

	@Test
	public void imageDownload() throws IOException, URISyntaxException{
		Image meme = getImageFromSite(IMAGE);
		Assert.assertNotNull(meme);
	}
	
	@Test 
	public void extractFromQuickMeme() throws IOException, URISyntaxException {
		Image meme = getImageFromSite(QUICKMEME);
		Assert.assertNotNull(meme);
	}
	
	@Test 
	public void extractFromImgur() throws IOException, URISyntaxException {
		Image meme = getImageFromSite(IMGUR);
		Assert.assertNotNull(meme);
	}
	
	@Test 
	public void extractFromLiveMeme() throws IOException, URISyntaxException {
		Image meme = getImageFromSite(LIVEMEME);
		Assert.assertNotNull(meme);
	}
	
	@Test 
	public void extractFromMemecreator() throws IOException, URISyntaxException {
		Image meme = getImageFromSite(MEMECREATOR);
		Assert.assertNotNull(meme);
	}
	
	@Test
	public void extractFromUnknownSite1() throws IOException, URISyntaxException {
		Image meme = getImageFromSite(UNKNOWN_SITE_1);
		Assert.assertNotNull(meme);
	}
	
	@Test 
	public void extractFromUnknownSite2() throws IOException, URISyntaxException {
		Image meme = getImageFromSite(UNKNOWN_SITE_2);
		Assert.assertNotNull(meme);
	}
	
	@Test 
	public void extractFromUnknownSite3() throws IOException, URISyntaxException {
		Image meme = getImageFromSite(UNKNOWN_SITE_3);
		Assert.assertNotNull(meme);
	}
	

	@Test (expected = URISyntaxException.class)
	public void extractFromBadUrl() throws IOException, URISyntaxException {
		Image meme = getImageFromSite(BAD_URL);
		Assert.assertNotNull(meme);
	}
	
	public static void main(String[] args) throws IOException, URISyntaxException {
		//main method to allow us to see the image downloaded
		MemeExtractor scraper = new MemeExtractor();
		final Image meme = scraper.extractMeme("");
		new ImageViewer(meme);
	}
}
