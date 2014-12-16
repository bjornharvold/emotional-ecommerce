package com.lela.commons.test.utilities;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import com.lela.util.utilities.storage.FileStorage;
import com.lela.util.utilities.storage.s3.S3FileStorage;

@RunWith(MockitoJUnitRunner.class)
public class S3FileStorageUnitTests {
	
	//Partial mock. Not testing all methods of this class for now.
	@Spy private FileStorage s3FileStorage = new S3FileStorage("accessKey","secretAccessKey","bucketName");
	
	@Test
	public void testGetObjectKeyForURLEndingInSlash(){
		String s = s3FileStorage.getObjectKeyForURL("http://some.domain.com/withEndingSlash/");
		Assert.assertEquals(s, "withEndingSlash");
	}
	@Test
	public void testGetObjectKeyForURLEndingInNoSlash(){
		String s = s3FileStorage.getObjectKeyForURL("http://some.domain.com/withNoEndingSlash");
		Assert.assertEquals(s, "withNoEndingSlash");
	}
	
	@Test
	public void testGetObjectKeyForURLMultipleSlash(){
		String s = s3FileStorage.getObjectKeyForURL("http://some.domain.com/first/second/third.jpg");
		Assert.assertEquals(s, "third.jpg");
	}
}
