package com.polarrose.amazon.s3;

/**
 * 
 * @author Piotr Maj &lt;piotr@polarrose.com&gt;
 */
public class Header {

	private String key;
	private String value;

	public Header() {
	}

	public Header(String name, String value) {
		key = name;
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
