package com.zackbleach.meme.classifier.index;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Matchers.eq;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import javax.imageio.ImageIO;

import net.semanticmetadata.lire.ImageSearchHits;
import net.semanticmetadata.lire.imageanalysis.ColorLayout;

import org.apache.lucene.document.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.zackbleach.meme.classifier.cache.Meme;
import com.zackbleach.meme.classifier.cache.MemeCache;
import com.zackbleach.meme.classifier.featureextraction.FeatureExtractor;
import com.zackbleach.meme.classifier.scraper.Scraper;
import com.zackbleach.meme.scraper.ScrapedImage;

@RunWith(MockitoJUnitRunner.class)
public class IndexTest {

    private static String TEST_IMAGE = "src/test/resources/test-image.jpg";
    private static String IMAGE_URL = "http://meme.com/sweetmeme";
    private static String IMAGE_NAME = "Chuck Norris";

    @Mock
    private Meme meme;

    @Mock
    private ScrapedImage image;

    @Mock
    private Scraper scraper;

    @Mock
    private MemeCache cache;

    @InjectMocks
    private Index index;

    @Before
    public void setup() throws IOException, URISyntaxException,
            BuildingIndexException {
        when(image.getName()).thenReturn(IMAGE_NAME);
        when(image.getSourceUrl()).thenReturn(IMAGE_URL);
        when(meme.getName()).thenReturn(IMAGE_NAME);
        when(meme.getImage()).thenReturn(readImageFromDisk(TEST_IMAGE));
        when(meme.getSourceUrl()).thenReturn(IMAGE_URL);
        when(cache.getMeme(any(ScrapedImage.class))).thenReturn(meme);
        ReflectionTestUtils.setField(index, "indexLocation", "meme-test-index/");
        index.create();
        index.featureExtractionMethod = ColorLayout.class;
    }

    @After
    public void tearDown() throws IOException {
        index.shutdown();
    }

    @Test
    public void whenUpdatingIndex_ifOneNewImage_indexSizeIncreasesByOne()
            throws URISyntaxException, IOException {
        when(scraper.getNewMemes()).thenReturn(ImmutableSet.of(image));
        index.update();
        assert index.getNumDocs() == 1;
    }

    @Test
    public void afterAddingImage_imageCanBeRetrieved() throws IOException,
            BuildingIndexException, URISyntaxException {
        when(scraper.getNewMemes()).thenReturn(ImmutableSet.of(image));
        index.update();
        ImageSearchHits hits = index.search(readImageFromDisk(TEST_IMAGE));
        assert (hits.length() == 1);
        assert (hits.score(0) == 0.0);
    }


    private BufferedImage readImageFromDisk(String path) throws IOException {
        File imageFile = new File(path);
        System.out.println(imageFile.getAbsoluteFile());
        BufferedImage image = ImageIO.read(imageFile);
        if (image == null) {
            throw new IllegalArgumentException(
                    "Could not read an image from the specified location");
        }
        return image;
    }
}
