package com.lela.web.test.controller;

import com.lela.commons.event.EventHelper;
import com.lela.commons.service.AffiliateSaleService;
import com.lela.commons.service.UserService;
import com.lela.commons.service.UserTrackerService;
import com.lela.domain.document.AffiliateSale;
import com.lela.domain.document.User;
import com.lela.domain.document.UserTracker;
import com.lela.web.web.controller.affiliate.SalesPixelController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletResponse;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

/**
 * Created with IntelliJ IDEA.
 * User: ballmw
 * Date: 12/14/12
 * Time: 8:15 AM
 * To change this template use File | Settings | File Templates.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(EventHelper.class)
public class SalesPixelControllerUnitTest {

    @Mock
    AffiliateSaleService affiliateSaleService;

    @Mock
    UserService userService;

    @Mock
    UserTrackerService userTrackerService;

    @InjectMocks
    SalesPixelController salesPixelController;

    @Test
    public void testTrack() throws Exception
    {
        String affiliate = "abc";
        String userCode = "xyz-def";
        String subid = "";//date-redirect-product
        String productId = "Z98765";
        String productCategory = "categoryName";
        String productName = "My item name";
        Double totalPrice = 10.01d;
        Double commission = 2.53d;
        String orderId = "orderId";
        Integer quantity = 1;
        HttpServletResponse response = new MockHttpServletResponse();

        User user = new User();
        UserTracker userTracker = new UserTracker();

        ArgumentCaptor< Object > trackEvent = (ArgumentCaptor) ArgumentCaptor.forClass(Object.class);
        when(userService.findUserByCode(userCode)).thenReturn(user);
        when(userTrackerService.findByUserCode(userCode)).thenReturn(userTracker);
        mockStatic(EventHelper.class);

        salesPixelController.servePixel(affiliate, subid, productId, productCategory, productName, totalPrice, commission, quantity, orderId, response);

        verifyStatic(times(1));
        EventHelper.post(trackEvent.capture());

        verify(affiliateSaleService).save(any(AffiliateSale.class));
    }
}
