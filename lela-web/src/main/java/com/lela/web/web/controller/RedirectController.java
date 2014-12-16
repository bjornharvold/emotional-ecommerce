/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.web.web.controller;

import com.lela.commons.service.MerchantService;
import com.lela.commons.service.RedirectService;
import com.lela.commons.service.UserTrackerService;
import com.lela.commons.web.ResourceNotFoundException;
import com.lela.commons.web.utils.WebConstants;
import com.lela.domain.document.AffiliateAccount;
import com.lela.domain.document.Redirect;
import com.lela.domain.document.User;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by IntelliJ IDEA.
 * User: Martin Gamboa
 * Date: 10/4/11
 * Time: 8:22 AM
 * To change this template use File | Settings | File Templates.
 */

@Controller
public class RedirectController extends AbstractController {

    /** Field description */
    private final RedirectService redirectService;

    /** Field description */
    private final MerchantService merchantService;

    private final UserTrackerService userTrackerService;

    private static final String AFFILIATE_REPORTING_COOKIE = "ARTOKEN";

    /**
     * Constructs ...
     *
     *
     * @param redirectService redirectService
     * @param userTrackerService
     */
    @Autowired
    public RedirectController(RedirectService redirectService, MerchantService merchantService, UserTrackerService userTrackerService) {
        this.redirectService = redirectService;
        this.merchantService = merchantService;
        this.userTrackerService = userTrackerService;
    }

    /**
     * Document merchant-item selected
     *
     * @param merchantId merchantId
     * @param itemUrlName itemId
     * @throws Exception Exception
     */
    @RequestMapping(value = "/merchant/redirect", method = RequestMethod.GET)
    public String redirect(@RequestParam("merchantId") String merchantId,
                           @RequestParam("itemId") String itemUrlName,
                           HttpServletResponse response,
                           HttpSession session) throws Exception {

        User user = retrieveUserFromPrincipalOrSession(session);

        //Add a cookie for affiliate reporting
        buildAffiliateReportingCookie(response);

        // Construction the URL for the specific merchant
        Redirect redirect = merchantService.constructRedirect(itemUrlName, merchantId, user.getId());

        // Track the Redirect
        userTrackerService.trackRedirect((String)session.getAttribute(WebConstants.SESSION_USER_CODE), redirect);
        String view = "redirect:" + redirect.getRl();

        return view;
    }

    /**
     * Document merchant-item Add to cart
     *
     * @param merchantId merchantId
     * @param itemUrlName itemId
     * @throws Exception Exception
     */
    @RequestMapping(value = "/merchant/addtocart", method = RequestMethod.GET)
    public String addToCart(@RequestParam("merchantId") String merchantId,
                            @RequestParam("itemId") String itemUrlName,
                            HttpServletRequest request,
                            HttpServletResponse response,
                            HttpSession session) throws Exception {

        AffiliateAccount domainAffiliate = (AffiliateAccount) request.getAttribute(WebConstants.DOMAIN_AFFILIATE);
        User user = retrieveUserFromPrincipalOrSession(session);

        // Construction the URL for the specific merchant
        Redirect redirect = merchantService.constructAddToCartRedirect(domainAffiliate, itemUrlName, merchantId, user.getId());

        // Track the Redirect
        userTrackerService.trackRedirect((String)session.getAttribute(WebConstants.SESSION_USER_CODE), redirect);
        String view = "redirect:" + redirect.getRl();

        return view;
    }

    /**
    * Affiliate Reporting will use this cookie to help ensure (among other things) that the user is not a robot.
    * @param response
    */
    private void buildAffiliateReportingCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie(AFFILIATE_REPORTING_COOKIE, RandomStringUtils.randomAlphabetic(16) );
        cookie.setDomain(".lela.com");
        cookie.setSecure(false);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
