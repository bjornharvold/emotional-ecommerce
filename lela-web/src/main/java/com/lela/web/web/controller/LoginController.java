/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */


package com.lela.web.web.controller;

//~--- non-JDK imports --------------------------------------------------------

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;

import com.lela.commons.service.ProfileService;
import com.lela.commons.spring.security.SpringSecurityHelper;
import com.lela.commons.web.utils.WebConstants;
import com.lela.domain.document.User;
import com.lela.domain.dto.Principal;

//~--- classes ----------------------------------------------------------------

/**
 * User: Bjorn Harvold
 * Date: Aug 26, 2009
 * Time: 5:32:36 PM
 * Responsibility:
 */
@Controller
public class LoginController extends AbstractController {

    /**
     * Field description
     */
    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    private final ProfileService profileService;
    
    @Autowired
    public LoginController(ProfileService profileService) {
    	this.profileService = profileService;
    }


    //~--- methods ------------------------------------------------------------

    /**
     * Handles cancellation URL from the Spring Social Facebook login
     *
     * @return Tile def
     * @throws Exception ex
     */
    @RequestMapping(value = "/signin", method = RequestMethod.GET)
    public String socialCancelSignin() throws Exception {

        return "redirect:/login";
    }

    /**
     * Displays the login box on the index page.
     *
     * @param redirectUrl redirectUrl
     * @param request     request
     * @param model       model
     * @param session     session
     * @return Tile def
     * @throws Exception ex
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(@RequestParam(value = WebConstants.REDIRECT, required = false) String redirectUrl,
                        WebRequest request, Model model, Device device, HttpSession session) throws Exception {
        String view = "login";

        if (StringUtils.isNotBlank(redirectUrl)) {
            session.setAttribute(WebConstants.REDIRECT, redirectUrl);
        }

        model.addAttribute(WebConstants.IS_AJAX, false);

        return returnMobileViewIfNecessary(model, device, session, view);
    }

    /**
     * Method determines whether to forward to the admin page or the player page.
     * If the user ended up here by mistake she won't have the right credentials and there is nothing to worry about
     *
     * @param session session
     * @return Tile def
     * @throws Exception ex
     */
    @RequestMapping(value = "/login/redirect", method = RequestMethod.GET)
    public String redirect(HttpSession session, HttpServletRequest request) throws Exception {
        Principal principal = SpringSecurityHelper.getSecurityContextPrincipal();
        if (principal != null && principal.getUser() != null){ 
        	//User is logged in
        	profileService.enhanceProfile(principal.getUser(), 	request.getRemoteAddr()); // "206.160.145.6"
        }
        User transientUser = getTransientUser(session);
        Authentication auth = SpringSecurityHelper.getSecurityContextAuthentication();
        String view = "redirect:/login?success=false";
        boolean isAdmin = false;
        boolean isUser = false;

        if (auth == null) {
            log.error("This is not possible. The user cannot log in and get to this place without having a principal. " +
                    "Redirecting to log in page.");
        } 	else if (auth.getAuthorities() != null) {

            // before we forward the user to the default page for that specific right, 
            // we check for a redirect url in the session
            String redirectUrl = (String) session.getAttribute(WebConstants.REDIRECT);

            if (StringUtils.isNotBlank(redirectUrl)) {
                session.removeAttribute(WebConstants.REDIRECT);
            }

            for (GrantedAuthority authority : auth.getAuthorities()) {
                if (authority.getAuthority().equals("RIGHT_LOGIN_ADMIN")) {
                    isAdmin = true;
                } else if (authority.getAuthority().equals("RIGHT_LOGIN_USER")) {
                    isUser = true;
                }
            }

            // TODO admin login will be moved over to the data app in the near future [BH]
            if (isAdmin) {
                view = "redirect:/administration/dashboard";
            } else if (isUser) {
                if (StringUtils.isNotBlank(redirectUrl)) {
                    view = "redirect:" + redirectUrl;
                } else {
                    view = "redirect:/";
                }

            }

        }

        return view;
    }
}
