/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.web.web.preparer;

import com.lela.commons.service.UserService;
import com.lela.commons.spring.security.SpringSecurityHelper;
import com.lela.commons.web.utils.WebConstants;
import com.lela.domain.document.User;
import com.lela.domain.document.UserSupplement;
import com.lela.domain.dto.Principal;
import org.apache.tiles.AttributeContext;
import org.apache.tiles.context.TilesRequestContext;
import org.apache.tiles.preparer.ViewPreparer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Bjorn Harvold
 * Date: 8/18/11
 * Time: 4:04 PM
 * Responsibility: This will load up the user supplement object for the user
 */
@Component("userSupplementPreparer")
public class UserSupplementPreparer implements ViewPreparer {

    private final UserService userService;

    @Autowired
    public UserSupplementPreparer(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void execute(TilesRequestContext tilesRequestContext, AttributeContext attributeContext) {

        if (!tilesRequestContext.getRequestScope().containsKey(WebConstants.USER_SUPPLEMENT)) {
            // we also want to load up the UserSupplement object if it is available
            Principal principal = SpringSecurityHelper.getSecurityContextPrincipal();
            User transientUser = (User) tilesRequestContext.getSessionScope().get(WebConstants.USER);
            User user = null;
            UserSupplement us = null;

            if (principal != null) {
                user = principal.getUser();
            } else if (transientUser != null) {
                user = transientUser;
            }

            if (user != null) {
                us = userService.findUserSupplement(user.getCd());
            } else {
                // just so we don't have to put in a bunch of null checks in the JSPs
                us = new UserSupplement();
            }

            tilesRequestContext.getRequestScope().put(WebConstants.USER_SUPPLEMENT, us);
        }
    }
}
