package com.polarrose.amazon.s3;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.httpclient.HttpMethodBase;

/**
 *
 * @author Piotr Maj &lt;piotr@polarrose.com&gt;
 *
 */
public class S3GetResponse extends S3Response {

	private InputStream inputStream;
	private HttpMethodBase method;

	public S3GetResponse(HttpMethodBase method) {
		this.method = method;
	}

	public S3GetResponse() {
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public void close() {
		try {
			inputStream.close();
		} catch (IOException e) {
		}
		if (method != null) {
			method.releaseConnection();
		}
	}
}
