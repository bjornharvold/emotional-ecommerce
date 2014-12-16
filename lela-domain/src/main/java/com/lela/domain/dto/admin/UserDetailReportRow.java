/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */
package com.lela.domain.dto.admin;

import com.lela.domain.document.User;
import com.lela.domain.document.UserSupplement;
import com.lela.domain.enums.DeviceType;

/**
 * User: Chris Tallent
 * Date: 6/10/12
 * Time: 10:46 PM
 */
public class UserDetailReportRow {
    private User user;
    private UserSupplement us;
    private DeviceType registrationDeviceType;
    private boolean repeatUser;
    private boolean quizComplete;

    public UserDetailReportRow(User user, UserSupplement us) {
        this.user = user;
        this.us = us;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public DeviceType getRegistrationDeviceType() {
        return registrationDeviceType;
    }

    public void setRegistrationDeviceType(DeviceType registrationDeviceType) {
        this.registrationDeviceType = registrationDeviceType;
    }

    public boolean isRepeatUser() {
        return repeatUser;
    }

    public void setRepeatUser(boolean repeatUser) {
        this.repeatUser = repeatUser;
    }

    public UserSupplement getUs() {
        return us;
    }

    public void setUs(UserSupplement us) {
        this.us = us;
    }

    public boolean isQuizComplete() {
        return quizComplete;
    }

    public void setQuizComplete(boolean quizComplete) {
        this.quizComplete = quizComplete;
    }
}
