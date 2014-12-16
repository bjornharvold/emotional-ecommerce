package com.lela.web.web.mailchimp;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lela.commons.service.UserService;
import com.lela.domain.document.UserSupplement;

@Component
public class WebHookAdapter implements IWebHookAdapter {

	private final static Logger LOG = LoggerFactory.getLogger(WebHookAdapter.class);
	
	private final UserService userService;
	
	@Autowired
	public WebHookAdapter(final UserService userService){
		this.userService = userService;
	}
	
	@Override
	public void cleaned(WebHookData arg0) {
		LOG.warn("Mailchimp callback not yet implemeted!" );

	}

	@Override
	public void eMailUpdated(WebHookData arg0) {
		LOG.warn("Mailchimp callback not yet implemeted!" );

	}

	@Override
	public boolean isValidRequest(HttpServletRequest arg0) {
		LOG.warn("Mailchimp callback not yet implemeted!" );
		return false;
	}

	@Override
	public void profileUpdated(WebHookData arg0) {
		LOG.warn("Mailchimp callback not yet implemeted!" );

	}

	@Override
	public void userSubscribed(WebHookData webHookData) {
		LOG.warn("Mailchimp callback not yet implemeted!" );
	}

	@Override
	public void userUnsubscribed(WebHookData webHookData) {
		LOG.debug("Received mailchimp webhook: " + webHookData);
		if (webHookData.getMemberInfo() != null) {
			UserSupplement us = userService.findUserSupplementByEmail(webHookData.getMemberInfo().getEmail());
			if (us != null){
				us.setPtn(false);
				userService.saveUserSupplement(us);
				LOG.info("Set optin to false for user with email %s", webHookData.getMemberInfo().getEmail());
			} else {
				LOG.warn("Mailchimp callback sent a %s request for a non-existing email address %s",webHookData.getType(),  webHookData.getMemberInfo().getEmail());
			}
			
		} else {
			LOG.warn("Mailchimp callback webhook is missing member info!");
		}

	}

}
