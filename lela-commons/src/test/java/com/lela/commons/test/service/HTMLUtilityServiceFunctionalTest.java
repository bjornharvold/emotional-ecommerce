/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.commons.test.service;

import com.lela.commons.service.HTMLUtilityService;
import com.lela.commons.test.AbstractFunctionalTest;
import com.lela.domain.dto.dom.DOMElement;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Created by Bjorn Harvold
 * Date: 6/23/11
 * Time: 11:16 AM
 * Responsibility:
 */
public class HTMLUtilityServiceFunctionalTest extends AbstractFunctionalTest {
    private static final Logger log = LoggerFactory.getLogger(HTMLUtilityServiceFunctionalTest.class);
    private static final String YAHOO_URL = "http://www.yahoo.com";
    private static final String GOOGLE_URL = "http://www.google.com";

    @Autowired
    private HTMLUtilityService htmlUtilityService;

    @Test
    public void testFetchImagesFromUrl() {
        log.info("Testing fetchImagesFromUrl()...");

        log.info("Retrieving all images from Yahoo's homepage");
        List<DOMElement> unrestrictedImages = htmlUtilityService.fetchImagesFromUrl(YAHOO_URL);
        assertNotNull("Images were null", unrestrictedImages);
        log.info("Retrieved " + unrestrictedImages.size() + " unrestricted images from Yahoo's homepage");

        log.info("Retrieving all images from Google's homepage BUT this time we have a crazy width and height restriction so we expect no images in return");
        List<DOMElement> restrictedImages = htmlUtilityService.fetchImagesFromUrl(GOOGLE_URL, 1000, 1000);
        assertNull("Images were not null", restrictedImages);

        log.info("Retrieving all images from Yahoo's homepage BUT this time we have a crazy width and height restriction so we expect no images in return");
        restrictedImages = htmlUtilityService.fetchImagesFromUrl(YAHOO_URL, 1000, 1000);
        assertNull("Images were not null", restrictedImages);

        log.info("Testing fetchImagesFromUrl() complete");
    }

}
