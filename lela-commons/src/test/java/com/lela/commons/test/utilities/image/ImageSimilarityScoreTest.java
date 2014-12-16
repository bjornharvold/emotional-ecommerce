package com.lela.commons.test.utilities.image;

import com.lela.util.utilities.image.ImagePHash;
import com.lela.util.utilities.image.ImageSimilarityScore;
import com.lela.util.utilities.image.ImageTrim;

import com.lela.util.utilities.image.ImageUtil;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

/**
 * Created with IntelliJ IDEA.
 * User: ballmw
 * Date: 8/15/12
 * Time: 6:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class ImageSimilarityScoreTest {

    ImageSimilarityScore similarityScore = new ImageSimilarityScore();

    @Test
    public void compareToOriginal() throws Exception
    {
        ClassPathResource original = new ClassPathResource("testdata/images/similar/original.jpg");
        ClassPathResource duplicate = new ClassPathResource("testdata/images/similar/duplicate.jpg");

        double distance = similarityScore.calculate(original.getURL(), duplicate.getURL());

        assertEquals("Original and duplicate should match", 100.0d, distance);

    }

    @Test
    public void compareToSimilar() throws Exception
    {
        ClassPathResource original = new ClassPathResource("testdata/images/similar/original.jpg");
        ClassPathResource duplicate = new ClassPathResource("testdata/images/similar/compare1.jpg");

        double distance = similarityScore.calculate(original.getURL(), duplicate.getURL());

        assertEquals("Original and duplicate should be 83.0", 83.0d, round(distance));
    }

    @Test
    public void compareToSimilarLowRez() throws Exception
    {
        ClassPathResource original = new ClassPathResource("testdata/images/similar/original.jpg");
        ClassPathResource duplicate = new ClassPathResource("testdata/images/similar/compare2.jpg");

        double distance = similarityScore.calculate(original.getURL(), duplicate.getURL());

        assertEquals("Original and duplicate should be 82.0", 82.0, round(distance));
    }

    @Test
    public void compareToDissimilar() throws Exception
    {
        ClassPathResource original = new ClassPathResource("testdata/images/similar/original.jpg");
        ClassPathResource duplicate = new ClassPathResource("testdata/images/similar/compare3.jpg");

        double distance = similarityScore.calculate(original.getURL(), duplicate.getURL());

        assertEquals("Original and duplicate should be 63.0", 63.0d, round(distance));
    }

    private static double round(double dub)
    {
        DecimalFormat twoDForm = new DecimalFormat("#.###");
        return Double.valueOf(twoDForm.format(dub));
    }

    @Test
    public void compareStrollers() throws Exception
    {
        ClassPathResource stroller1 = new ClassPathResource("testdata/images/similar/stroller1.jpg");
        ClassPathResource stroller2 = new ClassPathResource("testdata/images/similar/stroller2.jpg");
        ClassPathResource stroller3 = new ClassPathResource("testdata/images/similar/stroller3.jpg");
        ClassPathResource stroller4 = new ClassPathResource("testdata/images/similar/stroller4.jpg");
        ClassPathResource stroller5 = new ClassPathResource("testdata/images/similar/stroller5.jpg");
        ClassPathResource thumbnail = new ClassPathResource("testdata/images/similar/stroller_thumbnail.jpg");

        double distance = similarityScore.calculate(stroller1.getURL(), stroller1.getURL());
        assertEquals("Original should match itself", 100.0d, round(distance));

        distance = similarityScore.calculate(stroller1.getURL(), stroller2.getURL());
        assertEquals("Original should be similar ", 96.0d, round(distance));

        distance = similarityScore.calculate(stroller1.getURL(), stroller3.getURL());
        assertEquals("Original should be similar ", 99.0d, round(distance));

        distance = similarityScore.calculate(stroller1.getURL(), stroller4.getURL());
        assertEquals("Original should be dissimilar ", 88.0, round(distance));

        distance = similarityScore.calculate(stroller1.getURL(), stroller5.getURL());
        assertEquals("Original should be dissimilar ", 93.0d, round(distance));

        distance = similarityScore.calculate(stroller4.getURL(), stroller5.getURL());
        assertEquals("Original should be similar ", 94.0, round(distance));

        distance = similarityScore.calculate(stroller1.getURL(), thumbnail.getURL());
        assertEquals("Original should be similar ", 95.0d, round(distance));
    }

    @Test
    public void compareToFlipped() throws Exception
    {
        ClassPathResource original = new ClassPathResource("testdata/images/similar/stroller2.jpg");
        ClassPathResource flipped = new ClassPathResource("testdata/images/similar/stroller2_flipped.jpg");

        double distance = similarityScore.calculate(original.getURL(), flipped.getURL());

        assertEquals("Should be dissimilar", 84.0d, round(distance));
    }

    @Test
    public void compareToVeryDifferent() throws Exception
    {
        ClassPathResource original = new ClassPathResource("testdata/images/similar/stroller2.jpg");
        ClassPathResource different = new ClassPathResource("testdata/images/similar/fire_hydrant.jpg");

        double distance = similarityScore.calculate(original.getURL(), different.getURL());

        assertEquals("Should be very different", 71.0d, round(distance));
    }

    @Test
    public void compareJpgToPng() throws Exception
    {
        ClassPathResource original = new ClassPathResource("testdata/images/similar/stroller2.jpg");
        ClassPathResource different = new ClassPathResource("testdata/images/similar/fire_hydrant.png");

        double distance = similarityScore.calculate(original.getURL(), different.getURL());

        assertEquals("Should be very different", 47.0d, round(distance));
    }

    @Test
    public void compareCribA() throws Exception
    {
        ClassPathResource crib1 = new ClassPathResource("testdata/images/similar/crib_a_1.jpg");
        ClassPathResource crib2 = new ClassPathResource("testdata/images/similar/crib_a_2.jpg");
        ClassPathResource crib3 = new ClassPathResource("testdata/images/similar/crib_a_3.jpg");
        ClassPathResource crib4 = new ClassPathResource("testdata/images/similar/crib_a_4.jpg");
        ClassPathResource crib5 = new ClassPathResource("testdata/images/similar/crib_a_5.jpg");
        ClassPathResource crib6 = new ClassPathResource("testdata/images/similar/crib_a_6.jpg");

        double distance = similarityScore.calculate(crib1.getURL(), crib1.getURL());
        assertEquals("Original should match itself", 100.0d, round(distance));

        distance = similarityScore.calculate(crib1.getURL(), crib2.getURL());
        assertTrue("Original should be similar ", round(distance) > 90.0);

        distance = similarityScore.calculate(crib1.getURL(), crib3.getURL());
        assertTrue("Original should be similar ", round(distance) > 90.0);

        distance = similarityScore.calculate(crib1.getURL(), crib4.getURL());
        assertTrue("Original should be similar ", round(distance) > 90.0);

        distance = similarityScore.calculate(crib1.getURL(), crib5.getURL());
        assertTrue("Original should be dissimilar ", round(distance) < 90.0);

        distance = similarityScore.calculate(crib1.getURL(), crib6.getURL());
        assertTrue("Original should be similar ", round(distance) > 90.0);

    }



    @Test
    public void compareCribB() throws Exception
    {
        ClassPathResource crib1 = new ClassPathResource("testdata/images/similar/crib_b_1.jpg");
        ClassPathResource crib2 = new ClassPathResource("testdata/images/similar/crib_b_2.jpg");
        ClassPathResource crib3 = new ClassPathResource("testdata/images/similar/crib_b_3.jpg");
        ClassPathResource crib4 = new ClassPathResource("testdata/images/similar/crib_b_4.jpg");
        ClassPathResource crib5 = new ClassPathResource("testdata/images/similar/crib_b_5.jpg");
        ClassPathResource crib6 = new ClassPathResource("testdata/images/similar/crib_b_6.jpg");
        ClassPathResource crib7 = new ClassPathResource("testdata/images/similar/crib_b_7.jpg");
        ClassPathResource crib8 = new ClassPathResource("testdata/images/similar/crib_b_8.jpg");

        double distance = similarityScore.calculate(crib1.getURL(), crib1.getURL());
        assertEquals("Original should match itself", 100.0d, round(distance));

        distance = similarityScore.calculate(crib1.getURL(), crib2.getURL());
        assertTrue("Original should be similar ", round(distance) > 78.0d);

        distance = similarityScore.calculate(crib1.getURL(), crib3.getURL());
        assertTrue("Original should be dissimilar ", round(distance) < 78.0d);

        distance = similarityScore.calculate(crib3.getURL(), crib4.getURL());
        assertEquals("Original should be a false positive ", 93.0d, round(distance));

        distance = similarityScore.calculate(crib3.getURL(), crib4.getURL());
        assertTrue("Original should be a false positive ", round(distance) < 94.0d);

        distance = similarityScore.calculate(crib3.getURL(), crib5.getURL());
        assertTrue("Original should be identified as a flipped ", round(distance) > 78.0d);

        distance = similarityScore.calculate(crib5.getURL(), crib6.getURL());
        assertTrue("Original should be dissimilar 80.0", round(distance) < 80.0);

        distance = similarityScore.calculate(crib7.getURL(), crib8.getURL());
        assertTrue("Original should be dissimilar ", round(distance) < 78.0d);

    }

    @Test
    public void comparePHashCribA() throws Exception
    {
        ClassPathResource crib1 = new ClassPathResource("testdata/images/similar/crib_a_1.jpg");
        ClassPathResource crib2 = new ClassPathResource("testdata/images/similar/crib_a_2.jpg");
        ClassPathResource crib3 = new ClassPathResource("testdata/images/similar/crib_a_3.jpg");
        ClassPathResource crib4 = new ClassPathResource("testdata/images/similar/crib_a_4.jpg");
        ClassPathResource crib5 = new ClassPathResource("testdata/images/similar/crib_a_5.jpg");
        ClassPathResource crib6 = new ClassPathResource("testdata/images/similar/crib_a_6.jpg");

        ImagePHash imagePHash = new ImagePHash();

        int distance = imagePHash.distance(crib1.getURL(), crib1.getURL());
        assertEquals("Original should match itself", 0, distance);

        distance = imagePHash.distance(crib1.getURL(), crib2.getURL());
        assertTrue("Original should be similar 1->2 ", distance < 12);

        distance = imagePHash.distance(crib1.getURL(), crib3.getURL());
        assertTrue("Original should be similar 1->3", distance < 9);

        distance = imagePHash.distance(crib1.getURL(), crib4.getURL());
        assertTrue("Original should be similar 1->4", distance < 12);

        distance = imagePHash.distance(crib1.getURL(), crib5.getURL());
        assertTrue("Original should be dissimilar 1->5", distance > 8);

        distance = imagePHash.distance(crib1.getURL(), crib6.getURL());
        assertTrue("Original should be similar 1->6", distance < 9);

    }

    @Test
    public void comparePHashStrollers() throws Exception
    {
        ClassPathResource stroller1 = new ClassPathResource("testdata/images/similar/stroller1.jpg");
        ClassPathResource stroller2 = new ClassPathResource("testdata/images/similar/stroller2.jpg");
        ClassPathResource stroller3 = new ClassPathResource("testdata/images/similar/stroller3.jpg");
        ClassPathResource stroller4 = new ClassPathResource("testdata/images/similar/stroller4.jpg");
        ClassPathResource stroller5 = new ClassPathResource("testdata/images/similar/stroller5.jpg");
        ClassPathResource thumbnail = new ClassPathResource("testdata/images/similar/stroller_thumbnail.jpg");

        ImagePHash imagePHash = new ImagePHash();

        int distance = imagePHash.distance(stroller1.getURL(), stroller1.getURL());
        assertEquals("Original should match itself", 0, distance);

        distance = imagePHash.distance(stroller1.getURL(), stroller2.getURL());
        assertEquals("Original should be similar ", 4, distance);

        distance = imagePHash.distance(stroller1.getURL(), stroller3.getURL());
        assertEquals("Original should be similar ", 4, distance);

        distance = imagePHash.distance(stroller1.getURL(), stroller4.getURL());
        assertEquals("Original should be dissimilar ", 6, distance);

        distance = imagePHash.distance(stroller1.getURL(), stroller5.getURL());
        assertEquals("Original should be dissimilar ", 4, distance);

        distance = imagePHash.distance(stroller4.getURL(), stroller5.getURL());
        assertEquals("Original should be similar ", 6, distance);

        distance = imagePHash.distance(stroller1.getURL(), thumbnail.getURL());
        assertEquals("Original should be similar ", 6, distance);
    }

    @Test
    public void testPHashCribB() throws Exception
    {
        ClassPathResource crib1 = new ClassPathResource("testdata/images/similar/crib_b_1.jpg");
        ClassPathResource crib2 = new ClassPathResource("testdata/images/similar/crib_b_2.jpg");
        ClassPathResource crib3 = new ClassPathResource("testdata/images/similar/crib_b_3.jpg");
        ClassPathResource crib4 = new ClassPathResource("testdata/images/similar/crib_b_4.jpg");
        ClassPathResource crib5 = new ClassPathResource("testdata/images/similar/crib_b_5.jpg");
        ClassPathResource crib6 = new ClassPathResource("testdata/images/similar/crib_b_6.jpg");
        ClassPathResource crib7 = new ClassPathResource("testdata/images/similar/crib_b_7.jpg");
        ClassPathResource crib8 = new ClassPathResource("testdata/images/similar/crib_b_8.jpg");

        ImagePHash imagePHash = new ImagePHash();

        //if the image has a diff of >= 9 it's different
        //if the image has a score of >= 4 < 9 it

        int distance = imagePHash.distance(crib1.getURL(), crib1.getURL());
        assertEquals("Original should match itself", 0, distance);

        distance = imagePHash.distance(crib1.getURL(), crib2.getURL());
        assertEquals("Original should be kind of similar ", 10, distance);

        distance = imagePHash.distance(crib1.getURL(), crib2.getURL());
        assertTrue("Original should be dissimilar ", distance >= 9);

        distance = imagePHash.distance(crib1.getURL(), crib3.getURL());
        assertEquals("Original should be dissimilar ", 10, distance);

        distance = imagePHash.distance(crib1.getURL(), crib3.getURL());
        assertTrue("Original should be dissimilar ", distance >= 9);

        distance = imagePHash.distance(crib3.getURL(), crib4.getURL());
        assertEquals("Original will be a false positive ", 5, distance);

        //this one is bad
        distance = imagePHash.distance(crib3.getURL(), crib4.getURL());
        assertTrue("Original should be a false positive ", distance < 9);

        distance = imagePHash.distance(crib3.getURL(), crib5.getURL());
        assertTrue("Original should be identified as a flipped (dissimilar) ", distance >= 9);


        distance = imagePHash.distance(crib5.getURL(), crib6.getURL());
        assertTrue("Original should be dissimilar ", distance >= 9);

        distance = imagePHash.distance(crib7.getURL(), crib8.getURL());
        assertTrue("Original should be dissimilar ", distance >= 9);

    }

    @Test
    public void testTrimImage() throws Exception
    {
        ClassPathResource square = new ClassPathResource("testdata/images/similar/square.jpg");
        BufferedImage img = ImageIO.read(square.getURL());
        ImageTrim imageTrim = new ImageTrim(new Color(240, 240, 240).getRGB());
        img = imageTrim.trim(img);
        assertEquals("Width is 302", 298, img.getWidth());
        assertEquals("Height is 302", 299, img.getHeight());
    }

    @Test
    public void testWeirdImage() throws Exception
    {
        ClassPathResource cmyk = new ClassPathResource("testdata/images/cmyk.jpg");
        byte[] image = IOUtils.toByteArray(cmyk.getInputStream());

        ImageUtil imageUtil = new ImageUtil();
        BufferedImage bufferedImage = imageUtil.readMalformedJpeg(image);
        assertNotNull(bufferedImage);


    }

    @Test
    public void testSimilarTvImages() throws Exception
    {
        ClassPathResource tv2 = new ClassPathResource("testdata/images/similar/tv_b_2.jpg");
        ClassPathResource tv3 = new ClassPathResource("testdata/images/similar/tv_b_3.jpg");

        double distance = similarityScore.calculate(tv2.getURL(), tv3.getURL());
        assertEquals("Original should be dissimilar", 96.0d, round(distance));

    }

    @Test
    public void testSimilarCribs() throws Exception
    {
        ClassPathResource crib1 = new ClassPathResource("testdata/images/similar/duplicate_crib.jpg");
        ClassPathResource crib2 = new ClassPathResource("testdata/images/similar/duplicate_crib.jpg");

        double distance = similarityScore.calculate(crib1.getURL(), crib2.getURL());
        assertEquals("Original should be dissimilar", 100.0d, round(distance));

    }



    @Test
    public void compareToLowResolution() throws Exception
    {
        ClassPathResource original = new ClassPathResource("testdata/images/similar/low_rez_1.jpg");
        ClassPathResource duplicate = new ClassPathResource("testdata/images/similar/low_rez_2.jpg");

        double distance = similarityScore.calculate(original.getURL(), duplicate.getURL());

        assertEquals("Original and duplicate should match", 99.0, distance);

        ImagePHash imagePHash = new ImagePHash();

        //if the image has a diff of >= 9 it's different
        //if the image has a score of >= 4 < 9 it

        int phash = imagePHash.distance(original.getURL(), duplicate.getURL());
        assertEquals("Original should match itself", 1, phash);

    }

}
