/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.commons.test.utilities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import com.lela.commons.service.ImageUtilityService;
import com.lela.commons.service.impl.ImageUtilityServiceImpl;
import com.lela.util.UtilityException;
import com.lela.util.utilities.storage.FileStorage;
import com.lela.util.utilities.storage.dto.FileData;
import com.lela.util.utilities.storage.dto.ImageDigest;
import com.lela.util.utilities.storage.s3.S3FileStorage;

/**
 * Created by Bjorn Harvold
 * Date: 3/27/11
 * Time: 8:32 PM
 * Responsibility:
 */
@RunWith(MockitoJUnitRunner.class)
public class UtilityServiceFunctionalTest {
    private final static Logger log = LoggerFactory.getLogger(UtilityServiceFunctionalTest.class);

    private final static ClassPathResource okImageResource = new ClassPathResource("testdata/images/ok_image.jpg");
    private final static ClassPathResource blackandwhiteImageResource = new ClassPathResource("testdata/images/blackandwhite.jpg");
    private final static ClassPathResource badExifImageResource = new ClassPathResource("testdata/images/bad_exif_data.jpg");
    private final static ClassPathResource tooSmallImageResource = new ClassPathResource("testdata/images/too_small.jpg");
    private final static ClassPathResource rectangularImageResource = new ClassPathResource("testdata/images/rectangular_image.jpg");
    private final static ClassPathResource badWalmartImageResource = new ClassPathResource("testdata/images/bad_walmart.jpg");
    private final static ClassPathResource cmykImageResource = new ClassPathResource("testdata/images/cmyk.jpg");
    private final static ClassPathResource incompleteRgbImageResource = new ClassPathResource("testdata/images/incomplete_rgb.jpg");
    
    private final static String accessKey = "AKIAJL3BN3SG5RPJOCCQ";
    private final static String secretKey = "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";
    private final static String bucketName = "testimages.lela.com";

    private final static String CATEGORY = "Test-Category";
    private final static String NO_ERRORS_IMAGE = "No-Errors";
    private final static Integer large = 500;
    private final static Integer list = 250;
    private final static Integer medium = 155;
    private final static Integer small = 44;
    
    @Mock private FileStorage fileStorage;    
    @Spy private ImageUtilityServiceImpl service;

    @Before
    public void beforeEach(){
    	when(service.createFileStorage(anyString(), anyString(), anyString())).thenReturn(fileStorage);
    	when(fileStorage.storeFile(any(FileData.class))).thenReturn("aFileUrl");
    }
    
    @Test
    public void testUtilityServiceWithNoErrors() {
        log.info("Testing ImageUtilityService regular square image");
        
        try {
            
            URL url = okImageResource.getURL();
            
            ImageDigest digest = service.ingestImage(accessKey, secretKey, bucketName, CATEGORY, NO_ERRORS_IMAGE, url.toString(), new Integer[]{small, medium, large}, list);
            assertNotNull("digest is null", digest);
            assertNotNull("original image is null", digest.getOriginalImage());
            assertNotNull("content type is null", digest.getContentType());
            assertNotNull("extension is null", digest.getExtension());
            assertNotNull("resized images is null", digest.getResizedImages());
            assertNotNull("image urls is null", digest.getImageUrls());
            assertNotNull("rgb is null", digest.getRGB());
            assertNotNull("hex is null", digest.getHex());

            assertEquals("content type is incorrect", "image/jpeg", digest.getContentType());
            assertEquals("extension is incorrect", "jpg", digest.getExtension());
            assertTrue("small size is incorrect", digest.getResizedImages().containsKey(small));
            assertTrue("medium size is incorrect", digest.getResizedImages().containsKey(medium));
            assertTrue("large size is incorrect", digest.getResizedImages().containsKey(large));
            //assertTrue("list size is incorrect", digest.getResizedImages().containsKey(list));
            assertTrue("small image url is incorrect", digest.getImageUrls().containsKey(small));
            assertTrue("medium image url is incorrect", digest.getImageUrls().containsKey(medium));
            assertTrue("large image url is incorrect", digest.getImageUrls().containsKey(large));
            assertTrue("list image url is incorrect", digest.getImageUrls().containsKey(list));

        } catch (UtilityException e) {
            log.error(e.getMessage(), e);
            fail(e.getMessage());
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            fail(e.getMessage());
        }

        log.info("Testing ImageUtilityService regular square image complete!");
    }

