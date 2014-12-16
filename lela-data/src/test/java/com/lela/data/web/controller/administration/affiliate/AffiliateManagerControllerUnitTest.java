package com.lela.data.web.controller.administration.affiliate;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.OutputStream;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Future;

import org.apache.poi.ss.usermodel.Workbook;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import com.lela.commons.service.AffiliateService;


@RunWith(MockitoJUnitRunner.class)
public class AffiliateManagerControllerUnitTest {

	//Create mocks of the collaborators of the class under test
	@Mock private AffiliateService affiliateService;
	@Mock private  Workbook workbook;
	@Mock private  Future<Workbook> workbookFuture;
	@Mock private MessageSource messageSource ;//used in controller
	
	@Spy private MockHttpServletRequest aMockRequest ;
	@Spy private MockHttpServletResponse aMockResponse; 
	@Spy private MockHttpSession aMockSession; 
	@Spy private  RedirectAttributesModelMap redirectAttributes ;
	
	//Inject the previously created mocks into the class under test
	@InjectMocks private AffiliateManagerController controller;
	
	private  Locale locale;
	
	@SuppressWarnings("unchecked")
	@Before
	public  void beforeEach(){
		controller.setNumberOfSecondsToWaitForReportToGenerate(2);
		this.locale = Locale.US;
		this.redirectAttributes = new RedirectAttributesModelMap();
	}
	
	
	@SuppressWarnings("unchecked")
	@Test
	public void testGenerateAffiliateUsersReportThatRunsWithin2Seconds() throws Exception{	
		
		when(affiliateService.generateAffiliateUserDetailsReport(anyString())).thenReturn(workbookFuture);
		when(workbookFuture.isDone()).thenReturn(true);
		when(workbookFuture.get()).thenReturn(workbook);
		
		controller.generateAffiliateUsersReport(anyString(), redirectAttributes, aMockRequest, aMockResponse, locale);
		verify(workbook).write(any(OutputStream.class));
	}

	
	@Test
	public void testGenerateAffiliateUsersReportThatRunsLongerThan2Seconds() throws Exception{	
		
		when(affiliateService.generateAffiliateUserDetailsReport(anyString())).thenReturn(workbookFuture);
		when(workbookFuture.isDone()).thenReturn(false);
		when(workbookFuture.get()).thenReturn(workbook);
		
		controller.generateAffiliateUsersReport(anyString(), redirectAttributes, aMockRequest, aMockResponse, locale);
		verify(workbook, never()).write(any(OutputStream.class));
		Map<String, ?> map = redirectAttributes.getFlashAttributes();
		assertTrue(flashMapContainsKey(map, "usersReportFeedback"));		
	}
	

	@SuppressWarnings("unchecked")
	@Test
	public void testDownloadAffiliateUsersReportFinishedDownload() throws Exception{	
		
		when(aMockRequest.getSession(true)).thenReturn(aMockSession);
		when(((Future<Workbook>)aMockSession.getAttribute(anyString()))).thenReturn(workbookFuture);
		when(workbookFuture.isDone()).thenReturn(true);
		when(workbookFuture.get()).thenReturn(workbook);
		
		controller.downloadAffiliateUsersReport("REPORT_ID", anyString(), redirectAttributes, aMockRequest, aMockResponse, locale);
		verify(workbook).write(any(OutputStream.class));		
	}	

	@SuppressWarnings("unchecked")
	@Test
	public void testDownloadAffiliateUsersReportUnFinishedDownload() throws Exception{	
		
		when(aMockRequest.getSession(true)).thenReturn(aMockSession);
		when(((Future<Workbook>)aMockSession.getAttribute(anyString()))).thenReturn(workbookFuture);
		when(workbookFuture.isDone()).thenReturn(false);
		when(workbookFuture.get()).thenReturn(workbook);
		
		controller.downloadAffiliateUsersReport("REPORT_ID", anyString(), redirectAttributes, aMockRequest, aMockResponse, locale);
		verify(workbook, never()).write(any(OutputStream.class));	
		Map<String, ?> map = redirectAttributes.getFlashAttributes();
		assertTrue(flashMapContainsKey(map, "usersReportFeedback"));
	}
	
	@Test
	public void testGenerateTrackingReportThatRunsWithin2Seconds() throws Exception{	
		when(affiliateService.generateAffiliateUserTrackingReport(anyString())).thenReturn(workbookFuture);
		when(workbookFuture.isDone()).thenReturn(true);
		when(workbookFuture.get()).thenReturn(workbook);
		
		controller.generateTrackingReport(anyString(), redirectAttributes, aMockRequest, aMockResponse, locale);
		verify(workbook).write(any(OutputStream.class));
	}

