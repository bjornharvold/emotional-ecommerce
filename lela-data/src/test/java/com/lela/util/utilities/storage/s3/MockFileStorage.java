package com.lela.util.utilities.storage.s3;

import java.util.List;

import com.lela.util.utilities.storage.FileStorage;
import com.lela.util.utilities.storage.dto.FileData;

/**
 * Created with IntelliJ IDEA.
 * User: ballmw
 * Date: 7/27/12
 * Time: 3:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class MockFileStorage implements FileStorage{
    @Override
    public String absoluteUrl(String fileName) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String[] listAllBuckets() {
        return new String[0];  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String storeFile(FileData file) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
    
    @Override
    public List<String> listAllObjectsInABucket(String bucketName)  {
    	return null; //TODO: implement
    }
    
    @Override
    public void removeObjectFromBucket(String bucketName, String objectKey) {
    	//TODO: Implement as needed
    }
    
    @Override
	public String getObjectKeyForURL(String url) {
    	//TODO: Implement as needed
    	return null;
    }
    
    @Override
    public String getBucketName() {
    	//TODO: Implement as needed
    	return null;
    }
    
    @Override
    public boolean isUseBucketAsDomain() {
    	//TODO: Implement as needed
    	return false;
    }
}
