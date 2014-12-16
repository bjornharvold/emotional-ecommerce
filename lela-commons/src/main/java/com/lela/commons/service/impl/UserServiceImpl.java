/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.commons.service.impl;

//~--- non-JDK imports --------------------------------------------------------

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;

import mailjimp.dom.enums.EmailType;
import mailjimp.service.IMailJimpService;
import mailjimp.service.MailJimpException;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.bson.types.ObjectId;
import org.jasypt.util.password.PasswordEncryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import com.lela.commons.LelaException;
import com.lela.commons.event.DeleteUserEvent;
import com.lela.commons.event.EventHelper;
import com.lela.commons.event.MotivatorEvent;
import com.lela.commons.event.SetAddressEvent;
import com.lela.commons.event.SetAgeEvent;
import com.lela.commons.event.SetGenderEvent;
import com.lela.commons.event.SubscribeEvent;
import com.lela.commons.event.UnsubscribeEvent;
import com.lela.commons.event.UpdateRemoteIPAddressEvent;
import com.lela.commons.excel.UserMotivatorWorkbook;
import com.lela.commons.mail.MailServiceException;
import com.lela.commons.repository.UserRepository;
import com.lela.commons.repository.VerificationRepository;
import com.lela.commons.service.CacheService;
import com.lela.commons.service.ImageUtilityService;
import com.lela.commons.service.MailService;
import com.lela.commons.service.RoleService;
import com.lela.commons.service.UserService;
import com.lela.commons.service.UserSupplementService;
import com.lela.commons.spring.security.SpringSecurityHelper;
import com.lela.domain.ApplicationConstants;
import com.lela.domain.document.Coupon;
import com.lela.domain.document.Event;
import com.lela.domain.document.FunctionalFilterPreset;
import com.lela.domain.document.Motivator;
import com.lela.domain.document.QUser;
import com.lela.domain.document.Reward;
import com.lela.domain.document.Role;
import com.lela.domain.document.Social;
import com.lela.domain.document.User;
import com.lela.domain.document.UserAnswer;
import com.lela.domain.document.UserAssociation;
import com.lela.domain.document.UserEvent;
import com.lela.domain.document.UserSupplement;
import com.lela.domain.document.UserVerification;
import com.lela.domain.dto.Principal;
import com.lela.domain.dto.Profile;
import com.lela.domain.dto.SubscribeToEmailList;
import com.lela.domain.dto.UnsubscribeFromEmailList;
import com.lela.domain.dto.UserAccountDto;
import com.lela.domain.dto.UserAttributes;
import com.lela.domain.dto.report.UserMotivatorWorkbookData;
import com.lela.domain.dto.report.UserUserSupplementEntry;
import com.lela.domain.dto.user.Address;
import com.lela.domain.dto.user.ProfilePictureUpload;
import com.lela.domain.dto.user.UserSearchQuery;
import com.lela.domain.enums.CacheType;
import com.lela.domain.enums.Gender;
import com.lela.domain.enums.MailParameter;
import com.lela.domain.enums.MotivatorSource;
import com.lela.util.utilities.number.NumberUtils;
import com.lela.util.utilities.storage.dto.ImageDigest;

//~--- JDK imports ------------------------------------------------------------

//~--- classes ----------------------------------------------------------------

/**
 * Created by Bjorn Harvold
 * Date: 6/19/11
 * Time: 3:59 PM
 * Responsibility:
 */
@Service("userService")
public class UserServiceImpl extends AbstractCacheableService implements UserService {
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private static final int DEFAULT_CHUNK_SIZE = 100;

    //~--- fields -------------------------------------------------------------

    /**
     * Field description
     */
    @Value("${application.context}")
    private String context;

    /**
     * Field description
     */
    @Value("${server.port}")
    private Integer port;

    /**
     * Field description
     */
    @Value("${server.protocol}")
    private String protocol;

    /**
     * Field description
     */
    @Value("${server.name}")
    private String server;

    @Value("${mailchimp.list.id}")
    private String mailchimpListId;

    @Value("${amazon.access.key}")
    private String accessKey;

    @Value("${amazon.secret.key}")
    private String secretKey;

    @Value("${profile.images.amazon.bucket}")
    private String profileImagesBucketName;

    /**
     * Field description
     */
    private final MailService mailService;

    private final PasswordEncryptor passwordEncryptor;

    private final UserRepository userRepository;

    private final VerificationRepository verificationRepository;

    private final IMailJimpService mailChimpService;

    private final SessionRegistry sessionRegistry;

    private final RoleService roleService;

    private final UserSupplementService userSupplementService;

    private final ImageUtilityService utilityService;

    //~--- constructors -------------------------------------------------------

