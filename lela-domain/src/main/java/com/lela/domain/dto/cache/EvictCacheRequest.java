/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.domain.dto.cache;

import java.io.Serializable;

/**
 * Created by Bjorn Harvold
 * Date: 5/30/12
 * Time: 3:53 PM
 * Responsibility:
 */
public class EvictCacheRequest implements Serializable {
    private static final long serialVersionUID = 1552113328313975789L;

    private String urlName;
    
    private String cacheName;
    
    private String value;

    public String getUrlName() {
        return urlName;
    }

    public void setUrlName(String urlName) {
        this.urlName = urlName;
    }

	public String getCacheName() {
		return cacheName;
	}

	public void setCacheName(String cacheName) {
		this.cacheName = cacheName;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
