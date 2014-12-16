/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */
package com.lela.commons.test.service;

import com.lela.commons.event.*;
import com.lela.commons.repository.UserRepository;
import com.lela.commons.repository.VerificationRepository;
import com.lela.commons.service.CacheService;
import com.lela.commons.service.EventService;
import com.lela.commons.service.ImageUtilityService;
import com.lela.commons.service.MailService;
import com.lela.commons.service.ProfileService;
import com.lela.commons.service.RoleService;
import com.lela.commons.service.UserSupplementService;
import com.lela.commons.service.impl.UserServiceImpl;
import com.lela.domain.document.AffiliateAccount;
import com.lela.domain.document.Motivator;
import com.lela.domain.document.QUser;
import com.lela.domain.document.User;
import com.lela.domain.document.UserSupplement;
import com.lela.domain.dto.UnsubscribeFromEmailList;
import com.lela.domain.dto.UserDto;
import com.lela.domain.dto.user.Address;
import com.lela.domain.dto.user.RegisterUserRequest;
import com.lela.domain.enums.Gender;
import com.lela.domain.enums.MailParameter;
import com.mysema.query.types.Predicate;
import mailjimp.dom.enums.EmailType;
import mailjimp.service.IMailJimpService;
import org.bson.types.ObjectId;
import org.jasypt.util.password.PasswordEncryptor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.social.connect.UserProfile;
import org.springframework.social.connect.UserProfileBuilder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Locale;
import java.util.Map;

import static junit.framework.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

