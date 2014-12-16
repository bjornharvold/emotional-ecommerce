/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.util.utilities.storage.dto;

/**
 * Created by Bjorn Harvold
 * Date: 9/15/11
 * Time: 2:43 PM
 * Responsibility:
 */
public final class FileData {

	private final String name;

	private final byte[] bytes;

	private final String contentType;

	public FileData(String name, byte[] bytes, String contentType) {
		this.name = name;
		this.bytes = bytes;
		this.contentType = contentType;
	}

	/**
	 * The name of the file.
	 */
	public String getName() {
		return name;
	}

	/**
	 * The file data as a byte array.
	 */
	public byte[] getBytes() {
		return bytes;
	}

	/**
	 * The file content type.
	 */
	public String getContentType() {
		return contentType;
	}
	
	public String toString(){
		return name + "(" + contentType + ")";
	}

}
