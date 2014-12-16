package com.lela.data.web.controller.administration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.MessageSource;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.support.BindingAwareModelMap;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import com.lela.commons.service.AffiliateService;
import com.lela.commons.service.CampaignService;
import com.lela.commons.service.StaticContentService;
import com.lela.commons.web.utils.WebConstants;
import com.lela.data.web.controller.administration.affiliate.CampaignManagerController;
import com.lela.data.web.validator.CampaignValidator;
import com.lela.domain.document.Campaign;


/**
 * Unit test for all functionality in the CampaignManagerController class.
 * Note that using Mockito stubbing, this class ONLY tests code in the controller,  and not it's collaborators.
 * 
 * @author pankaj
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class CampaignManagerControllerUnitTests {

	//Create mocks of the collaborators of the class under test
	@Mock private CampaignService campaignService;
	@Mock private StaticContentService staticContentService;
	@SuppressWarnings("unused") //This is actually used in the controller
	@Mock private CampaignValidator campaignValidator;
	@Mock private MessageSource messageSource ;
	@Mock private AffiliateService affiliateService;
	@Mock private BindingResult errors;
	
	//Inject the previously created mocks into the class under test
	@InjectMocks private CampaignManagerController controller;
		
	private final String URL_NAME = "urlName";
	private final String BLANK_URL_NAME = "";
	private final String MESSAGE = "aMessage";
	
	private  Campaign campaign;
	private  Locale locale;
	private  Model model;
	private  RedirectAttributes redirectAttributes ;

	
	@Before
	public  void beforeEach(){
		this.campaign = new Campaign();
		this.locale = Locale.US;
		this.model = new BindingAwareModelMap();
		this.redirectAttributes = new RedirectAttributesModelMap();
		when(affiliateService.findAffiliateAccounts(new ArrayList<String>(anyInt()))).thenReturn(null);
		when(staticContentService.findStaticContents(new ArrayList<String>(anyInt()))).thenReturn(null);
	}
	
	@Test
	public void testShowCampaignWhenCampaignNotFound() throws Exception{			
		//Note that we should use a specific string for the arg 
		//instead of using the anyString matcher
		when(campaignService.findCampaignByUrlName(URL_NAME)).thenReturn(null);
		String view = controller.showCampaign(URL_NAME, model);		
		//If campaign is null, assert that model doesn't contain the campaign
		assertTrue(!model.containsAttribute(WebConstants.CAMPAIGN));
		//Also Make sure that the service method was called once
		verify(campaignService).findCampaignByUrlName(anyString());
	
		//Assert that a view is always returned
		assertNotNull(view);
		assertTrue(view.startsWith("campaign.form.view"));
	}
	@Test
	public void testShowCampaignWhenCampaignIdFound() throws Exception{			
		//stub the mock to return a campaign when a specific url is passed to it.
		when(campaignService.findCampaignByUrlName(URL_NAME)).thenReturn(campaign);
		String view = controller.showCampaign(URL_NAME, model);			
		//If campaign is stored in the model
		assertTrue(model.containsAttribute(WebConstants.CAMPAIGN));
		
		//Assert that a view is always returned
		assertNotNull(view);
		assertTrue(view.startsWith("campaign.form.view"));
	}
	@Test
	public void testEditCampaignThatExists() throws Exception{
		//Based on the code to test, we need to distinguish an existing campaign from a new campaign
		campaign.setRlnm("existingCampaign");
		when(campaignService.findCampaignByUrlName(anyString())).thenReturn(campaign);
		String view = controller.editCampaign(URL_NAME, model);
		//Verify that the service is called once
		verify(campaignService).findCampaignByUrlName(anyString());
		//Verify that the campaignEntry in the model is the same existing one (not a new one)
		
		assertTrue(model.containsAttribute(WebConstants.CAMPAIGN));
		Campaign retrievedCampaign = (Campaign)model.asMap().get(WebConstants.CAMPAIGN);
		assertEquals(campaign.getRlnm(), retrievedCampaign.getRlnm());
		//Assert that a view is always returned
		assertNotNull(view);
		assertTrue(view.startsWith("campaign.form.edit"));		
	}
	
	@Test
	public void testEditCampaignThatDoesNotExist() throws Exception{

		//stub the service to return a null campaign
		when(campaignService.findCampaignByUrlName(URL_NAME)).thenReturn(null);		
		//make the call
		String view = controller.editCampaign(URL_NAME, model);
		//Verify that the service is called once. 
		//Note that we should use a matcher for anyString instead of being specific.
		verify(campaignService).findCampaignByUrlName(anyString());
		//Verify that the campaign does not exist in the model
		assertNull(model.asMap().get(WebConstants.CAMPAIGN));
		//Assert that a view is always returned
		assertNotNull(view);
		assertTrue(view.startsWith("campaign.form.edit"));				
	}	
	@Test
	public void testEditCampaignForBlankURL() throws Exception{
		//Define a new campaign to distinguish it from an existing one
		campaign = new Campaign();
		//stub the service to return a new campaign
		when(campaignService.findCampaignByUrlName(anyString())).thenReturn(campaign);
		//make the call
		String view = controller.editCampaign(BLANK_URL_NAME, model);
		//Verify that the service is never called for any URL passed in
		verify(campaignService, never()).findCampaignByUrlName(anyString());
		//Verify that the campaign in the model is a new one as per the code
		assertTrue(model.containsAttribute(WebConstants.CAMPAIGN));
		Campaign retrievedCampaign = (Campaign)model.asMap().get(WebConstants.CAMPAIGN);
		assertTrue(retrievedCampaign.getRlnm() == null);
		//Assert that a view is always returned
		assertNotNull(view);
		assertTrue(view.startsWith("campaign.form.edit"));		
	}	

	@Test
	public void testSaveCampaignWithPassingValidation() throws Exception{
		when(errors.hasErrors()).thenReturn(false);
		when(messageSource.getMessage(anyString(), new String[]{anyString()}, any(Locale.class))).thenReturn(MESSAGE);
		//save campaign
		when(campaignService.saveCampaign(campaign)).thenReturn(campaign);
		String view = controller.saveCampaign(campaign, errors, redirectAttributes, locale, model);
		//Assert that message is in in the redirect
		String message = (String)redirectAttributes.getFlashAttributes().get(WebConstants.MESSAGE);
		assertEquals(MESSAGE, message);		
		//Assert that the view is the redirect one
		assertNotNull(view);
		assertTrue(view.startsWith("redirect"));	
	}
	@Test
	public void testSaveCampaignWithFailingValidation() throws Exception{
		when(errors.hasErrors()).thenReturn(true);
		when(campaignService.saveCampaign(campaign)).thenReturn(campaign);
		String view = controller.saveCampaign(campaign, errors, redirectAttributes, locale, model);
	    //Assert that the model contains campaign
		assertTrue(model.containsAttribute(WebConstants.CAMPAIGN));
		assertEquals(campaign, model.asMap().get(WebConstants.CAMPAIGN));
		//Assert that view returned is the campaign form
		assertNotNull(view);
		assertTrue(view.startsWith("campaign.form"));	
	}
	@Test
	public void testDeleteCampaign() throws Exception{
		//Note the condition of testing what would happen if a nonexistent URL is passed to campaignService.removeCampaign(...)
		//is not covered here. Instead, that should be covered in the unit test of the campaignService 
		RedirectAttributes redirectAttributes = new RedirectAttributesModelMap();
		when(messageSource.getMessage(anyString(), new String[]{anyString()}, any(Locale.class))).thenReturn(MESSAGE);
		when(campaignService.removeCampaign(URL_NAME)).thenReturn(campaign);
		String view = controller.deleteCampaign(URL_NAME, redirectAttributes, locale);
		assertNotNull(view);
		assertTrue(view.startsWith("redirect"));
	}
}
