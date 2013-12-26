package com.zackbleach.memetable.imagerecognition.test;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

import com.zackbleach.memetable.imagerecognition.Indexer;
import com.zackbleach.memetable.imagerecognition.Meme;
import com.zackbleach.memetable.imagerecognition.Result;
import com.zackbleach.memetable.util.ClassificationUtils;
import com.zackbleach.memetable.util.ImageUtils;

public class ClassificationTest {

    private static final Logger log = Logger
            .getLogger(ClassificationTest.class);
    public static final String TEST_FOLDER_PATH = "TestMemes/";

    private ClassificationUtils classificationUtils;

    /**
     * Checks that two different {@link Meme}s have a distance greater than the
     * distance threshold
     *
     * @throws IOException
     */
    @Test
    public void calculateDistanceDifferent() throws IOException {
        String mallardDir = TEST_FOLDER_PATH
                + Meme.ADVICE_MALLARD.getDirectory();
        String wolfDir = TEST_FOLDER_PATH + Meme.INSANITY_WOLF.getDirectory();
        float distance = calculateDistance(mallardDir, wolfDir);
        log.info("Different Distance = " + distance);
        Assert.assertTrue(distance > ClassificationUtils.THRESHOLD);
    }

    /**
     * Checks that two similar {@link Meme}s have a distance greater than the
     * distance threshold
     *
     * @throws IOException
     */
    @Test
    public void calculateDistanceSimilar() throws IOException {
        String mallardDir = TEST_FOLDER_PATH
                + Meme.ADVICE_MALLARD.getDirectory();
        String mallard2Dir = TEST_FOLDER_PATH
                + Meme.ADVICE_MALLARD.getDirectory();
        float distance = calculateDistance(mallardDir, mallard2Dir);
        log.info("Similar Distance = " + distance);
        Assert.assertTrue(distance < ClassificationUtils.THRESHOLD);
    }

    /**
     * For each {@link Meme} calls {@link getAccuracyForMeme()} TODO: Should
     * this have a pass or fail condition?
     *
     * @returns Average classification accuracy for all memes
     * @throws IOException
     */
    @Test
    public void classifyAllMemes() throws IOException {
        Indexer.index();
        float accuracy = 0.0f;
        int index = 1;
        for (Meme meme : Meme.values()) {
            if (meme != Meme.CATS) {
                float myAccuracy = getAccuracyForMeme(meme);
                if (myAccuracy != -1) {
                    accuracy += myAccuracy;
                    index++;
                }
            }
        }
        log.info("Average Accuracy: " + accuracy / index);
    }

    /**
     * Given a {@link Meme} will load all available test instances of that meme
     * and attempt to classify them. TODO: Get even amount of test data
     *
     * @param meme
     *            Meme to score accuracy of
     * @return Amount of correct identifications / Amount of attempted
     *         identifications
     * @throws IOException
     */
    public float getAccuracyForMeme(Meme meme) throws IOException {
        float index = 0.0f;
        float correct = 0.0f;
        List<BufferedImage> images = ImageUtils
                .readImagesFromFolder(TEST_FOLDER_PATH + meme.getDirectory());
        if (images.size() == 0) {
            return -1;
        }
        for (BufferedImage image : images) {
            Result r = classificationUtils.classifyMemeFromImage(image);
            if (r.getMeme() == meme) {
                correct++;
            }
            index++;
        }
        System.out.println(correct + "/" + index);
        return (correct / index);
    }

    /**
     * Picks a random image from each directory provided and calculates the
     * distance between them
     *
     * TODO: When threshold is implemented use it for this test
     *
     * @param directory1
     *            Directory of first meme
     * @param directory2
     *            Directory of second meme
     * @throws IOException
     */
    public float calculateDistance(String directory1, String directory2)
            throws IOException {
        Random random = new Random();
        List<BufferedImage> images1 = ImageUtils
                .readImagesFromFolder(directory1);
        List<BufferedImage> images2 = ImageUtils
                .readImagesFromFolder(directory2);
        BufferedImage one = images1.get(random.nextInt(images1.size()));
        BufferedImage two = images2.get(random.nextInt(images2.size()));
        return ClassificationUtils.getCEDDDistance(one, two);
    }
}
