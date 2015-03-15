//package com.zackbleach.meme.classification;
//
//import static org.mockito.Mockito.when;
//
//import java.awt.image.BufferedImage;
//import java.io.File;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.TimeUnit;
//
//import javax.imageio.ImageIO;
//
//import net.semanticmetadata.lire.DocumentBuilder;
//import net.semanticmetadata.lire.ImageSearchHits;
//import net.semanticmetadata.lire.imageanalysis.AutoColorCorrelogram;
//import net.semanticmetadata.lire.imageanalysis.CEDD;
//import net.semanticmetadata.lire.imageanalysis.ColorLayout;
//import net.semanticmetadata.lire.imageanalysis.EdgeHistogram;
//import net.semanticmetadata.lire.imageanalysis.FCTH;
//import net.semanticmetadata.lire.imageanalysis.FuzzyColorHistogram;
//import net.semanticmetadata.lire.imageanalysis.JCD;
//import net.semanticmetadata.lire.imageanalysis.JpegCoefficientHistogram;
//import net.semanticmetadata.lire.imageanalysis.LireFeature;
//import net.semanticmetadata.lire.imageanalysis.OpponentHistogram;
//import net.semanticmetadata.lire.imageanalysis.ScalableColor;
//import net.semanticmetadata.lire.imageanalysis.SimpleColorHistogram;
//
//import org.apache.commons.io.FileUtils;
//import org.apache.commons.io.filefilter.TrueFileFilter;
//import org.apache.lucene.document.Document;
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.test.util.ReflectionTestUtils;
//
//import com.google.common.collect.ImmutableMap;
//import com.zackbleach.meme.classifier.index.Index;
//import com.zackbleach.meme.classifier.scraper.QuickMemeScraper;
//import com.zackbleach.meme.classifier.scraper.ScrapedImage;
//import com.zackbleach.meme.classifier.utils.ImageUtils;
//
//public class AccuracyTest {
//
//    private static final String TEST_INDEX = "meme-test-index/";
//
//    @Mock
//    private QuickMemeScraper scraper;
//
//    @InjectMocks
//    private Index bucketer = new Index();
//
//    private List<ScrapedImage> examples;
//    private List<ScrapedImage> templates;
//
//    private List<String> messages = new ArrayList<String>();
//
//    @Before
//    public void setUp() throws IOException {
//        examples = getExamples();
//        templates = getTemplates();
//        MockitoAnnotations.initMocks(this);
//        when(scraper.scrapeTemplates()).thenReturn(templates);
//        ReflectionTestUtils.setField(bucketer, "INDEX_LOCATION", TEST_INDEX);
//    }
//
//    @After
//    public void cleanUp() {
//        File file = new File(TEST_INDEX);
//        file.delete();
//        for (String message : messages) {
//            System.out.println(message);
//        }
//    }
//
//    @Test
//    public void testAccuracyOfColorLayout() throws IOException {
//        testAccuracyOfLireFeature(ColorLayout.class, examples);
//    }
//
//    @Test
//    public void testAccuracyOfCEDD() throws IOException {
//        testAccuracyOfLireFeature(CEDD.class, examples);
//    }
//
//    @Test
//    public void testAccuracyOfOpponentHistogram() throws IOException {
//        testAccuracyOfLireFeature(OpponentHistogram.class, examples);
//    }
//
//    @Test
//    public void testAccuracyOfAutoColorCorrelogram() throws IOException {
//        testAccuracyOfLireFeature(AutoColorCorrelogram.class, examples);
//    }
//
//    @Test
//    public void testAccuracyOfSimpleColorHistogram() throws IOException {
//        testAccuracyOfLireFeature(SimpleColorHistogram.class, examples);
//    }
//
//    @Test
//    public void testAccuracyOfFuzzyColorHistogram() throws IOException {
//        testAccuracyOfLireFeature(FuzzyColorHistogram.class, examples);
//    }
//
//    @Test
//    public void testAccuracyOfJCD() throws IOException {
//        testAccuracyOfLireFeature(JCD.class, examples);
//    }
//
//    @Test
//    public void testAccuracyOfFCTH() throws IOException {
//        testAccuracyOfLireFeature(FCTH.class, examples);
//    }
//
//    @Test
//    public void testAccuracyOfEdgeHistogram() throws IOException {
//        testAccuracyOfLireFeature(EdgeHistogram.class, examples);
//    }
//
//    @Test
//    public void testAccuracyOfScalableColor() throws IOException {
//        testAccuracyOfLireFeature(ScalableColor.class, examples);
//    }
//
//    @Test
//    public void testAccuracyOfJpegCoefficientHistogram() throws IOException {
//        testAccuracyOfLireFeature(JpegCoefficientHistogram.class, examples);
//    }
//    public int testAccuracyOfLireFeature(Class<? extends LireFeature> feature,
//            List<ScrapedImage> examples) throws IOException {
//        long startTime = System.nanoTime();
//        ReflectionTestUtils.setField(bucketer, "feautureExtractionMethod",
//                feature);
//        ReflectionTestUtils.invokeMethod(bucketer, "update");
//        List<BadClassification> badClassifications = new ArrayList<BadClassification>();
//        for (ScrapedImage image : examples) {
//            ImageSearchHits hits = bucketer.search(image
//                    .getImage());
//            if (!isClassificationCorrect(image, hits.doc(0))) {
//                badClassifications
//                        .add(new BadClassification(image, hits.doc(0)));
//            }
//        }
//        printResults(badClassifications, examples.size());
//        printTime(startTime, "Time taken to test feature: ");
//        return examples.size() - badClassifications.size();
//    }
//
//    public boolean isClassificationCorrect(ScrapedImage image, Document document) {
//        String expected = image.getName();
//        String actual = document.get(DocumentBuilder.FIELD_NAME_IDENTIFIER);
//        return expected.equals(actual);
//    }
//
//    @SuppressWarnings("unchecked")
//    private Collection<File> readMemes() throws IOException {
//        long startTime = System.nanoTime();
//        File dir = new File("memes/").getCanonicalFile();
//        Collection<File> files = FileUtils.listFiles(dir,
//                TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
//        printTime(startTime, "Time taken to read in FILES: ");
//        return files;
//    }
//
//    private List<ScrapedImage> getTemplates() throws IOException {
//        long startTime = System.nanoTime();
//        List<ScrapedImage> templates = new ArrayList<ScrapedImage>();
//        Collection<File> memes = readMemes();
//        for (File meme : memes) {
//            if (isNotHiddenAndNotDir(meme) && meme.getName().equals("template")) {
//                ScrapedImage scrapedImage = getScrapedImageFromFile(meme);
//                templates.add(scrapedImage);
//            }
//        }
//        printTime(startTime, "Time taken to read templates: ");
//        return templates;
//    }
//
//    private boolean isNotHiddenAndNotDir(File file) throws IOException {
//        return !file.isHidden() && !file.isDirectory();
//    }
//
//    private List<ScrapedImage> getExamples() throws IOException {
//        long methodStartTime = System.nanoTime();
//        List<ScrapedImage> templates = new ArrayList<ScrapedImage>();
//        Collection<File> memes = readMemes();
//        for (File meme : memes) {
//            if (isNotHiddenAndNotDir(meme)
//                    && !meme.getName().equals("template")) {
//                ScrapedImage scrapedImage = getScrapedImageFromFile(meme);
//                if (scrapedImage.getImage().getType() > 0) {
//                    templates.add(scrapedImage);
//                }
//            }
//        }
//        System.out.println("Finished reading examples, found: "
//                + templates.size());
//        printTime(methodStartTime, "Time taken to read examples: ");
//        return templates;
//    }
//
//    private ScrapedImage getScrapedImageFromFile(File meme) throws IOException {
//        ScrapedImage scrapedImage = new ScrapedImage();
//        scrapedImage.setName(meme.getParentFile().getName());
//        scrapedImage.setImage(ImageIO.read(meme));
//        if (scrapedImage.getImage().getType() != BufferedImage.TYPE_3BYTE_BGR) {
//            scrapedImage.setImage(ImageUtils.convertToBgr(scrapedImage
//                    .getImage()));
//        }
//        scrapedImage.setSourceUrl(meme.getName());
//        return scrapedImage;
//    }
//
//    private class BadClassification {
//
//        private String expected;
//        private String actual;
//        private String filePath;
//
//        public BadClassification(ScrapedImage image, Document document) {
//            this.expected = document.get(DocumentBuilder.FIELD_NAME_IDENTIFIER);
//            this.actual = image.getName();
//            this.filePath = image.getSourceUrl();
//        }
//
//        @Override
//        public String toString() {
//            return "Expected: " + expected + ", Actual: " + actual + ", Path: "
//                    + filePath;
//        }
//    }
//
//    private void printResults(List<BadClassification> badClassifications,
//            int totalMemes) {
//        Map<Map<String, String>, List<String>> commonMistakes = collectCommonMistakes(badClassifications);
//        for (Map.Entry<Map<String, String>, List<String>> entry : commonMistakes
//                .entrySet()) {
//            System.out.println("Incorrect classicification: " + entry.getKey());
//            System.out.println("Occurances: " + entry.getValue().size());
//            System.out.println("Instances: " + entry.getValue());
//            System.out.println();
//        }
//        System.out.println("Successfull classifications: "
//                + (totalMemes - badClassifications.size()) + "/" + totalMemes);
//    }
//
//    private Map<Map<String, String>, List<String>> collectCommonMistakes(
//            List<BadClassification> badClassifications) {
//        Map<Map<String, String>, List<String>> commonMistakes = new HashMap<Map<String, String>, List<String>>();
//        for (BadClassification badClassification : badClassifications) {
//            Map<String, String> mistake = ImmutableMap.of(
//                    badClassification.expected, badClassification.actual);
//            if (!commonMistakes.containsKey(mistake)) {
//                commonMistakes.put(mistake, new ArrayList<String>());
//            }
//            commonMistakes.get(mistake).add(badClassification.filePath);
//        }
//        return commonMistakes;
//    }
//
//    private void printTime(long startTime, String message) {
//        long currentTime = System.nanoTime();
//        long timeTaken = currentTime - startTime;
//        messages.add(message + toSeconds(timeTaken));
//    }
//
//    private long toSeconds(long nanoTime) {
//        return TimeUnit.SECONDS.convert(nanoTime, TimeUnit.NANOSECONDS);
//    }
//}