	@Test
	public void testGenerateTrackingReportThatRunsLongerThan2Seconds() throws Exception{	
		
		when(affiliateService.generateAffiliateUserTrackingReport(anyString())).thenReturn(workbookFuture);
		when(workbookFuture.isDone()).thenReturn(false);
		when(workbookFuture.get()).thenReturn(workbook);
		
		controller.generateTrackingReport(anyString(), redirectAttributes, aMockRequest, aMockResponse, locale);
		verify(workbook, never()).write(any(OutputStream.class));
		Map<String, ?> map = redirectAttributes.getFlashAttributes();
		assertTrue(flashMapContainsKey(map, "trackingReportFeedback"));		
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testDownloadTrackingReportFinishedDownload() throws Exception{	
		
		when(aMockRequest.getSession(true)).thenReturn(aMockSession);
		when(((Future<Workbook>)aMockSession.getAttribute(anyString()))).thenReturn(workbookFuture);
		when(workbookFuture.isDone()).thenReturn(true);
		when(workbookFuture.get()).thenReturn(workbook);
		
		controller.downloadTrackingReport("REPORT_ID", anyString(), redirectAttributes, aMockRequest, aMockResponse, locale);
		verify(workbook).write(any(OutputStream.class));		
	}	

	@SuppressWarnings("unchecked")
	@Test
	public void testDownloadTrackingReportUnFinishedDownload() throws Exception{	
		
		when(aMockRequest.getSession(true)).thenReturn(aMockSession);
		when(((Future<Workbook>)aMockSession.getAttribute(anyString()))).thenReturn(workbookFuture);
		when(workbookFuture.isDone()).thenReturn(false);
		when(workbookFuture.get()).thenReturn(workbook);
		
		controller.downloadTrackingReport("REPORT_ID", anyString(), redirectAttributes, aMockRequest, aMockResponse, locale);
		verify(workbook, never()).write(any(OutputStream.class));	
		Map<String, ?> map = redirectAttributes.getFlashAttributes();
		assertTrue(flashMapContainsKey(map, "trackingReportFeedback"));
	}
	
	

	@Test
	public void testGenerateRegistrationsByAffiliateReportThatRunsWithin2Seconds() throws Exception{	
		when(affiliateService.generateRegistrationsByAffiliateReport()).thenReturn(workbookFuture);
		when(workbookFuture.isDone()).thenReturn(true);
		when(workbookFuture.get()).thenReturn(workbook);
		
		controller.generateRegistrationsByAffiliateReport(aMockRequest, aMockResponse, locale);
		verify(workbook).write(any(OutputStream.class));
	}
	
	@Test
	public void testGenerateRegistrationsByAffiliateReportThatRunsLongerThan2Seconds() throws Exception{	
		
		when(affiliateService.generateRegistrationsByAffiliateReport()).thenReturn(workbookFuture);
		when(workbookFuture.isDone()).thenReturn(false);
		when(workbookFuture.get()).thenReturn(workbook);
		
		controller.generateRegistrationsByAffiliateReport(aMockRequest, aMockResponse, locale);
		verify(workbook, never()).write(any(OutputStream.class));
		assertNotNull(aMockRequest.getAttribute("reportFeedback"));		
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testDownloadRegistrationsByAffiliateReportFinishedDownload() throws Exception{	
		
		when(aMockRequest.getSession(true)).thenReturn(aMockSession);
		when(((Future<Workbook>)aMockSession.getAttribute(anyString()))).thenReturn(workbookFuture);
		when(workbookFuture.isDone()).thenReturn(true);
		when(workbookFuture.get()).thenReturn(workbook);
		
		controller.downloadRegistrationsByAffiliateReport("REPORT_ID", aMockRequest, aMockResponse, locale);
		verify(workbook).write(any(OutputStream.class));		
	}	

	@SuppressWarnings("unchecked")
	@Test
	public void testDownloadRegistrationsByAffiliateReportUnFinishedDownload() throws Exception{	
		
		when(aMockRequest.getSession(true)).thenReturn(aMockSession);
		when(((Future<Workbook>)aMockSession.getAttribute(anyString()))).thenReturn(workbookFuture);
		when(workbookFuture.isDone()).thenReturn(false);
		when(workbookFuture.get()).thenReturn(workbook);
		
		controller.downloadRegistrationsByAffiliateReport("REPORT_ID", aMockRequest, aMockResponse, locale);
		verify(workbook, never()).write(any(OutputStream.class));	
		assertNotNull(aMockRequest.getAttribute("reportFeedback"));	
	}
	
	private boolean flashMapContainsKey(Map<String, ?> map, String key){
		return map.keySet().contains(key);
	}
	
}
