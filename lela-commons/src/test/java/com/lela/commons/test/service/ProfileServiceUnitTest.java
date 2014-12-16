/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.commons.test.service;

import com.lela.commons.event.EventHelper;
import com.lela.commons.event.RegistrationEvent;
import com.lela.commons.event.SubscribeEvent;
import com.lela.commons.service.MailService;
import com.lela.commons.service.impl.FacebookUserServiceImpl;
import com.lela.commons.service.impl.ProfileServiceImpl;
import com.lela.commons.service.impl.UserServiceImpl;
import com.lela.domain.document.AffiliateAccount;
import com.lela.domain.document.User;
import com.lela.domain.document.UserSupplement;
import com.lela.domain.dto.SubscribeToEmailList;
import com.lela.domain.dto.UnsubscribeFromEmailList;
import com.lela.domain.dto.UserDto;
import com.lela.domain.dto.user.DisableUser;
import com.lela.domain.dto.user.RegisterUserRequest;
import com.lela.domain.enums.MailParameter;
import com.mysema.query.types.Predicate;
import junit.framework.Assert;
import mailjimp.dom.enums.EmailType;
import org.bson.types.ObjectId;
import org.jasypt.util.password.PasswordEncryptor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.social.connect.UserProfile;
import org.springframework.social.connect.UserProfileBuilder;

import java.util.Locale;
import java.util.Map;

import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

