/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.data.web.controller.administration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.support.BindingAwareModelMap;

import com.lela.data.web.AbstractFunctionalTest;
import com.lela.data.web.controller.administration.item.ItemStatisticsController;

/**
 * Created by Bjorn Harvold
 * Date: 8/3/11
 * Time: 10:05 AM
 * Responsibility:
 */
@SuppressWarnings("unchecked")
public class ItemStatisticsControllerFunctionalTest extends AbstractFunctionalTest {
    private static final Logger log = LoggerFactory.getLogger(ItemStatisticsControllerFunctionalTest.class);

    @Autowired
    private ItemStatisticsController itemStatisticsController;

    /*
    @Before
    public void setUp() {

    }

    @After
    public void tearDown() {

    }
    */


    @Test
    public void testShowStoresPerItem() {
        log.info("Testing item statistics stores per item controller...");

        Model model = new BindingAwareModelMap();

        try {
            String view = itemStatisticsController.showStoresPerItem(null, model);
            assertNotNull("Tile view returned is null", view);
            assertEquals("Tile view is incorrect", "administration.stores.per.item", view);
        } catch (Exception e) {
            log.info(e.getMessage(), e);
            fail("Method threw unexpected exception: " + e.getMessage());
        }

        log.info("Testing item statistics stores per item controller complete");
    }

    @Test
    public void testShowItemsPerStore() {
        log.info("Testing item statistics items per store controller...");

        Model model = new BindingAwareModelMap();

        try {
            String view = itemStatisticsController.showItemsPerStore(model);
            assertNotNull("Tile view returned is null", view);
            assertEquals("Tile view is incorrect", "administration.items.per.store", view);
        } catch (Exception e) {
            log.info(e.getMessage(), e);
            fail("Method threw unexpected exception: " + e.getMessage());
        }

        log.info("Testing item statistics items per store controller complete");
    }
}