    @Test
    public void testUtilityServiceOnlyScaleImage() {
        log.info("Testing ImageUtilityService to scale an image...");

        // instantiate utility service
        //ImageUtilityService service = new ImageUtilityServiceImpl();

        try {

            URL url = rectangularImageResource.getURL();

            ImageDigest digest = service.ingestScaledImage(accessKey, secretKey, bucketName, CATEGORY, "Rectangular", url.toString(), list);
            assertNotNull("digest is null", digest);
            assertNotNull("original image is null", digest.getOriginalImage());
            assertNotNull("content type is null", digest.getContentType());
            assertNotNull("extension is null", digest.getExtension());
            assertNotNull("resized images is null", digest.getResizedImages());
            assertNotNull("image urls is null", digest.getImageUrls());
            assertNull("rgb is null", digest.getRGB());
            assertNull("hex is null", digest.getHex());
            assertNull("original padded image is null", digest.getOriginalPaddedImage());

            assertEquals("content type is incorrect", "image/jpeg", digest.getContentType());
            assertEquals("extension is incorrect", "jpg", digest.getExtension());
            //assertNull("image height is incorrect", digest.getOriginalImageHeight());
            //assertNull("image width is incorrect", digest.getOriginalImageWidth());

            assertFalse("small size is incorrect", digest.getResizedImages().containsKey(small));
            assertFalse("medium size is incorrect", digest.getResizedImages().containsKey(medium));
            assertFalse("large size is incorrect", digest.getResizedImages().containsKey(large));
            assertTrue("large size is incorrect", digest.getResizedImages().containsKey(list));
            assertFalse("small image url is incorrect", digest.getImageUrls().containsKey(small));
            assertFalse("medium image url is incorrect", digest.getImageUrls().containsKey(medium));
            assertFalse("large image url is incorrect", digest.getImageUrls().containsKey(large));
            assertTrue("large image url is incorrect", digest.getImageUrls().containsKey(list));

        } catch (UtilityException e) {
            log.error(e.getMessage(), e);
            fail(e.getMessage());
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            fail(e.getMessage());
        }

        log.info("Testing ImageUtilityService with rectangular image complete!");
    }

    @Test
    public void testUtilityServiceWithBlackImage() {
        log.info("Testing ImageUtilityService B&W image...");

        // instantiate utility service
        //ImageUtilityService service = new ImageUtilityServiceImpl();

        try {
            URL url = blackandwhiteImageResource.getURL();

            ImageDigest digest = service.ingestImage(accessKey, secretKey, bucketName, CATEGORY, "Black-Image", url.toString(), new Integer[]{small, medium, large}, list);
            assertNotNull("digest is null", digest);
            assertNotNull("original image is null", digest.getOriginalImage());
            assertNotNull("content type is null", digest.getContentType());
            assertNotNull("extension is null", digest.getExtension());
            assertNotNull("resized images is null", digest.getResizedImages());
            assertNotNull("image urls is null", digest.getImageUrls());
            assertNotNull("rgb is null", digest.getRGB());
            assertNotNull("hex is null", digest.getHex());

            assertEquals("content type is incorrect", "image/jpeg", digest.getContentType());
            assertEquals("extension is incorrect", "jpg", digest.getExtension());
            assertTrue("small size is incorrect", digest.getResizedImages().containsKey(small));
            assertTrue("medium size is incorrect", digest.getResizedImages().containsKey(medium));
            assertTrue("large size is incorrect", digest.getResizedImages().containsKey(large));
            assertTrue("small image url is incorrect", digest.getImageUrls().containsKey(small));
            assertTrue("medium image url is incorrect", digest.getImageUrls().containsKey(medium));
            assertTrue("large image url is incorrect", digest.getImageUrls().containsKey(large));

        } catch (UtilityException e) {
            log.error(e.getMessage(), e);
            fail(e.getMessage());
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            fail(e.getMessage());
        }

        log.info("Testing ImageUtilityService B&W image complete!");
    }

    @Test
    public void testUtilityServiceWithImageWithBadExifData() {
        log.info("Testing ImageUtilityService bad exif data...");

        // instantiate utility service
        //ImageUtilityService service = new ImageUtilityServiceImpl();

        try {
            URL url = badExifImageResource.getURL();

            ImageDigest digest = service.ingestImage(accessKey, secretKey, bucketName, CATEGORY, "Bad-Exif", url.toString(), new Integer[]{small, medium, large}, list);
            assertNotNull("digest is null", digest);
            assertNotNull("original image is null", digest.getOriginalImage());
            assertNotNull("content type is null", digest.getContentType());
            assertNotNull("extension is null", digest.getExtension());
            assertNotNull("resized images is null", digest.getResizedImages());
            assertNotNull("image urls is null", digest.getImageUrls());
            assertNotNull("rgb is null", digest.getRGB());
            assertNotNull("hex is null", digest.getHex());

            assertEquals("content type is incorrect", "image/jpeg", digest.getContentType());
            assertEquals("extension is incorrect", "jpg", digest.getExtension());
            assertTrue("small size is incorrect", digest.getResizedImages().containsKey(small));
            assertTrue("medium size is incorrect", digest.getResizedImages().containsKey(medium));
            assertTrue("large size is incorrect", digest.getResizedImages().containsKey(large));
            assertTrue("small image url is incorrect", digest.getImageUrls().containsKey(small));
            assertTrue("medium image url is incorrect", digest.getImageUrls().containsKey(medium));
            assertTrue("large image url is incorrect", digest.getImageUrls().containsKey(large));

        } catch (UtilityException e) {
            log.error(e.getMessage(), e);
            fail(e.getMessage());
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            fail(e.getMessage());
        }

        log.info("Testing ImageUtilityService bad exif data complete!");
    }

