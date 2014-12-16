package com.lela.web.test.controller;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.Locale;
import java.util.Map;

import junit.framework.Assert;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.MessageSource;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.ui.Model;
import org.springframework.validation.support.BindingAwareModelMap;

import com.lela.commons.service.AffiliateService;
import com.lela.commons.service.CampaignService;
import com.lela.commons.service.StaticContentService;
import com.lela.commons.service.UserService;
import com.lela.commons.service.UserTrackerService;
import com.lela.commons.utilities.UserSessionTrackingHelper;
import com.lela.commons.web.utils.WebConstants;
import com.lela.domain.document.AffiliateAccount;
import com.lela.domain.document.Campaign;
import com.lela.domain.document.StaticContent;
import com.lela.domain.document.User;
import com.lela.domain.dto.UserAttributes;
import com.lela.web.web.controller.AffiliateController;

@RunWith(MockitoJUnitRunner.class)
public class AffiliateControllerUnitTests {

    @Mock private UserService userService;
    @Mock private AffiliateService affiliateService;
    @Mock private CampaignService campaignService;
    @Mock private UserTrackerService userTrackerService;
    @Mock private StaticContentService staticContentService;
    @Mock private UserSessionTrackingHelper userSessionTrackingHelper;
    @Mock private MessageSource messageSource;
    
	@InjectMocks private AffiliateController affiliateController ;

	@Spy private MockHttpServletRequest aMockRequest ;
	@Spy private MockHttpServletResponse aMockResponse; 
	@Spy private MockHttpSession aMockSession; 
	
	
	private Campaign aCampaign;
	private User aUser;
	private StaticContent aStaticContent;
	private AffiliateAccount anAffiliate;
	private UserAttributes someUserAttributes;	
	private Model aModel ;

	
	@Before
	public void beforeEach(){
        affiliateController.setUserSessionTrackingHelper(userSessionTrackingHelper);

		aUser = new User();
		aCampaign = new Campaign();
		DateTime dt = new DateTime(new Date());
		DateTime tomorrow = dt.plusDays(1);
		DateTime yesterday = dt.minusDays(1);
		
		aCampaign.setStrtdt(yesterday.toDate());
		aCampaign.setNddt(tomorrow.toDate());
		
        aStaticContent = new StaticContent();
		anAffiliate = new AffiliateAccount();
		someUserAttributes = new UserAttributes();
		aModel = new BindingAwareModelMap();
	}

	@Test
	public void testLandingPageWithStaticContent() throws Exception{

		aCampaign.setSttccntnt("aStaticContentId");
		when(userSessionTrackingHelper.retrieveUserFromPrincipalOrSession(any(MockHttpSession.class))).thenReturn(aUser);
		
		when(affiliateService.findActiveAffiliateAccountByUrlName(anyString())).thenReturn(anAffiliate);
		when(staticContentService.findStaticContentByUrlName(any(String.class), any(Map.class))).thenReturn(aStaticContent);
		
		when(campaignService.findCampaignByUrlName(any(String.class))).thenReturn(aCampaign);
		String view = affiliateController.landingPage(someUserAttributes, "aCampaignName", "aReferrerUrlName", aMockSession, aMockRequest, aMockResponse, aModel, Locale.US);
		
		Assert.assertTrue(aModel.containsAttribute(WebConstants.STATIC_CONTENT));
		Assert.assertTrue(StringUtils.equals(view, "static.content"));
		
	}

	@Test
	public void testLandingPageWithRedirectUrl() throws Exception{
		aCampaign.setRdrctrl("aRedirectUrl");
		when(userSessionTrackingHelper.retrieveUserFromPrincipalOrSession(any(MockHttpSession.class))).thenReturn(aUser);
		
		when(affiliateService.findActiveAffiliateAccountByUrlName(anyString())).thenReturn(anAffiliate);
		when(staticContentService.findStaticContentByUrlName(any(String.class), any(Map.class))).thenReturn(aStaticContent);
		
		when(campaignService.findCampaignByUrlName(any(String.class))).thenReturn(aCampaign);
		String view = affiliateController.landingPage(someUserAttributes, "aCampaignName", "aReferrerUrlName", aMockSession, aMockRequest, aMockResponse, aModel, Locale.US);
		Assert.assertNotNull(view);
		Assert.assertTrue(view.startsWith("redirect"));
		
	}
	
