/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.domain.dto.dom;

import java.util.Map;

/**
 * Created by Bjorn Harvold
 * Date: 10/1/12
 * Time: 9:37 PM
 * Responsibility:
 */
public class DOMElement {
    private String text;
    private Map<String, String> attributes;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    public String getAttribute(String key) {
        String result = null;

        if (this.attributes != null && !this.attributes.isEmpty() && this.attributes.containsKey(key)) {
            result = this.attributes.get(key);
        }

        return result;
    }
}
