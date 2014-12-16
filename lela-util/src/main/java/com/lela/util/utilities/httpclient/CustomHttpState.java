/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.util.utilities.httpclient;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.auth.AuthScope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Bjorn Harvold
 * Date: 2/29/12
 * Time: 9:14 PM
 * Responsibility:
 */
public class CustomHttpState extends HttpState {
    private static final Logger log = LoggerFactory.getLogger(CustomHttpState.class);
    private Credentials credentials;

    public void setCredentials(final Credentials credentials) {
        this.credentials = credentials;
        super.setCredentials(AuthScope.ANY, credentials);

        /*
        if (credentials instanceof UsernamePasswordCredentials) {
            log.warn("HTTP client credentials: " + ((UsernamePasswordCredentials) credentials).getUserName() + " / " + ((UsernamePasswordCredentials) credentials).getPassword());
        }
        */
    }

    public Credentials getCredentials() {
        return credentials;
    }
}