    @Test
    public void testUtilityServiceWithImageWithCmyk() {
        log.info("Testing ImageUtilityService bad exif data...");

        // instantiate utility service
        //ImageUtilityService service = new ImageUtilityServiceImpl();

        try {
            URL url = cmykImageResource.getURL();

            ImageDigest digest = service.ingestImage(accessKey, secretKey, bucketName, CATEGORY, "CMYK", url.toString(), new Integer[]{small, medium, large}, list);
            assertNotNull("digest is null", digest);
            assertNotNull("original image is null", digest.getOriginalImage());
            assertNotNull("content type is null", digest.getContentType());
            assertNotNull("extension is null", digest.getExtension());
            assertNotNull("resized images is null", digest.getResizedImages());
            assertNotNull("image urls is null", digest.getImageUrls());
            assertNotNull("rgb is null", digest.getRGB());
            assertNotNull("hex is null", digest.getHex());

            assertEquals("content type is incorrect", "image/jpeg", digest.getContentType());
            assertEquals("extension is incorrect", "jpg", digest.getExtension());
            assertTrue("small size is incorrect", digest.getResizedImages().containsKey(small));
            assertTrue("medium size is incorrect", digest.getResizedImages().containsKey(medium));
            assertTrue("large size is incorrect", digest.getResizedImages().containsKey(large));
            assertTrue("small image url is incorrect", digest.getImageUrls().containsKey(small));
            assertTrue("medium image url is incorrect", digest.getImageUrls().containsKey(medium));
            assertTrue("large image url is incorrect", digest.getImageUrls().containsKey(large));

        } catch (UtilityException e) {
            log.error(e.getMessage(), e);
            fail(e.getMessage());
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            fail(e.getMessage());
        }

        log.info("Testing ImageUtilityService bad exif data complete!");
    }

    @Test
    public void testUtilityServiceWithRectangularImage() {
        log.info("Testing ImageUtilityService with rectangular image...");

        // instantiate utility service
        //ImageUtilityService service = new ImageUtilityServiceImpl();

        try {

            URL url = rectangularImageResource.getURL();

            ImageDigest digest = service.ingestImage(accessKey, secretKey, bucketName, CATEGORY, "Rectangular", url.toString(), new Integer[]{small, medium, large}, list);
            assertNotNull("digest is null", digest);
            assertNotNull("original image is null", digest.getOriginalImage());
            assertNotNull("content type is null", digest.getContentType());
            assertNotNull("extension is null", digest.getExtension());
            assertNotNull("resized images is null", digest.getResizedImages());
            assertNotNull("image urls is null", digest.getImageUrls());
            assertNotNull("rgb is null", digest.getRGB());
            assertNotNull("hex is null", digest.getHex());
            assertNotNull("original padded image is null", digest.getOriginalPaddedImage());

            assertEquals("content type is incorrect", "image/jpeg", digest.getContentType());
            assertEquals("extension is incorrect", "jpg", digest.getExtension());
            assertEquals("image height is incorrect", 225, digest.getOriginalImageHeight());
            assertEquals("image width is incorrect", 300, digest.getOriginalImageWidth());

            assertTrue("small size is incorrect", digest.getResizedImages().containsKey(small));
            assertTrue("medium size is incorrect", digest.getResizedImages().containsKey(medium));
            assertTrue("large size is incorrect", digest.getResizedImages().containsKey(large));
            assertTrue("small image url is incorrect", digest.getImageUrls().containsKey(small));
            assertTrue("medium image url is incorrect", digest.getImageUrls().containsKey(medium));
            assertTrue("large image url is incorrect", digest.getImageUrls().containsKey(large));

        } catch (UtilityException e) {
            log.error(e.getMessage(), e);
            fail(e.getMessage());
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            fail(e.getMessage());
        }

        log.info("Testing ImageUtilityService with rectangular image complete!");
    }

