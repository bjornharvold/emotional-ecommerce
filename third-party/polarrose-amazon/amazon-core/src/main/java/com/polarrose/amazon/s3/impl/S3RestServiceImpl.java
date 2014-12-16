package com.polarrose.amazon.s3.impl;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.digester.Digester;
import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpMethodRetryHandler;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.HeadMethod;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.httpclient.util.DateUtil;
import org.springframework.beans.factory.InitializingBean;
import org.xml.sax.SAXException;

import com.polarrose.amazon.aws.AuthenticationUtils;
import com.polarrose.amazon.aws.AwsAccount;
import com.polarrose.amazon.s3.Header;
import com.polarrose.amazon.s3.S3ErrorDetails;
import com.polarrose.amazon.s3.S3GetResponse;
import com.polarrose.amazon.s3.S3Response;
import com.polarrose.amazon.s3.S3RestService;

public class S3RestServiceImpl implements S3RestService, InitializingBean {

	private static final String S3_SERVICE_HOST = "s3.amazonaws.com";

	private HttpClient httpClient;

	public void afterPropertiesSet() throws Exception {
		httpClient = new HttpClient(new MultiThreadedHttpConnectionManager());
		HttpClientParams httpClientParams = httpClient.getParams();
		httpClientParams.setAuthenticationPreemptive(false);
		httpClientParams.setConnectionManagerTimeout(5000);
		httpClientParams.setSoTimeout(5000);
		
		HttpConnectionManagerParams httpConnectionManagerParams = httpClient.getHttpConnectionManager().getParams();
		httpConnectionManagerParams.setConnectionTimeout(5000);
		httpConnectionManagerParams.setMaxConnectionsPerHost(HostConfiguration.ANY_HOST_CONFIGURATION, 100);
		httpConnectionManagerParams.setMaxTotalConnections(300);
		httpConnectionManagerParams.setSoTimeout(5000);
		httpConnectionManagerParams.setTcpNoDelay(true);
		
		String proxyHost = System.getProperty("http.proxy.host");
		String proxyPort = System.getProperty("http.proxy.port");
		if (proxyHost != null) {
			Integer port = Integer.valueOf(proxyPort);
			httpClient.getHostConfiguration().setProxy(proxyHost, port);
		}

	}

	public S3Response putBucket(AwsAccount account, String bucket) {
		if (bucket == null) {
			throw new IllegalArgumentException("Bucket cannot be null.");
		}

		PutMethod putMethod = new PutMethod(String.format("http://%s/%s", S3_SERVICE_HOST, bucket));
		try {
			calculateAndAddAuthenticationHeader(account, putMethod, bucket, "");
			httpClient.executeMethod(putMethod);
			S3Response response = new S3Response();
			fillS3ResponseFromMethod(response, putMethod);
			return response;
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			putMethod.releaseConnection();
		}
	}

	public S3Response deleteBucket(AwsAccount account, String bucket) {
		if (bucket == null) {
			throw new IllegalArgumentException("Bucket cannot be null.");
		}

		DeleteMethod deleteMethod = new DeleteMethod(String.format("http://%s/%s", S3_SERVICE_HOST, bucket));
		try {
			calculateAndAddAuthenticationHeader(account, deleteMethod, bucket, "");
			httpClient.executeMethod(deleteMethod);
			S3Response response = new S3Response();
			fillS3ResponseFromMethod(response, deleteMethod);
			return response;
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			deleteMethod.releaseConnection();
		}
	}

	public S3Response deleteObject(AwsAccount account, String bucket, String path) {
		assertBucketAndPath(bucket, path);

		if (!path.startsWith("/")) {
			path = "/" + path;
		}
		DeleteMethod deleteMethod = new DeleteMethod(String.format("http://%s.%s%s", bucket, S3_SERVICE_HOST, path));
		try {
			calculateAndAddAuthenticationHeader(account, deleteMethod, bucket, path);
			httpClient.executeMethod(deleteMethod);
			S3Response response = new S3Response();
			fillS3ResponseFromMethod(response, deleteMethod);
			return response;
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			deleteMethod.releaseConnection();
		}
	}

