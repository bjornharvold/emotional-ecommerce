/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */
package com.lela.domain.dto.quiz;

import com.lela.domain.enums.ProfileStatus;

/**
 * User: Chris Tallent
 * Date: 8/30/12
 * Time: 9:41 AM
 */
public class ProfileStatusResponse {
    private ProfileStatus profileStatus;

    public ProfileStatus getProfileStatus() {
        return profileStatus;
    }

    public void setProfileStatus(ProfileStatus profileStatus) {
        this.profileStatus = profileStatus;
    }
}
