/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.web.web.preparer;

import com.lela.commons.service.*;
import com.lela.commons.spring.security.SpringSecurityHelper;
import com.lela.commons.web.utils.WebConstants;
import com.lela.domain.document.AffiliateAccount;
import com.lela.domain.document.Category;
import com.lela.domain.document.CssStyle;
import com.lela.domain.document.NavigationBar;
import com.lela.domain.document.User;
import com.lela.domain.document.UserSupplement;
import com.lela.domain.dto.Principal;
import org.apache.tiles.AttributeContext;
import org.apache.tiles.context.TilesRequestContext;
import org.apache.tiles.preparer.ViewPreparer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Bjorn Harvold
 * Date: 8/18/11
 * Time: 4:04 PM
 * Responsibility: This will load up categories for every tile
 */
@Component("masterTemplatePreparer")
public class MasterTemplatePreparer implements ViewPreparer {

    private final NavigationBarService navigationBarService;
    private final CategoryService categoryService;
    private final UserService userService;
    private final TaskService taskService;
    private final CssStyleService cssStyleService;

    @Value("${facebook.id}")
    private String facebookApplicationId;

    @Autowired
    public MasterTemplatePreparer(NavigationBarService navigationBarService,
                                  CategoryService categoryService,
                                  UserService userService,
                                  TaskService taskService,
                                  CssStyleService cssStyleService) {
        this.navigationBarService = navigationBarService;
        this.categoryService = categoryService;
        this.userService = userService;
        this.taskService = taskService;
        this.cssStyleService = cssStyleService;
    }

    @Override
    public void execute(TilesRequestContext tilesRequestContext, AttributeContext attributeContext) {
        // TODO very soon we will be deriving the locale based on the user's locale
        // LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
        AffiliateAccount domainAffiliate = (AffiliateAccount)tilesRequestContext.getRequestScope().get(WebConstants.DOMAIN_AFFILIATE);
        if (!tilesRequestContext.getRequestScope().containsKey(WebConstants.NAVIGATION_BAR)) {
            NavigationBar navbar = null;

            // If there is an affiliate domain, determine if it has a navbar override
            if (domainAffiliate != null && domainAffiliate.getDmnnvbr() != null) {
                navbar = navigationBarService.findNavigationBarByUrlName(domainAffiliate.getDmnnvbr());
            }
            if (navbar == null) {
                navbar = navigationBarService.findNavigationBarByUrlName(WebConstants.CONSUMER_NAVIGATION_BAR_URL_NAME);
            }

            tilesRequestContext.getRequestScope().put(WebConstants.NAVIGATION_BAR, navbar);
        }

        if (!tilesRequestContext.getRequestScope().containsKey(WebConstants.CATEGORIES)) {
            tilesRequestContext.getRequestScope().put(WebConstants.CATEGORIES, categoryService.findCategories());
        }

        // Set the department
        tilesRequestContext.getSessionScope().put("categoryGroupUrlName", "none");
        if (!tilesRequestContext.getRequestScope().containsKey(WebConstants.CURRENT_DEPARTMENT) && tilesRequestContext.getRequestScope().containsKey(WebConstants.CATEGORY)) {
            Object categoryValue = tilesRequestContext.getRequestScope().get(WebConstants.CATEGORY);
            if (categoryValue instanceof Category) {
                tilesRequestContext.getSessionScope().put(WebConstants.CURRENT_DEPARTMENT, categoryService.determineDefaultDepartmentUrl((Category)categoryValue));
            }
        }

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

        // Check for async tasks
        if (user != null && !tilesRequestContext.getRequestScope().containsKey(WebConstants.TASKS)) {
            tilesRequestContext.getRequestScope().put(WebConstants.TASKS, taskService.findTasksByRecipient(user.getCd()));
        }

        // expose facebook app id
        tilesRequestContext.getRequestScope().put(WebConstants.FACEBOOK_APPLICATION_ID, facebookApplicationId);

        // Add Affiliate Domain styles if defined
        if (domainAffiliate != null && domainAffiliate.getStls() != null) {
            Map<String, CssStyle> styleMap = new HashMap<String, CssStyle>();
            for (CssStyle style : cssStyleService.findCssStyles()) {
                styleMap.put(style.getRlnm(), style);
            }
            tilesRequestContext.getRequestScope().put(WebConstants.STYLES, styleMap);
        }
    }
}
