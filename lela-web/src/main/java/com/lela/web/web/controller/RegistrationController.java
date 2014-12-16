/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.web.web.controller;

import com.lela.commons.service.ProfileService;
import com.lela.commons.service.UserService;
import com.lela.commons.service.UserTrackerService;
import com.lela.commons.web.utils.WebConstants;
import com.lela.domain.document.AffiliateAccount;
import com.lela.domain.document.User;
import com.lela.domain.dto.UserDto;
import com.lela.domain.enums.AuthenticationType;
import com.lela.domain.enums.RegistrationType;
import com.lela.web.web.validator.UserValidator;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.mobile.device.Device;
import org.springframework.social.connect.web.SignInAdapter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Locale;

/**
 * Created by Bjorn Harvold
 * Date: 7/21/11
 * Time: 10:27 AM
 * Responsibility:
 */
@Controller
@SessionAttributes(types = {UserDto.class})
public class RegistrationController extends AbstractController {
    private static final String USER_REGISTRATION_SUCCESSFUL = "user.registration.successful";
    private final MessageSource messageSource;
    private final UserService userService;
    private final ProfileService profileService;
    private final UserValidator userValidator;
    private final SignInAdapter signInAdapter;
    private final UserTrackerService userTrackerService;

    @Autowired
    public RegistrationController(MessageSource messageSource, UserService userService,
                                  ProfileService profileService, UserTrackerService userTrackerService,
                                  UserValidator userValidator,
                                  SignInAdapter signInAdapter) {
        this.messageSource = messageSource;
        this.userService = userService;
        this.profileService = profileService;
        this.userValidator = userValidator;
        this.signInAdapter = signInAdapter;
        this.userTrackerService = userTrackerService;
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String showRegistrationForm(@RequestParam(value = WebConstants.REDIRECT, required = false) String redirectUrl,
                                       Device device,
                                       HttpSession session,
                                       WebRequest request,
                                       Model model) {
        String view = "register";
        model.addAttribute(WebConstants.IS_AJAX, false);

        UserDto dto = new UserDto();

        // if there is a redirect specified, save it in the session
        if (StringUtils.isNotBlank(redirectUrl)) {
            session.setAttribute(WebConstants.REDIRECT, redirectUrl);
        }

        model.addAttribute(WebConstants.USER_DTO, dto);

        // Registration form has been shown
        userTrackerService.trackRegistrationStart((String) session.getAttribute(WebConstants.SESSION_USER_CODE));

        return returnMobileViewIfNecessary(model, device, session, view);
    }

    /**
     * Submits a user registration request
     *
     * @param userDto            userDto
     * @param errors             errors
     * @param webRequest         webRequest
     * @param request            request
     * @param response           response
     * @param session            session
     * @param redirectAttributes redirectAttributes
     * @param locale             locale
     * @return
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String submitRegistrationForm(@Valid UserDto userDto, BindingResult errors,
                                         NativeWebRequest webRequest, HttpServletRequest request,
                                         HttpServletResponse response, HttpSession session,
                                         RedirectAttributes redirectAttributes,
                                         Locale locale) {
        String result = null;

        userValidator.validate(userDto, errors);

        if (errors.hasErrors()) {
            result = "register";
        } else {
            result = processRegistrationForm(userDto, webRequest, request,
                    response, session, redirectAttributes, locale);
        }

        return result;
    }

    @RequestMapping(value = "/register/verify/{tkn}", method = RequestMethod.GET)
    public String verifyEmail(@PathVariable("tkn") String tkn, Model uiModel) {
        try {
            userService.verifyEmail(tkn);
        } catch (IllegalAccessException e) {
            //the token could not be found, it could be old and deleted, it could be a fat fingered url
            return "register.invalid.verify";
        }
        return "register.valid.verify";
    }

    private String processRegistrationForm(UserDto userDto,
                                           NativeWebRequest webRequest,
                                           HttpServletRequest request,
                                           HttpServletResponse response,
                                           HttpSession session,
                                           RedirectAttributes redirectAttributes,
                                           Locale locale) {
        // before we register the user, we want to check if the user has already answered
        // some of our questions that are accessible via the session
        // if so, we add those motivators onto the newly created user
        User transientUser = getTransientUser(session);

        userDto.setLcl(locale);

        // Register the user
        User persistedUser = profileService.registerWebUser(userDto, transientUser, (AffiliateAccount)request.getAttribute(WebConstants.DOMAIN_AFFILIATE));

        if (redirectAttributes != null) {
            String message = messageSource.getMessage(USER_REGISTRATION_SUCCESSFUL, null, locale);
            // store a success message for rendering on the next request after redirect
            redirectAttributes.addFlashAttribute(WebConstants.MESSAGE, message);
        }

        // common things to do for after registration occurred
        processPostRegistration(request, response, persistedUser, RegistrationType.WEBSITE);
        String springSecurityPriorUrl = signInAdapter.signIn(persistedUser.getIdString(), null, webRequest);

        // Merge the transient and persistent user
        processPostLogin(request, persistedUser, AuthenticationType.USERNAME_PASSWORD);

        if (StringUtils.isNotEmpty(springSecurityPriorUrl)) {
            return "redirect:" + springSecurityPriorUrl;
        } else {
            return "redirect:/login/redirect";
        }
    }

}
