/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.compute.test;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by Bjorn Harvold
 * Date: 2/1/12
 * Time: 9:25 PM
 * Responsibility:
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/META-INF/spring/applicationContext.xml"})
public abstract class AbstractFunctionalTest extends AbstractJUnit4SpringContextTests {

}
