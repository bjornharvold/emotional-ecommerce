package com.lela.web.test.controller;

import javax.servlet.http.HttpSession;

import com.lela.commons.service.ProfileService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.ui.Model;
import org.springframework.validation.support.BindingAwareModelMap;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.handler.DispatcherServletWebRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import com.lela.commons.service.UserSupplementService;
import com.lela.commons.spring.security.SpringSecurityHelper;
import com.lela.domain.document.User;
import com.lela.domain.document.UserSupplement;
import com.lela.web.test.AbstractFunctionalTest;
import com.lela.web.web.controller.mailchimp.WebhookController;

public class WebHookControllerFunctionalTest extends AbstractFunctionalTest{

	@Autowired
	private WebhookController webhookController;
	
	@Autowired
	private UserSupplementService userSupplementService;

    @Autowired
    private ProfileService profileService;

    private String fakeEmail = "fake@fake.com";
    Model map = new BindingAwareModelMap();
    MockHttpServletRequest request = new MockHttpServletRequest();
    MockHttpServletResponse response = new MockHttpServletResponse();
    
    RedirectAttributes redirectAttributes = new RedirectAttributesModelMap();
    HttpSession session = new MockHttpSession();
    private User user;
    private UserSupplement us;
    
    WebRequest webRequest = null;
    
    @Before
    public void beforeEach(){
        SpringSecurityHelper.secureChannel();
        user = createRandomUser(true);
        us = createRandomUserSupplement(user);
        
        //Change the email to a specific one
        user.setMl(fakeEmail);
        us.setMl(fakeEmail);
        
        us = userService.saveUserSupplement(us);
        user = profileService.registerTestUser(user);
        
        //registerTestUser forces a Optin of false. So set it explicitly to true
        us = userSupplementService.findUserSupplementByEmail(fakeEmail);
        us.setPtn(true);
        userSupplementService.saveUserSupplement(us);
        
        SpringSecurityHelper.unsecureChannel();
        
    	request.addParameter("fired_at", "2012-12-12 12:12:12");
        request.addParameter("data[email]", fakeEmail);
        request.addParameter("data[email_type]", "html");
        request.addParameter("data[id]", "irrelevant");
        request.addParameter("data[list_id]", "someId");

        
        webRequest = new DispatcherServletWebRequest(request, response);
    }
    
    @After
    public void afterEach() {
        SpringSecurityHelper.secureChannel();
        if (user != null) {
            userService.removeUser(user);
        }
        SpringSecurityHelper.unsecureChannel();
    }
    
    @Test
    public void testUnsubscribeWebHook() throws Exception {
    	UserSupplement usBefore = userSupplementService.findUserSupplementByEmail(fakeEmail);
    	Assert.assertTrue(usBefore.getPtn());
    	
    	webhookController.unsubscribe(webRequest);
    	
    	//Will exercise cache
    	UserSupplement usAfter = userSupplementService.findUserSupplementByEmail(fakeEmail);
    	Assert.assertFalse(usAfter.getPtn());
    }
}
