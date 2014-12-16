/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */
package com.lela.commons.service.impl;

import com.lela.commons.event.EventHelper;
import com.lela.commons.event.RegistrationEvent;
import com.lela.commons.event.UpdateRemoteIPAddressEvent;
import com.lela.commons.event.UpdateZipEvent;
import com.lela.commons.mail.MailServiceException;
import com.lela.commons.service.AffiliateService;
import com.lela.commons.service.FacebookUserService;
import com.lela.commons.service.MailService;
import com.lela.commons.service.ProfileService;
import com.lela.commons.service.UserService;
import com.lela.commons.service.UserSupplementService;
import com.lela.commons.service.UserTrackerService;
import com.lela.commons.spring.security.SpringSecurityHelper;
import com.lela.domain.ApplicationConstants;
import com.lela.domain.document.AffiliateAccount;
import com.lela.domain.document.User;
import com.lela.domain.document.UserSupplement;
import com.lela.domain.dto.AffiliateIdentifiers;
import com.lela.domain.dto.Principal;
import com.lela.domain.dto.SubscribeToEmailList;
import com.lela.domain.dto.UnsubscribeFromEmailList;
import com.lela.domain.dto.UserDto;
import com.lela.domain.dto.user.Address;
import com.lela.domain.dto.user.DisableUser;

import com.lela.domain.dto.user.RegisterUserRequest;
import com.lela.domain.enums.CacheType;
import com.lela.domain.enums.MailParameter;
import com.lela.domain.enums.RegistrationType;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.bson.types.ObjectId;
import org.jasypt.util.password.PasswordEncryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.social.connect.UserProfile;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * User: Chris Tallent
 * Date: 9/6/12
 * Time: 6:06 PM
 */
@Service("profileService")
public class ProfileServiceImpl implements ProfileService {
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserService userService;
    private final FacebookUserService facebookUserService;
    private final PasswordEncryptor passwordEncryptor;
    private final MailService mailService;
    private final UserTrackerService userTrackerService;
    private final AffiliateService affiliateService;

    @Value("${mailchimp.list.id}")
    private String mailchimpListId;

    @Autowired
    public ProfileServiceImpl(UserService userService,
                              FacebookUserService facebookUserService,
                              PasswordEncryptor passwordEncryptor,
                              MailService mailService,
                              UserTrackerService userTrackerService,
                              AffiliateService affiliateService) {
        this.userService = userService;
        this.facebookUserService = facebookUserService;
        this.passwordEncryptor = passwordEncryptor;
        this.mailService = mailService;
        this.userTrackerService = userTrackerService;
        this.affiliateService = affiliateService;
    }

    /**
     * Method description
     *
     * @return Return value
     */
    @Override
    public User registerFacebookUser(UserProfile dto, String sessionUserCode, AffiliateAccount domainAffiliate) {
        log.info(String.format("Registering Facebook User with email %s ", dto.getEmail()));

        User user = new User(dto);

        // this gets encrypted in the user service
        user.setPsswrd(RandomStringUtils.randomAlphabetic(8));

        InternalUserData data = new InternalUserData();
        data.setMl(dto.getEmail());
        data.setFnm(dto.getFirstName());
        data.setLnm(dto.getLastName());
        data.setFllnm(dto.getName());
        data.setPtn(true);

        // Verify that user code in the session is not already used in the database
        if (sessionUserCode != null && userService.findUserByCode(sessionUserCode) == null) {
            user.setCd(sessionUserCode);
        }

        return registerUser(user, data, RegistrationType.FACEBOOK, domainAffiliate, true);
    }

    /**
     * This type of registration warrants
     *
     *
     * @param dto dto
     * @param domainAffiliate
     * @return
     */
    @Override
    public User registerApiUser(RegisterUserRequest dto, String sessionUserCode, AffiliateAccount domainAffiliate) {
        User user = new User(dto);
        user.setPsswrd(new BigInteger(39, new SecureRandom()).toString(32));

        // Ensure the email address is lowercase
        user.setMl(user.getMl().toLowerCase());

        // Verify that user code in the session is not already used in the database
        if (sessionUserCode != null && userService.findUserByCode(sessionUserCode) == null) {
            user.setCd(sessionUserCode);
        }

        InternalUserData data = new InternalUserData();
        data.setMl(dto.getEmail());
        data.setFnm(dto.getFirstName());
        data.setLnm(dto.getLastName());
        data.setLcl(dto.getLocale());
        data.setAttrs(dto.getAttributes());
        data.setDdrss(dto.getAddresses());
        data.setPtn(dto.getOptin());

        log.info(String.format("Registering API User with email %s ", user.getMl()));
        // save user and send registration confirmation email
        user = registerUser(user, data, RegistrationType.API, domainAffiliate, true);

        return user;
    }