    @Test
    public void testUtilityServiceWithTooSmallImage() {
        log.info("Testing ImageUtilityService with too small image...");

        // instantiate utility service
        //ImageUtilityService service = new ImageUtilityServiceImpl();

        try {
            URL url = tooSmallImageResource.getURL();

            ImageDigest digest = service.ingestImage(accessKey, secretKey, bucketName, CATEGORY, "Too-Small", url.toString(), new Integer[]{small, medium, large}, list);
            assertNotNull("digest is null", digest);
            assertNotNull("original image is null", digest.getOriginalImage());
            assertNotNull("content type is null", digest.getContentType());
            assertNotNull("extension is null", digest.getExtension());
            assertNotNull("resized images is null", digest.getResizedImages());
            assertNotNull("image urls is null", digest.getImageUrls());
            assertNotNull("rgb is null", digest.getRGB());
            assertNotNull("hex is null", digest.getHex());
            assertNotNull("original padded image is null", digest.getOriginalPaddedImage());

            assertEquals("content type is incorrect", "image/jpeg", digest.getContentType());
            assertEquals("extension is incorrect", "jpg", digest.getExtension());
            assertTrue("small size is incorrect", digest.getResizedImages().containsKey(small));
            assertTrue("medium size is incorrect", digest.getResizedImages().containsKey(medium));
            assertTrue("large size is incorrect", digest.getResizedImages().containsKey(large));
            assertTrue("small image url is incorrect", digest.getImageUrls().containsKey(small));
            assertTrue("medium image url is incorrect", digest.getImageUrls().containsKey(medium));
            assertTrue("large image url is incorrect", digest.getImageUrls().containsKey(large));
            
            BufferedImage bi = digest.getResizedImages().get(large);
            assertEquals("Image size is incorrect", 500, bi.getWidth(), 0);

        } catch (UtilityException e) {
            log.error(e.getMessage(), e);
            fail(e.getMessage());
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            fail(e.getMessage());
        }

        log.info("Testing ImageUtilityService with to small image complete!");
    }

    @Test
    public void testUtilityServiceWithBadWalmartImage() {
        log.info("Testing ImageUtilityService with bad walmart image...");

        // instantiate utility service
        //ImageUtilityService service = new ImageUtilityServiceImpl();

        try {
            URL url = badWalmartImageResource.getURL();

            log.info("This test should throw an exception becaus the image is a bad one");
            service.ingestImage(accessKey, secretKey, bucketName, CATEGORY, "Bad-Walmart", url.toString(), new Integer[]{small, medium, large}, list);
            fail("Method call should've thrown an error");
        } catch (UtilityException e) {
            log.info("Method call successfully threw an exception");
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            fail(e.getMessage());
        }

        log.info("Testing ImageUtilityService with bad walmart image complete!");
    }

    @Test
    public void testUtilityServiceWithSolidBackGroundImages() {
        log.info("Testing ImageUtilityService with too small image...");

        // instantiate utility service
        //ImageUtilityService service = new ImageUtilityServiceImpl();

        try {
            String imageUrl = "http://ecx.images-amazon.com/images/I/41GRJZC2zKL._SL500_.jpg";
            ImageDigest digest = service.ingestScaledImage(accessKey, secretKey, bucketName, CATEGORY, "Scaled1", imageUrl, list);
            assertTrue("large image url is incorrect", digest.getImageUrls().containsKey(list));

            imageUrl = "http://images.lela.com/Crib-Pali-Milano-Crib-385/20120925/Crib-B003TJS5DI-250px.jpg";
            digest = service.ingestScaledImage(accessKey, secretKey, bucketName, CATEGORY, "Scaled2", imageUrl, list);
            assertTrue("large image url is incorrect", digest.getImageUrls().containsKey(list));

            imageUrl = "http://c.shld.net/rpx/i/s/pi/mp/3198/6793081002p?src=http%3A%2F%2Fcommon1.csnimages.com%2Flf%2F49%2Fhash%2F11866%2F4365826%2F1%2F1.jpg&d=35664ccc4f870a583163d7b2d6b5452bfd37ea7b";
            digest = service.ingestScaledImage(accessKey, secretKey, bucketName, CATEGORY, "Scaled3", imageUrl, list);
            assertTrue("large image url is incorrect", digest.getImageUrls().containsKey(list));

            imageUrl = "http://ecx.images-amazon.com/images/I/51GdBKO3clL._SL500_.jpg";
            digest = service.ingestScaledImage(accessKey, secretKey, bucketName, CATEGORY, "Scaled4", imageUrl, list);
            assertTrue("large image url is incorrect", digest.getImageUrls().containsKey(list));

            imageUrl = "http://ecx.images-amazon.com/images/I/41ukY4qpGFL._SL500_.jpg";
            digest = service.ingestScaledImage(accessKey, secretKey, bucketName, CATEGORY, "Scaled5", imageUrl, list);
            assertTrue("large image url is incorrect", digest.getImageUrls().containsKey(list));
        }
        catch(UtilityException e){
            fail(e.getMessage());
        }

    }
}