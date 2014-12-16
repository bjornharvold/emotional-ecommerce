/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.commons.spring.security;

import org.jasypt.util.password.StrongPasswordEncryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

/**
 * User: Bjorn Harvold
 * Date: Sep 27, 2009
 * Time: 7:58:31 PM
 * Responsibility:
 */
@Component("myPasswordEncoder")
public class PasswordEncoder implements org.springframework.security.authentication.encoding.PasswordEncoder {
    private static final Logger log = LoggerFactory.getLogger(PasswordEncoder.class);
    private final StrongPasswordEncryptor passwordEncryptor;

    @Autowired
    public PasswordEncoder(StrongPasswordEncryptor passwordEncryptor) {
        this.passwordEncryptor = passwordEncryptor;
    }

    @Override
    public String encodePassword(String rawPassword, Object salt) throws DataAccessException {
        return passwordEncryptor.encryptPassword(rawPassword);
    }

    @Override
    public boolean isPasswordValid(String encodedPassword, String rawPassword, Object salt) throws DataAccessException {
        Boolean isValid = passwordEncryptor.checkPassword(rawPassword, encodedPassword);

        if (log.isTraceEnabled()) {
            log.trace("Password valid: " + isValid);
        }

        return isValid;
    }

}
