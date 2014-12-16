/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */
package com.lela.commons.service;

import com.lela.domain.document.AffiliateAccount;
import com.lela.domain.document.User;
import com.lela.domain.dto.UserDto;
import com.lela.domain.dto.user.DisableUser;
import com.lela.domain.dto.user.RegisterUserRequest;
import org.bson.types.ObjectId;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.social.connect.UserProfile;

import java.util.List;

/**
 * User: Chris Tallent
 * Date: 9/6/12
 * Time: 6:05 PM
 */
public interface ProfileService {
    List<User> registerUsers(List<User> list, boolean sendEmail);
    User registerWebUser(UserDto userDto, User transientUser, AffiliateAccount attribute);
    User registerFacebookUser(UserProfile userProfile, String sessionUserCode, AffiliateAccount domainAffiliate);
    User registerTestUser(User user);
    User registerContentIngestionUser(User user);
    User registerApiUser(RegisterUserRequest dto, String sessionUserCode, AffiliateAccount domainAffiliate);

    void removeUserProfile(DisableUser du, ObjectId userId);
    void removeLoggedInUserProfile(DisableUser du);

    void enhanceProfile(User user, String remoteIPAddress);
}
