package com.polarrose.amazon.s3;

public class S3ErrorDetails {

	private String errorCode;
	private String message;
	private String resource;
	private String requestId;

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getResource() {
		return resource;
	}

	public void setResource(String resource) {
		this.resource = resource;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	@Override
	public String toString() {
		StringBuilder buff = new StringBuilder();
		buff.append("S3ErrorDetails[");
		buff.append("errorCode=").append(errorCode).append(", ");
		buff.append("message=").append(message).append(", ");
		buff.append("resource=").append(resource).append(", ");
		buff.append("requestId=").append(requestId);
		buff.append("]");
		return buff.toString();
	}
}