/**
 * Created by Bjorn Harvold
 * Date: 8/18/12
 * Time: 10:02 PM
 * Responsibility:
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(EventHelper.class)
public class ProfileServiceUnitTest {
    private static final Logger log = LoggerFactory.getLogger(ProfileServiceUnitTest.class);
    private static final String MAILCHIMP_ID = "mailchimpid";
    private static final String EMAIL = "somestupidemail@home.com";
   // private static final String SOME_CODE = "somecode";
    private static final String EMAIL_MIXED_CASE = "SomeStupidEmail@Home.com";
    private static final String USER_CODE = "USER_CODE";
    private static final String FIRST = "FIRST";
    private static final String LAST = "LAST";
    private static final String FIRST_LAST = "FIRST LAST";
    private static final String PASSWORD = "PASSWORD";
    private static final String ENCRYPTED_PASSWORD = "ENCRYPTED";
    private static final String AFFILIATE_ID = "AFFILIATE_ID";
    private static final String APPLICATION_ID = "APPLICATION_ID";

    @Mock
    private UserServiceImpl userService;

    @Mock
    private FacebookUserServiceImpl facebookUserService;

    @Mock
    private PasswordEncryptor passwordEncryptor;

    @Mock
    private MailService mailService;

    @InjectMocks
    ProfileServiceImpl profileService;

    private ObjectId userId;
    private User user;
    private UnsubscribeFromEmailList unsubscribe;
    private AffiliateAccount affiliateNoEmail;
    private AffiliateAccount affiliateWithEmail;

    @Before
    public void setUp() {
        profileService.setMailchimpListId(MAILCHIMP_ID);

        userId = new ObjectId();
        user = new User();
        user.setId(userId);
        user.setMl(EMAIL);
        user.setCd(USER_CODE);

        unsubscribe = new UnsubscribeFromEmailList();
        unsubscribe.setEmail(EMAIL);
        unsubscribe.setListId(MAILCHIMP_ID);

        affiliateNoEmail = new AffiliateAccount();
        affiliateNoEmail.setNm("Test Affiliate");
        affiliateNoEmail.setRlnm("test-affiliate-rlnm");
        affiliateNoEmail.setSndrgcnf(false);

        affiliateWithEmail = new AffiliateAccount();
        affiliateWithEmail.setNm("Email Affiliate");
        affiliateWithEmail.setRlnm("email-affiliate-rlnm");
        affiliateWithEmail.setSndrgcnf(true);
    }

    @Test
    public void testRegisterFacebookUser() {
        try {
            log.info("Verify Facebook email optin");

            // Set up the mock data
            final User[] saved = { null };
            when(userService.findUserByEmail(EMAIL)).thenReturn(null);
            when(userService.findUserByCode(USER_CODE)).thenAnswer(new Answer<User>() {
                @Override
                public User answer(InvocationOnMock invocationOnMock) throws Throwable {
                    return saved[0];
                }
            });
            when(passwordEncryptor.encryptPassword(anyString())).thenReturn(ENCRYPTED_PASSWORD);
            when(userService.findUserSupplement(USER_CODE)).thenReturn(null);
            when(userService.saveUser(any(User.class))).thenAnswer(new Answer<User>() {
                @Override
                public User answer(InvocationOnMock invocationOnMock) throws Throwable {
                    saved[0] = (User)invocationOnMock.getArguments()[0];
                    return (User) invocationOnMock.getArguments()[0];
                }
            });
            when(userService.saveUserSupplement(any(UserSupplement.class))).thenAnswer(new Answer<UserSupplement>() {
                @Override
                public UserSupplement answer(InvocationOnMock invocationOnMock) throws Throwable {
                    return (UserSupplement)invocationOnMock.getArguments()[0];
                }
            });

            UserProfileBuilder builder = new UserProfileBuilder();
            builder.setEmail(EMAIL_MIXED_CASE);
            builder.setFirstName(FIRST);
            builder.setLastName(LAST);
            builder.setName(FIRST_LAST);
            builder.setUsername(EMAIL_MIXED_CASE);
            UserProfile dto = builder.build();

            ArgumentCaptor<Object> trackEvent = (ArgumentCaptor)ArgumentCaptor.forClass(Object.class);
            mockStatic(EventHelper.class);

            // execute the service method
            profileService.registerFacebookUser(dto, USER_CODE, null);

            verifyStatic(times(1));
            EventHelper.post(trackEvent.capture());
            assertTrue("RegistrationEvent not posted", trackEvent.getAllValues().get(0) instanceof RegistrationEvent);

            // verify
            ArgumentCaptor<User> userArg = ArgumentCaptor.forClass(User.class);
            ArgumentCaptor<UserSupplement> usArg = ArgumentCaptor.forClass(UserSupplement.class);
            ArgumentCaptor<Map> mailArg = ArgumentCaptor.forClass(Map.class);
            ArgumentCaptor<SubscribeToEmailList> subscribeArg = ArgumentCaptor.forClass(SubscribeToEmailList.class);
            verify(userService, times(1)).saveUser(userArg.capture());
            verify(userService, times(1)).saveUserSupplement(usArg.capture());
            verify(passwordEncryptor, times(1)).encryptPassword(anyString());
            verify(mailService, times(1)).sendRegistrationConfirmation(eq(EMAIL), mailArg.capture(), eq(Locale.US));
            verify(userService, times(1)).subscribeToEmailList(subscribeArg.capture());

            // Verify new user
            User captured = userArg.getValue();
            Assert.assertEquals("Email should be lower-cased", EMAIL, captured.getMl());
            Assert.assertEquals("Password should be encrypted", ENCRYPTED_PASSWORD, captured.getPsswrd());

            // Verify new user supplement
            UserSupplement us = usArg.getValue();
            Assert.assertEquals("UserCode should be set on User Supplement", USER_CODE, us.getCd());
            Assert.assertEquals("Optin should be true", Boolean.TRUE, us.getPtn());
            Assert.assertEquals("Email should be set on User Supplement", EMAIL, us.getMl());
            Assert.assertEquals("First Name should be set on User Supplement", FIRST, us.getFnm());
            Assert.assertEquals("Last Name should be set on User Supplement", LAST, us.getLnm());
            Assert.assertEquals("Fullname should be set on User Supplement", FIRST_LAST, us.getFllnm());

            // Verify email subscription
            SubscribeToEmailList subscribe = subscribeArg.getValue();
            assertNotNull(subscribe);
            assertEquals("Email is not correct", EMAIL, subscribe.getEmail());
            assertEquals("Mail chip list id is not correct", MAILCHIMP_ID, subscribe.getListId());

            // Verify new user email parameters
            Map<MailParameter, Object> mailParams = (Map<MailParameter, Object>) mailArg.getValue();
            Assert.assertNotNull("Mail params null", mailParams);
            Assert.assertNotNull("Mail password not set", mailParams.get(MailParameter.PASSWORD));
            Assert.assertEquals("First name not set in email", FIRST, mailParams.get(MailParameter.USER_FIRST_NAME));
            Assert.assertEquals("Last name not set in email", LAST, mailParams.get(MailParameter.USER_LAST_NAME));
            Assert.assertEquals("User Email not set in email", EMAIL, mailParams.get(MailParameter.USER_EMAIL));
            Assert.assertEquals("Opt In not set in email", true, mailParams.get(MailParameter.NEWSLETTER_OPTIN_FLAG));
            Assert.assertEquals("Mail List not set in email", MAILCHIMP_ID, mailParams.get(MailParameter.MAILCHIMP_LIST_ID));

            log.info("Facebook register user test success");
        } catch (Exception e) {
            fail("Exception on register facebook user: " + e.getMessage());
            log.error("Exception on register facebook user", e);
        }
    }

    @Test
    public void testRegisterFacebookUserNoDomainEmail() {
        try {
            log.info("Verify Facebook email optin without domain email");

            // Set up the mock data
            final User[] saved = { null };
            when(userService.findUserByEmail(EMAIL)).thenReturn(null);
            when(userService.findUserByCode(USER_CODE)).thenAnswer(new Answer<User>() {
                @Override
                public User answer(InvocationOnMock invocationOnMock) throws Throwable {
                    return saved[0];
                }
            });
            when(passwordEncryptor.encryptPassword(anyString())).thenReturn(ENCRYPTED_PASSWORD);
            when(userService.findUserSupplement(USER_CODE)).thenReturn(null);
            when(userService.saveUser(any(User.class))).thenAnswer(new Answer<User>() {
                @Override
                public User answer(InvocationOnMock invocationOnMock) throws Throwable {
                    saved[0] = (User)invocationOnMock.getArguments()[0];
                    return (User) invocationOnMock.getArguments()[0];
                }
            });
            when(userService.saveUserSupplement(any(UserSupplement.class))).thenAnswer(new Answer<UserSupplement>() {
                @Override
                public UserSupplement answer(InvocationOnMock invocationOnMock) throws Throwable {
                    return (UserSupplement)invocationOnMock.getArguments()[0];
                }
            });

            UserProfileBuilder builder = new UserProfileBuilder();
            builder.setEmail(EMAIL_MIXED_CASE);
            builder.setFirstName(FIRST);
            builder.setLastName(LAST);
            builder.setName(FIRST_LAST);
            builder.setUsername(EMAIL_MIXED_CASE);
            UserProfile dto = builder.build();

            ArgumentCaptor<Object> trackEvent = (ArgumentCaptor)ArgumentCaptor.forClass(Object.class);
            mockStatic(EventHelper.class);

            // execute the service method
            profileService.registerFacebookUser(dto, USER_CODE, affiliateNoEmail);

            verifyStatic(times(1));
            EventHelper.post(trackEvent.capture());

            assertTrue("RegistrationEvent not posted", trackEvent.getAllValues().get(0) instanceof RegistrationEvent);

            // verify
            ArgumentCaptor<User> userArg = ArgumentCaptor.forClass(User.class);
            ArgumentCaptor<UserSupplement> usArg = ArgumentCaptor.forClass(UserSupplement.class);
            ArgumentCaptor<SubscribeToEmailList> subscribeArg = ArgumentCaptor.forClass(SubscribeToEmailList.class);
            verify(userService, times(1)).saveUser(userArg.capture());
            verify(userService, times(1)).saveUserSupplement(usArg.capture());
            verify(passwordEncryptor, times(1)).encryptPassword(anyString());
            verify(userService, times(1)).subscribeToEmailList(subscribeArg.capture());

            // Verify new user
            User captured = userArg.getValue();
            Assert.assertEquals("Email should be lower-cased", EMAIL, captured.getMl());
            Assert.assertEquals("Password should be encrypted", ENCRYPTED_PASSWORD, captured.getPsswrd());

            // Verify new user supplement
            UserSupplement us = usArg.getValue();
            Assert.assertEquals("UserCode should be set on User Supplement", USER_CODE, us.getCd());
            Assert.assertEquals("Optin should be true", Boolean.TRUE, us.getPtn());
            Assert.assertEquals("Email should be set on User Supplement", EMAIL, us.getMl());
            Assert.assertEquals("First Name should be set on User Supplement", FIRST, us.getFnm());
            Assert.assertEquals("Last Name should be set on User Supplement", LAST, us.getLnm());
            Assert.assertEquals("Fullname should be set on User Supplement", FIRST_LAST, us.getFllnm());

            // Verify email subscription
            SubscribeToEmailList subscribe = subscribeArg.getValue();
            assertNotNull(subscribe);
            assertEquals("Email is not correct", EMAIL, subscribe.getEmail());
            assertEquals("Mail chip list id is not correct", MAILCHIMP_ID, subscribe.getListId());

            log.info("Facebook register user test success");
        } catch (Exception e) {
            fail("Exception on register facebook user: " + e.getMessage());
        }
    }

    /*
    @Test
    public void testRegisterAPIUser() {
        try {
            log.info("Verify Register API User");

            // Set up the mock data
            final User[] saved = { null };
            when(userRepository.findByCode(USER_CODE)).thenReturn(null);
            when(userRepository.findOne(any(Predicate.class))).thenAnswer(new Answer<User>() {
                @Override
                public User answer(InvocationOnMock invocationOnMock) throws Throwable {
                    return saved[0];
                }
            });
            when(passwordEncryptor.encryptPassword(anyString())).thenReturn(ENCRYPTED_PASSWORD);
            when(userSupplementService.findUserSupplement(USER_CODE)).thenReturn(null);
            when(cacheService.retrieveFromCache(anyString(), anyString())).thenReturn(null);
            when(userRepository.save(any(User.class))).thenAnswer(new Answer<User>() {
                @Override
                public User answer(InvocationOnMock invocationOnMock) throws Throwable {
                    saved[0] = (User)invocationOnMock.getArguments()[0];
                    return (User) invocationOnMock.getArguments()[0];
                }
            });
            when(userSupplementService.saveUserSupplement(any(UserSupplement.class))).thenAnswer(new Answer<UserSupplement>() {
                @Override
                public UserSupplement answer(InvocationOnMock invocationOnMock) throws Throwable {
                    return (UserSupplement)invocationOnMock.getArguments()[0];
                }
            });

            RegisterUserRequest dto = new RegisterUserRequest();
            dto.setEmail(EMAIL_MIXED_CASE);
            dto.setAffiliateId(AFFILIATE_ID);
            dto.setApplicationId(APPLICATION_ID);
            dto.setLocale(Locale.US);
            dto.setFirstName(FIRST);
            dto.setLastName(LAST);

            ArgumentCaptor<Object> trackEvent = (ArgumentCaptor)ArgumentCaptor.forClass(Object.class);
            mockStatic(EventHelper.class);

            // execute the service method
            userService.registerApiUser(dto, USER_CODE, null);

            verifyStatic(times(2));
            EventHelper.post(trackEvent.capture());

            assertTrue("RegistrationEvent not posted", trackEvent.getAllValues().get(0) instanceof RegistrationEvent);
            assertTrue("SubscribeEvent not posted", trackEvent.getAllValues().get(1) instanceof SubscribeEvent);

            // verify
            ArgumentCaptor<User> userArg = ArgumentCaptor.forClass(User.class);
            ArgumentCaptor<UserSupplement> usArg = ArgumentCaptor.forClass(UserSupplement.class);
            ArgumentCaptor<Map> mailArg = ArgumentCaptor.forClass(Map.class);
            verify(userRepository, times(1)).save(userArg.capture());
            verify(userSupplementService, times(1)).saveUserSupplement(usArg.capture());
            verify(passwordEncryptor, times(1)).encryptPassword(anyString());
            verify(mailService, times(1)).sendApiRegistrationConfirmation(eq(EMAIL), mailArg.capture(), eq(Locale.US));
            verify(mailChimpService, times(1)).listSubscribe(MAILCHIMP_ID, EMAIL, null, EmailType.HTML, false, true, false, false);

            // Verify new user
            User captured = userArg.getValue();
            Assert.assertEquals("Email should be lower-cased", EMAIL, captured.getMl());
            Assert.assertEquals("Password should be encrypted", ENCRYPTED_PASSWORD, captured.getPsswrd());

            // Verify new user supplement
            UserSupplement us = usArg.getValue();
            Assert.assertEquals("UserCode should be set on User Supplement", USER_CODE, us.getCd());
            Assert.assertEquals("Optin should be true", Boolean.TRUE, us.getPtn());
            Assert.assertEquals("Email should be set on User Supplement", EMAIL, us.getMl());
            Assert.assertEquals("First Name should be set on User Supplement", FIRST, us.getFnm());
            Assert.assertEquals("Last Name should be set on User Supplement", LAST, us.getLnm());
            Assert.assertEquals("Fullname should be set on User Supplement", FIRST_LAST, us.getFllnm());

            // Verify new user email parameters
            Map<MailParameter, Object> mailParams = (Map<MailParameter, Object>) mailArg.getValue();
            Assert.assertNotNull("Mail params null", mailParams);
            Assert.assertNotNull("Mail password not set", mailParams.get(MailParameter.PASSWORD));
            Assert.assertEquals("First name not set in email", FIRST, mailParams.get(MailParameter.USER_FIRST_NAME));
            Assert.assertEquals("Last name not set in email", LAST, mailParams.get(MailParameter.USER_LAST_NAME));
            Assert.assertEquals("User Email not set in email", EMAIL, mailParams.get(MailParameter.USER_EMAIL));
            Assert.assertEquals("Opt In not set in email", true, mailParams.get(MailParameter.NEWSLETTER_OPTIN_FLAG));
            Assert.assertEquals("Mail List not set in email", MAILCHIMP_ID, mailParams.get(MailParameter.MAILCHIMP_LIST_ID));

            log.info("API register user test success");
        } catch (Exception e) {
            fail("Exception on register API user: " + e.getMessage());
        }
    }

    @Test
    public void testRegisterAPIUserNoDomainEmail() {
        try {
            log.info("Verify Register API User");

            // Set up the mock data
            final User[] saved = { null };
            when(userRepository.findByCode(USER_CODE)).thenReturn(null);
            when(userRepository.findOne(any(Predicate.class))).thenAnswer(new Answer<User>() {
                @Override
                public User answer(InvocationOnMock invocationOnMock) throws Throwable {
                    return saved[0];
                }
            });
            when(passwordEncryptor.encryptPassword(anyString())).thenReturn(ENCRYPTED_PASSWORD);
            when(userSupplementService.findUserSupplement(USER_CODE)).thenReturn(null);
            when(cacheService.retrieveFromCache(anyString(), anyString())).thenReturn(null);
            when(userRepository.save(any(User.class))).thenAnswer(new Answer<User>() {
                @Override
                public User answer(InvocationOnMock invocationOnMock) throws Throwable {
                    saved[0] = (User)invocationOnMock.getArguments()[0];
                    return (User) invocationOnMock.getArguments()[0];
                }
            });
            when(userSupplementService.saveUserSupplement(any(UserSupplement.class))).thenAnswer(new Answer<UserSupplement>() {
                @Override
                public UserSupplement answer(InvocationOnMock invocationOnMock) throws Throwable {
                    return (UserSupplement)invocationOnMock.getArguments()[0];
                }
            });

            RegisterUserRequest dto = new RegisterUserRequest();
            dto.setEmail(EMAIL_MIXED_CASE);
            dto.setAffiliateId(AFFILIATE_ID);
            dto.setApplicationId(APPLICATION_ID);
            dto.setLocale(Locale.US);
            dto.setFirstName(FIRST);
            dto.setLastName(LAST);

            ArgumentCaptor<Object> trackEvent = (ArgumentCaptor)ArgumentCaptor.forClass(Object.class);
            mockStatic(EventHelper.class);

            // execute the service method
            userService.registerApiUser(dto, USER_CODE, affiliateNoEmail);

            verifyStatic(times(2));
            EventHelper.post(trackEvent.capture());

            assertTrue("RegistrationEvent not posted", trackEvent.getAllValues().get(0) instanceof RegistrationEvent);
            assertTrue("SubscribeEvent not posted", trackEvent.getAllValues().get(1) instanceof SubscribeEvent);

            // verify
            ArgumentCaptor<User> userArg = ArgumentCaptor.forClass(User.class);
            ArgumentCaptor<UserSupplement> usArg = ArgumentCaptor.forClass(UserSupplement.class);
            verify(userRepository, times(1)).save(userArg.capture());
            verify(userSupplementService, times(1)).saveUserSupplement(usArg.capture());
            verify(passwordEncryptor, times(1)).encryptPassword(anyString());
            verify(mailChimpService, times(1)).listSubscribe(MAILCHIMP_ID, EMAIL, null, EmailType.HTML, false, true, false, false);

            // Verify new user
            User captured = userArg.getValue();
            Assert.assertEquals("Email should be lower-cased", EMAIL, captured.getMl());
            Assert.assertEquals("Password should be encrypted", ENCRYPTED_PASSWORD, captured.getPsswrd());

            // Verify new user supplement
            UserSupplement us = usArg.getValue();
            Assert.assertEquals("UserCode should be set on User Supplement", USER_CODE, us.getCd());
            Assert.assertEquals("Optin should be true", Boolean.TRUE, us.getPtn());
            Assert.assertEquals("Email should be set on User Supplement", EMAIL, us.getMl());
            Assert.assertEquals("First Name should be set on User Supplement", FIRST, us.getFnm());
            Assert.assertEquals("Last Name should be set on User Supplement", LAST, us.getLnm());
            Assert.assertEquals("Fullname should be set on User Supplement", FIRST_LAST, us.getFllnm());

            log.info("API register user test success");
        } catch (Exception e) {
            fail("Exception on register API user: " + e.getMessage());
        }
    }

    @Test
    public void testRegisterStandardUser() {
        try {
            log.info("Verify Register Standard User");

            // Set up the mock data
            final User[] saved = { null };
            when(userRepository.findByCode(USER_CODE)).thenReturn(null);
            when(userRepository.findOne(any(Predicate.class))).thenAnswer(new Answer<User>() {
                @Override
                public User answer(InvocationOnMock invocationOnMock) throws Throwable {
                    return saved[0];
                }
            });
            when(passwordEncryptor.encryptPassword(anyString())).thenReturn(ENCRYPTED_PASSWORD);
            when(userSupplementService.findUserSupplement(USER_CODE)).thenReturn(null);
            when(cacheService.retrieveFromCache(anyString(), anyString())).thenReturn(null);
            when(userRepository.save(any(User.class))).thenAnswer(new Answer<User>() {
                @Override
                public User answer(InvocationOnMock invocationOnMock) throws Throwable {
                    saved[0] = (User)invocationOnMock.getArguments()[0];
                    return (User) invocationOnMock.getArguments()[0];
                }
            });
            when(userSupplementService.saveUserSupplement(any(UserSupplement.class))).thenAnswer(new Answer<UserSupplement>() {
                @Override
                public UserSupplement answer(InvocationOnMock invocationOnMock) throws Throwable {
                    return (UserSupplement)invocationOnMock.getArguments()[0];
                }
            });

            UserDto dto = new UserDto();
            dto.setMl(EMAIL_MIXED_CASE);
            dto.setLcl(Locale.US);
            dto.setFnm(FIRST);
            dto.setLnm(LAST);
            dto.setPsswrd(PASSWORD);
            dto.setOptin(true);

            User transientUser = new User();
            transientUser.setCd(USER_CODE);

            ArgumentCaptor<Object> trackEvent = (ArgumentCaptor)ArgumentCaptor.forClass(Object.class);
            mockStatic(EventHelper.class);

            // execute the service method
            userService.registerWebUser(dto, transientUser, null);

            verifyStatic(times(2));
            EventHelper.post(trackEvent.capture());

            assertTrue("RegistrationEvent not posted", trackEvent.getAllValues().get(0) instanceof RegistrationEvent);
            assertTrue("SubscribeEvent not posted", trackEvent.getAllValues().get(1) instanceof SubscribeEvent);

            // verify
            ArgumentCaptor<User> userArg = ArgumentCaptor.forClass(User.class);
            ArgumentCaptor<UserSupplement> usArg = ArgumentCaptor.forClass(UserSupplement.class);
            ArgumentCaptor<Map> mailArg = ArgumentCaptor.forClass(Map.class);
            verify(userRepository, times(1)).save(userArg.capture());
            verify(userSupplementService, times(1)).saveUserSupplement(usArg.capture());
            verify(passwordEncryptor, times(1)).encryptPassword(PASSWORD);
            verify(mailService, times(1)).sendRegistrationConfirmation(eq(EMAIL), mailArg.capture(), eq(Locale.US));
            verify(mailChimpService, times(1)).listSubscribe(MAILCHIMP_ID, EMAIL, null, EmailType.HTML, false, true, false, false);

            // Verify new user
            User captured = userArg.getValue();
            Assert.assertEquals("Email should be lower-cased", EMAIL, captured.getMl());
            Assert.assertEquals("Password should be encrypted", ENCRYPTED_PASSWORD, captured.getPsswrd());

            // Verify new user supplement
            UserSupplement us = usArg.getValue();
            Assert.assertEquals("UserCode should be set on User Supplement", USER_CODE, us.getCd());
            Assert.assertEquals("Optin should be true", Boolean.TRUE, us.getPtn());
            Assert.assertEquals("Email should be set on User Supplement", EMAIL, us.getMl());
            Assert.assertEquals("First Name should be set on User Supplement", FIRST, us.getFnm());
            Assert.assertEquals("Last Name should be set on User Supplement", LAST, us.getLnm());
            Assert.assertEquals("Fullname should be set on User Supplement", FIRST_LAST, us.getFllnm());

            // Verify new user email parameters
            Map<MailParameter, Object> mailParams = (Map<MailParameter, Object>) mailArg.getValue();
            Assert.assertNotNull("Mail params null", mailParams);
            Assert.assertNotNull("Mail password not set", mailParams.get(MailParameter.PASSWORD));
            Assert.assertEquals("First name not set in email", FIRST, mailParams.get(MailParameter.USER_FIRST_NAME));
            Assert.assertEquals("Last name not set in email", LAST, mailParams.get(MailParameter.USER_LAST_NAME));
            Assert.assertEquals("User Email not set in email", EMAIL, mailParams.get(MailParameter.USER_EMAIL));
            Assert.assertEquals("Opt In not set in email", true, mailParams.get(MailParameter.NEWSLETTER_OPTIN_FLAG));
            Assert.assertEquals("Mail List not set in email", MAILCHIMP_ID, mailParams.get(MailParameter.MAILCHIMP_LIST_ID));

            log.info("Standard register user test success");
        } catch (Exception e) {
            fail("Exception on register Standard user: " + e.getMessage());
        }
    }

    @Test
    public void testRegisterStandardUserDomainWithEmail() {
        try {
            log.info("Verify Register Standard User domain with email");

            // Set up the mock data
            final User[] saved = { null };
            when(userRepository.findByCode(USER_CODE)).thenReturn(null);
            when(userRepository.findOne(any(Predicate.class))).thenAnswer(new Answer<User>() {
                @Override
                public User answer(InvocationOnMock invocationOnMock) throws Throwable {
                    return saved[0];
                }
            });
            when(passwordEncryptor.encryptPassword(anyString())).thenReturn(ENCRYPTED_PASSWORD);
            when(userSupplementService.findUserSupplement(USER_CODE)).thenReturn(null);
            when(cacheService.retrieveFromCache(anyString(), anyString())).thenReturn(null);
            when(userRepository.save(any(User.class))).thenAnswer(new Answer<User>() {
                @Override
                public User answer(InvocationOnMock invocationOnMock) throws Throwable {
                    saved[0] = (User)invocationOnMock.getArguments()[0];
                    return (User) invocationOnMock.getArguments()[0];
                }
            });
            when(userSupplementService.saveUserSupplement(any(UserSupplement.class))).thenAnswer(new Answer<UserSupplement>() {
                @Override
                public UserSupplement answer(InvocationOnMock invocationOnMock) throws Throwable {
                    return (UserSupplement)invocationOnMock.getArguments()[0];
                }
            });

            UserDto dto = new UserDto();
            dto.setMl(EMAIL_MIXED_CASE);
            dto.setLcl(Locale.US);
            dto.setFnm(FIRST);
            dto.setLnm(LAST);
            dto.setPsswrd(PASSWORD);
            dto.setOptin(true);

            User transientUser = new User();
            transientUser.setCd(USER_CODE);

            ArgumentCaptor<Object> trackEvent = (ArgumentCaptor)ArgumentCaptor.forClass(Object.class);
            mockStatic(EventHelper.class);

            // execute the service method
            userService.registerWebUser(dto, transientUser, affiliateWithEmail);

            verifyStatic(times(2));
            EventHelper.post(trackEvent.capture());

            assertTrue("RegistrationEvent not posted", trackEvent.getAllValues().get(0) instanceof RegistrationEvent);
            assertTrue("SubscribeEvent not posted", trackEvent.getAllValues().get(1) instanceof SubscribeEvent);

            // verify
            ArgumentCaptor<User> userArg = ArgumentCaptor.forClass(User.class);
            ArgumentCaptor<UserSupplement> usArg = ArgumentCaptor.forClass(UserSupplement.class);
            ArgumentCaptor<Map> mailArg = ArgumentCaptor.forClass(Map.class);
            verify(userRepository, times(1)).save(userArg.capture());
            verify(userSupplementService, times(1)).saveUserSupplement(usArg.capture());
            verify(passwordEncryptor, times(1)).encryptPassword(PASSWORD);
            verify(mailService, times(1)).sendRegistrationConfirmation(eq(EMAIL), mailArg.capture(), eq(Locale.US));
            verify(mailChimpService, times(1)).listSubscribe(MAILCHIMP_ID, EMAIL, null, EmailType.HTML, false, true, false, false);

            // Verify new user
            User captured = userArg.getValue();
            Assert.assertEquals("Email should be lower-cased", EMAIL, captured.getMl());
            Assert.assertEquals("Password should be encrypted", ENCRYPTED_PASSWORD, captured.getPsswrd());

            // Verify new user supplement
            UserSupplement us = usArg.getValue();
            Assert.assertEquals("UserCode should be set on User Supplement", USER_CODE, us.getCd());
            Assert.assertEquals("Optin should be true", Boolean.TRUE, us.getPtn());
            Assert.assertEquals("Email should be set on User Supplement", EMAIL, us.getMl());
            Assert.assertEquals("First Name should be set on User Supplement", FIRST, us.getFnm());
            Assert.assertEquals("Last Name should be set on User Supplement", LAST, us.getLnm());
            Assert.assertEquals("Fullname should be set on User Supplement", FIRST_LAST, us.getFllnm());

            // Verify new user email parameters
            Map<MailParameter, Object> mailParams = (Map<MailParameter, Object>) mailArg.getValue();
            Assert.assertNotNull("Mail params null", mailParams);
            Assert.assertNotNull("Mail password not set", mailParams.get(MailParameter.PASSWORD));
            Assert.assertEquals("First name not set in email", FIRST, mailParams.get(MailParameter.USER_FIRST_NAME));
            Assert.assertEquals("Last name not set in email", LAST, mailParams.get(MailParameter.USER_LAST_NAME));
            Assert.assertEquals("User Email not set in email", EMAIL, mailParams.get(MailParameter.USER_EMAIL));
            Assert.assertEquals("Opt In not set in email", true, mailParams.get(MailParameter.NEWSLETTER_OPTIN_FLAG));
            Assert.assertEquals("Mail List not set in email", MAILCHIMP_ID, mailParams.get(MailParameter.MAILCHIMP_LIST_ID));

            log.info("Standard register user test success");
        } catch (Exception e) {
            fail("Exception on register Standard user: " + e.getMessage());
        }
    }

    @Test
    public void testRegisterStandardUserNoDomainEmail() {
        try {
            log.info("Verify Register Standard User no domain email");

            // Set up the mock data
            final User[] saved = { null };
            when(userRepository.findByCode(USER_CODE)).thenReturn(null);
            when(userRepository.findOne(any(Predicate.class))).thenAnswer(new Answer<User>() {
                @Override
                public User answer(InvocationOnMock invocationOnMock) throws Throwable {
                    return saved[0];
                }
            });
            when(passwordEncryptor.encryptPassword(anyString())).thenReturn(ENCRYPTED_PASSWORD);
            when(userSupplementService.findUserSupplement(USER_CODE)).thenReturn(null);
            when(cacheService.retrieveFromCache(anyString(), anyString())).thenReturn(null);
            when(userRepository.save(any(User.class))).thenAnswer(new Answer<User>() {
                @Override
                public User answer(InvocationOnMock invocationOnMock) throws Throwable {
                    saved[0] = (User)invocationOnMock.getArguments()[0];
                    return (User) invocationOnMock.getArguments()[0];
                }
            });
            when(userSupplementService.saveUserSupplement(any(UserSupplement.class))).thenAnswer(new Answer<UserSupplement>() {
                @Override
                public UserSupplement answer(InvocationOnMock invocationOnMock) throws Throwable {
                    return (UserSupplement)invocationOnMock.getArguments()[0];
                }
            });

            UserDto dto = new UserDto();
            dto.setMl(EMAIL_MIXED_CASE);
            dto.setLcl(Locale.US);
            dto.setFnm(FIRST);
            dto.setLnm(LAST);
            dto.setPsswrd(PASSWORD);
            dto.setOptin(true);

            User transientUser = new User();
            transientUser.setCd(USER_CODE);

            ArgumentCaptor<Object> trackEvent = (ArgumentCaptor)ArgumentCaptor.forClass(Object.class);
            mockStatic(EventHelper.class);

            // execute the service method
            userService.registerWebUser(dto, transientUser, affiliateNoEmail);

            verifyStatic(times(2));
            EventHelper.post(trackEvent.capture());

            assertTrue("RegistrationEvent not posted", trackEvent.getAllValues().get(0) instanceof RegistrationEvent);
            assertTrue("SubscribeEvent not posted", trackEvent.getAllValues().get(1) instanceof SubscribeEvent);

            // verify
            ArgumentCaptor<User> userArg = ArgumentCaptor.forClass(User.class);
            ArgumentCaptor<UserSupplement> usArg = ArgumentCaptor.forClass(UserSupplement.class);
            ArgumentCaptor<Map> mailArg = ArgumentCaptor.forClass(Map.class);
            verify(userRepository, times(1)).save(userArg.capture());
            verify(userSupplementService, times(1)).saveUserSupplement(usArg.capture());
            verify(passwordEncryptor, times(1)).encryptPassword(PASSWORD);
            verify(mailChimpService, times(1)).listSubscribe(MAILCHIMP_ID, EMAIL, null, EmailType.HTML, false, true, false, false);

            // Verify new user
            User captured = userArg.getValue();
            Assert.assertEquals("Email should be lower-cased", EMAIL, captured.getMl());
            Assert.assertEquals("Password should be encrypted", ENCRYPTED_PASSWORD, captured.getPsswrd());

            // Verify new user supplement
            UserSupplement us = usArg.getValue();
            Assert.assertEquals("UserCode should be set on User Supplement", USER_CODE, us.getCd());
            Assert.assertEquals("Optin should be true", Boolean.TRUE, us.getPtn());
            Assert.assertEquals("Email should be set on User Supplement", EMAIL, us.getMl());
            Assert.assertEquals("First Name should be set on User Supplement", FIRST, us.getFnm());
            Assert.assertEquals("Last Name should be set on User Supplement", LAST, us.getLnm());
            Assert.assertEquals("Fullname should be set on User Supplement", FIRST_LAST, us.getFllnm());

            log.info("Standard register user test success");
        } catch (Exception e) {
            fail("Exception on register Standard user: " + e.getMessage());
        }
    }
    */

    @Test
    public void testRemoveUserProfile() {
        log.info("Set up user data to remove");

        // Set up the mock data
        when(userService.findUser(userId)).thenReturn(user);

        // execute the service method
        profileService.removeUserProfile(new DisableUser(), userId);

        // verify
        verify(userService, times(1)).removeUser(user);
        verify(userService, times(1)).unsubscribeFromEmailList(unsubscribe);
        verify(facebookUserService, times(1)).removeFacebookSnapshot(user.getMl());

        log.info("User profile was removed successfully");
    }
}