    /**
     * Constructs ...
     *
     * @param cacheService          cacheService
     * @param userRepository        userRepository
     * @param passwordEncryptor     passwordEncryptor
     * @param mailService           mailService
     * @param mailChimpService      mailChimpService
     * @param sessionRegistry       sessionRegistry
     * @param roleService           roleService
     * @param userSupplementService userSupplementService
     * @param utilityService        utilityService
     */
    @Autowired
    public UserServiceImpl(CacheService cacheService,
                           UserRepository userRepository,
                           PasswordEncryptor passwordEncryptor,
                           MailService mailService,
                           IMailJimpService mailChimpService,
                           SessionRegistry sessionRegistry,
                           VerificationRepository verificationRepository,
                           RoleService roleService,
                           UserSupplementService userSupplementService,
                           ImageUtilityService utilityService) {
        super(cacheService);

        this.userRepository = userRepository;
        this.passwordEncryptor = passwordEncryptor;
        this.mailService = mailService;
        this.mailChimpService = mailChimpService;
        this.sessionRegistry = sessionRegistry;
        this.verificationRepository = verificationRepository;
        this.roleService = roleService;
        this.userSupplementService = userSupplementService;
        this.utilityService = utilityService;
    }

    //~--- methods ------------------------------------------------------------

    /**
     * Method description
     *
     * @param id id
     * @return Return value
     */
    @Override
    public User findUser(ObjectId id) {
        return findUser(id, true);
    }

    /**
     * Method description
     *
     * @param id     id
     * @param enrich enrich
     * @return Return value
     */
    @Override
    public User findUser(ObjectId id, boolean enrich) {
        User result = userRepository.findOne(id);

        if ((result != null) && enrich) {
            enrichUser(result);
        }

        return result;
    }

    /**
     * Method description
     *
     * @param userCode checksum
     * @return Return value
     */
    @Override
    public User findUserByCode(String userCode) {
        User result = null;

        Cache.ValueWrapper wrapper = retrieveFromCache(ApplicationConstants.USER_CACHE, userCode);

        if (wrapper != null && wrapper.get() != null && wrapper.get() instanceof User) {
            result = (User) wrapper.get();
        } else {
            result = userRepository.findByCode(userCode);

            if (result != null) {
                putInCache(ApplicationConstants.USER_CACHE, userCode, result);
            }
        }

        return result;
    }

    @Override
    public Gender saveGender(String userCode, Gender gender) {
        userSupplementService.saveGender(userCode, gender);

        // Notify kissmetrics
        EventHelper.post(new SetGenderEvent(findUserByCode(userCode), gender));
        return gender;
    }

    @Override
    public void saveAge(String userCode, Integer age) {
        userSupplementService.saveAge(userCode, age);
        EventHelper.post(new SetAgeEvent(findUserByCode(userCode), age));
    }

    @Override
    public Address addOrUpdateAddress(String userCode, Address address) {
        address = userSupplementService.addOrUpdateAddress(userCode, address);
        EventHelper.post(new SetAddressEvent(findUserByCode(userCode), address));

        return address;
    }

    @Override
    public Map<String, List<String>> findUserAttributes(String userCode) {
        return userSupplementService.findUserAttributes(userCode);
    }

    @Override
    public List<UserEvent> findUserEvents(String userCode) {
        UserSupplement us = findUserSupplement(userCode);

        return us.getVnts();
    }

    @Override
    public List<Reward> findRewards(String userCode) {
        UserSupplement us = findUserSupplement(userCode);

        return us.getRwrds();
    }

    @Override
    public void removeUserSupplement(String userCode) {
        userSupplementService.removeUserSupplement(userCode);
    }

    @Override
    public List<UserUserSupplementEntry> findsUsersForCampaignReport(String campaignUrlName) {
        List<UserSupplement> list = userSupplementService.findUserSupplementsForCampaignReport(campaignUrlName);

        return populateUserUserSupplementEntriesFromUserSupplements(list);
    }

    @Override
    public List<UserUserSupplementEntry> findsUsersForAffiliateReport(String affiliateUrlName) {
        List<UserSupplement> list = userSupplementService.findUserSupplementsForAffiliateReport(affiliateUrlName);

        return populateUserUserSupplementEntriesFromUserSupplements(list);
    }

    @Override
    public List<UserUserSupplementEntry> findUsersForEventReport(String eventUrlName) {
        List<UserSupplement> list = userSupplementService.findUserSupplementsForEventReport(eventUrlName);

        return populateUserUserSupplementEntriesFromUserSupplements(list);
    }

    private List<UserUserSupplementEntry> populateUserUserSupplementEntriesFromUserSupplements(List<UserSupplement> list) {
        List<UserUserSupplementEntry> result = null;
        if (list != null && !list.isEmpty()) {
            List<User> users = findUsersFromSupplements(list);

            if (users != null && !users.isEmpty()) {

                result = new ArrayList<UserUserSupplementEntry>(users.size());

                for (User user : users) {
                    for (UserSupplement us : list) {
                        if (StringUtils.equals(user.getCd(), us.getCd())) {
                            result.add(new UserUserSupplementEntry(user, us));
                        }
                    }
                }
            }
        }
        return result;
    }

    private List<UserUserSupplementEntry> populateUserUserSupplementEntriesFromUsers(List<User> list) {
    	return populateUserUserSupplementEntriesFromUsers(list, null);
    }

