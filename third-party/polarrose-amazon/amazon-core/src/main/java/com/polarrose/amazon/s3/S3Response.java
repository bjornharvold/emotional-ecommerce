package com.polarrose.amazon.s3;


/**
 * 
 * @author Piotr Maj &lt;piotr@polarrose.com&gt;
 */
public class S3Response {

	private Header[] headers;
	private int responseCode;
	private S3ErrorDetails s3ErrorDetails;

	public S3ErrorDetails getS3ErrorDetails() {
		return s3ErrorDetails;
	}

	public void setS3ErrorDetails(S3ErrorDetails errorDetails) {
		s3ErrorDetails = errorDetails;
	}

	public Header[] getHeaders() {
		return headers;
	}

	public void setHeaders(Header[] headers) {
		this.headers = headers;
	}

	public int getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}


	@Override
	public String toString() {
		StringBuilder buff = new StringBuilder();
		buff.append("S3Response[");
		buff.append("responseCode=").append(responseCode).append(", headers=[");

		if (headers != null) {
			for (Header header : headers) {
				buff.append(header.getKey()).append("=").append(header.getValue());
				buff.append(",");
			}
			buff.deleteCharAt(buff.length() - 1);
		}
		buff.append("]");
		if (s3ErrorDetails != null) {
			buff.append(", error=").append(s3ErrorDetails.toString());
		}
		buff.append("]");
		return buff.toString();
	}

	public String getHeader(String name) {
		if (headers == null) {
			return null;
		}
		for (Header header : headers) {
			if (header.getKey().toLowerCase().equals(name.toLowerCase())) {
				return header.getValue();
			}
		}
		return null;
	}
}
