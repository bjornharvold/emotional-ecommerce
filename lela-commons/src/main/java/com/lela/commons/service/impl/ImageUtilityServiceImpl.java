/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.commons.service.impl;

//~--- non-JDK imports --------------------------------------------------------

import java.awt.Color;
import java.awt.color.CMMException;
import java.awt.image.BufferedImage;
import java.awt.image.renderable.ParameterBlock;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import javax.media.jai.JAI;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.sanselan.ImageFormat;
import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.Sanselan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import com.lela.commons.service.ImageUtilityService;
import com.lela.util.UtilityException;
import com.lela.util.utilities.image.ImageTrim;
import com.lela.util.utilities.image.ImageUtil;
import com.lela.util.utilities.storage.FileStorage;
import com.lela.util.utilities.storage.dto.FileData;
import com.lela.util.utilities.storage.dto.ImageDigest;
import com.lela.util.utilities.storage.s3.S3FileStorage;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import com.sun.media.jai.codec.ByteArraySeekableStream;
import com.sun.media.jai.codec.SeekableStream;

//~--- JDK imports ------------------------------------------------------------

//~--- classes ----------------------------------------------------------------

/**
 * Created by Bjorn Harvold
 * Date: 9/15/11
 * Time: 2:54 PM
 * Responsibility:
 */
@Service("imageUtilityService")
public class ImageUtilityServiceImpl implements ImageUtilityService {

    /**
     * Field description
     */
    private final static Logger log = LoggerFactory.getLogger(ImageUtilityServiceImpl.class);

    /**
     * Field description
     */
    private static final double COLOR_SWATCH_PERCENTAGE_RECTANGLE = 0.15;

    /**
     * Field description
     */
    private static final List<String> IMAGE_TYPES = Arrays.asList("image/jpeg", "image/jpg", "image/gif", "image/png");

    /**
     * Field description
     */
    private final static ImageUtil imageUtil = new ImageUtil();

    private final static DateFormat folderFormat = new SimpleDateFormat("yyyyMMdd");

    private final ClassPathResource walmartNoImageAvailable = new ClassPathResource("images/walmart_no_image_available.jpg");

    private BufferedImage walmartNoImageAvailableBI;

    private FileStorage storage;
    
    //~--- methods ------------------------------------------------------------