    private List<UserUserSupplementEntry> populateUserUserSupplementEntriesFromUsers(List<User> list, List<String> fieldsOnUserSupplementList) {
        List<UserUserSupplementEntry> result = null;
        if (list != null && !list.isEmpty()) {
        	List<UserSupplement> userSupplements = null;
        	if (fieldsOnUserSupplementList == null){
        		userSupplements = findUserSupplementsFromUsers(list, null);
        	} else {
        		userSupplements = findUserSupplementsFromUsers(list, fieldsOnUserSupplementList);
        	}
            if (userSupplements != null && !userSupplements.isEmpty()) {

                result = new ArrayList<UserUserSupplementEntry>(userSupplements.size());

                for (User user : list) {
                    for (UserSupplement us : userSupplements) {
                        if (StringUtils.equals(user.getCd(), us.getCd())) {
                            result.add(new UserUserSupplementEntry(user, us));
                        }
                    }
                }
            }
        }
        return result;
    }

    /**
     * Method description
     *
     * @param email email
     * @return Return value
     */
    @Override
    public User findUserByEmail(String email) {
        User result = null;

        if (StringUtils.isNotBlank(email)) {
            result = userRepository.findOne(QUser.user.ml.equalsIgnoreCase(email.trim()));
            if (result != null) {
                enrichUser(result);
            }
        }

        return result;
    }

    /**
     * Method description
     *
     * @param email email
     * @return Return value
     */
    @Override
    public UserSupplement findUserSupplementByEmail(String email) {
        return userSupplementService.findUserSupplementByEmail(email);
    }

    @Override
    public List<User> findUserIdsBySocialNetwork(String providerId, String providerUserId) {
        List<UserSupplement> list = userSupplementService.findUserIdsBySocialNetwork(providerId, providerUserId);

        return findUsersFromSupplements(list, null);
    }

    /**
     * @param list list
     * @return users
     */
    private List<User> findUsersFromSupplements(List<UserSupplement> list, List<String> fields) {
        List<String> userCodes = getUserCodesFromSupplement(list);

        return userRepository.findByCodes(userCodes, fields);
    }

    private List<UserSupplement> findUserSupplementsFromUsers(List<User> list, List<String> fieldsOnUserSupplementList) {
        List<String> userCodes = getUserCodesFromUser(list);

        return userSupplementService.findUserSupplementsByCodes(userCodes, fieldsOnUserSupplementList);
    }

    /**
     * @param list list
     * @return users
     */
    private List<User> findUsersFromSupplements(List<UserSupplement> list) {
        return findUsersFromSupplements(list, null);
    }

    /**
     * @param us us
     * @return users
     */
    private User findUserFromSupplement(UserSupplement us) {
        return userRepository.findByCode(us.getCd());
    }

    /**
     * Method description
     *
     * @param providerId      providerId
     * @param providerUserIds providerUserIds
     * @return Return value
     */
    @Override
    public List<User> findUserIdsConnectedToSocialNetwork(String providerId, Set<String> providerUserIds) {
        List<UserSupplement> list = userSupplementService.findUserIdsConnectedToSocialNetwork(providerId, providerUserIds);

        return findUsersFromSupplements(list);
    }

    /**
     * Find ALL users for social network
     *
     * @param providerId Social network provider id
     * @return list of users
     */
    @Override
    public List<User> findUsersBySocialNetwork(String providerId) {
        List<UserSupplement> list = userSupplementService.findUsersBySocialNetwork(providerId);

        return findUsersFromSupplements(list);
    }

    /**
     * Method description
     *
     * @param page       page
     * @param maxResults maxResults
     * @return Return value
     */
    @PreAuthorize("hasAnyRole('RIGHT_READ_USER_AS_ADMIN')")
    @Override
    public List<UserUserSupplementEntry> findUsers(Integer page, Integer maxResults, List<String> fields) {
        List<User> list = userRepository.findUsers(page, maxResults, fields);

        return populateUserUserSupplementEntriesFromUsers(list);
    }
    
    @PreAuthorize("hasAnyRole('RIGHT_READ_USER_AS_ADMIN')")
    @Override
    public List<UserUserSupplementEntry> findUsers(Integer page, Integer maxResults, List<String> fieldsOnUser, List<String> fieldsOnUserSupplement){
        List<User> list = userRepository.findUsers(page, maxResults, fieldsOnUser);

        return populateUserUserSupplementEntriesFromUsers(list, fieldsOnUserSupplement);
    }

    @PreAuthorize("hasAnyRole('RIGHT_READ_USER_AS_ADMIN')")
    @Override
    public List<UserUserSupplementEntry> findUsersByQuery(UserSearchQuery query) {
        List<UserSupplement> list = userSupplementService.findUserSupplementsByQuery(query);

        return populateUserUserSupplementEntriesFromUserSupplements(list);
    }

