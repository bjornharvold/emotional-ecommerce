/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.commons.spring.data;

import com.lela.domain.document.Social;
import com.lela.domain.document.UserSupplement;
import com.mongodb.DBObject;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;

/**
 * Created by Bjorn Harvold
 * Date: 10/28/11
 * Time: 12:38 PM
 * Responsibility: We need to encrypt certain values on the user before saving
 */
public class UserMongoEventListener extends AbstractMongoEventListener<UserSupplement> {
    private final static Logger log = LoggerFactory.getLogger(UserMongoEventListener.class);

    private final StandardPBEStringEncryptor stringEncryptor;

    public UserMongoEventListener(StandardPBEStringEncryptor stringEncryptor) {
        this.stringEncryptor = stringEncryptor;
    }

    @Override
    public void onBeforeConvert(UserSupplement us) {
        if (us != null && us.getScls() != null && !us.getScls().isEmpty()) {
            for (Social social : us.getScls()) {
                try {
                    while (true) {
                        // check if this string can be decrypted. if it can, it's already been encrypted
                        // we're checking here because some values in the current db might already have been
                        // double encrypted
                        social.setAccessToken(stringEncryptor.decrypt(social.getAccessToken()));
                        social.setSecret(stringEncryptor.decrypt(social.getSecret()));
                        social.setImageUrl(stringEncryptor.decrypt(social.getImageUrl()));
                        social.setProfileUrl(stringEncryptor.decrypt(social.getProfileUrl()));
                        social.setDisplayName(stringEncryptor.decrypt(social.getDisplayName()));
                    }
                } catch (Exception ex) {
                    // this will happend when the string can no longer be decrypted
                    if (log.isDebugEnabled()) {
                        log.debug("This is expected: " + ex.getMessage(), ex);
                    }
                }

                // this provider is in clear text and needs to be encrypted before being persisted
                social.setAccessToken(stringEncryptor.encrypt(social.getAccessToken()));
                social.setSecret(stringEncryptor.encrypt(social.getSecret()));
                social.setImageUrl(stringEncryptor.encrypt(social.getImageUrl()));
                social.setProfileUrl(stringEncryptor.encrypt(social.getProfileUrl()));
                social.setDisplayName(stringEncryptor.encrypt(social.getDisplayName()));
            }
        }
    }

    @Override
    public void onAfterConvert(DBObject dbObject, UserSupplement us) {
        if (us != null && us.getScls() != null && !us.getScls().isEmpty()) {
            for (Social social : us.getScls()) {
                try {
                    while (true) {
                        // this provider is in clear text and needs to be encrypted before being persisted
                        social.setAccessToken(stringEncryptor.decrypt(social.getAccessToken()));
                        social.setSecret(stringEncryptor.decrypt(social.getSecret()));
                        social.setImageUrl(stringEncryptor.decrypt(social.getImageUrl()));
                        social.setProfileUrl(stringEncryptor.decrypt(social.getProfileUrl()));
                        social.setDisplayName(stringEncryptor.decrypt(social.getDisplayName()));
                    }
                } catch (Exception ex) {
                    // this will most likely happen if the strings are not encrypted but in clear text
                    if (log.isDebugEnabled()) {
                        log.debug("This is expected: " + ex.getMessage(), ex);
                    }
                }
            }
        }
    }
}
