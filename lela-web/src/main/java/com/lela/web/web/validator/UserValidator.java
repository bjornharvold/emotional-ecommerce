/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.web.web.validator;

import com.lela.commons.service.UserService;
import com.lela.domain.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * User: bjorn Date: Sep 25, 2008 Time: 1:32:16 PM
 */
@Component
public class UserValidator implements Validator {
    private final UserService userService;

    @Autowired
    public UserValidator(UserService userService) {
        this.userService = userService;
    }

    public boolean supports(Class<?> clazz) {
		return UserDto.class.isAssignableFrom(clazz);
	}

	public void validate(Object target, Errors errors) {
		UserDto user = (UserDto) target;

		if (!errors.hasErrors()) {
			// check for email availability
			if (userService.findUserByEmail(user.getMl()) != null) {
				errors.rejectValue("ml", "error.email.taken", null, "E-mail is already taken");
			}
		}
	}
}