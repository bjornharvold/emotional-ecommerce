/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.commons.web.utils;

import org.springframework.web.context.request.WebRequest;

public class AjaxUtils {

	public static boolean isAjaxRequest(WebRequest webRequest) {
		String requestedWith = webRequest.getHeader("X-Requested-With");
		return requestedWith != null ? "XMLHttpRequest".equals(requestedWith) : false;
	}

	public static boolean isAjaxUploadRequest(WebRequest webRequest) {
		return webRequest.getParameter("ajaxUpload") != null;
	}
	
	private AjaxUtils() {}

}
