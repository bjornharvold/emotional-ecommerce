/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.commons.test.json;

import com.amazonaws.util.json.JSONObject;
import com.lela.ingest.test.AbstractFunctionalTest;
import com.lela.ingest.json.JsonTemplateEngine;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

/**
 * User: Chris Tallent
 * Date: 4/21/12
 * Time: 8:15 PM
 */
public class JsonTemplateEngineFunctionalTest extends AbstractFunctionalTest {
    private static final Logger log = LoggerFactory.getLogger(JsonTemplateEngineFunctionalTest.class);

    @Autowired
    private JsonTemplateEngine engine;

    @Ignore
    @Test
    public void testTemplates() {

        try {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("CategoryID", "21");
            //params.put("ItemID", "1757");
            params.put("ItemID", "6779");
            params.put("BrandID", "149");

            String json = engine.generate("item.json", params);
            System.out.println("RAW: " + json);

            // Pretty Print
            json = new JSONObject(json).toString(3);
            System.out.println("PRETTY: " + json);

            assertNotNull("No JSON result", json);
        } catch (Exception e) {
            fail(e.getMessage());
        }

    }
}
