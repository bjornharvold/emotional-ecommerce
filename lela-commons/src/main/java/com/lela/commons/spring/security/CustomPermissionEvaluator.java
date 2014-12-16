/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.commons.spring.security;

import com.lela.domain.document.User;
import com.lela.domain.dto.Principal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;

/**
 * Created by Bjorn Harvold
 * Date: 3/17/11
 * Time: 3:42 PM
 * Responsibility:
 */
public class CustomPermissionEvaluator implements PermissionEvaluator {
    private final static Logger log = LoggerFactory.getLogger(CustomPermissionEvaluator.class);

    private static enum permissionType {delete, list, merge, insert}

    @Override
    public boolean hasPermission(Authentication auth, Object target, Object perm) {

        if (target instanceof User) {
            User user = (User) target;
            return processUserPermission(auth, user, perm);
        }
        
        throw new UnsupportedOperationException("hasPermission not supported for object <" + target + "> and permission <" + perm + ">");
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        throw new UnsupportedOperationException("Not supported");
    }

    private boolean processUserPermission(Authentication auth, User user, Object perm) {
        boolean result = false;

        permissionType t = permissionType.valueOf((String) perm);

        Principal principal = (Principal) auth.getPrincipal();

        switch (t) {

            case merge:
                result = user.getId().equals(principal.getUser().getId()) &&
                        hasRole(auth, "RIGHT_UPDATE_USER");
                break;

            case insert:
                result = user.getId().equals(principal.getUser().getId()) &&
                        hasRole(auth, "RIGHT_INSERT_USER");
                break;
        }

        return result;
    }

    private boolean hasRole(Authentication auth, String role) {
        for (GrantedAuthority right : auth.getAuthorities()) {
            if (right.getAuthority().equals(role)) {
                return true;
            }
        }

        return false;
    }
}
