/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */


package com.lela.commons.spring.security;

//~--- non-JDK imports --------------------------------------------------------

import com.lela.domain.dto.Principal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

//~--- JDK imports ------------------------------------------------------------

//~--- classes ----------------------------------------------------------------

/**
 * Created by Bjorn Harvold
 * Date: 12/20/10
 * Time: 1:26 AM
 * Responsibility:
 */
public class SpringSecurityHelper {
    private static final Logger log = LoggerFactory.getLogger(SpringSecurityHelper.class);
    private static String[] rights = {
            "RIGHT_UPDATE_USER_AS_ADMIN",
            "RIGHT_INSERT_USER_AS_ADMIN",
            "RIGHT_READ_USER_AS_ADMIN",
            "RIGHT_DELETE_USER_AS_ADMIN",

            "RIGHT_UPDATE_OWNER_AS_ADMIN",
            "RIGHT_INSERT_OWNER_AS_ADMIN",
            "RIGHT_READ_OWNER_AS_ADMIN",
            "RIGHT_DELETE_OWNER_AS_ADMIN",

            "RIGHT_READ_ROLE_AS_ADMIN",
            "RIGHT_INSERT_ROLE_AS_ADMIN",
            "RIGHT_DELETE_ROLE_AS_ADMIN",

            "RIGHT_READ_QUESTION_AS_ADMIN",
            "RIGHT_INSERT_QUESTION_AS_ADMIN",
            "RIGHT_DELETE_QUESTION_AS_ADMIN",

            "RIGHT_READ_ITEM_AS_ADMIN",
            "RIGHT_INSERT_ITEM_AS_ADMIN",
            "RIGHT_DELETE_ITEM_AS_ADMIN",

            "RIGHT_READ_CATEGORY_AS_ADMIN",
            "RIGHT_INSERT_CATEGORY_AS_ADMIN",
            "RIGHT_DELETE_CATEGORY_AS_ADMIN",

            "RIGHT_CONTENT_INGEST",
            "RIGHT_UPDATE_AVAILABLE_STORES_ON_ITEM",
            "RIGHT_DELETE_METRIC_AS_ADMIN",

            "RIGHT_READ_BLOG_AS_ADMIN",
            "RIGHT_INSERT_BLOG",
            "RIGHT_DELETE_BLOG",

            "RIGHT_READ_PRESS",
            "RIGHT_INSERT_PRESS",
            "RIGHT_DELETE_PRESS",

            "RIGHT_READ_PRODUCT_GRID",
            "RIGHT_INSERT_PRODUCT_GRID",
            "RIGHT_DELETE_PRODUCT_GRID",

            "RIGHT_READ_SEO_URL_NAME",
            "RIGHT_INSERT_SEO_URL_NAME",
            "RIGHT_DELETE_SEO_URL_NAME",

            "RIGHT_READ_QUIZ",
            "RIGHT_INSERT_QUIZ",
            "RIGHT_DELETE_QUIZ",

            "RIGHT_READ_APPLICATION",
            "RIGHT_INSERT_APPLICATION",
            "RIGHT_DELETE_APPLICATION",

            "RIGHT_READ_NAVIGATIONBAR",
            "RIGHT_INSERT_NAVIGATIONBAR",
            "RIGHT_DELETE_NAVIGATIONBAR",

            "RIGHT_READ_CSS_STYLE",
            "RIGHT_INSERT_CSS_STYLE",
            "RIGHT_DELETE_CSS_STYLE",

            "RIGHT_READ_AFFILIATE",
            "RIGHT_INSERT_AFFILIATE",
            "RIGHT_DELETE_AFFILIATE",
            "RIGHT_READ_CAMPAIGN",
            
            "RIGHT_REPORT_GENERATION",

            "RIGHT_LOGIN_ONBOARD"
    };

    public static void secureChannel() {
        log.info("Securing channel...");
        List<GrantedAuthority> auths = new ArrayList<GrantedAuthority>();

        for (String right : rights) {
            auths.add(new SimpleGrantedAuthority(right));
        }

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken("BOOTSTRAPPER", "BOOTSTRAPPER", auths);
        SecurityContextHolder.getContext().setAuthentication(token);

        log.info("Channel administration");
    }

    public static void secureChannel(String username, String password, Collection<? extends GrantedAuthority> auths) {
        log.info("Securing channel...");

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password, auths);
        SecurityContextHolder.getContext().setAuthentication(token);

        log.info("Channel administration");
    }

    public static void secureChannel(Principal principal) {
        log.info("Securing channel...");

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(principal, principal.getPassword(), principal.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(token);

        log.info("Channel administration");
    }

    public static void unsecureChannel() {
        log.info("Un-securing channel...");
        SecurityContextHolder.getContext().setAuthentication(null);
        log.info("Channel insecure");
    }

    /**
     * Retrieves the Principal from the spring security context. Null if Principal is not logged in.
     *
     * @return Return value
     */
    public static Principal getSecurityContextPrincipal() {
        Principal result = null;
        SecurityContext sc     = SecurityContextHolder.getContext();

        if (sc != null) {
            Authentication authentication = sc.getAuthentication();

            if (authentication != null) {
                Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

                if ((principal != null) && (principal instanceof Principal)) {
                    result = (Principal) principal;
                }
            }
        }

        return result;
    }

    public static Authentication getSecurityContextAuthentication() {
        Authentication result = null;
        SecurityContext sc     = SecurityContextHolder.getContext();

        if (sc != null) {
            result = sc.getAuthentication();
        }

        return result;
    }

    public static SecurityContext getSecurityContext() {
        return SecurityContextHolder.getContext();
    }
}