    /**
     * Method description
     *
     * @param user user
     * @return Return value
     */
    @Override
    public User registerTestUser(User user) {
        //Explicitly set the optin to false
        InternalUserData data = new InternalUserData();
        data.setPtn(false);

        return registerUser(user, data, RegistrationType.WEBSITE, null, false);
    }

    /**
     * Method description
     *
     * @return Return value
     */
    @Override
    public User registerWebUser(UserDto dto, User transientUser, AffiliateAccount domainAffiliate) {
        if (transientUser != null) {
            transientUser.setMl(dto.getMl());
            transientUser.setPsswrd(dto.getPsswrd());
        } else {
            transientUser = new User(dto);
        }

        InternalUserData data = new InternalUserData();
        data.setMl(dto.getMl());
        data.setFnm(dto.getFnm());
        data.setLnm(dto.getLnm());
        data.setLcl(dto.getLcl());
        data.setPtn(dto.getOptin());

        log.info(String.format("Registering Web User with email %s ", transientUser.getMl()));
        return registerUser(transientUser, data, RegistrationType.WEBSITE, domainAffiliate, true);
    }

    /**
     * Method description
     *
     * @param list      list
     * @param sendEmail sendEmail
     * @return Return value
     */
    @Override
    public List<User> registerUsers(List<User> list, boolean sendEmail) {
        if ((list != null) && !list.isEmpty()) {
            for (User user : list) {
                InternalUserData data = new InternalUserData();
                registerUser(user, data, RegistrationType.WEBSITE, null, sendEmail);
            }
        }

        return list;
    }

    @Override
    public User registerContentIngestionUser(User user) {
        // encrypt password
        user.setPsswrd(passwordEncryptor.encryptPassword(user.getPsswrd()));

        // persist
        user = userService.saveUser(user);

        return user;
    }

    @Override
    public void removeLoggedInUserProfile(DisableUser du) {
        Principal principal = SpringSecurityHelper.getSecurityContextPrincipal();
        if (principal != null) {
            User user = principal.getUser();
            removeUser(user);
        }
    }

    @PreAuthorize("hasAnyRole('RIGHT_DELETE_USER_AS_ADMIN', 'RIGHT_CONTENT_INGEST')")
    @Override
    public void removeUserProfile(DisableUser du, ObjectId userId) {
        User user = userService.findUser(userId);
        removeUser(user);
    }
    
    public void enhanceProfile(User user, String remoteIPAddress) {
    	if (StringUtils.isEmpty(user.getLgnrmtddrss()) || //There is no recorded IP address
    			(!StringUtils.equals(user.getLgnrmtddrss(), remoteIPAddress)) //OR the recorded IP address is different from the current one    			
    			){
    		UpdateZipEvent uze = new UpdateZipEvent(user, remoteIPAddress);
    		EventHelper.post(uze);
    		
    		UpdateRemoteIPAddressEvent uriae = new UpdateRemoteIPAddressEvent(user, remoteIPAddress);  
    		EventHelper.post(uriae);
    	}
    }

