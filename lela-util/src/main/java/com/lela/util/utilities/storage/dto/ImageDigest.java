/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.util.utilities.storage.dto;

import org.apache.commons.lang.StringUtils;

import java.awt.image.BufferedImage;
import java.util.Map;

/**
 * Created by Bjorn Harvold
 * Date: 9/15/11
 * Time: 2:50 PM
 * Responsibility:
 */
public final class ImageDigest {
    private static final String JPG = "jpg";
    private static final String JPEG = "jpeg";
    private Map<Integer, String> imageUrls;
    private Map<Integer, String> relativeImageUrls;
    private String contentType;
    private BufferedImage originalImage;
    private BufferedImage originalPaddedImage;
    private byte[] originalImageBytes;
    private Map<Integer, BufferedImage> resizedImages;
    private Integer[] RGB;
    private String hex;

    private int originalImageHeight;
    private int originalImageWidth;

    public Map<Integer, String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(Map<Integer, String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public BufferedImage getOriginalImage() {
        return originalImage;
    }

    public void setOriginalImage(BufferedImage originalImage) {
        this.originalImage = originalImage;
    }

    public byte[] getOriginalImageBytes() {
        return originalImageBytes;
    }

    public void setOriginalImageBytes(byte[] originalImageBytes) {
        this.originalImageBytes = originalImageBytes;
    }

    public Map<Integer, BufferedImage> getResizedImages() {
        return resizedImages;
    }

    public void setResizedImages(Map<Integer, BufferedImage> resizedImages) {
        this.resizedImages = resizedImages;
    }

    public String getExtension() {
        String extension = contentType.split("/")[1];

        if (StringUtils.equals(extension, JPEG)) {
            extension = JPG;
        }

        return extension;
    }

    public Integer[] getRGB() {
        return RGB;
    }

    public void setRGB(Integer[] RGB) {
        this.RGB = RGB;
    }

    public String getHex() {
        return hex;
    }

    public void setHex(String hex) {
        this.hex = hex;
    }

    public Map<Integer, String> getRelativeImageUrls() {
        return relativeImageUrls;
    }

    public void setRelativeImageUrls(Map<Integer, String> relativeImageUrls) {
        this.relativeImageUrls = relativeImageUrls;
    }

    public BufferedImage getOriginalPaddedImage() {
        return originalPaddedImage;
    }

    public void setOriginalPaddedImage(BufferedImage originalPaddedImage) {
        this.originalPaddedImage = originalPaddedImage;
    }

    public int getOriginalImageHeight() {
        return originalImageHeight;
    }

    public void setOriginalImageHeight(int originalImageHeight) {
        this.originalImageHeight = originalImageHeight;
    }

    public int getOriginalImageWidth() {
        return originalImageWidth;
    }

    public void setOriginalImageWidth(int originalImageWidth) {
        this.originalImageWidth = originalImageWidth;
    }
}
