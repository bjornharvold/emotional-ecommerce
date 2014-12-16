/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.domain.dto;

/**
 * Created by Bjorn Harvold
 * Date: 1/12/12
 * Time: 12:57 AM
 * Responsibility:
 */
public class LoginStatus {

    private final boolean loggedIn;
    private final String username;

    public LoginStatus(boolean loggedIn, String username) {
        this.loggedIn = loggedIn;
        this.username = username;
    }

    public boolean getLoggedIn() {
        return loggedIn;
    }

    public String getUsername() {
        return username;
    }

}