    @PreAuthorize("hasAnyRole('RIGHT_READ_USER_AS_ADMIN')")
    @Override
    public Long findFacebookUserCount(UserSearchQuery query) {
        List<UserSupplement> users = userSupplementService.findUserSupplementsByQuery(query);
        List<String> userCodes = getUserCodesFromUserSupplements(users);
        return userSupplementService.findFacebookUserCount(userCodes);
    }

    @PreAuthorize("hasAnyRole('RIGHT_READ_USER_AS_ADMIN')")
    @Override
    public Long findFacebookUserCount() {
        return userSupplementService.findFacebookUserCount(null);
    }

    //~--- get methods --------------------------------------------------------

    /**
     * Method description
     *
     * @param encryptedPassword encryptedPassword
     * @param rawPassword       rawPassword
     * @return Return value
     */
    @Override
    public Boolean isValid(String encryptedPassword, String rawPassword) {
        return passwordEncryptor.checkPassword(rawPassword, encryptedPassword);
    }

    //~--- methods ------------------------------------------------------------

    /**
     * Method description
     *
     * @param email email
     * @return Return value
     * @throws UsernameNotFoundException UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User result = findUserByEmail(email.trim());

        if (result == null) {
            throw new UsernameNotFoundException(email);
        }

        return new Principal(result);
    }

    /**
     * WARNING!!! This will remove the User and UserSupplement entries, but will not remove the full
     * user profile including Facebook Snapshots and unsubscribing from mail lists.
     *
     * @see ProfileService.removeUserProfile()
     */
    //@PreAuthorize("hasAnyRole('RIGHT_DELETE_USER_AS_ADMIN', 'RIGHT_CONTENT_INGEST')")
    @Override
    public void removeUser(User user) {
        if (user.getId() != null) {
            userRepository.delete(user);
            EventHelper.post(new DeleteUserEvent(user));

            removeFromCache(CacheType.USER, user.getCd());
        }

        removeUserSupplement(user.getCd());
    }

    /**
     * WARNING!!! This will remove the User and UserSupplement entries, but will not remove the full
     * user profile including Facebook Snapshots and unsubscribing from mail lists.
     *
     * @see ProfileService.removeUserProfile()
     */
    @PreAuthorize("hasAnyRole('RIGHT_DELETE_USER_AS_ADMIN', 'RIGHT_CONTENT_INGEST')")
    @Override
    public void removeUser(String userCode) {
        User user = findUserByCode(userCode);

        if (user != null) {
            userRepository.delete(user);
            EventHelper.post(new DeleteUserEvent(user));

            removeFromCache(CacheType.USER, user.getCd());
        }

        removeUserSupplement(userCode);
    }

    /**
     * Method description
     *
     * @param email email
     */
    @Override
    public Boolean resetPassword(String email) {
        Boolean result = false;

        User user = findUserByEmail(email);
        UserSupplement us = findUserSupplementByEmail(email);

        if (user != null && us != null) {
            String password = new BigInteger(130, new SecureRandom()).toString(32);
            Locale locale = us.getLcl();

            // send email
            try {
                mailService.sendPasswordResetEmail(email, password, locale);
            } catch (MailServiceException e) {
                log.error(e.getMessage(), e);
            }

            // encrypt password
            user.setPsswrd(passwordEncryptor.encryptPassword(password));

            // update user
            userRepository.save(user);

            removeFromCache(CacheType.USER, user.getCd());

            result = true;
        }

        return result;
    }

    private UserSupplement updateEmailSubscription(UserSupplement us) {
        //Once the user is saved, subscribe or unsubscribe him
        if (us.getPtn() != null && us.getPtn()) {
            SubscribeToEmailList subscribeToEmailList = new SubscribeToEmailList(us.getMl(), mailchimpListId, null);
            this.subscribeToEmailList(subscribeToEmailList);

            if (log.isDebugEnabled()) {
                log.debug(String.format("Subscribed user with email %s from the mailing list %s", us.getMl(), mailchimpListId));
            }
        } else {
            UnsubscribeFromEmailList mc = new UnsubscribeFromEmailList(us.getMl(), mailchimpListId);
            this.unsubscribeFromEmailList(mc);

            if (log.isDebugEnabled()) {
                log.debug(String.format("Unsubscribed user with email %s from the mailing list %s", us.getMl(), mailchimpListId));
            }
        }

        return us;
    }

    /**
     * Updates account related info
     * @param user user
     * @param userAccountDto userAccountDto
     * @return UserSupplement
     */
    @Override
    public UserSupplement updateUserAccount(User user, UserAccountDto userAccountDto) {
        UserSupplement us = findUserSupplement(user.getCd());
        boolean dirty = false;
        Boolean updateEmailSubscription = us.getPtn() == null || !us.getPtn().equals(userAccountDto.getOptin());

        us.updateUserAccount(userAccountDto);

        // see if email subscription needs an update
        if (updateEmailSubscription) {
            updateEmailSubscription(us);
        }

        if (StringUtils.isNotBlank(userAccountDto.getPsswrd())) {
            String encryptedPassword = encryptPassword(userAccountDto.getPsswrd());
            user.setPsswrd(encryptedPassword);

            dirty = true;
        }

        // update email (username)
        if (StringUtils.isNotBlank(userAccountDto.getMl()) && !StringUtils.equals(user.getMl(), userAccountDto.getMl())) {
            user.setMl(userAccountDto.getMl());

            dirty = true;
        }

        if (dirty) {
            updateUserAndRefreshSecurityContext(user);
        }

        return saveUserSupplement(us);
    }

