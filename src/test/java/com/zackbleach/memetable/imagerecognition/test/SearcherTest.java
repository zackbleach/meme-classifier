package com.zackbleach.memetable.imagerecognition.test;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URISyntaxException;

import net.semanticmetadata.lire.DocumentBuilder;
import net.semanticmetadata.lire.ImageSearchHits;

import org.junit.Assert;
import org.junit.Test;

import com.zackbleach.memetable.contentextraction.MemeExtractor;
import com.zackbleach.memetable.imagerecognition.Searcher;
import com.zackbleach.memetable.util.ImageViewer;

import static com.zackbleach.memetable.util.ClassificationUtils.getImageFromSite;
import static com.zackbleach.memetable.util.ClassificationUtils.classifyMeme;

public class SearcherTest {
	
	//TODO: Save these test images locally
	public static final String PHILOSORAPTOR_IMAGE = "http://25.media.tumblr.com/tumblr_lsvqs4bOBT1r4yxoho1_500.jpg";
	public static final String INTERNET_WIFE_IMAGE = "http://29.media.tumblr.com/tumblr_lrmu85jCWf1qb5gkjo1_400.jpg";
	public static final String INSANITY_WOLF_IMAGE = "https://i.chzbgr.com/maxW500/7027276544/h190097E5/";
	
	@Test
	public void accuracyPhilosoraptor() throws IOException, URISyntaxException {
		Assert.assertTrue(classifyMeme(PHILOSORAPTOR_IMAGE).equals(Searcher.PHILOSORAPTOR));
	}
	
	@Test
	public void accuracyInternetWife() throws IOException, URISyntaxException {
		Assert.assertTrue(classifyMeme(INTERNET_WIFE_IMAGE).equals(Searcher.INTERNET_WIFE));
	}
	
	@Test
	public void accuracyInsanityWolf() throws IOException, URISyntaxException {
		Assert.assertTrue(classifyMeme(INSANITY_WOLF_IMAGE).equals(Searcher.INSANITY_WOLF));
	}

	public static void main(String[] args) throws IOException, URISyntaxException {
		//main method to allow us to see the image downloaded
		MemeExtractor scraper = new MemeExtractor();
		BufferedImage meme = getImageFromSite(PHILOSORAPTOR_IMAGE);
		new ImageViewer(meme);
	}
	

}
