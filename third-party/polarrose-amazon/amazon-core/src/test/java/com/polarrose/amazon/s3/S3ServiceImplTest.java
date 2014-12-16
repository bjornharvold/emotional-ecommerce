package com.polarrose.amazon.s3;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.polarrose.amazon.aws.AwsAccount;
import com.polarrose.amazon.s3.impl.S3RestServiceImpl;

public class S3ServiceImplTest {

	private static final String POLARROSE_TEST_BUCKET = "polarrose.test";

	private AwsAccount account = new AwsAccount(System.getProperty("com.polarrose.amazon.s3.TestAccount"), System.getProperty("com.polarrose.amazon.s3.TestSecretKey"));

	private S3RestServiceImpl restServiceImpl;

	@Before
	public void setupSimpleQueueService() {
		restServiceImpl = new S3RestServiceImpl();
	}

	@Test
	public void testPutDeleteBucket() throws Exception {
		S3Response response = restServiceImpl.putBucket(account, POLARROSE_TEST_BUCKET);
		Assert.assertEquals(200, response.getResponseCode());

		response = restServiceImpl.deleteBucket(account, POLARROSE_TEST_BUCKET);
		Assert.assertEquals(204, response.getResponseCode());
	}

	@Test
	public void testGetObject() throws Exception {
		S3Response response = restServiceImpl.getObject(account, POLARROSE_TEST_BUCKET, "/test-file");
		Assert.assertEquals(404, response.getResponseCode());

		response = restServiceImpl.putBucket(account, POLARROSE_TEST_BUCKET);
		Assert.assertEquals(200, response.getResponseCode());

		String content = "Test Amazon S3";
		response = restServiceImpl.putObject(account, POLARROSE_TEST_BUCKET, "/test-file", new ByteArrayInputStream(content.getBytes()), content.length());
		Assert.assertEquals(200, response.getResponseCode());

		response = restServiceImpl.getObject(account, POLARROSE_TEST_BUCKET, "/test-file");
		Assert.assertEquals(200, response.getResponseCode());
		InputStream inputStream = ((S3GetResponse) response).getInputStream();
		byte[] buff = new byte[1024];
		int c = -1;
		c = inputStream.read(buff);
		Assert.assertEquals(content, new String(buff, 0, c));
		inputStream.close();

		response = restServiceImpl.deleteObject(account, POLARROSE_TEST_BUCKET, "/test-file");
		Assert.assertEquals(204, response.getResponseCode());

		response = restServiceImpl.deleteBucket(account, POLARROSE_TEST_BUCKET);
		Assert.assertEquals(204, response.getResponseCode());
	}

	@Test
	public void testAnonymousGetObject() throws Exception {
		S3Response response = restServiceImpl.getObject(account, POLARROSE_TEST_BUCKET, "/test-file");
		Assert.assertEquals(404, response.getResponseCode());

		response = restServiceImpl.putBucket(account, POLARROSE_TEST_BUCKET);
		Assert.assertEquals(200, response.getResponseCode());

		String content = "Test Amazon S3";
		List<Header> headers = new ArrayList<Header>();
		headers.add(new Header(S3RestService.HEADER_X_AMZ_ACL, "public-read"));
		response = restServiceImpl.putObject(account, POLARROSE_TEST_BUCKET, "/test-file", new ByteArrayInputStream(content.getBytes()), content.length(), headers);
		Assert.assertEquals(200, response.getResponseCode());

		response = restServiceImpl.getObject(POLARROSE_TEST_BUCKET, "/test-file");
		Assert.assertEquals(200, response.getResponseCode());
		InputStream inputStream = ((S3GetResponse) response).getInputStream();
		byte[] buff = new byte[1024];
		int c = -1;
		c = inputStream.read(buff);
		Assert.assertEquals(content, new String(buff, 0, c));
		inputStream.close();

		response = restServiceImpl.deleteObject(account, POLARROSE_TEST_BUCKET, "/test-file");
		Assert.assertEquals(204, response.getResponseCode());

		response = restServiceImpl.deleteBucket(account, POLARROSE_TEST_BUCKET);
		Assert.assertEquals(204, response.getResponseCode());
	}

	@Test
	public void testHeadObject() throws Exception {
		S3Response response = restServiceImpl.getObject(account, POLARROSE_TEST_BUCKET, "/test-file");
		Assert.assertEquals(404, response.getResponseCode());

		response = restServiceImpl.putBucket(account, POLARROSE_TEST_BUCKET);
		Assert.assertEquals(200, response.getResponseCode());

		String content = "Test Amazon S3";
		response = restServiceImpl.putObject(account, POLARROSE_TEST_BUCKET, "/test-file", new ByteArrayInputStream(content.getBytes()), content.length());
		Assert.assertEquals(200, response.getResponseCode());

		response = restServiceImpl.headObject(account, POLARROSE_TEST_BUCKET, "/test-file");
		Assert.assertEquals(200, response.getResponseCode());
		Assert.assertEquals(Integer.parseInt(response.getHeader("Content-length")), content.length());

		response = restServiceImpl.deleteObject(account, POLARROSE_TEST_BUCKET, "/test-file");
		Assert.assertEquals(204, response.getResponseCode());

		response = restServiceImpl.deleteBucket(account, POLARROSE_TEST_BUCKET);
		Assert.assertEquals(204, response.getResponseCode());
	}

	@Test
	public void testAnonymousHeadObject() throws Exception {
		S3Response response = restServiceImpl.getObject(account, POLARROSE_TEST_BUCKET, "/test-file");
		Assert.assertEquals(404, response.getResponseCode());

		response = restServiceImpl.putBucket(account, POLARROSE_TEST_BUCKET);
		Assert.assertEquals(200, response.getResponseCode());

		String content = "Test Amazon S3";
		List<Header> headers = new ArrayList<Header>();
		headers.add(new Header(S3RestService.HEADER_X_AMZ_ACL, "public-read"));
		response = restServiceImpl.putObject(account, POLARROSE_TEST_BUCKET, "/test-file", new ByteArrayInputStream(content.getBytes()), content.length(), headers);
		Assert.assertEquals(200, response.getResponseCode());

		response = restServiceImpl.headObject(POLARROSE_TEST_BUCKET, "/test-file");
		Assert.assertEquals(200, response.getResponseCode());
		Assert.assertEquals(Integer.parseInt(response.getHeader("Content-length")), content.length());

		response = restServiceImpl.deleteObject(account, POLARROSE_TEST_BUCKET, "/test-file");
		Assert.assertEquals(204, response.getResponseCode());

		response = restServiceImpl.deleteBucket(account, POLARROSE_TEST_BUCKET);
		Assert.assertEquals(204, response.getResponseCode());
	}
}
