/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.commons.test.service;

import com.lela.commons.service.ProductEngineService;
import com.lela.commons.service.impl.ProductEngineServiceImpl;
import junit.framework.TestCase;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Bjorn Harvold
 * Date: 7/22/11
 * Time: 1:17 PM
 * Responsibility:
 */
public class MotivatorMatchingTest extends TestCase {
    private final static Logger log = LoggerFactory.getLogger(MotivatorMatchingTest.class);

    @Test
    public void testMotivatorAlgorithm1() {
        Map<String, Integer> userMotivators = new HashMap<String, Integer>();

        userMotivators.put("A", 8);
        userMotivators.put("B", 9);
        userMotivators.put("C", 3);
        userMotivators.put("D", 8);
        userMotivators.put("E", 9);
        userMotivators.put("F", 8);
        userMotivators.put("G", 8);

        Map<String, Integer> itemMotivators = new HashMap<String, Integer>();

        itemMotivators.put("A", 9);
        itemMotivators.put("B", 9);
        itemMotivators.put("C", 5);
        itemMotivators.put("D", 9);
        itemMotivators.put("E", 9);
        itemMotivators.put("F", 9);
        itemMotivators.put("G", 1);

        ProductEngineService impl           = new ProductEngineServiceImpl(null, null, null, null, null, null, null);
        Map<String, Integer>                map            = impl.computeMotivatorRelevancy(userMotivators,
                                                                 itemMotivators);
        Integer                             totalRelevancy = impl.computeTotalRelevancy(map);

        for (String key : map.keySet()) {
            log.info("Relevancy for: " + key + " = " + map.get(key));
        }

        assertEquals("Motivator A relevancy does not match", 56, map.get("A"), 0);
        assertEquals("Motivator B relevancy does not match", 81, map.get("B"), 0);
        assertEquals("Motivator C relevancy does not match", 3, map.get("C"), 0);
        assertEquals("Motivator D relevancy does not match", 56, map.get("D"), 0);
        assertEquals("Motivator E relevancy does not match", 81, map.get("E"), 0);
        assertEquals("Motivator F relevancy does not match", 56, map.get("F"), 0);
        assertEquals("Motivator G relevancy does not match", 8, map.get("G"), 0);

        assertEquals("Total relevancy does not match", 341, totalRelevancy, 0);

    }

    @Test
    public void testMotivatorAlgorithm2() {
        Map<String, Integer> userMotivators = new HashMap<String, Integer>();

        userMotivators.put("A", 7);
        userMotivators.put("B", 9);
        userMotivators.put("C", 3);
        userMotivators.put("D", 8);
        userMotivators.put("E", 9);
        userMotivators.put("F", 8);
        userMotivators.put("G", 8);

        Map<String, Integer> itemMotivators = new HashMap<String, Integer>();

        itemMotivators.put("A", 7);
        itemMotivators.put("B", 9);
        itemMotivators.put("C", 5);
        itemMotivators.put("D", 9);
        itemMotivators.put("E", 9);
        itemMotivators.put("F", 9);
        itemMotivators.put("G", 1);

        ProductEngineService impl           = new ProductEngineServiceImpl(null, null, null, null, null, null, null);
        Map<String, Integer>                map            = impl.computeMotivatorRelevancy(userMotivators,
                                                                 itemMotivators);
        Integer                             totalRelevancy = impl.computeTotalRelevancy(map);

        for (String key : map.keySet()) {
            log.info("Relevancy for: " + key + " = " + map.get(key));
        }

        assertEquals("Motivator A relevancy does not match", 49, map.get("A"), 0);
        assertEquals("Motivator B relevancy does not match", 81, map.get("B"), 0);
        assertEquals("Motivator C relevancy does not match", 3, map.get("C"), 0);
        assertEquals("Motivator D relevancy does not match", 56, map.get("D"), 0);
        assertEquals("Motivator E relevancy does not match", 81, map.get("E"), 0);
        assertEquals("Motivator F relevancy does not match", 56, map.get("F"), 0);
        assertEquals("Motivator G relevancy does not match", 8, map.get("G"), 0);

        assertEquals("Total relevancy does not match", 334, totalRelevancy, 0);

    }
}
