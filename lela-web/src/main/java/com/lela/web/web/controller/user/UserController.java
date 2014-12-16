/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.web.web.controller.user;

import com.lela.domain.ApplicationConstants;
import com.lela.commons.service.OfferService;
import com.lela.commons.service.UserService;
import com.lela.commons.web.utils.WebConstants;
import com.lela.domain.document.Coupon;
import com.lela.domain.document.Offer;
import com.lela.domain.document.Social;
import com.lela.domain.document.User;
import com.lela.domain.document.UserSupplement;
import com.lela.domain.dto.UserAccountDto;
import com.lela.web.web.controller.AbstractController;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

@Controller("userController")
public class UserController extends AbstractController {

    private final UserService userService;
    private final ConnectionFactoryLocator connectionFactoryLocator;
    private final OfferService offerService;

    @Autowired
    public UserController(UserService userService,
                          ConnectionFactoryLocator connectionFactoryLocator,
                          OfferService offerService) {
        this.userService = userService;
        this.connectionFactoryLocator = connectionFactoryLocator;
        this.offerService = offerService;
    }

    /**
     * Get user data
     *
     * @param model model
     * @return Return value
     * @throws Exception Exception
     */
    @RequestMapping(value = "/user/dashboard/overview", method = RequestMethod.GET)
    public String showUserAccount(HttpSession session, Model model) throws Exception {
        User user = retrieveUserFromPrincipalOrSession(session);
        UserSupplement us = userService.findUserSupplement(user.getCd());

        List<Social> socialList = us.getScls();
        model.addAttribute(WebConstants.USER_SOCIAL_NETWORKS, socialList);
        model.addAttribute(WebConstants.USER_ACCOUNT_DTO, new UserAccountDto(us));

        // create a list of configured social networks the user can connect to
        model.addAttribute(WebConstants.SOCIAL_NETWORKS, connectionFactoryLocator.registeredProviderIds());

        boolean socialNetworksConnected = false;
        boolean facebookConnected = false;
        if ((socialList != null) && (socialList.size() > 0)) {
            socialNetworksConnected = true;

            // we do this extra work because we don't want to let the user change his email
            // if the user connected via facebook
            for (Social social : socialList) {
                if (social.getProviderId().equals(ApplicationConstants.FACEBOOK)) {
                    facebookConnected = true;
                    break;
                }
            }
        }
        model.addAttribute(WebConstants.SOCIAL_NETWORKS_CONNECTED, socialNetworksConnected);
        model.addAttribute(WebConstants.FACEBOOK_CONNECTED, facebookConnected);

        model.addAttribute(WebConstants.ACCOUNT, "account-overview");

        return "user.dashboard.overview";
    }

    /**
     * Update user data
     *
     * @param userAccountDto userAccountDto
     * @return Return value
     * @throws Exception Exception
     */
    @RequestMapping(value = "/user/dashboard/overview/update", method = RequestMethod.PUT)
    public String updateUserAccount(UserAccountDto userAccountDto, HttpSession session) throws Exception {
        User user = retrieveUserFromPrincipalOrSession(session);

        userService.updateUserAccount(user, userAccountDto);

        return "redirect:/user/dashboard/overview";
    }

    
    @RequestMapping(value = "/user/dashboard/coupons", method = RequestMethod.GET)
    public String showUserCoupons(HttpSession session, Model model) throws Exception {
        User user = retrieveUserFromPrincipalOrSession(session);
        UserSupplement us = userService.findUserSupplement(user.getCd());

        //Iterate through user coupons and remove those that have expired.
        if ((us.getCpns() != null) && (us.getCpns().size() > 0)) {
            Coupon coupon = null;
            List<Coupon> coupons = us.getCpns();
            Offer offer = null;
            Date now = new Date();
            for (int i = 0; i < coupons.size(); i++) {
                coupon = coupons.get(i);
                offer = offerService.findOfferByUrlName(coupon.getFfrrlnm());
                // Check if offer no longer exists in system or if offer is expired and if so remove coupon
                if ((offer == null) || (offer.getXprtndt().before(now))) {
                    coupons.remove(i);
                }
            }
        }

        model.addAttribute(WebConstants.ACCOUNT, "account-coupons");
        model.addAttribute(WebConstants.USER, user);
        
        return "user.dashboard.coupons";
    }
}