	@Test
	public void testLandingPageWithCampaignView() throws Exception{
		aCampaign.setVwnm("aViewName");
		when(userSessionTrackingHelper.retrieveUserFromPrincipalOrSession(any(MockHttpSession.class))).thenReturn(aUser);
		
		when(affiliateService.findActiveAffiliateAccountByUrlName(anyString())).thenReturn(anAffiliate);
		when(staticContentService.findStaticContentByUrlName(any(String.class), any(Map.class))).thenReturn(aStaticContent);
		
		when(campaignService.findCampaignByUrlName(any(String.class))).thenReturn(aCampaign);
		String view = affiliateController.landingPage(someUserAttributes, "aCampaignName", "aReferrerUrlName", aMockSession, aMockRequest, aMockResponse, aModel, Locale.US);
		Assert.assertTrue(aModel.containsAttribute(WebConstants.CAMPAIGN));
		Assert.assertTrue(aModel.containsAttribute(WebConstants.AFFILIATE));
		Assert.assertNotNull(view);
		Assert.assertTrue(StringUtils.equals(view, "aViewName"));
	}
	
	@Test
	public void testLandingPageForCorrectAttributesInSession() throws Exception{
		aCampaign.setRlnm("aCampaignRlNm");
		aMockRequest.setParameter(WebConstants.CAMPAIGN_ID_REQUEST_PARAMETER, aCampaign.getRlnm());
		
		aCampaign.setVwnm("aViewName");
		when(userSessionTrackingHelper.retrieveUserFromPrincipalOrSession(any(MockHttpSession.class))).thenReturn(aUser);
		when(campaignService.findCampaignByUrlName(any(String.class))).thenReturn(aCampaign);
		when(affiliateService.findActiveAffiliateAccountByUrlName(anyString())).thenReturn(anAffiliate);
		when(staticContentService.findStaticContentByUrlName(any(String.class), any(Map.class))).thenReturn(aStaticContent);
		
		when(campaignService.findCampaignByUrlName(any(String.class))).thenReturn(aCampaign);
		String view = affiliateController.landingPage(someUserAttributes, "aCampaignName", "aReferrerUrlName", aMockSession, aMockRequest, aMockResponse, aModel, Locale.US);
		Assert.assertTrue(aModel.containsAttribute(WebConstants.CAMPAIGN));
		Assert.assertTrue(aModel.containsAttribute(WebConstants.AFFILIATE));
		Assert.assertNotNull(view);
		Assert.assertTrue(StringUtils.equals(view, "aViewName"));
	}

	@Test
	public void testLandingPageForNonExistentCampaign() throws Exception{
		aCampaign.setRlnm("aCampaignRlNm");
		aMockRequest.setParameter(WebConstants.CAMPAIGN_ID_REQUEST_PARAMETER, "aNonExistentCampaignRlNm");
		
		when(userSessionTrackingHelper.retrieveUserFromPrincipalOrSession(any(MockHttpSession.class))).thenReturn(aUser);
		when(campaignService.findCampaignByUrlName(any(String.class))).thenReturn(null);
		when(affiliateService.findActiveAffiliateAccountByUrlName(anyString())).thenReturn(anAffiliate);
		String view = affiliateController.landingPage(someUserAttributes, "aCampaignName", "aReferrerUrlName", aMockSession, aMockRequest, aMockResponse, aModel, Locale.US);
		verify(messageSource).getMessage(eq("campaign.not.found"), any(String[].class), any(Locale.class));
		Assert.assertNotNull(view);
		Assert.assertTrue(StringUtils.equals(view, "campaign.not.found.view"));	
	}
	
