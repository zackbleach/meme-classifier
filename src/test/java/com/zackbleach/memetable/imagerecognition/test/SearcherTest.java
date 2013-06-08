package com.zackbleach.memetable.imagerecognition.test;

import static com.zackbleach.memetable.util.ClassificationUtils.classifyMemeFromPath;
import static com.zackbleach.memetable.util.ImageUtils.getImageFromSite;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.Assert;
import org.junit.Test;

import com.zackbleach.memetable.contentextraction.MemeExtractor;
import com.zackbleach.memetable.imagerecognition.Meme;
import com.zackbleach.memetable.imagerecognition.Searcher;
import com.zackbleach.memetable.util.ImageViewer;

public class SearcherTest {
	
	//TODO: Save these test images locally
	public static final String PHILOSORAPTOR_IMAGE = "http://25.media.tumblr.com/tumblr_lsvqs4bOBT1r4yxoho1_500.jpg";
	public static final String INTERNET_WIFE_IMAGE = "http://29.media.tumblr.com/tumblr_lrmu85jCWf1qb5gkjo1_400.jpg";
	public static final String INSANITY_WOLF_IMAGE = "https://i.chzbgr.com/maxW500/7027276544/h190097E5/";
	
	@Test
	public void accuracyPhilosoraptor() throws IOException, URISyntaxException {
		Assert.assertTrue(classifyMemeFromPath(PHILOSORAPTOR_IMAGE).getMeme() == Meme.PHILOSORAPTOR);
	}
	
	@Test
	public void accuracyInternetWife() throws IOException, URISyntaxException {
		Assert.assertTrue(classifyMemeFromPath(INTERNET_WIFE_IMAGE).getMeme() == Meme.INTERNET_WIFE);
	}
	
	@Test
	public void accuracyInsanityWolf() throws IOException, URISyntaxException {
		Assert.assertTrue(classifyMemeFromPath(INSANITY_WOLF_IMAGE).getMeme() == Meme.INSANITY_WOLF);
	}

	public static void main(String[] args) throws IOException, URISyntaxException {
		//main method to allow us to see the image downloaded
		MemeExtractor scraper = new MemeExtractor();
		BufferedImage meme = getImageFromSite(PHILOSORAPTOR_IMAGE);
		new ImageViewer(meme);
	}
	
}
