/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.commons.test.service;

import com.lela.commons.service.EventService;
import com.lela.commons.service.MetricService;
import com.lela.commons.spring.security.SpringSecurityHelper;
import com.lela.commons.test.AbstractFunctionalTest;
import com.lela.domain.document.Metric;
import com.lela.domain.document.MetricValue;
import com.lela.domain.enums.MetricType;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

/**
 * Created by Bjorn Harvold
 * Date: 6/23/11
 * Time: 11:16 AM
 * Responsibility:
 */
public class MetricServiceFunctionalTest extends AbstractFunctionalTest {
    private static final Logger log = LoggerFactory.getLogger(MetricServiceFunctionalTest.class);

    @Autowired
    private MetricService metricService;

    @Test
    public void testMetric() {
        SpringSecurityHelper.unsecureChannel();

        log.info("Create a metric and save it");
        Metric metric = new Metric();
        metric.setTp(MetricType.INDUSTRY);
        List<MetricValue> values = new ArrayList<MetricValue>();
        values.add(new MetricValue("industry.accounting", 1));
        values.add(new MetricValue("industry.aerospace.defence", 2));
        metric.setVls(values);

        try {
            metricService.saveMetric(metric);
            fail("Metric should not be able to be saved here. Missing credentials.");
        } catch (Exception ex) {
            log.info("Tried to save metric without credentials. An exception was expected: " + ex.getMessage());
        }

        log.info("Securing channel...");
        SpringSecurityHelper.secureChannel();
        log.info("Channel secured");

        log.info("Saving a metric. This time with a secure channel");
        try {
            metric = metricService.saveMetric(metric);
            assertNotNull("Metric is missing an id", metric.getId());
            log.info("Metric persisted successfully");
        } catch (Exception ex) {
            fail("Did not expect an exception here: " + ex.getMessage());
            log.info("Was not able to persist a metric within secure context", ex);
        }

        log.info("Retrieving metric...");
        metric = metricService.findMetricByType(MetricType.INDUSTRY);
        assertNotNull("Metric is missing", metric);
        assertNotNull("Metric is missing an id", metric.getId());
        log.info("Metric retrieved successfully");

        log.info("Deleting metric...");
        metricService.removeMetric(metric.getId());

        metric = metricService.findMetricByType(MetricType.INDUSTRY);
        assertNull("Metric still exists", metric);

        log.info("Deleted metric successfully");
        log.info("Test complete!");
    }


}