	@Test
	public void testLandingPageForInactiveCampaign() throws Exception{
		aCampaign.setRlnm("aCampaignRlNm");
		DateTime dt = new DateTime(new Date());
		DateTime yesterday = dt.minusDays(1);
		DateTime dayBefore = dt.minusDays(2);
		aCampaign.setStrtdt(dayBefore.toDate());
		aCampaign.setNddt(yesterday.toDate());
		aMockRequest.setParameter(WebConstants.CAMPAIGN_ID_REQUEST_PARAMETER, aCampaign.getRlnm());
		
		when(userSessionTrackingHelper.retrieveUserFromPrincipalOrSession(any(MockHttpSession.class))).thenReturn(aUser);
		when(campaignService.findCampaignByUrlName(any(String.class))).thenReturn(aCampaign);
		when(affiliateService.findActiveAffiliateAccountByUrlName(anyString())).thenReturn(null);
		String view = affiliateController.landingPage(someUserAttributes, "aCampaignName", "aReferrerUrlName", aMockSession, aMockRequest, aMockResponse, aModel, Locale.US);
		verify(messageSource).getMessage(eq("campaign.not.active.or.current"), any(String[].class), any(Locale.class));
		Assert.assertNotNull(view);
		Assert.assertTrue(StringUtils.equals(view, "campaign.not.found.view"));
	}	
	
	@Test
	public void testLandingPageForNonExistentAffiliateId() throws Exception{
		aCampaign.setRlnm("aCampaignRlNm");
		DateTime dt = new DateTime(new Date());
		DateTime tomorrow = dt.plusDays(1);
		DateTime dayBefore = dt.minusDays(2);
		aCampaign.setStrtdt(dayBefore.toDate());
		aCampaign.setNddt(tomorrow.toDate());
		aMockRequest.setParameter(WebConstants.CAMPAIGN_ID_REQUEST_PARAMETER, aCampaign.getRlnm());
		
		when(userSessionTrackingHelper.retrieveUserFromPrincipalOrSession(any(MockHttpSession.class))).thenReturn(aUser);
		when(campaignService.findCampaignByUrlName(any(String.class))).thenReturn(aCampaign);
		when(affiliateService.findActiveAffiliateAccountByUrlName(anyString())).thenReturn(null);
		String view = affiliateController.landingPage(someUserAttributes, "aCampaignName", "aReferrerUrlName", aMockSession, aMockRequest, aMockResponse, aModel, Locale.US);
		verify(messageSource).getMessage(eq("affiliate.not.found"), any(String[].class), any(Locale.class));
		Assert.assertNotNull(view);
		Assert.assertTrue(StringUtils.equals(view, "campaign.not.found.view"));
	}
	@Test
	public void testBuildVelocityMap() throws Exception{
		aMockRequest.setAttribute("aRequestAttribute1", new String("aRequestAttributeValue1"));
		aMockRequest.setAttribute("aRequestAttribute2", new String("aRequestAttributeValue2"));
		aMockRequest.setParameter("aRequestParameter1", new String("aRequestParameterValue1"));
		aMockRequest.setParameter("aRequestParameter2", new String("aRequestParameterValue2"));
		
		aMockSession.setAttribute("aSessionAttribute1", new String("aSessionAttributeValue1"));
		aMockSession.setAttribute("aSessionAttribute2", new String("aSessionAttributeValue2"));
		aMockSession.setAttribute("aSessionAttribute3", new String("aSessionAttributeValue3")); 
		
		when(aMockRequest.getSession()).thenReturn(aMockSession);
		Map<String, Object> aMap  = affiliateController.buildVelocityMap(aCampaign, anAffiliate, aMockRequest); //((Map)aMap.get("parameter")).get("aSessionAttribute1")
		
		Assert.assertNotNull(aMap);
		Assert.assertTrue(aMap.size() == 3);
		Assert.assertNotNull(aMap.get(WebConstants.AFFILIATE));
		Assert.assertEquals(aMap.get(WebConstants.AFFILIATE), anAffiliate);
		Assert.assertNotNull(aMap.get(WebConstants.CAMPAIGN));
		Assert.assertEquals(aMap.get(WebConstants.CAMPAIGN), aCampaign);		
		Assert.assertNotNull(aMap.get("parameter"));
		Assert.assertTrue(((Map<String, Object>)aMap.get("parameter")).size() == 7);
	}
}
