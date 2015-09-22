package com.zackbleach.meme.classifier.scraper;

import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.ImmutableList;
import com.zackbleach.meme.classifier.cache.MemeCache;
import com.zackbleach.meme.classifier.featureextraction.FeatureExtractor;
import com.zackbleach.meme.scraper.ScrapedImage;

@RunWith(MockitoJUnitRunner.class)
public class ScraperTest {

    private static String IMAGE_URL = "http://meme.com/sweetmeme";
    private static String IMAGE_NAME = "Chuck Norris";

    @Mock
    private ScrapedImage image;

    @Mock
    private com.zackbleach.meme.scraper.Scraper mockMemeScraper;

    @Mock
    private MemeCache cache;

    @Mock
    private FeatureExtractor featureExtractor;

    @InjectMocks
    private Scraper scraper;

    @Before
    public void setup() throws IOException, URISyntaxException {
        when(image.getName()).thenReturn(IMAGE_NAME);
        when(image.getSourceUrl()).thenReturn(IMAGE_URL);
        scraper.setScrapers(ImmutableList.of(mockMemeScraper));
        when(mockMemeScraper.scrape()).thenReturn(ImmutableList.of(image));
    }

    @Test
    public void whenScrapingMemes_ifNewImage_SaveNewImage()
            throws URISyntaxException {
        Set<ScrapedImage> memes = scraper.getNewMemes();
        assert(memes.size() == 1);
    }

    @Test
    public void whenScrapingMemes_ifDuplicate_DontReturnImage() {
        when(mockMemeScraper.scrape()).thenReturn(ImmutableList.of(image, image));
        Set<ScrapedImage> memes = scraper.getNewMemes();
        assert(memes.size() == 1);
    }
}
