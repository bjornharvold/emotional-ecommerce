/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.util.utilities.storage;

import java.util.List;

import com.lela.util.utilities.storage.dto.FileData;

/**
 * Created by Bjorn Harvold
 * Date: 9/15/11
 * Time: 2:43 PM
 * Responsibility:
 */
public interface FileStorage {

	/**
	 * Get a storage URL for the file identified with the following name.
	 * @param fileName the relative file name
	 * @return the full path to the file in the store
	 */
	String absoluteUrl(String fileName);

	/**
	 * Put a file into storage.
	 * @param file the file data
	 * @return a URL that can be used to obtain the file
	 */
	String storeFile(FileData file);

    String[] listAllBuckets();
    
    List<String> listAllObjectsInABucket(String bucketName) ;
    
    void removeObjectFromBucket(String bucketName, String objectKey) ;
    
    String getObjectKeyForURL(String url);
    
    String getBucketName();
    
    boolean isUseBucketAsDomain();
}
