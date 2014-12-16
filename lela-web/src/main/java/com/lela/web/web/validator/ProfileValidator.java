/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.web.web.validator;

import com.lela.domain.dto.Profile;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * User: bjorn Date: Sep 25, 2008 Time: 1:32:16 PM
 */
public class ProfileValidator implements Validator {

    public boolean supports(Class<?> clazz) {
		return Profile.class.isAssignableFrom(clazz);
	}

	public void validate(Object target, Errors errors) {
		Profile profile = (Profile) target;

		if (!errors.hasErrors()) {

		}
	}
}