	/**
	 * @see #getObject(AwsAccount, String, String, List)
	 */
	public S3GetResponse getObject(AwsAccount account, String bucket, String path) {
		return getObject(account, bucket, path, null);
	}

	/**
	 * Caution!!! When using getObject() method always remember to close the input 
	 * stream you get from {@link S3GetResponse}. This is required because
	 * if releases the underlying HttpCommons client connection.
	 */
	public S3GetResponse getObject(AwsAccount account, String bucket, String path, List<Header> requestHeaders) {
		assertBucketAndPath(bucket, path);

		if (!path.startsWith("/")) {
			path = "/" + path;
		}
		final GetMethod getMethod = new GetMethod(String.format("http://%s.%s%s", bucket, S3_SERVICE_HOST, path));
		if (requestHeaders != null) {
			for (Header header : requestHeaders) {
				getMethod.addRequestHeader(header.getKey(), header.getValue());
			}
		}

		try {
			if (account != null) {
				calculateAndAddAuthenticationHeader(account, getMethod, bucket, path);
			}

			httpClient.executeMethod(getMethod);
			S3GetResponse response = new S3GetResponse(getMethod);
			fillS3ResponseFromMethod(response, getMethod);
			if (response.getResponseCode() == 200) {
				response.setInputStream(new BufferedInputStream(getMethod.getResponseBodyAsStream()) {
					@Override
					public void close() throws IOException {
						super.close();
						getMethod.releaseConnection();
					}
				});
			}
			return response;
		} catch (Exception e) {
			getMethod.releaseConnection();
			throw new RuntimeException(e);
		}
	}

	/**
	 * @see #getObject(AwsAccount, String, String, List)
	 */
	public S3GetResponse getObject(String bucket, String path) {
		return getObject(bucket, path, null);
	}

	/**
	 * @see #getObject(AwsAccount, String, String, List)
	 */
	public S3GetResponse getObject(String bucket, String path, List<Header> requestHeaders) {
		return getObject(null, bucket, path, requestHeaders);
	}

	public S3Response headObject(AwsAccount account, String bucket, String path) {
		return headObject(account, bucket, path, null);
	}

	public S3Response headObject(AwsAccount account, String bucket, String path, List<Header> requestHeaders) {
		assertBucketAndPath(bucket, path);

		if (!path.startsWith("/")) {
			path = "/" + path;
		}
		HeadMethod headMethod = new HeadMethod(String.format("http://%s.%s%s", bucket, S3_SERVICE_HOST, path));
		if (requestHeaders != null) {
			for (Header header : requestHeaders) {
				headMethod.addRequestHeader(header.getKey(), header.getValue());
			}
		}

		try {
			if (account != null) {
				calculateAndAddAuthenticationHeader(account, headMethod, bucket, path);
			}

			httpClient.executeMethod(headMethod);
			S3GetResponse response = new S3GetResponse();
			fillS3ResponseFromMethod(response, headMethod);
			return response;
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			headMethod.releaseConnection();
		}
	}

	public S3Response headObject(String bucket, String path) {
		return headObject(bucket, path, null);
	}

	public S3Response headObject(String bucket, String path, List<Header> requestHeaders) {
		return headObject(null, bucket, path, requestHeaders);
	}

	public S3Response putObject(AwsAccount account, String bucket, String path, InputStream is, int contentLength) {
		return putObject(account, bucket, path, is, contentLength, null);
	}

