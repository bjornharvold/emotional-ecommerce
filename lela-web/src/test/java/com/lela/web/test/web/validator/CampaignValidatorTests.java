package com.lela.web.test.web.validator;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import junit.framework.Assert;

import org.bson.types.ObjectId;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.validation.BindException;

import com.lela.commons.service.CampaignService;
import com.lela.domain.document.Campaign;
import com.lela.web.web.validator.CampaignValidator;

/**
 * Unit Tests for functionality coded in the CampaignValidator
 * Note use of partial mocking using @Spy
 * 
 * @see {@link org.mockito.Spy}
 * @author pankaj
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class CampaignValidatorTests {

	//Mock the collaborators of the class under test
	@Mock private CampaignService campaignService;
	//Create a partial mock because we want to stub the hasErrors method but not the getErrorCount() method of this class.
	@Spy private BindException errors = new BindException(new Campaign(), "test");
	//Inject the mocked collaborators into the class under test
	@InjectMocks private CampaignValidator validator;
	
	private static final String URLNAME = "URL";

	private Campaign campaign1;
	private Campaign campaign2 ;
	private ObjectId id1;
	private ObjectId id2;
	private DateTime today = new DateTime();
	private DateTime tomorrow = today.plusDays(1);
	
	@Before
	public void beforeEach(){
		//Mockito does not allow the mocking of equals method, else it would be easy to stub the equals method of the 
		//ObjectId class. So we have to construct the ObjectIds and ensure that the Ids are unequal		
		//Created two campaign objects that have the same URLNAME but different Ids to simulate invalid state
		
		//Needed a handy way to construct an ObjectId, so used jodaTime
		this.id1 = new ObjectId(today.toDate());
		this.id2 = new ObjectId(tomorrow.toDate());
		
		this.campaign1 = new Campaign();
		campaign1.setId(id1);
		campaign1.setRlnm(URLNAME);
		this.campaign2 = new Campaign();
		campaign2.setId(id2);
		campaign2.setRlnm(URLNAME);
	}
	@Test
	public void testValidateWithNoPreexistingErrorsRaisesError() throws Exception{	
		when(errors.hasErrors()).thenReturn(false);
		
		when(campaignService.findCampaignByUrlName(anyString())).thenReturn(campaign2);
		//doNothing(errors.rejectValue(anyString(), anyString()));
		//make the call
		validator.validate(campaign1, errors);
		verify(campaignService).findCampaignByUrlName(anyString());
		verify(errors).rejectValue(anyString(), anyString());
		//Assert that an error was registered.
		//Assert.assertEquals(1, errors.getErrorCount());
	}
	@Test
	public void testValidateWithNoPreexistingErrorsAndRaiseNoError() throws Exception{	
		
		when(errors.hasErrors()).thenReturn(false);
		
		//Ensure that ids are the same
		campaign2.setId(id1);
		when(campaignService.findCampaignByUrlName(anyString())).thenReturn(campaign2);
		
		//make the call
		validator.validate(campaign1, errors);
		verify(campaignService).findCampaignByUrlName(anyString());
		
		//Assert that an error was not registered.
		Assert.assertEquals(0, errors.getErrorCount());
	}
	@Test
	public void testValidateWithPreexistingErrorsRaisesNoError() throws Exception{	
		when(errors.hasErrors()).thenReturn(true);
		//make the call
		validator.validate(campaign1, errors);
		verify(campaignService, never()).findCampaignByUrlName(anyString());
		
		//Assert that an error was not registered.
		Assert.assertEquals(0, errors.getErrorCount());
		
	}
	
	@Test
	public void testValidateTwoOfThreeRaisesError() throws Exception{	
		when(errors.hasErrors()).thenReturn(false);
		when(campaignService.findCampaignByUrlName(anyString())).thenReturn(null);
		
		//Set 2 values on the campaign1 object
		campaign1.setRdrctrl("foo");
		campaign1.setVwnm("bar");
		//make the call
		validator.validate(campaign1, errors);
		
		//Assert that an error was registered.
		Assert.assertEquals(1, errors.getErrorCount());
	}
	
	@Test
	public void testValidateOneOfThreeRaisesNoError() throws Exception{	
		when(errors.hasErrors()).thenReturn(false);
		when(campaignService.findCampaignByUrlName(anyString())).thenReturn(null);
		
		//Set 1 value on the campaign1 object
		campaign1.setRdrctrl("foo");
		//make the call
		validator.validate(campaign1, errors);
		
		//Assert that an error was not registered.
		Assert.assertEquals(0, errors.getErrorCount());	
	}
}
