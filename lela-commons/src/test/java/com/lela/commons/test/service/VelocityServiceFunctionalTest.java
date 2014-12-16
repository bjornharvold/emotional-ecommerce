/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.commons.test.service;

import com.lela.commons.service.BranchService;
import com.lela.commons.service.StoreService;
import com.lela.commons.service.VelocityService;
import com.lela.commons.spring.security.SpringSecurityHelper;
import com.lela.commons.test.AbstractFunctionalTest;
import com.lela.domain.document.Branch;
import com.lela.domain.document.Store;
import com.lela.domain.dto.BranchSearchResult;
import com.lela.domain.dto.store.BranchDistance;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by Bjorn Harvold
 * Date: 6/23/11
 * Time: 11:16 AM
 * Responsibility: Testing out the VelocityService
 */
public class VelocityServiceFunctionalTest extends AbstractFunctionalTest {
    private static final Logger log = LoggerFactory.getLogger(VelocityServiceFunctionalTest.class);

    private static final String FORMATTING_NUMBERS_TEMPLATE = "formattingTestTemplate.txt";

    @Autowired
    private VelocityService velocityService;

    @Test
    public void testMergeTemplateWithString() {
        log.info("Testing velocityService.mergeTemplateWithString()...");

        Map<String, Object> model = new HashMap<String, Object>();
        model.put("theNumber", 3.15);

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 2012);
        cal.set(Calendar.MONTH, 3);
        cal.set(Calendar.DAY_OF_MONTH, 5);

        model.put("theDate", cal.getTime());

        String mergedTemplate = velocityService.mergeTemplateIntoString(FORMATTING_NUMBERS_TEMPLATE, model);
        assertNotNull("mergedTemplate is null", mergedTemplate);
        log.info("Velocity merge result:\n\n" + mergedTemplate);
        assertTrue("Missing value", StringUtils.contains(mergedTemplate, "2012"));
        assertTrue("Missing value", StringUtils.contains(mergedTemplate, "$3"));
        assertTrue("Missing value", StringUtils.contains(mergedTemplate, "315%"));

        log.info("Testing velocityService.mergeTemplateWithString() complete");
    }
}
