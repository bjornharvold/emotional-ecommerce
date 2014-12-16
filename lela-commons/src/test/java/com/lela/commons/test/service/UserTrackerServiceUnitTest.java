package com.lela.commons.test.service;

import com.google.common.collect.Lists;
import com.lela.commons.event.EventHelper;
import com.lela.commons.event.PurchaseEvent;
import com.lela.commons.event.RegistrationEvent;
import com.lela.commons.event.SubscribeEvent;
import com.lela.commons.repository.UserTrackerRepository;
import com.lela.commons.service.UserService;
import com.lela.commons.service.impl.UserServiceImpl;
import com.lela.commons.service.impl.UserTrackerServiceImpl;
import com.lela.domain.document.Redirect;
import com.lela.domain.document.Sale;
import com.lela.domain.document.User;
import com.lela.domain.document.UserTracker;
import com.lela.domain.dto.AffiliateTransaction;
import com.mongodb.WriteResult;
import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.List;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

/**
 * Created with IntelliJ IDEA.
 * User: ballmw
 * Date: 11/8/12
 * Time: 1:10 PM
 * To change this template use File | Settings | File Templates.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(EventHelper.class)
public class UserTrackerServiceUnitTest {

    @Mock
    private MongoTemplate mongoTemplate;

    @Mock
    private UserTrackerRepository userTrackerRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    UserTrackerServiceImpl userTrackerService;

    ObjectId redirectId;
    ObjectId userTrackerId;

    @Before
    public void init()
    {
        redirectId = new ObjectId();
        userTrackerId = new ObjectId();
    }

    @Test
    public void testTrackRedirectSale()
    {
        Sale sale = new Sale();
        List<Sale> sales = Lists.newArrayList(sale);

        Redirect redirect = new Redirect();

        redirect.setId(redirectId);
        redirect.setSls(sales);

        AffiliateTransaction affiliateTransaction = new AffiliateTransaction();
        affiliateTransaction.setRedirect(redirect);

        UserTracker userTracker = new UserTracker();
        userTracker.setId(userTrackerId);

        User user = new User();

        when(mongoTemplate.updateFirst(any(Query.class), any(Update.class), eq(UserTracker.class))).thenReturn(mock(WriteResult.class));
        when(userTrackerRepository.findByRedirectId(affiliateTransaction.getRedirect().getId())).thenReturn(userTracker);
        when(userService.findUserByCode(eq(userTracker.getSrcd()))).thenReturn(user);

        ArgumentCaptor < Object > trackEvent = (ArgumentCaptor) ArgumentCaptor.forClass(Object.class);
        mockStatic(EventHelper.class);

        // execute the service method
        userTrackerService.trackRedirectSale(affiliateTransaction);

        verifyStatic(times(1));
        EventHelper.post(trackEvent.capture());

        assertTrue("PurchaseEvent not posted", trackEvent.getAllValues().get(0) instanceof PurchaseEvent);
        PurchaseEvent purchaseEvent =  (PurchaseEvent)trackEvent.getAllValues().get(0);
        assertNotNull("User was null", purchaseEvent.getUser());
    }

    @Test
    public void testTrackRedirectSaleAnonymouUser()
    {
        Sale sale = new Sale();
        List<Sale> sales = Lists.newArrayList(sale);

        Redirect redirect = new Redirect();

        redirect.setId(redirectId);
        redirect.setSls(sales);

        AffiliateTransaction affiliateTransaction = new AffiliateTransaction();
        affiliateTransaction.setRedirect(redirect);

        UserTracker userTracker = new UserTracker();
        userTracker.setId(userTrackerId);

        User user = new User();

        when(mongoTemplate.updateFirst(any(Query.class), any(Update.class), eq(UserTracker.class))).thenReturn(mock(WriteResult.class));
        when(userTrackerRepository.findByRedirectId(affiliateTransaction.getRedirect().getId())).thenReturn(userTracker);
        when(userService.findUserByCode(eq(userTracker.getSrcd()))).thenReturn(null);

        ArgumentCaptor < Object > trackEvent = (ArgumentCaptor) ArgumentCaptor.forClass(Object.class);
        mockStatic(EventHelper.class);

        // execute the service method
        userTrackerService.trackRedirectSale(affiliateTransaction);

        verifyStatic(times(1));
        EventHelper.post(trackEvent.capture());

        assertTrue("PurchaseEvent not posted", trackEvent.getAllValues().get(0) instanceof PurchaseEvent);

        PurchaseEvent purchaseEvent =  (PurchaseEvent)trackEvent.getAllValues().get(0);
        assertNull("User was not null", purchaseEvent.getUser());

    }

    @Test
    public void testTrackAnonymousSale()
    {
        Sale sale = new Sale();
        List<Sale> sales = Lists.newArrayList(sale);

        Redirect redirect = new Redirect();

        redirect.setId(redirectId);
        redirect.setSls(sales);

        AffiliateTransaction affiliateTransaction = new AffiliateTransaction();
        affiliateTransaction.setRedirect(redirect);

        ArgumentCaptor<Object> trackEvent = (ArgumentCaptor)ArgumentCaptor.forClass(Object.class);
        mockStatic(EventHelper.class);

        // execute the service method
        userTrackerService.trackAnonymousSale(affiliateTransaction);

        verifyStatic(times(1));
        EventHelper.post(trackEvent.capture());

        assertTrue("PurchaseEvent not posted", trackEvent.getAllValues().get(0) instanceof PurchaseEvent);

    }
}