/**
 * User: Chris Tallent
 * Date: 11/1/12
 * Time: 5:22 PM
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(EventHelper.class)
public class UserServiceUnitTest {
    private static Logger log = LoggerFactory.getLogger(ProfileServiceUnitTest.class);

    private static final String CONTEXT = "CONTEXT";
    private static final Integer PORT = 8080;
    private static final String PROTOCOL = "http";
    private static final String SERVER = "server.com";
    private static final String ACCESS_KEY = "SOME_ACCESS_KEY";
    private static final String SECRET_KEY = "SOME_SECRET_KEY";
    private static final String BUCKET_NAME = "SOME_BUCKET_NAME";
    private static final String MAILCHIMP_ID = "MAILCHIMP_ID";
    private static final String EMAIL = "somestupidemail@home.com";
    private static final String USER_CODE = "USER_CODE";

    @Mock
    private MailService mailService;

    @Mock
    private PasswordEncryptor passwordEncryptor;

    @Mock
    private UserRepository userRepository;

    @Mock
    private VerificationRepository verificationRepository;

    @Mock
    private IMailJimpService mailChimpService;

    @Mock
    private SessionRegistry sessionRegistry;

    @Mock
    private RoleService roleService;

    @Mock
    private UserSupplementService userSupplementService;

    @Mock
    private EventService eventService;

    @Mock
    private ImageUtilityService utilityService;

    @Mock
    private CacheService cacheService;

    @Mock
    private ProfileService profileService;

    @InjectMocks
    UserServiceImpl userService;

    private ObjectId userId;
    private User user;

    @Before
    public void setUp() {
        ReflectionTestUtils.setField(userService, "context", CONTEXT);
        ReflectionTestUtils.setField(userService, "port", PORT);
        ReflectionTestUtils.setField(userService, "protocol", PROTOCOL);
        ReflectionTestUtils.setField(userService, "server", SERVER);
        ReflectionTestUtils.setField(userService, "mailchimpListId", MAILCHIMP_ID);
        ReflectionTestUtils.setField(userService, "accessKey", ACCESS_KEY);
        ReflectionTestUtils.setField(userService, "secretKey", SECRET_KEY);
        ReflectionTestUtils.setField(userService, "profileImagesBucketName", BUCKET_NAME);

        userId = new ObjectId();
        user = new User();
        user.setId(userId);
        user.setMl(EMAIL);
        user.setCd(USER_CODE);
    }

    @Test
    public void testUnsubscribeFromEmailList() throws Exception
    {
        UnsubscribeFromEmailList unsubscribe = new UnsubscribeFromEmailList();
        unsubscribe.setEmail(EMAIL);
        unsubscribe.setListId(MAILCHIMP_ID);

        when(userRepository.findOne(QUser.user.ml.equalsIgnoreCase(EMAIL.trim()))).thenReturn(user);

        ArgumentCaptor<Object> trackEvent = (ArgumentCaptor)ArgumentCaptor.forClass(Object.class);
        mockStatic(EventHelper.class);

        // execute the service method
        userService.unsubscribeFromEmailList(unsubscribe);

        verifyStatic(times(1));
        EventHelper.post(trackEvent.capture());

        assertTrue("UnsubscribeEvet not posted", trackEvent.getAllValues().get(0) instanceof UnsubscribeEvent);

        verify(mailChimpService).listUnsubscribe(unsubscribe.getListId(), unsubscribe.getEmail(), true, false, false);
    }

    @Test
    public void testSaveMotivator()
    {
        Motivator motivator = new Motivator();

        ArgumentCaptor<Object> trackEvent = (ArgumentCaptor)ArgumentCaptor.forClass(Object.class);
        mockStatic(EventHelper.class);

        // execute the service method
        userService.saveMotivator(this.user.getCd(), motivator);

        verifyStatic(times(1));
        EventHelper.post(trackEvent.capture());

        assertTrue("MotivatorEvent not posted", trackEvent.getAllValues().get(0) instanceof MotivatorEvent);

        verify(userSupplementService).saveMotivator(user.getCd(), motivator);
    }

    @Test
    public void testSaveGender()
    {
        ArgumentCaptor<Object> trackEvent = (ArgumentCaptor)ArgumentCaptor.forClass(Object.class);
        mockStatic(EventHelper.class);

        // execute the service method
        userService.saveGender(this.user.getCd(), Gender.FEMALE);

        verifyStatic(times(1));
        EventHelper.post(trackEvent.capture());

        assertTrue("SetGenderEvent not posted", trackEvent.getAllValues().get(0) instanceof SetGenderEvent);

        verify(userSupplementService).saveGender(user.getCd(), Gender.FEMALE);
    }

    @Test
    public void testSaveAge()
    {
        ArgumentCaptor<Object> trackEvent = (ArgumentCaptor)ArgumentCaptor.forClass(Object.class);
        mockStatic(EventHelper.class);

        // execute the service method
        userService.saveAge(this.user.getCd(), 37);

        verifyStatic(times(1));
        EventHelper.post(trackEvent.capture());

        assertTrue("SetAgeEvent not posted", trackEvent.getAllValues().get(0) instanceof SetAgeEvent);

        verify(userSupplementService).saveAge(user.getCd(), 37);
    }

    @Test
    public void testAddOrUpdateAddress()
    {
        Address address = new Address();
        ArgumentCaptor<Object> trackEvent = (ArgumentCaptor)ArgumentCaptor.forClass(Object.class);
        mockStatic(EventHelper.class);

        // execute the service method
        userService.addOrUpdateAddress(this.user.getCd(), address);

        verifyStatic(times(1));
        EventHelper.post(trackEvent.capture());

        assertTrue("SetAddressEvent not posted", trackEvent.getAllValues().get(0) instanceof SetAddressEvent);

        verify(userSupplementService).addOrUpdateAddress(user.getCd(), address);
    }

    @Test
    public void testRemoveUser()
    {
        ArgumentCaptor<Object> trackEvent = (ArgumentCaptor)ArgumentCaptor.forClass(Object.class);
        mockStatic(EventHelper.class);

        // execute the service method
        userService.removeUser(this.user);

        verifyStatic(times(1));
        EventHelper.post(trackEvent.capture());

        assertTrue("DeleteUserEvent not posted", trackEvent.getAllValues().get(0) instanceof DeleteUserEvent);

        verify(userRepository).delete(user);
    }

    @Test
    public void testRemoveUserByUserCode()
    {
        when(userRepository.findByCode(user.getCd())).thenReturn(user);

        ArgumentCaptor<Object> trackEvent = (ArgumentCaptor)ArgumentCaptor.forClass(Object.class);
        mockStatic(EventHelper.class);

        // execute the service method
        userService.removeUser(this.user.getCd());

        verifyStatic(times(1));
        EventHelper.post(trackEvent.capture());

        assertTrue("DeleteUserEvent not posted", trackEvent.getAllValues().get(0) instanceof DeleteUserEvent);

        verify(userRepository).delete(user);
    }

}

