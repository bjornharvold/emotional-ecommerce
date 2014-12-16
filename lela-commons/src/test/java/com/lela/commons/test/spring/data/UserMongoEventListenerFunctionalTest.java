/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.commons.test.spring.data;

import com.lela.commons.spring.data.UserMongoEventListener;
import com.lela.domain.document.Social;
import com.lela.domain.document.UserSupplement;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

/**
 * Created by Bjorn Harvold
 * Date: 3/23/12
 * Time: 2:48 AM
 * Responsibility:
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/META-INF/spring/encryption.xml"})
public class UserMongoEventListenerFunctionalTest extends AbstractJUnit4SpringContextTests {
    private static final Logger log = LoggerFactory.getLogger(UserMongoEventListenerFunctionalTest.class);
    private static final String ACCESS_TOKEN = "accesstoken";
    private static final String SECRET = "secret";
    private static final String DISPLAY_NAME = "displayname";
    private static final String IMAGE_URL = "imageurl";
    private static final String PROFILE_URL = "profileurl";

    @Autowired
    private StandardPBEStringEncryptor stringEncryptor;
    
    @Test
    public void testUserMongoEventListener() {
        log.info("Testing UserMongoEventListener...");

        UserMongoEventListener listener = new UserMongoEventListener(stringEncryptor);
        
        UserSupplement user = new UserSupplement(this.getClass().getSimpleName());
        List<Social> list = new ArrayList<Social>(1);
        Social social = new Social();
        social.setAccessToken(ACCESS_TOKEN);
        social.setSecret(SECRET);
        social.setDisplayName(DISPLAY_NAME);
        social.setImageUrl(IMAGE_URL);
        social.setProfileUrl(PROFILE_URL);
        list.add(social);
        user.setScls(list);
        
        listener.onBeforeConvert(user);
        log.info("At this point, the strings have been encrypted and are no longer the same");
        assertThat(ACCESS_TOKEN, not(equalTo(social.getAccessToken())));
        assertThat(SECRET, not(equalTo(social.getSecret())));
        assertThat(DISPLAY_NAME, not(equalTo(social.getDisplayName())));
        assertThat(IMAGE_URL, not(equalTo(social.getImageUrl())));
        assertThat(PROFILE_URL, not(equalTo(social.getProfileUrl())));
        log.info("Strings have been encrypted and are no longer the same");
        
        log.info("Now we decrypt the strings again");
        listener.onAfterConvert(null, user);
        assertThat(ACCESS_TOKEN, is(equalTo(social.getAccessToken())));
        assertThat(SECRET, is(equalTo(social.getSecret())));
        assertThat(DISPLAY_NAME, is(equalTo(social.getDisplayName())));
        assertThat(IMAGE_URL, is(equalTo(social.getImageUrl())));
        assertThat(PROFILE_URL, is(equalTo(social.getProfileUrl())));
        log.info("Strings have been decrypted and are the same again");

        log.info("Now we are going to encrypt twice but only decrypt once, and we want the strings to match after");
        listener.onBeforeConvert(user);
        listener.onBeforeConvert(user);
        listener.onAfterConvert(null, user);
        assertThat(ACCESS_TOKEN, is(equalTo(social.getAccessToken())));
        assertThat(SECRET, is(equalTo(social.getSecret())));
        assertThat(DISPLAY_NAME, is(equalTo(social.getDisplayName())));
        assertThat(IMAGE_URL, is(equalTo(social.getImageUrl())));
        assertThat(PROFILE_URL, is(equalTo(social.getProfileUrl())));
        log.info("Strings have been decrypted and are the same again");
        
        log.info("Testing UserMongoEventListener completed");
    }
}