    /**
     * Method description
     *
     * @param user user
     * @return Return value
     */
    @Override
    public User saveUser(User user) {

        // add default role for user
        addApplicationUserRoleIfEmpty(user);

        // if this doesn't have a password, we will disable her for now
        if (StringUtils.isBlank(user.getPsswrd())) {
            user.setNbld(false);
        }

        // persist
        user = userRepository.save(user);

        removeFromCache(CacheType.USER, user.getCd());

        return user;
    }

    /**
     * Method description
     *
     * @param password password
     * @return Return value
     */
    @Override
    public String encryptPassword(String password) {
        return passwordEncryptor.encryptPassword(password);
    }

    /**
     * Method description
     *
     * @param list list
     * @return Return value
     */
    @Override
    public List<User> saveUsers(List<User> list) {
        if ((list != null) && !list.isEmpty()) {
            for (User user : list) {

                // we need to check that we aren't inserting duplicate users
                User tmp = findUserByEmail(user.getMl());

                if (tmp == null) {
                    saveUser(user);
                } else {
                    log.warn("Trying to insert duplicate user with email: " + user.getMl());
                }
            }
        }

        return list;
    }

    @Override
    public void recomputeFriendshipLevel(String userCode) {
        userSupplementService.recomputeFriendshipLevel(userCode);
    }

    @Override
    public List<UserUserSupplementEntry> findUsersWithAlerts() {
        List<UserSupplement> list = userSupplementService.findUsersWithAlerts();

        return populateUserUserSupplementEntriesFromUserSupplements(list);
    }

    /**
     * Method description
     *
     * @param user user
     */
    private void addApplicationUserRoleIfEmpty(User user) {
        if (user.getRrlnms().isEmpty()) {
            user.getRrlnms().add(ApplicationConstants.APPLICATION_USER_ROLE);
        }
    }

    /**
     * Method description
     *
     * @param user user
     */
    private void enrichUser(User user) {
        if (log.isDebugEnabled()) {
            log.debug("Enriching user...");
        }

        if (user != null) {
            if (user.getRrlnms() != null) {
                for (String urlName : user.getRrlnms()) {
                    Role role = roleService.findRoleByUrlName(urlName);

                    if (role != null) {
                        // add it to object
                        user.getRoles().add(role);
                    } else {
                        log.warn("There is no reason why this role is not available. Url name: " + urlName);
                    }
                }
            }
        }

        if (log.isDebugEnabled()) {
            log.debug("Enriching user complete");
        }
    }
    @Async
    @Override
    public Future<String> subscribeToEmailList(SubscribeToEmailList dto) {

        if (StringUtils.isBlank(dto.getEmail()) || StringUtils.isBlank(dto.getListId())) {
            dto.setMessage(ApplicationConstants.FAILURE);
        } else {
            // Verify the user exists
            User user = findUserByEmail(dto.getEmail());
            if (user != null) {
                try {
                    mailChimpService.listSubscribe(dto.getListId(), dto.getEmail(), dto.getMergeVars(), EmailType.HTML, false, true, false, false);
                    dto.setMessage(ApplicationConstants.SUCCESS);

                    EventHelper.post(new SubscribeEvent(user, dto.getListId()));
                } catch (MailJimpException ex) {
                    if (log.isWarnEnabled()) {
                        log.warn(ex.getMessage(), ex);
                    }
                    dto.setMessage(ApplicationConstants.FAILURE);
                }
            } else {
                dto.setMessage(ApplicationConstants.FAILURE);
            }
        }

        return new AsyncResult<String>(dto.getMessage());
    }

    @Async
    @Override
    public Future<String> unsubscribeFromEmailList(UnsubscribeFromEmailList dto) {
        if (StringUtils.isBlank(dto.getEmail()) || StringUtils.isBlank(dto.getListId())) {
            dto.setMessage(ApplicationConstants.FAILURE);
        } else {
            try {
                mailChimpService.listUnsubscribe(dto.getListId(), dto.getEmail(), true, false, false);
                dto.setMessage(ApplicationConstants.SUCCESS);

                // Try to find the user to send an event
                User user = findUserByEmail(dto.getEmail());
                if (user != null) {
                    EventHelper.post(new UnsubscribeEvent(user, dto.getListId()));
                }
            } catch (Exception ex) {
                if (log.isWarnEnabled()) {
                    log.warn(ex.getMessage(), ex);
                }
                dto.setMessage(ApplicationConstants.FAILURE);
            }
        }

        return new AsyncResult<String>(dto.getMessage());
    }

