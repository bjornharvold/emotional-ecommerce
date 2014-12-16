/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.domain.dto;

/**
 * Created by Bjorn Harvold
 * Date: 1/12/12
 * Time: 1:16 AM
 * Responsibility:
 */
public class Login {
    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