    /**
     * This is the single point at which all Registration Paths MUST come together.
     *
     * @param user      user
     * @param type      type
     * @param sendEmail sendConfirmationEmail
     * @return Return value
     */
    private User registerUser(User user, InternalUserData data, RegistrationType type, AffiliateAccount domainAffiliate, boolean sendEmail) {
        String rawPassword = user.getPsswrd();

        if (StringUtils.isBlank(user.getMl())) {
            throw new IllegalStateException("User is missing an email");
        }

        if (StringUtils.isBlank(rawPassword)) {
            throw new IllegalStateException("User: " + user.getMl() + " is missing a password");
        }

        // Lowercase email
        user.setMl(user.getMl().toLowerCase());
        String email = user.getMl();

        // If there is already a user with this email, throw an exception
        User existingUser = userService.findUserByEmail(email);
        if (existingUser != null) {
            throw new IllegalStateException("User already exists for email: " + email);
        }

        UserSupplement us = null;

        // This is to guarantee that a transient user will not assume an existing User code based on a _lelaCd cookie
        // We need to make sure no User object exists with that user code.  If a User already exists
        // this transient user will be given a new User Code and a new UserSupplement will be
        // created so that the current user's data will not be overwritten
        existingUser = userService.findUserByCode(user.getCd());
        if (existingUser != null) {
            // User already exists, reset the transient user code and create a new user supplement object
            user.setCd(User.generateUserCode());
            us = new UserSupplement();
        } else {
            // User is not registered, look for an existing user supplement object to populate
            us = userService.findUserSupplement(user.getCd());
        }

        // Ensure that a UserSupplement object exists
        if (us == null) {
            us = new UserSupplement();
        }

        // Find the user supplement and set the data
        if (data.getMl() != null) {
            us.setMl(data.getMl().toLowerCase());
        }

        if (data.getFnm() != null) {
            us.setFnm(data.getFnm());
        }

        if (data.getLnm() != null) {
            us.setLnm(data.getLnm());
        }

        if (data.getFllnm() != null) {
            us.setFllnm(data.getFllnm());
        }

        if (data.getLcl() != null) {
            us.setLcl(data.getLcl());
        }

        if (data.getAttrs() != null) {
            us.setAttrs(data.getAttrs());
        }

        if (data.getPtn() != null) {
            us.setPtn(data.getPtn());
        }

        if (us.getLcl() == null) {
            throw new IllegalStateException("User: " + email + " is missing a locale");
        }

        // encrypt password
        user.setPsswrd(passwordEncryptor.encryptPassword(rawPassword));

        // persist user and user supplement
        user = userService.saveUser(user);

        // make sure we link the two together
        us.setCd(user.getCd());

        us = userService.saveUserSupplement(us);

        // Process addresses
        if (data.getDdrss() != null) {
            for (Address address : data.getDdrss()) {
                userService.addOrUpdateAddress(user.getCd(), address);
            }
        }

        log.debug(String.format("Registered user with email %s ", user.getMl()));
        RegistrationEvent registrationEvent = new RegistrationEvent(user, us, type);
        registrationEvent.setRawPassword(rawPassword);
        EventHelper.post(registrationEvent);

        // subscribe to mailing list automatically

        if (us.getPtn() != null && us.getPtn()) {
            // sign up with mail chimp automatically as well
            SubscribeToEmailList subscribeToEmailList = new SubscribeToEmailList(email, mailchimpListId, null);
            userService.subscribeToEmailList(subscribeToEmailList);
        }

        // email welcome email here
        if (sendEmail && (domainAffiliate == null || domainAffiliate.getSndrgcnf())) {
            Locale locale = us.getLcl();

            try {
                Map<MailParameter, Object> params = new HashMap<MailParameter, Object>();
                params.put(MailParameter.PASSWORD, rawPassword);
                params.put(MailParameter.USER_FIRST_NAME, us.getFnm());
                params.put(MailParameter.USER_LAST_NAME, us.getLnm());
                params.put(MailParameter.USER_EMAIL, user.getMl());
                params.put(MailParameter.NEWSLETTER_OPTIN_FLAG, us.getPtn());
                params.put(MailParameter.MAILCHIMP_LIST_ID, this.mailchimpListId);

                switch (type) {
                    case API:
                        mailService.sendApiRegistrationConfirmation(email, params, locale);

                        break;
                    case WEBSITE:
                        mailService.sendRegistrationConfirmation(email, params, locale);

                        break;
                    case FACEBOOK:
                        mailService.sendRegistrationConfirmation(email, params, locale);

                        break;
                }
            } catch (MailServiceException e) {
                log.error(e.getMessage(), e);
            }
        }

        return user;
    }


    private void removeUser(User user) {
        if (user != null) {
            // unsubscribe from mailing list
            UnsubscribeFromEmailList mc = new UnsubscribeFromEmailList(user.getMl(), mailchimpListId);
            userService.unsubscribeFromEmailList(mc);

            facebookUserService.removeFacebookSnapshot(user.getMl());
            userService.removeUser(user);
        } else {
            throw new IllegalArgumentException("User cannot be null");
        }
    }

    public void setMailchimpListId(String value) {
        this.mailchimpListId = value;
    }

    /**
     * Internal DTO
     */
    private class InternalUserData {
        private String ml;
        private String fnm;
        private String lnm;
        private String fllnm;
        private Locale lcl;
        private Map<String, List<String>> attrs;
        private List<Address> ddrss;
        private Boolean ptn;

        public String getMl() {
            return ml;
        }

        public void setMl(String ml) {
            this.ml = ml;
        }

        public String getFnm() {
            return fnm;
        }

        public void setFnm(String fnm) {
            this.fnm = fnm;
        }

        public String getLnm() {
            return lnm;
        }

        public void setLnm(String lnm) {
            this.lnm = lnm;
        }

        public String getFllnm() {
            return fllnm;
        }

        public void setFllnm(String fllnm) {
            this.fllnm = fllnm;
        }

        public Locale getLcl() {
            return lcl;
        }

        public void setLcl(Locale lcl) {
            this.lcl = lcl;
        }

        public Map<String, List<String>> getAttrs() {
            return attrs;
        }

        public void setAttrs(Map<String, List<String>> attrs) {
            this.attrs = attrs;
        }

        public Boolean getPtn() {
            return ptn;
        }

        public void setPtn(Boolean ptn) {
            this.ptn = ptn;
        }

        public List<Address> getDdrss() {
            return ddrss;
        }

        public void setDdrss(List<Address> ddrss) {
            this.ddrss = ddrss;
        }
    }
}
