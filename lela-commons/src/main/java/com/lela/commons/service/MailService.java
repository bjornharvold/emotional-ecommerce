/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.commons.service;

import com.lela.commons.mail.MailServiceException;
import com.lela.domain.enums.MailParameter;

import java.util.Locale;
import java.util.Map;

/**
 * User: Bjorn Harvold
 * Date: Apr 24, 2007
 * Time: 11:50:44 AM
 */
public interface MailService {
    void sendRegistrationConfirmation(String email, Map<MailParameter, Object> parameters, Locale l) throws MailServiceException;

    void sendPasswordResetEmail(String email, String newPassword, Locale l) throws MailServiceException;

    void sendShareWithFriends(String email, Map<MailParameter, Object> parameters, Locale l) throws MailServiceException;

    void sendShareWithFriendsRegistrationConfirmation(String email, Map<MailParameter, Object> map, Locale locale) throws MailServiceException;

    void sendRelationshipRequest(String email, Map<MailParameter, Object> parameters, Locale l) throws MailServiceException;

    void sendRelationshipConfirmation(String email, Map<MailParameter, Object> parameters, Locale l) throws MailServiceException;

    void sendAllInvitationsUsedConfirmation(String email, Map<MailParameter, Object> parameters, Locale l) throws MailServiceException;

    void sendEventSignUpConfirmation(String email, Map<MailParameter, Object> parameters, Locale l) throws MailServiceException;

    void sendCouponConfirmation(String email, Map<MailParameter, Object> parameters, Locale locale) throws MailServiceException;

    void sendCouponRequest(String email, Map<MailParameter, Object> parameters, Locale locale) throws MailServiceException;

    void sendCouponTransaction(String email, Map<MailParameter, Object> parameters, Locale locale) throws MailServiceException;

    void sendPriceAlert(String email, Map<MailParameter, Object> parameters, Locale l) throws MailServiceException;

    Boolean isAvailable();

    void sendApiRegistrationConfirmation(String email, Map<MailParameter, Object> parameters, Locale locale) throws MailServiceException;
}
