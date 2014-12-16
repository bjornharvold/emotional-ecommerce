/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.commons.service;

import com.lela.util.UtilityException;
import com.lela.util.utilities.storage.FileStorage;
import com.lela.util.utilities.storage.dto.ImageDigest;

import java.io.InputStream;

/**
 * Created by Bjorn Harvold
 * Date: 9/15/11
 * Time: 2:30 PM
 * Responsibility: This is the main interface fo the Lela utilities package
 */
public interface ImageUtilityService {

    ImageDigest ingestImage(String accessKey, String secretKey, String bucketName, String subDirectory, String imageName, String imageUrl, Integer[] sizes, Integer scaledImageSize) throws UtilityException;
    ImageDigest ingestImage(String accessKey, String secretKey, String bucketName, String subDirectory, String imageName, String imageUrl, Integer[] sizes, Double colorSwatchPercentageRectangle, Integer scaledImageSize) throws UtilityException;
    ImageDigest ingestImage(String accessKey, String secretKey, String bucketName, String subDirectory, String imageName, InputStream is, Integer[] sizes, Integer scaledImageSize) throws UtilityException;

    ImageDigest ingestScaledImage(String accessKey, String secretKey, String bucketName, String category, String imageName, String imageUrl, Integer scaledImageSize)throws UtilityException;

    ImageDigest retrieveExternalImage(String imageUrl) throws UtilityException;
    
    FileStorage createFileStorage(String accessKey,String secretKey, String bucketName);
}