    @Override
    public void autoLogin(ObjectId userId, String remoteAddress) {
        User user = findUser(userId, true);

        // Save the user login time and date
        user.setLgndt(new Date());
        user.setLgnrmtddrss(remoteAddress);
        user = saveUser(user);

        // set user in secure context
        Principal principal = new Principal(user);
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(principal, principal.getPassword(), principal.getAuthorities()));
    }

    /**
     * TODO still not being used but could be nifty in the admin ui to see who is logged in
     *
     * @return
     */
    private List<Principal> getAllAuthenticatedUsers() {
        List<Principal> result = null;

        List<Object> principals = sessionRegistry.getAllPrincipals();

        if (principals != null && !principals.isEmpty()) {
            for (Object principal : principals) {
                if (principal instanceof Principal) {
                    if (result == null) {
                        result = new ArrayList<Principal>();
                    }

                    result.add((Principal) principal);
                }
            }
        }

        return result;
    }

    /**
     * Convenience method for anyone wishing to keep both the
     * user and her's security context in sync
     *
     * @param user user
     * @return user
     */
    @Override
    public User updateUserAndRefreshSecurityContext(User user) {

        if (user.getId() != null) {
            // only update principal if there already is a principal
            Principal principal = SpringSecurityHelper.getSecurityContextPrincipal();

            if (principal != null) {
                // update security context
                SpringSecurityHelper.secureChannel(new Principal(user));
            }

            // save to db
            saveUser(user);
        }

        return user;
    }

    @Override
    public boolean shouldUserSeeRecommendedProducts(String userCode) {
        return userSupplementService.shouldUserSeeRecommendedProducts(userCode);
    }

    @Override
    public Motivator findBestMotivator(String userCode) {
        return userSupplementService.findBestMotivator(userCode);
    }

    @Override
    public Motivator findMotivator(String userCode, MotivatorSource type) {
        return userSupplementService.findMotivator(userCode, type);
    }

    @Override
    public List<UserAnswer> saveUserAnswers(String userCode, List<UserAnswer> list) {
        return userSupplementService.saveUserAnswers(userCode, list);
    }

    @Override
    public List<UserAnswer> findUserAnswers(String userCode) {
        return userSupplementService.findUserAnswers(userCode);
    }

    @Override
    public Motivator saveMotivator(String userCode, Motivator motivator) {
        Motivator result = userSupplementService.saveMotivator(userCode, motivator);

        EventHelper.post(new MotivatorEvent(findUserByCode(userCode), motivator));

        return result;
    }

    @Override
    public List<FunctionalFilterPreset> findFunctionalFilterPresets(String userCode) {
        return userSupplementService.findFunctionalFilterPresets(userCode);
    }

    @Override
    public List<FunctionalFilterPreset> saveFunctionalFilterPresets(String userCode, List<FunctionalFilterPreset> filterPresets) {
        return userSupplementService.saveFunctionalFilterPresets(userCode, filterPresets);
    }

    @Override
    public List<UserAssociation> findUserAssociations(String userCode) {
        return userSupplementService.findUserAssociations(userCode);
    }

    @Override
    public List<UserAssociation> saveUserAssociations(String userCode, List<UserAssociation> associations) {
        return userSupplementService.saveUserAssociations(userCode, associations);
    }

    @Override
    public List<Coupon> saveCoupons(String userCode, List<Coupon> coupons) {
        return userSupplementService.saveCoupons(userCode, coupons);
    }

    @Override
    public List<Social> saveSocials(String userCode, List<Social> socials) {
        return userSupplementService.saveSocials(userCode, socials);
    }

    @Override
    public Long findUserCountByCouponCode(String couponCode) {
        return userSupplementService.findUserCountByCouponCode(couponCode);
    }

    @Override
    public UserSupplement findUserSupplementByCouponCode(String couponCode) {
        return userSupplementService.findUserSupplementByCouponCode(couponCode);
    }

    @Override
    public Coupon findCouponByCouponCode(String couponCode) {
        return userSupplementService.findCouponByCouponCode(couponCode);
    }

    @Override
    public List<Coupon> findCouponsByOfferUrlName(String offerUrlName) {
        return userSupplementService.findCouponsByOfferUrlName(offerUrlName);
    }

    @Override
    public Long findUserByCouponOfferUrlNameCount(String offerUrlName) {
        return userSupplementService.findUserSupplementByOfferUrlNameCount(offerUrlName);
    }

    @Override
    public List<Coupon> findCoupons(String userCode) {
        return userSupplementService.findCoupons(userCode);
    }

    @Override
    public List<Social> findSocials(String userCode) {
        return userSupplementService.findSocials(userCode);
    }

    @Override
    public UserSupplement findUserSupplement(String userCode) {
        return userSupplementService.findUserSupplement(userCode);
    }

    @Override
    public UserSupplement findUserSupplement(String userCode, List<String> fields) {
        return userSupplementService.findUserSupplement(userCode, fields);
    }

    /**
     * Upload the user picture to s3 to users profile in 2 sizes
     *
     * @param userCode userCode
     * @param picture  picture
     */
    @Override
    public void updateProfilePicture(String userCode, ProfilePictureUpload picture) {

        try {
            if (StringUtils.isNotBlank(userCode)) {
                UserSupplement us = findUserSupplement(userCode);

                if (picture.getMultipartFile() != null && !picture.getMultipartFile().isEmpty()) {
                    MultipartFile file = picture.getMultipartFile();
                    ImageDigest id = utilityService.ingestImage(accessKey, secretKey, profileImagesBucketName, null, us.getCd(), file.getInputStream(), new Integer[]{50, 200}, -1);

                    // we have to convert the integer keys to strings because JSPs cann't access Integer keys because they treat it as a long
                    if (id.getImageUrls() != null && !id.getImageUrls().isEmpty()) {
                        for (Map.Entry<Integer, String> entry : id.getImageUrls().entrySet()) {
                            if (us.getMg() == null) {
                                us.setMg(new HashMap<String, String>());
                            }
                            us.getMg().put(entry.getKey().toString(), entry.getValue());
                        }
                    }

                    saveUserSupplement(us);
                }
            } else {
                if (log.isErrorEnabled()) {
                    log.error("User is not persisted and cannot have a profile picture saved to her. User code: " + userCode);
                }
            }
        } catch (Exception e) {
            throw new LelaException(e.getMessage(), e);
        }
    }

    @Override
    public void updateProfile(String userCode, Profile profile) {
        UserSupplement us = findUserSupplement(userCode);
        us.updateProfile(profile);

        saveUserSupplement(us);
    }

    @Override
    public UserSupplement saveUserSupplement(UserSupplement us) {
        return userSupplementService.saveUserSupplement(us);
    }

    private void sendEventRegistrationConfirmation(UserSupplement us, UserEvent userEvent, Event event) {
        try {
            // and now we send out confirmation email
            Map<MailParameter, Object> map = new HashMap<MailParameter, Object>();
            map.put(MailParameter.EVENT_NAME, event.getNm());
            map.put(MailParameter.USER_FIRST_NAME, us.getFnm());
            map.put(MailParameter.USER_LAST_NAME, us.getLnm());
            map.put(MailParameter.URL_NAME, event.getRlnm());
            map.put(MailParameter.START_DATE, event.getStrtdt());
            map.put(MailParameter.END_DATE, event.getNddt());
            map.put(MailParameter.USER_ATTRIBUTES, userEvent.getAttrs());

            mailService.sendEventSignUpConfirmation(us.getMl(), map, us.getLcl());
        } catch (MailServiceException e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public void verifyEmail(String tkn) throws IllegalAccessException {
        UserVerification verification = this.verificationRepository.findByToken(tkn);
        if (verification == null) {
            throw new IllegalAccessException("Token not found.");
        } else {
            UserSupplement us = findUserSupplementByEmail(verification.getMl());
            if (us != null) {
                if (StringUtils.equals(us.getMl(), verification.getMl())) {
                    us.setVrfd(Boolean.TRUE);

                    saveUserSupplement(us);

                    verification.setVrfdt(new Date());
                    verificationRepository.save(verification);
                }
            } else {
                throw new IllegalAccessException("Token can only verify current email address.");
            }

        }
    }

    @Override
    public UserEvent saveUserEvent(String userCode, Event event, String urlName, Map params) throws IllegalAccessException {
        return userSupplementService.saveUserEvent(userCode, event, urlName, params);
    }

    @Override
    public List<UserSupplement> findUsersByEventUrlName(String urlName) {
        return userSupplementService.findByEventUrlName(urlName);
    }

    @Override
    public List<User> findUsersByCampaignUrlName(String urlName) {
        List<UserSupplement> list = userSupplementService.findByCampaignUrlName(urlName);

        return findUsersFromSupplements(list);
    }

    @Override
    public List<User> findUsersByAffiliateAccountUrlName(String urlName) {
        List<UserSupplement> list = userSupplementService.findByAffiliateAccountUrlName(urlName);

        return findUsersFromSupplements(list);
    }

    @Override
    public List<UserUserSupplementEntry> findAllUsers(Integer chunk, List<String> fields) {
        List<UserUserSupplementEntry> result = null;
        Long count;
        Integer iterations;
        count = findUserCount();

        if (count != null && count > 0) {
            result = new ArrayList<UserUserSupplementEntry>(count.intValue());
            iterations = NumberUtils.calculateIterations(count, chunk.longValue());

            // load up items in a paginated fashion from mongo
            for (int i = 0; i < iterations; i++) {
                result.addAll(findUsers(i, chunk.intValue(), fields));
            }
        }

        return result;
    }

    private List<UserUserSupplementEntry> findUserMotivatorWorkbookDataEntries(Integer chunk, List<String> fields) {
        List<UserUserSupplementEntry> result = null;
        Long count;
        Integer iterations;
        count = findUserCount();

        if (count != null && count > 0) {
            result = new ArrayList<UserUserSupplementEntry>(count.intValue());
            iterations = NumberUtils.calculateIterations(count, chunk.longValue());

            // load up items in a paginated fashion from mongo
            for (int i = 0; i < iterations; i++) {
                List<UserUserSupplementEntry> users = findUsersMotivatorWorkbookDataEntry(i, chunk, fields);

                if (users != null && !users.isEmpty()) {
                    result.addAll(users);
                }
            }
        }

        return result;
    }

    /**
     * Looks for user supplement objects that have motivators on them
     *
     * @param page       page
     * @param maxResults maxResults
     * @return Return value
     */
    private List<UserUserSupplementEntry> findUsersMotivatorWorkbookDataEntry(Integer page, Integer maxResults, List<String> fields) {
        List<UserUserSupplementEntry> result = null;

        List<UserSupplement> userSupplements = userSupplementService.findUsersWithMotivators(page, maxResults, fields);

        if (userSupplements != null && !userSupplements.isEmpty()) {
            result = new ArrayList<UserUserSupplementEntry>();
            List<User> users = findUsersFromSupplements(userSupplements, fields);

            for (User user : users) {
                for (UserSupplement userSupplement : userSupplements) {
                    if (StringUtils.equals(userSupplement.getCd(), user.getCd())) {
                        result.add(new UserUserSupplementEntry(user, userSupplement));
                    }
                }

            }
        }

        return result;
    }

    private List<String> getUserCodesFromSupplement(List<UserSupplement> list) {
        List<String> result = null;

        if (list != null && !list.isEmpty()) {
            result = new ArrayList<String>(list.size());

            for (UserSupplement us : list) {
                result.add(us.getCd());
            }
        }

        return result;
    }

    private List<String> getUserCodesFromUser(List<User> list) {
        List<String> result = null;

        if (list != null && !list.isEmpty()) {
            result = new ArrayList<String>(list.size());

            for (User us : list) {
                result.add(us.getCd());
            }
        }

        return result;
    }

    private List<String> getUserCodesFromUserSupplements(List<UserSupplement> list) {
        List<String> result = null;

        if (list != null && !list.isEmpty()) {
            result = new ArrayList<String>(list.size());

            for (UserSupplement us : list) {
                result.add(us.getCd());
            }
        }

        return result;
    }

    @Override
    public Long findUserCount() {
        return userRepository.count();
    }

    @Override
    public Long findUserCount(UserSearchQuery query) {
        return userSupplementService.findUserSupplementCountByQuery(query);
    }

    @PreAuthorize("hasAnyRole('RIGHT_REPORT_GENERATION')")
    @Override
    public Workbook generateUserMotivatorReport() {
        Workbook result = null;

        List<String> fields = new ArrayList<String>(5);
        fields.add("fnm");
        fields.add("lnm");
        fields.add("ml");
        fields.add("gndr");
        fields.add("cd");
        fields.add("mtvtrmp");

        List<UserUserSupplementEntry> entries = findUserMotivatorWorkbookDataEntries(100, fields);

        if (entries != null && !entries.isEmpty()) {

            UserMotivatorWorkbookData data = new UserMotivatorWorkbookData(entries);

            // retrieve all motivators for users
            UserMotivatorWorkbook workbook = new UserMotivatorWorkbook(data);

            result = workbook.generate();
        }

        return result;
    }

    @Override
    public List<String> motivatorLocalizedKeys(User user) {
        return userSupplementService.motivatorLocalizedKeys(user.getCd());
    }

    @Override
    public void processUserAttributes(String userCode, UserAttributes userAttributes) {
        userSupplementService.processUserAttributes(userCode, userAttributes);
    }

    @Override
    public List<User> findUsersCreatedBetween(Date startDate, Date endDate) {
        List<User> result = null;
        Long count;
        Integer iterations;
        count = findUserCount();

        if (count != null && count > 0) {
            result = new ArrayList<User>(count.intValue());
            iterations = NumberUtils.calculateIterations(count, (long)DEFAULT_CHUNK_SIZE);

            // load up items in a paginated fashion from mongo
            for (int i = 0; i < iterations; i++) {
                result.addAll(userRepository.findUsersCreatedBetween(startDate, endDate, i, DEFAULT_CHUNK_SIZE, null));
            }
        }

        return result;
    }
    
    @Override
    @Subscribe
    @AllowConcurrentEvents
    public void updateLastLoginRemoteAddress(UpdateRemoteIPAddressEvent uriae){ 
    	User user = uriae.getUser();
    	User enrichedUser = this.findUser(user.getId(), true);
    	if (!StringUtils.isEmpty(uriae.getRemoteIPAddress())) {
	    	enrichedUser.setLgnrmtddrss(uriae.getRemoteIPAddress());
	    	this.saveUser(enrichedUser);
    	}
    }

    public static void main(String[] args) {
        String password = new BigInteger(160, new SecureRandom()).toString(32);

        System.out.println(password);
    }
}
