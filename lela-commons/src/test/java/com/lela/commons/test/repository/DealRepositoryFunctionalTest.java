/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.commons.test.repository;

import com.lela.commons.repository.DealRepository;
import com.lela.commons.test.AbstractFunctionalTest;
import com.lela.domain.document.Deal;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by Bjorn Harvold
 * Date: 7/18/11
 * Time: 12:06 PM
 * Responsibility:
 */
public class DealRepositoryFunctionalTest extends AbstractFunctionalTest {
    private Deal deal = null;

    @Autowired
    private DealRepository dealRepository;

    @Before
    public void setUp() {
        deal = new Deal();
        deal.setRlnm(DealRepositoryFunctionalTest.class.getSimpleName());
        deal.setNm(DealRepositoryFunctionalTest.class.getSimpleName());

        deal = dealRepository.save(deal);
    }

    @After
    public void tearDown() {
        dealRepository.delete(deal);
    }

    @Test
    public void testFindDealsForStores() {
        List<Deal> deals = dealRepository.findDealsForStore(DealRepositoryFunctionalTest.class.getSimpleName());
        assertNotNull("Deals are null", deals);
    }

}
