/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.commons.test.utilities.image;

import com.lela.util.UtilityException;
import com.lela.util.utilities.image.ImageUtil;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.UrlResource;


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Created by Bjorn Harvold
 * Date: 1/20/12
 * Time: 10:43 PM
 * Responsibility:
 */
public class ImageUtilTest {

    private static final String EXTENSION = "jpg";

    @Test
    public void testImageQuality() {
        ImageUtil util = new ImageUtil();

        try {
            ClassPathResource resource = new ClassPathResource("testdata/images/pink_camera.jpg");

            // this should return the same quality image
            BufferedImage bi = util.resize(resource.getInputStream(), 200, EXTENSION);

            ByteArrayOutputStream bos = new ByteArrayOutputStream();

            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(bos);
            JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(bi);
            param.setQuality(0.85f, true);

            encoder.setJPEGEncodeParam(param);
            encoder.encode(bi);

            ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
            BufferedImage encodedImage = ImageIO.read(bis);

            double distance = util.computeImageDistance(bi, encodedImage);

            assertTrue("Encoded image is too different from the original", distance < 10);

        } catch (IOException e) {
            fail(e.getMessage());
        } catch (UtilityException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testIsSolidBackground() throws Exception
    {
        ImageUtil util = new ImageUtil();
        String imageUrl = "http://ecx.images-amazon.com/images/I/41GRJZC2zKL._SL500_.jpg";
        UrlResource urlResource = new UrlResource(imageUrl);
        BufferedImage bufferedImage = ImageIO.read(urlResource.getInputStream());
        assertTrue("Could not detect solid background", util.isSolidBackground(bufferedImage));

        imageUrl = "http://images.lela.com/Crib-Pali-Milano-Crib-385/20120925/Crib-B003TJS5DI-250px.jpg";
        urlResource = new UrlResource(imageUrl);
        bufferedImage = ImageIO.read(urlResource.getInputStream());
        assertTrue("Could not detect solid background", util.isSolidBackground(bufferedImage));

        imageUrl = "http://c.shld.net/rpx/i/s/pi/mp/3198/6793081002p?src=http%3A%2F%2Fcommon1.csnimages.com%2Flf%2F49%2Fhash%2F11866%2F4365826%2F1%2F1.jpg&d=35664ccc4f870a583163d7b2d6b5452bfd37ea7b";
        urlResource = new UrlResource(imageUrl);
        bufferedImage = ImageIO.read(urlResource.getInputStream());
        assertFalse("Incorrectly detected solid background", util.isSolidBackground(bufferedImage));

        imageUrl = "http://ecx.images-amazon.com/images/I/51GdBKO3clL._SL500_.jpg";
        urlResource = new UrlResource(imageUrl);
        bufferedImage = ImageIO.read(urlResource.getInputStream());
        assertTrue("Could not detect solid background", util.isSolidBackground(bufferedImage));

        imageUrl = "http://ecx.images-amazon.com/images/I/41ukY4qpGFL._SL500_.jpg";
        urlResource = new UrlResource(imageUrl);
        bufferedImage = ImageIO.read(urlResource.getInputStream());
        assertFalse("Incorrectly detected solid background", util.isSolidBackground(bufferedImage));


    }

    /*
    @Test
    public void testImageQualityAfterResize() {
        ImageUtil util = new ImageUtil();
        FileOutputStream fis = null;
        
        try {
            ClassPathResource resource = new ClassPathResource("resize_resample.jpg");

            // this should return the same quality image
            BufferedImage bi = util.resize(resource.getInputStream(), 155, EXTENSION);

            ByteArrayOutputStream bos = new ByteArrayOutputStream();

            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(bos);
            JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(bi);
            param.setQuality(0.85f, true);

            encoder.setJPEGEncodeParam(param);
            encoder.encode(bi);

            File file = new File("test_155.jpg");
            fis = new FileOutputStream(file);
            ImageIO.write(bi, EXTENSION, fis);

        } catch (IOException e) {
            fail(e.getMessage());
        } finally {
            if (fis != null) {
                try {
                fis.flush();
                fis.close();
                } catch (IOException e) {
                    fail();
                }
            }
        }
    }
    */

    /*
    @Test
    public void testImagePadding() {
        ImageUtil util = new ImageUtil();
        FileOutputStream fis = null;

        try {
            ClassPathResource resource = new ClassPathResource("ok_image.jpg");

            BufferedImage bi = ImageIO.read(resource.getInputStream());

            // this should return the same quality image
            bi = util.padImageToDesiredSize(bi, 500);

            ByteArrayOutputStream bos = new ByteArrayOutputStream();

            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(bos);
            JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(bi);
            param.setQuality(0.85f, true);

            encoder.setJPEGEncodeParam(param);
            encoder.encode(bi);

            File file = new File("test_padded.jpg");
            fis = new FileOutputStream(file);
            ImageIO.write(bi, EXTENSION, fis);

        } catch (IOException e) {
            fail(e.getMessage());
        } finally {
            if (fis != null) {
                try {
                    fis.flush();
                    fis.close();
                } catch (IOException e) {
                    fail();
                }
            }
        }
    }
    */
}
