/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */
package com.lela.ingest.json;

import com.lela.commons.LelaException;

/**
 * User: Chris Tallent
 * Date: 8/20/12
 * Time: 12:29 PM
 */
public class JsonTemplateQueryException extends LelaException {
    public JsonTemplateQueryException() {
    }

    public JsonTemplateQueryException(String msg) {
        super(msg);
    }

    public JsonTemplateQueryException(String msg, Throwable t) {
        super(msg, t);
    }

    public JsonTemplateQueryException(String msg, Throwable t, String... params) {
        super(msg, t, params);
    }

    public JsonTemplateQueryException(String key, String... params) {
        super(key, params);
    }
}
