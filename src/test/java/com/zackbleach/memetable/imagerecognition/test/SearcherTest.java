package com.zackbleach.memetable.imagerecognition.test;

import static com.zackbleach.memetable.util.ClassificationUtils.classifyMeme;
import static com.zackbleach.memetable.util.ClassificationUtils.getImageFromSite;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.Assert;
import org.junit.Test;

import com.zackbleach.memetable.contentextraction.MemeExtractor;
import com.zackbleach.memetable.imagerecognition.Searcher;
import com.zackbleach.memetable.util.ImageViewer;

public class SearcherTest {
	
	//TODO: Save these test images locally
	public static final String PHILOSORAPTOR_IMAGE = "http://25.media.tumblr.com/tumblr_lsvqs4bOBT1r4yxoho1_500.jpg";
	public static final String INTERNET_WIFE_IMAGE = "http://29.media.tumblr.com/tumblr_lrmu85jCWf1qb5gkjo1_400.jpg";
	public static final String INSANITY_WOLF_IMAGE = "https://i.chzbgr.com/maxW500/7027276544/h190097E5/";
	public static final String BAD_ADVICE_MALLARD_IMAGE = "http://wac.450f.edgecastcdn.net/80450F/k945.com/files/2013/01/malicious-advice-mallard-630x444.jpg";
	
	@Test
	public void accuracyPhilosoraptor() throws IOException, URISyntaxException {
		Assert.assertTrue(classifyMeme(PHILOSORAPTOR_IMAGE).getMeme().equals(Searcher.PHILOSORAPTOR));
	}
	
	@Test
	public void accuracyInternetWife() throws IOException, URISyntaxException {
		Assert.assertTrue(classifyMeme(INTERNET_WIFE_IMAGE).getMeme().equals(Searcher.INTERNET_WIFE));
	}
	
	@Test
	public void accuracyInsanityWolf() throws IOException, URISyntaxException {
		Assert.assertTrue(classifyMeme(INSANITY_WOLF_IMAGE).getMeme().equals(Searcher.INSANITY_WOLF));
	}
	
	@Test
	public void accuracyBadAdviceMallardf() throws IOException, URISyntaxException {
		Assert.assertTrue(classifyMeme(BAD_ADVICE_MALLARD_IMAGE).getMeme().equals(Searcher.BAD_ADVICE_MALLARD));
	}

	public static void main(String[] args) throws IOException, URISyntaxException {
		//main method to allow us to see the image downloaded
		MemeExtractor scraper = new MemeExtractor();
		BufferedImage meme = getImageFromSite(PHILOSORAPTOR_IMAGE);
		new ImageViewer(meme);
	}
	

}
