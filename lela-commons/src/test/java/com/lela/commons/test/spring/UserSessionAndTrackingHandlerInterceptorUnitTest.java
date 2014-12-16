package com.lela.commons.test.spring;

import com.lela.commons.event.EventHelper;
import com.lela.commons.event.PageViewEvent;
import com.lela.commons.event.RegistrationEvent;
import com.lela.commons.event.SubscribeEvent;
import com.lela.commons.service.AffiliateService;
import com.lela.commons.service.UserTrackerService;
import com.lela.commons.spring.analytics.UserSessionAndTrackingHandlerInterceptor;
import com.lela.commons.spring.security.SpringSecurityHelper;
import com.lela.commons.utilities.UserSessionTrackingHelper;
import com.lela.commons.web.utils.WebConstants;
import com.lela.domain.document.AffiliateAccount;
import com.lela.domain.document.User;
import com.lela.domain.document.UserTracker;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;

import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

/**
 * Created with IntelliJ IDEA.
 * User: ballmw
 * Date: 11/8/12
 * Time: 2:30 PM
 * To change this template use File | Settings | File Templates.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(value = { EventHelper.class, SpringSecurityHelper.class })
public class UserSessionAndTrackingHandlerInterceptorUnitTest {
    private final String CAMPAIGN_URL_NAME = "aCampaignRlnm";
    private final String CAMPAIGN_BANNER_URL = "aCampaignBannerUrl";
    private final String CAMPAIGN_BANNER_POINT_TO_URL = "aCampaignBannerPointToUrl";
    
    @Mock
    UserSessionTrackingHelper userSessionTrackingHelper;

    @Mock
    UserTrackerService userTrackerService;
    
    @Mock
    AffiliateService affiliateService;

    @InjectMocks
    UserSessionAndTrackingHandlerInterceptor interceptor;

    private User user;
    private AffiliateAccount affiliate;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private MockHttpSession session;

    @Before
    public void init()
    {
        user = new User();
        
        affiliate = new AffiliateAccount();
        affiliate.setBnrrl(CAMPAIGN_BANNER_POINT_TO_URL);
        affiliate.setBnrmgrl(CAMPAIGN_BANNER_URL);
        affiliate.setRlnm(CAMPAIGN_URL_NAME);
        PowerMockito.mockStatic(SpringSecurityHelper.class);

        request = new MockHttpServletRequest();
        
        session = new MockHttpSession();
        session.setAttribute(WebConstants.SESSION_USER_CODE, "user-code");

        request.setSession(session);

        response = new MockHttpServletResponse();
        response.setContentType("text/html");
    }

    @Test
    public void testAfterCompletion() throws Exception
    {
        Object handler = new Object();
        Exception exception = null;

        when(userSessionTrackingHelper.retrieveUserFromPrincipalOrSession(request.getSession())).thenReturn(user);

        ArgumentCaptor<Object> trackEvent = (ArgumentCaptor)ArgumentCaptor.forClass(Object.class);
        mockStatic(EventHelper.class);

        // execute the service method
        interceptor.afterCompletion(request, response, handler, exception);

        verifyStatic(times(1));
        EventHelper.post(trackEvent.capture());

        assertTrue("PageViewEvent not posted", trackEvent.getAllValues().get(0) instanceof PageViewEvent);
    }

    @Test
    public void testSessionAttributeslWhenBannerIsAssociatedToCampaign() throws Exception {
        when(SpringSecurityHelper.getSecurityContextPrincipal()).thenReturn(null);
        when (affiliateService.findActiveAffiliateAccountByUrlName(anyString())).thenReturn(affiliate);

        request.setParameter(WebConstants.AFFILIATE_ID_REQUEST_PARAMETER, "anAffiliateId");
        request.setParameter(WebConstants.CAMPAIGN_ID_REQUEST_PARAMETER, "aCampaignId");

        interceptor.preHandle(request, response, null);

        Assert.assertNotNull(request.getSession().getAttribute(WebConstants.BANNER_URL));
        Assert.assertTrue(request.getSession().getAttribute(WebConstants.BANNER_URL).equals(CAMPAIGN_BANNER_URL));

        Assert.assertNotNull(request.getSession().getAttribute(WebConstants.BANNER_TARGET_URL));
        Assert.assertTrue(request.getSession().getAttribute(WebConstants.BANNER_TARGET_URL).equals(CAMPAIGN_BANNER_POINT_TO_URL));
    }

    @Test
    public void testSessionAttributesWhenNoBannerIsAssociatedToCampaign() throws Exception {
        //No Banner
        affiliate.setBnrmgrl(null);

        when(SpringSecurityHelper.getSecurityContextPrincipal()).thenReturn(null);
        when (affiliateService.findActiveAffiliateAccountByUrlName(anyString())).thenReturn(affiliate);

        request.setParameter(WebConstants.AFFILIATE_ID_REQUEST_PARAMETER, "anAffiliateId");
        request.setParameter(WebConstants.CAMPAIGN_ID_REQUEST_PARAMETER, "aCampaignId");

        interceptor.preHandle(request, response, null);

        Assert.assertNull(request.getSession().getAttribute(WebConstants.BANNER_URL));
        Assert.assertNull(request.getSession().getAttribute(WebConstants.BANNER_TARGET_URL));
    }

    @Test
    public void testSessionAttributesWhenNoCampaignIdParameterSet() throws Exception {
        when(SpringSecurityHelper.getSecurityContextPrincipal()).thenReturn(null);
        when (affiliateService.findActiveAffiliateAccountByUrlName(anyString())).thenReturn(affiliate);

        request.setParameter(WebConstants.AFFILIATE_ID_REQUEST_PARAMETER, "anAffiliateId");
        //request.setParameter(WebConstants.CAMPAIGN_ID_REQUEST_PARAMETER, "aCampaignId");

        interceptor.preHandle(request, response, null);

        Assert.assertNotNull(request.getSession().getAttribute(WebConstants.BANNER_URL));
        Assert.assertNotNull(request.getSession().getAttribute(WebConstants.BANNER_TARGET_URL));
    }
}