	public S3Response putObject(AwsAccount account, String bucket, String path, InputStream is, int contentLength, List<Header> requestHeaders) {
		assertBucketAndPath(bucket, path);

		if (!path.startsWith("/")) {
			path = "/" + path;
		}
		PutMethod putMethod = new PutMethod(String.format("http://%s.%s%s", bucket, S3_SERVICE_HOST, path));
		if (requestHeaders != null) {
			for (Header header : requestHeaders) {
				putMethod.addRequestHeader(header.getKey(), header.getValue());
			}
		}
		putMethod.setRequestEntity(new InputStreamRequestEntity(is, contentLength));

		// since we started to read from the stream we should never try to rerun 
		// this method if anything goes wrong
		putMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new HttpMethodRetryHandler() {
			public boolean retryMethod(HttpMethod method, IOException exception, int executionCount) {
				return false;
			}
		});
		try {
			calculateAndAddAuthenticationHeader(account, putMethod, bucket, path);

			httpClient.executeMethod(putMethod);
			S3GetResponse response = new S3GetResponse();
			fillS3ResponseFromMethod(response, putMethod);
			return response;
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			putMethod.releaseConnection();
		}
	}

	private void assertBucketAndPath(String bucket, String path) {
		if (bucket == null) {
			throw new IllegalArgumentException("Bucket cannot be null.");
		}
		if (path == null) {
			throw new IllegalArgumentException("Path cannot be null");
		}
	}

	private void fillS3ResponseFromMethod(S3Response response, HttpMethod httpMethod) throws IOException, SAXException {
		response.setResponseCode(httpMethod.getStatusCode());
		org.apache.commons.httpclient.Header[] responseHeaders = httpMethod.getResponseHeaders();
		List<Header> s3Headers = new ArrayList<Header>();
		for (org.apache.commons.httpclient.Header header : responseHeaders) {
			s3Headers.add(new Header(header.getName(), header.getValue()));
		}
		response.setHeaders(s3Headers.toArray(new Header[s3Headers.size()]));
		if (httpMethod.getStatusCode() >= 300) {
			response.setS3ErrorDetails(parseErrorMessage(httpMethod.getResponseBodyAsString()));
		}
	}

	private S3ErrorDetails parseErrorMessage(String errorMessage) throws IOException, SAXException {
		Digester digester = new Digester();
		digester.setValidating(false);
		digester.addObjectCreate("Error", S3ErrorDetails.class);
		digester.addCallMethod("Error/Code", "setErrorCode", 0);
		digester.addCallMethod("Error/Message", "setMessage", 0);
		digester.addCallMethod("Error/Resource", "setResource", 0);
		digester.addCallMethod("Error/RequestId", "setRequestId", 0);
		return (S3ErrorDetails) digester.parse(new StringReader(errorMessage));
	}

	private void calculateAndAddAuthenticationHeader(AwsAccount account, HttpMethod method, String bucket, String path) throws URIException {
		String authorization = "AWS " + account.getAccessKeyId() + ":";
		StringBuilder toSign = new StringBuilder(method.getName()).append("\n");
		org.apache.commons.httpclient.Header md5Header = method.getRequestHeader("content-md5");
		if (md5Header != null) {
			toSign.append(md5Header.getValue());
		}
		toSign.append("\n");
		org.apache.commons.httpclient.Header contentTypeHeader = method.getRequestHeader("content-type");
		if (contentTypeHeader != null) {
			toSign.append(contentTypeHeader.getValue());
		}
		toSign.append("\n");
		Date requestDate = new Date();
		String formattedDate = DateUtil.formatDate(requestDate);
		method.setRequestHeader("Date", formattedDate);
		toSign.append(formattedDate);
		toSign.append("\n");

		// sorting custom headers in lexicographocal order
		List<String> amzHeaders = new ArrayList<String>();
		org.apache.commons.httpclient.Header[] requestHeaders = method.getRequestHeaders();
		for (org.apache.commons.httpclient.Header header : requestHeaders) {
			String name = header.getName();
			if (name.toLowerCase().startsWith("x-amz-")) {
				if (!amzHeaders.contains(name)) {
					amzHeaders.add(name.trim());
				}
			}
		}
		Collections.sort(amzHeaders);

		for (String headerName : amzHeaders) {
			org.apache.commons.httpclient.Header[] headers = method.getRequestHeaders(headerName);
			if (headers.length == 0) {
				continue;
			} else if (headers.length == 1) {
				toSign.append(headers[0].getName()).append(":").append(headers[0].getValue().trim());
			} else {
				toSign.append(headers[0].getName()).append(":");
				for (org.apache.commons.httpclient.Header multiHeader : headers) {
					toSign.append(multiHeader.getValue().trim()).append(",");
				}
				toSign.deleteCharAt(toSign.length() - 1);
			}
			toSign.append("\n");
		}
		toSign.append("/").append(bucket).append(path);
		//System.out.println(toSign);

		authorization += AuthenticationUtils.createSignature(account, toSign.toString());
		//System.out.println(authorization);
		method.setRequestHeader("Authorization", authorization);
	}
}