    public ImageUtilityServiceImpl() {
        // initiate walmart image
        try {
            walmartNoImageAvailableBI = ImageIO.read(walmartNoImageAvailable.getInputStream());
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * Method description
     *
     * @param accessKey  accessKey
     * @param secretKey  secretKey
     * @param bucketName bucketName
     * @param imageUrl   imageUrl
     * @param sizes      sizes
     * @return Return value
     * @throws UtilityException UtilityException
     */
    @Override
    public ImageDigest ingestImage(String accessKey, String secretKey, String bucketName, String category, String imageName, String imageUrl,
                                   Integer[] sizes, Integer scaledImageSize)
            throws UtilityException {
        return ingestImage(accessKey, secretKey, bucketName, category, imageName, imageUrl, sizes, COLOR_SWATCH_PERCENTAGE_RECTANGLE, scaledImageSize);
    }

    /**
     * Method description
     *
     * @param accessKey  accessKey
     * @param secretKey  secretKey
     * @param bucketName bucketName
     * @param imageUrl   imageUrl
     * @return Return value
     * @throws UtilityException UtilityException
     */
    @Override
    public ImageDigest ingestScaledImage(String accessKey, String secretKey, String bucketName, String category, String imageName, String imageUrl, Integer scaledOriginalSize)
            throws UtilityException {
        if (StringUtils.isBlank(accessKey)) {
            throw new UtilityException("accessKey cannot be null");
        }

        if (StringUtils.isBlank(secretKey)) {
            throw new UtilityException("secretKey cannot be null");
        }

        if (StringUtils.isBlank(bucketName)) {
            throw new UtilityException("bucketName cannot be null");
        }

        if (StringUtils.isBlank(imageUrl)) {
            throw new UtilityException("imageUrl cannot be null");
        }

        ImageDigest digest = new ImageDigest();

        // retrieve file from remote host
        retrieveExternalImage(digest, imageUrl, true);

        //resize unpadded
        resizeUnpaddedImage(digest, scaledOriginalSize);

        // save to file storage
        saveImagesToStorage(digest, accessKey, secretKey, bucketName, category, imageName);

        return digest;
    }

    /**
     * Method description
     *
     * @param accessKey                      accessKey
     * @param secretKey                      secretKey
     * @param bucketName                     bucketName
     * @param imageUrl                       imageUrl
     * @param sizes                          sizes
     * @param colorSwatchPercentageRectangle colorSwatchPercentageRectangle
     * @return Return value
     * @throws UtilityException UtilityException
     */
    @Override
    public ImageDigest ingestImage(String accessKey, String secretKey, String bucketName, String subDirectory, String imageName, String imageUrl,
                                   Integer[] sizes, Double colorSwatchPercentageRectangle, Integer scaledImageSize)
            throws UtilityException {
        if (StringUtils.isBlank(accessKey)) {
            throw new UtilityException("accessKey cannot be null");
        }

        if (StringUtils.isBlank(secretKey)) {
            throw new UtilityException("secretKey cannot be null");
        }

        if (StringUtils.isBlank(bucketName)) {
            throw new UtilityException("bucketName cannot be null");
        }

        if (StringUtils.isBlank(imageUrl)) {
            throw new UtilityException("imageUrl cannot be null");
        }

        ImageDigest digest = new ImageDigest();

        // retrieve file from remote host
        retrieveExternalImage(digest, imageUrl, true);

        //catpure original height, width
        determineOriginalImageSize(digest);

        // add white space to rectangular images
        padExternalImageIfNecessary(digest);

        // determine image segmentation
        determineImageColor(digest, colorSwatchPercentageRectangle);

        // resize
        resizeImage(digest, sizes);

        //resize unpadded
        resizeUnpaddedImage(digest, scaledImageSize);

        // save to file storage
        saveImagesToStorage(digest, accessKey, secretKey, bucketName, subDirectory, imageName);

        return digest;
    }

    @Override
    public ImageDigest ingestImage(String accessKey, String secretKey, String bucketName, String subDirectory, String imageName, InputStream is, Integer[] sizes, Integer scaledImageSize) throws UtilityException {

        ImageDigest digest = new ImageDigest();

        // retrieve file from remote host
        retrieveExternalImage(digest, is);

        //catpure original height, width
        determineOriginalImageSize(digest);

        // add white space to rectangular images
        padExternalImageIfNecessary(digest);

        // resize
        resizeImage(digest, sizes);

        //resize unpadded
        resizeUnpaddedImage(digest, scaledImageSize);

        // save to file storage
        saveImagesToStorage(digest, accessKey, secretKey, bucketName, subDirectory, imageName);

        return digest;
    }

    /**
     * Method description
     *
     * @param imageBytes imageBytes
     * @throws UtilityException UtilityException
     */
    private void assertBytesLength(byte[] imageBytes) throws UtilityException {
        if (imageBytes.length == 0) {
            throw new UtilityException("Cannot accept empty picture byte[] as a profile picture");
        }
    }

    /**
     * Method description
     *
     * @param contentType contentType
     * @throws UtilityException UtilityException
     */
    private void assertContentType(String url, String contentType) throws UtilityException {
        if (!IMAGE_TYPES.contains(contentType)) {
            throw new UtilityException("Cannot accept content type '" + contentType + "' as a url: " + url + " is not an image.");
        }
    }

    /**
     * This method will capture the original height and width of the image.
     *
     * @param digest digest
     */
    private void determineOriginalImageSize(ImageDigest digest) {
        BufferedImage originalImage = digest.getOriginalImage();

        digest.setOriginalImageHeight(originalImage.getHeight());
        digest.setOriginalImageWidth(originalImage.getWidth());
    }

    /**
     * This method will go to the center of the image and move out a certain percentage and create an area from
     * where it can extract the mean rgb values of that area
     *
     * @param digest                         digest
     * @param colorSwatchPercentageRectangle colorSwatchPercentageRectangle
     */
    private void determineImageColor(ImageDigest digest, Double colorSwatchPercentageRectangle) {
        BufferedImage originalImage = digest.getOriginalPaddedImage() != null ? digest.getOriginalPaddedImage() : digest.getOriginalImage();
        Integer[] meanRGB = imageUtil.measureRGB(originalImage, colorSwatchPercentageRectangle);

        // set average rgb values
        digest.setRGB(meanRGB);
        digest.setHex(imageUtil.toHex(meanRGB[0]) + imageUtil.toHex(meanRGB[1]) + imageUtil.toHex(meanRGB[2]));
    }

    /**
     * Method description
     *
     * @param imageBytes imageBytes
     * @return Return value
     * @throws IOException IOException
     */
    private String guessContentType(byte[] imageBytes) throws IOException {
        return URLConnection.guessContentTypeFromStream(new ByteArrayInputStream(imageBytes));
    }

    /**
     * If an image is determined to be rectangular in either width or height, we pad it with whitespace
     * to make it square with the original image in the middle.
     *
     * @param digest digest
     */
    private void padExternalImageIfNecessary(ImageDigest digest) {
        BufferedImage image = digest.getOriginalImage();

        if (image.getWidth() > image.getHeight()) {
            BufferedImage newImage = imageUtil.padImageHeightAndCenter(image);
            digest.setOriginalPaddedImage(newImage);
        } else if (image.getWidth() < image.getHeight()) {
            BufferedImage newImage = imageUtil.padImageWidthAndCenter(image);
            digest.setOriginalPaddedImage(newImage);
        }
    }

    /**
     * Resize an image into requested sizes
     *
     * @param digest digest
     * @param sizes  sizes
     * @throws UtilityException UtilityException
     */
    private void resizeImage(ImageDigest digest, Integer[] sizes) throws UtilityException {
        Map<Integer, BufferedImage> resizedImages = new HashMap<Integer, BufferedImage>();
        BufferedImage originalImage = digest.getOriginalPaddedImage() != null ? digest.getOriginalPaddedImage() : digest.getOriginalImage();

        try {
            for (Integer size : sizes) {
                resizedImages.put(size, imageUtil.resize(originalImage, size));
            }

            digest.setResizedImages(resizedImages);
        } catch (IOException e) {
            log.error(e.getMessage(), e);

            throw new UtilityException(e.getMessage(), e);
        }
    }

    private void resizeUnpaddedImage(ImageDigest digest, Integer size) throws UtilityException
    {


        if(digest.getResizedImages() == null)
        {
            Map<Integer, BufferedImage> resizedImages = new HashMap<Integer, BufferedImage>();
            digest.setResizedImages(resizedImages);
        }

        if(size > 0)
        {
            BufferedImage originalImage = digest.getOriginalImage();

            if(imageUtil.isSolidBackground(originalImage))
            {
                int WHITE = new Color(246, 246, 246).getRGB();
                ImageTrim imageTrim = new ImageTrim(WHITE);
                originalImage = imageTrim.trim(originalImage);
            }


            digest.getResizedImages().put(size, imageUtil.scale(originalImage, size));
        }
    }

    @Override
    public ImageDigest retrieveExternalImage(String imageUrl) throws UtilityException {
        if (StringUtils.isBlank(imageUrl)) {
            throw new UtilityException("imageUrl cannot be null");
        }

        ImageDigest result = new ImageDigest();

        retrieveExternalImage(result, imageUrl, false);

        return result;
    }

    /**
     * Retrieves an image externally and saves it in a BufferedImage
     *
     * @param digest   digest
     * @param imageUrl imageUrl
     * @throws UtilityException UtilityException
     */
    private void retrieveExternalImage(ImageDigest digest, String imageUrl, boolean validate) throws UtilityException {
        InputStream is = null;

        try {
            // grab the resource specified by the url
            UrlResource resource = new UrlResource(imageUrl);

            // get input stream
            is = resource.getInputStream();

            // get byte[]
            byte[] imageBytes = IOUtils.toByteArray(is);

            // validate byte array
            assertBytesLength(imageBytes);

            // get content type
            String contentType = guessContentType(imageBytes);

            // validate content type
            assertContentType(imageUrl, contentType);

            // set all result on the digest object
            digest.setContentType(contentType);
            digest.setOriginalImageBytes(imageBytes);

            BufferedImage bi = readBufferedImage(imageBytes, digest.getExtension(), validate);

            digest.setOriginalImage(bi);
        } catch (MalformedURLException e) {
            log.error(e.getMessage(), e);

            throw new UtilityException(e.getMessage(), e);
        } catch (IOException e) {
            log.error(e.getMessage(), e);

            throw new UtilityException(e.getMessage(), e);
        } finally {
            if (is != null) {
                IOUtils.closeQuietly(is);
            }
        }
    }

    /**
     * Retrieves an image externally and saves it in a BufferedImage
     *
     * @param digest digest
     * @param is     is
     * @throws UtilityException UtilityException
     */
    private void retrieveExternalImage(ImageDigest digest, InputStream is) throws UtilityException {

        try {
            // get byte[]
            byte[] imageBytes = IOUtils.toByteArray(is);

            // validate byte array
            assertBytesLength(imageBytes);

            // get content type
            String contentType = guessContentType(imageBytes);

            // validate content type
            assertContentType("inputstream_only", contentType);

            // set all result on the digest object
            digest.setContentType(contentType);
            digest.setOriginalImageBytes(imageBytes);

            BufferedImage bi = readBufferedImage(imageBytes, digest.getExtension(), false);

            digest.setOriginalImage(bi);
        } catch (MalformedURLException e) {
            log.error(e.getMessage(), e);

            throw new UtilityException(e.getMessage(), e);
        } catch (IOException e) {
            log.error(e.getMessage(), e);

            throw new UtilityException(e.getMessage(), e);
        } finally {
            if (is != null) {
                IOUtils.closeQuietly(is);
            }
        }
    }

    private BufferedImage readBufferedImage(byte[] imageBytes, String extension, boolean validate) throws IOException, UtilityException {


        BufferedImage bi = null;

        try {
            bi = ImageIO.read(new ByteArrayInputStream(imageBytes));

            if (validate) {
                // validate image before continuing - throws exception if fails
                validateExternalImageAgainstWalmartNoImageAvailable(bi);
            }

        } catch (CMMException ex) {
            log.warn(
                    "Image could not be read by the ImageIO class most likely due to bad exif / jfif data in the image.");
            log.info("Attempting to read image using JAI...");

            SeekableStream seekableStream = new ByteArraySeekableStream(imageBytes);
            ParameterBlock pb = new ParameterBlock();

            pb.add(seekableStream);

            if (StringUtils.equals(extension, "jpg")) {
                bi = JAI.create("jpeg", pb).getAsBufferedImage();
            } else if (StringUtils.equals(extension, "png")) {
                bi = JAI.create("png", pb).getAsBufferedImage();
            } else if (StringUtils.equals(extension, "gif")) {
                bi = JAI.create("gif", pb).getAsBufferedImage();
            }

            log.info("Reading image using JAI worked. Continuing.");
        } catch (IIOException e) {
            log.error(e.getMessage(), e);

            //if this is a jpeg image it may just be malformed and we can
            //try reading it again
            try {
                if (Sanselan.getImageInfo(imageBytes).getFormat() == ImageFormat.IMAGE_FORMAT_JPEG) {
                    bi = imageUtil.readMalformedJpeg(imageBytes);
                }
                //it's not a jpeg so we'll just keep bubbling up the original error
                else {
                    throw e;
                }

            } catch (ImageReadException imageReadException) {
                //couldn't read the image, it's very messed up
                throw e;
            }
        }
        return bi;
    }

    private void validateExternalImageAgainstWalmartNoImageAvailable(BufferedImage bi) throws UtilityException {
        double distance = imageUtil.computeImageDistance(walmartNoImageAvailableBI, bi);

        if (distance == 0.0) {
            throw new UtilityException("Image is a bad Walmart image");
        }
    }

    /**
     * Method description
     *
     * @param digest     digest
     * @param accessKey  accessKey
     * @param secretKey  secretKey
     * @param bucketName bucketName
     * @throws UtilityException UtilityException
     */
    private void saveImagesToStorage(ImageDigest digest, String accessKey, String secretKey, String bucketName, String subDirectory, String imageName)
            throws UtilityException {
        try {
            //storage = new S3FileStorage(accessKey, secretKey, bucketName, true);
        	storage = createFileStorage(accessKey, secretKey, bucketName);
            Map<Integer, String> imageUrls = new HashMap<Integer, String>(digest.getResizedImages().size());
            Map<Integer, String> relativeImageUrls = new HashMap<Integer, String>(digest.getResizedImages().size());

            for (Integer size : digest.getResizedImages().keySet()) {
                BufferedImage image = digest.getResizedImages().get(size);
                ByteArrayOutputStream bos = new ByteArrayOutputStream();

                // if the image is a jpg we need to compress using the jpeg codec
                // as we can control the quality
                if (StringUtils.equals(digest.getExtension(), "jpg")) {
                    JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(bos);
                    JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(image);
                    param.setQuality(0.85f, true);

                    encoder.setJPEGEncodeParam(param);
                    encoder.encode(image);
                } else {
                    ImageIO.write(image, digest.getExtension(), bos);
                }

                String dateSubDirectory = folderFormat.format(new Date());

                StringBuilder fileName = new StringBuilder();
                if (StringUtils.isNotBlank(subDirectory)) {
                    fileName.append(subDirectory).append("/");
                }

                fileName.append(dateSubDirectory).append("/");
                fileName.append(imageName).append("-").append(size).append("px").append(".").append(digest.getExtension());

                String imageUrl = storage.storeFile(new FileData(fileName.toString(), bos.toByteArray(), digest.getContentType()));

                relativeImageUrls.put(size, fileName.toString());
                imageUrls.put(size, imageUrl);
            }

            digest.setRelativeImageUrls(relativeImageUrls);
            digest.setImageUrls(imageUrls);
        } catch (IOException e) {
            log.error(e.getMessage(), e);

            throw new UtilityException(e.getMessage(), e);
        }
    }
    
    //Introduced public method only for mocking out FileStorage during testing
    public FileStorage createFileStorage(String accessKey, String secretKey, String bucketName){
    	return new S3FileStorage(accessKey, secretKey, bucketName, true);
    }
}
