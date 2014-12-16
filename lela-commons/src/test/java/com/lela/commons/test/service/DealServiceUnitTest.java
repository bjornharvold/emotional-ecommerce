/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.commons.test.service;

import com.lela.commons.repository.DealRepository;
import com.lela.commons.service.CacheService;
import com.lela.commons.service.impl.DealServiceImpl;
import com.lela.domain.document.Deal;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DealServiceUnitTest {
    private static final Logger log = LoggerFactory.getLogger(DealServiceUnitTest.class);
    private static final String STORE_URL_NAME = "STORE_URL_NAME";

    @Mock
    private DealRepository dealRepository;

    @Mock
    private CacheService cacheService;

    @InjectMocks
    private DealServiceImpl dealService;

    @Test
    public void testFindDealsForStores() {
        log.info("Testing findDealsForStore()...");

        // set up objects
        List<String> list = new ArrayList<String>();
        list.add(STORE_URL_NAME);

        List<Deal> deals = new ArrayList<Deal>();
        Deal deal = new Deal();
        deal.setRlnm(STORE_URL_NAME);
        deals.add(deal);

        // configure mocks
        when(dealRepository.findDealsForStore(STORE_URL_NAME)).thenReturn(deals);

        // execute service method
        deals = dealService.findDealsForStores(list);

        // verify
        verify(dealRepository, times(1)).findDealsForStore(STORE_URL_NAME);
        assertNotNull("Deals is null", deals);

        log.info("Testing findDealsForStore() complete");
    }
}
