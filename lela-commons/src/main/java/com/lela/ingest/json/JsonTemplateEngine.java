/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */
package com.lela.ingest.json;

import com.lela.commons.service.VelocityService;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.ui.velocity.VelocityEngineUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: Chris Tallent
 * Date: 4/21/12
 * Time: 7:09 PM
 */
public class JsonTemplateEngine {
    private final VelocityService velocityService;
    private final JsonQueryEngine jsonQueryEngine;
    private Map<String, Object> context;

    public JsonTemplateEngine(VelocityService velocityService, JsonQueryEngine jsonQueryEngine) {
        this.velocityService = velocityService;
        this.jsonQueryEngine = jsonQueryEngine;
    }

    public String generate(String template) {
        return generate(template, null);
    }

    public String generate(String template, Map<String, Object> params) {

        Map<String, Object> actual = new HashMap<String, Object>();

        if (params != null) {
            actual.putAll(params);
        }

        if (context != null) {
            actual.putAll(context);
        }

        // Add a query context
        actual.put("sql", jsonQueryEngine.getQueryContext(actual));
        actual.put("util", JsonUtils.class);

        String json = velocityService.mergeTemplateIntoString(template, actual);

        return json;
    }

    public Map<String, Object> getContext() {
        return context;
    }

    public void setContext(Map<String, Object> context) {
        this.context = context;
    }
}
