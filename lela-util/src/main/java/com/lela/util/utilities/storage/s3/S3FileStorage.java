/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.util.utilities.storage.s3;

import com.lela.util.utilities.storage.FileStorage;
import com.lela.util.utilities.storage.dto.FileData;

import org.apache.commons.lang.StringUtils;
import org.jets3t.service.S3Service;
import org.jets3t.service.S3ServiceException;
import org.jets3t.service.ServiceException;
import org.jets3t.service.acl.AccessControlList;
import org.jets3t.service.acl.GroupGrantee;
import org.jets3t.service.acl.Permission;
import org.jets3t.service.impl.rest.httpclient.RestS3Service;
import org.jets3t.service.model.S3Bucket;
import org.jets3t.service.model.S3Object;
import org.jets3t.service.security.AWSCredentials;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bjorn Harvold
 * Date: 9/15/11
 * Time: 2:41 PM
 * Responsibility: Uploads a file to S3
 */
public class S3FileStorage implements FileStorage {

    private static final String S3_AMAZONAWS_COM = ".s3.amazonaws.com";
    private final AWSCredentials awsCredentials;

    private final String bucketName;
    private final boolean useBucketAsDomain;

    /**
     * Creates a S3-based file storage.
     *
     * @param accessKey       the S3 access key
     * @param secretAccessKey S3 the secret
     * @param bucketName      the bucket in your account where files should be stored
     */
    public S3FileStorage(String accessKey, String secretAccessKey, String bucketName) {
        this(accessKey, secretAccessKey, bucketName, false);
    }

    public S3FileStorage(String accessKey, String secretAccessKey, String bucketName, boolean useBucketAsDomain) {
        awsCredentials = new AWSCredentials(accessKey, secretAccessKey);
        this.bucketName = bucketName;
        this.useBucketAsDomain = useBucketAsDomain;
    }

    public String absoluteUrl(String fileName) {
        if (useBucketAsDomain) {
            return "http://" + bucketName + "/" + fileName;
        } else {
            return "http://" + bucketName + S3_AMAZONAWS_COM + "/" + fileName;
        }
    }

    public String[] listAllBuckets() {
        List<String> result = null;

        try {
            S3Service s3 = createS3Service();
            S3Bucket[] s3Buckets = s3.listAllBuckets();
            shutdownS3Service(s3);
            if (s3Buckets != null) {
                result = new ArrayList<String>(s3Buckets.length);

                for (S3Bucket s3Bucket : s3Buckets) {
                    result.add(s3Bucket.getName());
                }
            }
        } catch (S3ServiceException e) {
            throw new RuntimeException("Unable to list all buckets from S3", e);
        }

        return result.toArray(new String[result.size()]);
    }

    public List<String> listAllObjectsInABucket(String bucketName) {
        List<String> result = null;

        try {
            S3Service s3 = createS3Service();
            S3Object[] s3Objects = s3.listObjects(bucketName);
            shutdownS3Service(s3);
            if (s3Objects != null) {
                result = new ArrayList<String>(s3Objects.length);

                for (S3Object s3Object : s3Objects) {
                    result.add(s3Object.getName());
                }
            }
        } catch (S3ServiceException e) {
            throw new RuntimeException("Unable to list all buckets from S3", e);
        }

        return result;
    }

    public void removeObjectFromBucket(String bucketName, String objectKey) {
        List<String> result = null;

        try {
            S3Service s3 = createS3Service();
            s3.deleteObject(bucketName, objectKey);
            shutdownS3Service(s3);
        } catch (ServiceException e) {
            throw new RuntimeException(String.format("Unable to delete object S3 with bucketName %s and key %s", bucketName, objectKey),  e);
        }
    }

    public String storeFile(FileData file) {
        S3Service s3 = createS3Service();
        S3Bucket bucket;
        try {
            bucket = s3.getBucket(bucketName);
            if (bucket == null) {
            	throw new IllegalStateException(String.format("Unable to retrieve S3 Bucket with name %s", bucketName) );
            }
        } catch (S3ServiceException e) {
            throw new IllegalStateException("Unable to retrieve S3 Bucket", e);
        }
        S3Object object = new S3Object(file.getName());
        object.setDataInputStream(new ByteArrayInputStream(file.getBytes()));
        object.setContentLength(file.getBytes().length);
        object.setContentType(file.getContentType());
        AccessControlList acl = new AccessControlList();
        acl.setOwner(bucket.getOwner());
        acl.grantPermission(GroupGrantee.ALL_USERS, Permission.PERMISSION_READ);
        object.setAcl(acl);
        try {
            s3.putObject(bucket, object);
        } catch (S3ServiceException e) {
            throw new RuntimeException("Unable to put object into S3", e);
        }
        shutdownS3Service(s3);
        return absoluteUrl(file.getName());
    }

    /**
     * Returns a key for a given URL.
     * This key can be used to manipulate a stored S3Object
     * Since we are storing the file name for the s3 object key, parse out the file name from the URL
     * @param url
     * @return
     */
    public String getObjectKeyForURL(String url){
    	String s = null;
    	if (url.endsWith("/")){
    		url = StringUtils.chop(url);
    	}
    	if (url.indexOf("/") != -1) {
    		int lastSlashPlusOne = url.lastIndexOf("/") + 1;
    		s = url.substring(lastSlashPlusOne, url.length());
    	}
    	return s;
    }
    
    @Override
    public String getBucketName() {
    	return this.bucketName;
    }
    
    @Override
    public boolean isUseBucketAsDomain() {
    	return this.useBucketAsDomain;
    }

    
    // internal helpers

    private S3Service createS3Service() {
        try {
            return new RestS3Service(awsCredentials);
        } catch (S3ServiceException e) {
            throw new IllegalArgumentException("Unable to init REST-based S3Service with provided credentials", e);
        }
    }

    private void shutdownS3Service(S3Service s3Service) {
        try {
            s3Service.shutdown();
        } catch (ServiceException e) {
            throw new RuntimeException("Unable to shutdown S3 connection");
        }
    }
}