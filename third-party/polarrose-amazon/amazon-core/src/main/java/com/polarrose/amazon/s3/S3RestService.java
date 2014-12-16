package com.polarrose.amazon.s3;

import java.io.InputStream;
import java.util.List;

import com.polarrose.amazon.aws.AwsAccount;

/**
 * 
 * @author Piotr Maj &lt;piotr@polarrose.com&gt;
 *
 */
public interface S3RestService {

	public static final String HEADER_X_AMZ_ACL = "x-amz-acl";
	public static final String HEADER_X_AMZ_REQUEST_ID = "x-amz-request-id";
	public static final String HEADER_X_AMZ_ID_2 = "x-amz-id-2";

	// access policy constants
	public static final String ACCESS_POLICY_PRIVATE = "private";
	public static final String ACCESS_POLICY_PUBLIC_READ = "public-read";
	public static final String ACCESS_POLICY_PUBLIC_READ_WRITE = "public-read-write";
	public static final String ACCESS_POLICY_AUTHENTICATED_READ = "authenticated-read";

	S3Response putBucket(AwsAccount account, String bucket);

	S3Response deleteBucket(AwsAccount account, String bucket);

	S3GetResponse getObject(AwsAccount account, String bucket, String path);

	S3GetResponse getObject(AwsAccount account, String bucket, String path, List<Header> requestHeaders);

	S3GetResponse getObject(String bucket, String path);

	S3GetResponse getObject(String bucket, String path, List<Header> requestHeaders);

	S3Response headObject(AwsAccount account, String bucket, String path);

	S3Response headObject(AwsAccount account, String bucket, String path, List<Header> requestHeaders);

	S3Response headObject(String bucket, String path);

	S3Response headObject(String bucket, String path, List<Header> requestHeaders);

	S3Response putObject(AwsAccount account, String bucket, String path, InputStream is, int contentLength);

	S3Response putObject(AwsAccount account, String bucket, String path, InputStream is, int contentLength, List<Header> requestHeaders);

	S3Response deleteObject(AwsAccount account, String bucket, String path);

}
