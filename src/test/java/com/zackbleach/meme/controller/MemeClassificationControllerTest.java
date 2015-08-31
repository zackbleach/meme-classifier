package com.zackbleach.meme.controller;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;

import javax.imageio.ImageIO;

import net.semanticmetadata.lire.DocumentBuilder;
import net.semanticmetadata.lire.ImageSearchHits;
import net.semanticmetadata.lire.imageanalysis.ColorLayout;
import net.semanticmetadata.lire.impl.GenericDocumentBuilder;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.StoredField;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.zackbleach.meme.classifier.controller.MemeClassificationController;
import com.zackbleach.meme.classifier.controller.Result;
import com.zackbleach.meme.classifier.index.BuildingIndexException;
import com.zackbleach.meme.classifier.index.Index;
import com.zackbleach.meme.classifier.utils.ImageUtils;

@RunWith(MockitoJUnitRunner.class)
public class MemeClassificationControllerTest {

    private static String TEST_IMAGE = "src/test/resources/test-image.jpg";
    private static String TEST_IMAGE_NAME = "Chuck Norris";
    private static float SCORE = 1;

    @Mock
    Index index;

    @Mock
    ImageUtils imageUtils;

    @Mock
    ImageSearchHits hits;

    @InjectMocks
    MemeClassificationController controller = new MemeClassificationController();

    @Before
    public void setup() throws IOException, URISyntaxException,
            BuildingIndexException {
        BufferedImage image = readImageFromDisk(TEST_IMAGE);
        when(imageUtils.getImageFromUrl(any(String.class))).thenReturn(image);
        when(index.search(image)).thenReturn(hits);
        when(hits.length()).thenReturn(1);
        when(hits.score(0)).thenReturn(SCORE);
        DocumentBuilder builder = new GenericDocumentBuilder(ColorLayout.class);
        Document document = builder.createDocument(image,
                    TEST_IMAGE);
        when(hits.doc(0)).thenReturn(document);
    }

    @Test
    public void givenValidPath_whenClassifyingMeme_thenReturnClassification()
            throws IOException, URISyntaxException, ExecutionException,
            BuildingIndexException {
   System.out.println(controller.classifyMeme("http://www.memes.com"));
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
