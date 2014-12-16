/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.commons.service.impl;

import com.lela.commons.service.RedirectService;
import com.lela.commons.service.UserTrackerService;
import com.lela.domain.document.Redirect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Martin Gamboa
 * Date: 10/4/11
 * Time: 9:13 AM
 * To change this template use File | Settings | File Templates.
 */

@Service("redirectService")
public class RedirectServiceImpl implements RedirectService {

    /**
     * Field description
     */
    private final List<String> redirectUrlList;

    private final UserTrackerService userTrackerService;

    /**
     * Constructs ...
     *
     * @param userTrackerService
     */
    @Autowired
    public RedirectServiceImpl(List<String> redirectUrlList, UserTrackerService userTrackerService) {
        this.redirectUrlList = redirectUrlList;
        this.userTrackerService = userTrackerService;
    }

    @Override
    public Boolean isValidRedirect(String url) {
        boolean valid = false;

        if (redirectUrlList != null && !redirectUrlList.isEmpty()) {
            for (String validRedirectUrl : redirectUrlList) {
                if (url.matches("^" + validRedirectUrl + ".*")) {
                    valid = true;
                    break;
                }
            }
        }

        return valid;
    }
}
