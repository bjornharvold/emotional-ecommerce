/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.commons.service;

import com.lela.domain.document.Redirect;

/**
 * Created by IntelliJ IDEA.
 * User: Martin Gamboa
 * Date: 10/4/11
 * Time: 9:12 AM
 * To change this template use File | Settings | File Templates.
 */
public interface RedirectService {
    public Boolean isValidRedirect(String url);
}
