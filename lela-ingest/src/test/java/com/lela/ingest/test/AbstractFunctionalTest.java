/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.ingest.test;

import com.lela.util.test.AbstractTest;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertNotNull;

/**
 * Created by Bjorn Harvold
 * Date: 6/23/11
 * Time: 11:12 AM
 * Responsibility:
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/META-INF/spring/applicationContext.xml","/META-INF/spring/test.xml",
        "/META-INF/spring/webmvc-component-scan.xml",
        "/META-INF/spring/webmvc-config.xml",
        "/META-INF/spring/webmvc-security.xml",
        "/META-INF/spring/ingest.xml",
        "/META-INF/spring/ingest-talend.xml"})
public abstract class AbstractFunctionalTest extends AbstractTest {

}
