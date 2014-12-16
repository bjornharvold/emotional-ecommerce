/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.commons.test.jobs;

import com.lela.commons.jobs.alert.PriceAlertJob;
import com.lela.commons.jobs.java.JavaExecutionContext;
import com.lela.commons.service.impl.FavoritesListServiceImpl;
import com.lela.commons.service.impl.UserServiceImpl;
import com.lela.domain.document.Alert;
import com.lela.domain.document.Attribute;
import com.lela.domain.document.Item;
import com.lela.domain.document.ListCard;
import com.lela.domain.document.ListCardBoard;
import com.lela.domain.document.User;
import com.lela.domain.document.UserSupplement;
import com.lela.domain.dto.report.UserUserSupplementEntry;
import com.lela.domain.enums.MailParameter;
import com.lela.domain.enums.list.ListCardType;
import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Chris Tallent
 */
@RunWith(MockitoJUnitRunner.class)
public class PriceAlertJobUnitTest {
    private static final Logger log = LoggerFactory.getLogger(PriceAlertJobUnitTest.class);
    private static final String EMAIL = "somestupidemail@home.com";
    private static final String USER_CODE = "somecode";
    private static final String PRICE_ALERT_TEST_ITEM_RLNM = "priceAlertTestItemRlnm";

    @Mock
    private UserServiceImpl userService;

    @Mock
    private JavaExecutionContext executionContext;

    @Mock
    private FavoritesListServiceImpl favoritesListService;

    @InjectMocks
    PriceAlertJob job;

    private ObjectId userId;
    private User user;

    @Before
    public void setUp() {
        userId = new ObjectId();
        user = new User();
        user.setId(userId);
        user.setMl(EMAIL);
        user.setCd(USER_CODE);
    }

    @Test
    public void testEmailPriceAlert() {
        log.info("Test email price alert");

        // Set up the mock data
        TestData testData = new TestData().invoke(true, 10.00, 8.00);
        List<UserUserSupplementEntry> entries = testData.getEntries();
        Item item = testData.getItem();
        Map<MailParameter, Object> params = testData.getParams();
        UserSupplement us = testData.getUs();

        // Set up mock expectations
        when(userService.findUsersWithAlerts()).thenReturn(entries);

        // execute the service method
        try {
            job.execute(executionContext);

            // verify
            verify(userService, times(1)).findUsersWithAlerts();
            verify(favoritesListService, times(1)).sendValidPriceAlerts(USER_CODE);

            log.info("Test price alert email successful");
        } catch (Exception e) {
            fail("Test failed with exception: " + e.getMessage());
        }
    }

    @Test
    public void testNoPriceAlertWithHigherPrice() {
        log.info("Test no price alert for higher price");

        // Set up the mock data
        TestData testData = new TestData().invoke(true, 10.00, 11.00);
        List<UserUserSupplementEntry> entries = testData.getEntries();
        Item item = testData.getItem();
        Map<MailParameter, Object> params = testData.getParams();
        UserSupplement us = testData.getUs();

        // Set up mock expectations
        when(userService.findUsersWithAlerts()).thenReturn(entries);

        // execute the service method
        try {
            job.execute(executionContext);

            // verify
            verify(userService, times(1)).findUsersWithAlerts();
            verify(favoritesListService, times(1)).sendValidPriceAlerts(USER_CODE);

            log.info("Test price alert email successful");
        } catch (Exception e) {
            fail("Test failed with exception: " + e.getMessage());
        }
    }

    @Test
    public void testNoPriceAlertWithDisabledAlert() {
        log.info("Test no price alert for diasbled alert");

        // Set up the mock data
        TestData testData = new TestData().invoke(false, 10.00, 8.00);
        List<UserUserSupplementEntry> entries = testData.getEntries();
        Item item = testData.getItem();
        Map<MailParameter, Object> params = testData.getParams();
        UserSupplement us = testData.getUs();

        // Set up mock expectations
        when(userService.findUsersWithAlerts()).thenReturn(entries);

        // execute the service method
        try {
            job.execute(executionContext);

            // verify
            verify(userService, times(1)).findUsersWithAlerts();
            verify(favoritesListService, times(1)).sendValidPriceAlerts(USER_CODE);

            log.info("Test price alert email successful");
        } catch (Exception e) {
            fail("Test failed with exception: " + e.getMessage());
        }
    }

    private class TestData {
        private UserSupplement us;
        private List<UserUserSupplementEntry> entries;
        private Item item;
        private Map<MailParameter, Object> params;

        public UserSupplement getUs() {
            return us;
        }

        public List<UserUserSupplementEntry> getEntries() {
            return entries;
        }

        public Item getItem() {
            return item;
        }

        public Map<MailParameter, Object> getParams() {
            return params;
        }

        public TestData invoke(boolean alertEnabled, double alertPrice, double itemPrice) {
            ListCard lc = new ListCard();
            lc.setTp(ListCardType.ITEM);
            lc.setRlnm(PRICE_ALERT_TEST_ITEM_RLNM);

            ListCardBoard lcb = new ListCardBoard();
            lcb.addListCard(lc);

            Alert alert = new Alert();
            alert.setPrc(alertPrice);
            alert.setMl(EMAIL);
            alert.setPrclrt(alertEnabled);
            lcb.addAlert(PRICE_ALERT_TEST_ITEM_RLNM, alert);

            us = new UserSupplement();
            us.setLcl(Locale.US);
            us.setCd(USER_CODE);

            List<ListCardBoard> boards = new ArrayList<ListCardBoard>();
            boards.add(lcb);
            us.setBrds(boards);

            entries = new ArrayList<UserUserSupplementEntry>();
            UserUserSupplementEntry entry = new UserUserSupplementEntry(user, us);
            entries.add(entry);

            createItem(itemPrice);

            params = new HashMap<MailParameter, Object>();
            params.put(MailParameter.ITEM, item);
            params.put(MailParameter.PRICE_ALERT, alert);
            params.put(MailParameter.USER_SUPPLEMENT, us);
            params.put(MailParameter.ITEM_RELEVANCY, 99);
            return this;
        }

        private void createItem(double itemPrice) {
            item = new Item();

            Attribute attr = new Attribute("LowestPrice", itemPrice);
            List<Attribute> sbttrs = new ArrayList<Attribute>();
            sbttrs.add(attr);
            item.setSbttrs(sbttrs);
        }
    }
}
