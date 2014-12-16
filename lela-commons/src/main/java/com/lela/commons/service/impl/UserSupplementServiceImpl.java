/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.commons.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.stereotype.Service;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import com.lela.commons.comparator.MotivatorLocalizedKeysComparator;
import com.lela.commons.event.UpdateZipEvent;
import com.lela.commons.repository.UserSupplementRepository;
import com.lela.commons.service.CacheService;
import com.lela.commons.service.LelaMonitoringService;
import com.lela.commons.service.UserSupplementService;
import com.lela.commons.service.ValidatorService;
import com.lela.domain.ApplicationConstants;
import com.lela.domain.document.Coupon;
import com.lela.domain.document.Event;
import com.lela.domain.document.FunctionalFilterPreset;
import com.lela.domain.document.Motivator;
import com.lela.domain.document.QUserSupplement;
import com.lela.domain.document.Social;
import com.lela.domain.document.User;
import com.lela.domain.document.UserAnswer;
import com.lela.domain.document.UserAssociation;
import com.lela.domain.document.UserEvent;
import com.lela.domain.document.UserSupplement;
import com.lela.domain.dto.UserAttributes;
import com.lela.domain.dto.user.Address;
import com.lela.domain.dto.user.UserSearchQuery;
import com.lela.domain.enums.CacheType;
import com.lela.domain.enums.Gender;
import com.lela.domain.enums.MotivatorSource;
import com.maxmind.geoip.Location;
import com.maxmind.geoip.LookupService;

/**
 * Created by Bjorn Harvold
 * Date: 8/4/12
 * Time: 7:35 PM
 * Responsibility:
 */
@Service("userSupplementService")
public class UserSupplementServiceImpl extends AbstractCacheableService implements UserSupplementService {
    private final static Logger log = LoggerFactory.getLogger(UserSupplementServiceImpl.class);

    private final UserSupplementRepository userSupplementRepository;
    private final ValidatorService validatorService;
    private final LookupService geoipLookupServiceIpv4;
    private final LookupService geoipLookupServiceIpv6;
    private final LelaMonitoringService lelaMonitoringService;
    
    @Autowired
    public UserSupplementServiceImpl(CacheService cacheService,
                                     UserSupplementRepository userSupplementRepository,
                                     ValidatorService validatorService, 
                                     LookupService geoipLookupServiceIpv4,  
                                     LookupService geoipLookupServiceIpv6,
                                     LelaMonitoringService lelaMonitoringService
    		) {
        super(cacheService);
        this.userSupplementRepository = userSupplementRepository;
        this.validatorService = validatorService;
        this.geoipLookupServiceIpv4 = geoipLookupServiceIpv4;
        this.geoipLookupServiceIpv6 = geoipLookupServiceIpv6;
        this.lelaMonitoringService = lelaMonitoringService;
    }


    /**
     * Retrieves the motivators in order of priority
     * 1. if a quiz motivators are filled out => return quiz motivators
     * 2. if Facebook motivators are filled out => return fb motivators
     * 3. if no motivators exist => return null
     *
     * @param userCode userCode
     * @return Motivator
     */
    @Override
    public Motivator findBestMotivator(String userCode) {
        UserSupplement us = findUserSupplement(userCode);

        return us.getMotivator();
    }

    @Override
    public Motivator findMotivator(String userCode, MotivatorSource tp) {
        UserSupplement us = findUserSupplement(userCode);
        Motivator result = null;

        if (us.getMtvtrmp() != null && us.getMtvtrmp().containsKey(tp)) {
            result = us.getMtvtrmp().get(tp);
        }

        return result;
    }

    @Override
    public void removeMotivator(String userCode, MotivatorSource tp) {
        UserSupplement us = findUserSupplement(userCode);

        if (us.getMtvtrmp() != null && !us.getMtvtrmp().isEmpty() && us.getMtvtrmp().containsKey(tp)) {
            us.getMtvtrmp().remove(tp);
            saveUserSupplement(us);
        }
    }

    @Override
    public Motivator saveMotivator(String userCode, Motivator motivator) {
        UserSupplement us = findUserSupplement(userCode);

        if (us.getMtvtrmp() == null) {
            us.setMtvtrmp(new HashMap<MotivatorSource, Motivator>());
        }

        // insert new motivator in list
        us.getMtvtrmp().put(motivator.getTp(), motivator);

        // save user supplement
        saveUserSupplement(us);

        return motivator;
    }

    @Override
    public UserSupplement saveUserSupplement(UserSupplement us) {
        us = userSupplementRepository.save(us);
        lelaMonitoringService.recordDBStat();

        // delete old cache value
        removeFromCache(CacheType.USER_SUPPLEMENT, us.getCd());

        return us;
    }

    @Override
    public List<Social> findSocials(String userCode) {
        UserSupplement us = findUserSupplement(userCode);

        return us.getScls();
    }

    @Override
    public List<Social> saveSocials(String userCode, List<Social> socials) {
        UserSupplement us = findUserSupplement(userCode);

        us.setScls(socials);

        saveUserSupplement(us);

        return socials;
    }

    @Override
    public UserSupplement findUserSupplement(String userCode) {
        UserSupplement result = null;

        if (StringUtils.isBlank(userCode)) {
            throw new IllegalArgumentException("UserCode cannot be null");
        }

        Cache.ValueWrapper wrapper = retrieveFromCache(ApplicationConstants.USER_SUPPLEMENT_CACHE, userCode);

        if (wrapper != null && wrapper.get() != null && wrapper.get() instanceof UserSupplement) {
            result = (UserSupplement) wrapper.get();
        } else {
            result = userSupplementRepository.findUserSupplement(userCode);

            if (result == null) {
                // the user should always have a user supplement object
                // if it doesn't exist, create it
                result = new UserSupplement(userCode);
            }

            putInCache(ApplicationConstants.USER_SUPPLEMENT_CACHE, userCode, result);
        }


        return result;
    }

    @Override
    public List<UserSupplement> findUserSupplementsForCampaignReport(String campaignUrlName) {
        return userSupplementRepository.findUserSupplementsForCampaign(campaignUrlName);
    }

    @Override
    public UserSupplement findUserSupplement(String userCode, List<String> fields) {
        return userSupplementRepository.findUserSupplement(userCode, fields);
    }

    @Override
    public List<UserSupplement> findUserSupplementsForAffiliateReport(String affiliateUrlName) {
        return userSupplementRepository.findUserReportDataByAffiliateAccountUrlName(affiliateUrlName);
    }

    @Override
    public List<UserSupplement> findUserSupplementsForEventReport(String eventUrlName) {
        return userSupplementRepository.findUserReportDataByEventUrlName(eventUrlName);
    }

    @Override
    public UserSupplement findUserSupplementByEmail(String email) {
        UserSupplement result = null;

        if (StringUtils.isNotBlank(email)) {
            result = userSupplementRepository.findOne(QUserSupplement.userSupplement.ml.equalsIgnoreCase(email.trim()));
        }

        return result;
    }

    @Override
    public List<UserSupplement> findUserSupplementsByCodes(List<String> userCodes, List<String> fields) {
        return userSupplementRepository.findByCodes(userCodes, fields);
    }

    @Override
    public List<UserSupplement> findUserSupplementsByQuery(UserSearchQuery query) {
        return userSupplementRepository.findUserSupplementsByQuery(query);
    }

    @Override
    public Long findUserSupplementCountByQuery(UserSearchQuery query) {
        return userSupplementRepository.findUserSupplementCountByQuery(query);
    }

    @Override
    public List<UserAnswer> saveUserAnswers(String userCode, List<UserAnswer> list) {
        UserSupplement us = findUserSupplement(userCode);

        us.addUserAnswers(list);

        saveUserSupplement(us);

        return list;
    }

    @Override
    public List<UserAnswer> findUserAnswers(String userCode) {
        UserSupplement us = findUserSupplement(userCode);

        return us.getNswrs();
    }

    @Override
    public List<FunctionalFilterPreset> findFunctionalFilterPresets(String userCode) {
        UserSupplement us = findUserSupplement(userCode);

        return us.getFltrprsts();
    }

    @Override
    public List<UserAssociation> findUserAssociations(String userCode) {
        UserSupplement us = findUserSupplement(userCode);

        return us.getSsctns();
    }

    @Override
    public List<Coupon> findCoupons(String userCode) {
        UserSupplement us = findUserSupplement(userCode);

        return us.getCpns();
    }

    @Override
    public List<FunctionalFilterPreset> saveFunctionalFilterPresets(String userCode, List<FunctionalFilterPreset> filterPresets) {
        UserSupplement us = findUserSupplement(userCode);

        us.setFltrprsts(filterPresets);

        saveUserSupplement(us);

        return filterPresets;
    }

    @Override
    public List<UserAssociation> saveUserAssociations(String userCode, List<UserAssociation> userAssociations) {
        UserSupplement us = findUserSupplement(userCode);

        us.setSsctns(userAssociations);

        saveUserSupplement(us);

        return userAssociations;
    }

    @Override
    public List<Coupon> saveCoupons(String userCode, List<Coupon> coupons) {
        UserSupplement us = findUserSupplement(userCode);

        us.setCpns(coupons);

        saveUserSupplement(us);

        return coupons;
    }

    @Override
    public UserSupplement findUserSupplementByCouponCode(String couponCode) {
        return userSupplementRepository.findByCouponCode(couponCode);
    }

    @Override
    public Coupon findCouponByCouponCode(String couponCode) {
        UserSupplement us = findUserSupplementByCouponCode(couponCode);

        if (us != null && us.getCpns() != null && !us.getCpns().isEmpty()) {
            for (Coupon existingCoupon : us.getCpns()) {
                if (existingCoupon.getCpncd().equals(couponCode)) {
                    return existingCoupon;
                }
            }
        }

        return null;
    }

    @Override
    public List<Coupon> findCouponsByOfferUrlName(String offerUrlName) {
        List<UserSupplement> list = findUserSupplementsByOfferUrlName(offerUrlName);
        List<Coupon> result = null;

        if (list != null && !list.isEmpty()) {
            result = new ArrayList<Coupon>();
            for (UserSupplement us : list) {
                result.addAll(us.findCouponsByOfferUrlName(offerUrlName));
            }
        }

        return result;
    }

    private List<UserSupplement> findUserSupplementsByOfferUrlName(String offerUrlName) {
        return userSupplementRepository.findByOfferUrlName(offerUrlName);
    }

    @Override
    public Long findUserSupplementByOfferUrlNameCount(String offerUrlName) {
        return userSupplementRepository.findByOfferUrlNameCount(offerUrlName);
    }

    @Override
    public Map<String, List<String>> findUserAttributes(String userCode) {
        UserSupplement us = findUserSupplement(userCode);

        return us.getAttrs();
    }

    @Override
    public Long findUserCountByCouponCode(String couponCode) {
        return userSupplementRepository.findUserCountByCouponCode(couponCode);
    }

    @Override
    public boolean shouldUserSeeRecommendedProducts(String userCode) {
        UserSupplement us = findUserSupplement(userCode);

        return (us != null && us.getMotivator() != null);
    }

    /**
     * The caller wants these attributes added to the user attrs map. Before
     * that happens, we need to validate that the keys and values are ones we know and expect
     *
     * @param userCode       userCode
     * @param userAttributes userAttributes
     */
    @Override
    public void processUserAttributes(String userCode, UserAttributes userAttributes) {
        UserSupplement us = findUserSupplement(userCode);

        if (userAttributes != null &&
                userAttributes.getMap() != null && !userAttributes.getMap().isEmpty()) {

            // use our validator service to check on valid keys and values
            if (validatorService.isPayloadValid(ApplicationConstants.USER_ATTRIBUTES, userAttributes.getMap())) {
                if (us.getAttrs() == null) {
                    us.setAttrs(new HashMap<String, List<String>>());
                }

                for (Map.Entry<String, List<String>> entry : userAttributes.getMap().entrySet()) {
                    us.getAttrs().put(entry.getKey(), entry.getValue());
                }

                saveUserSupplement(us);
            }
        }
    }

    /**
     * Whenever some action happens that affect the relationship between the user and Lela,
     * this is the method to call in order for the rules to be run and percentages calculated
     * <p/>
     * These are the current rules:
     * - 1st image question = 30%
     * - Regular question = 10%
     * - Facebook sign-in = 5%
     * - Netflix integration = 5%
     *
     * @param userCode userCode
     */
    @Override
    public void recomputeFriendshipLevel(String userCode) {
        UserSupplement us = findUserSupplement(userCode);

        // start with zero
        int percentage = 0;

        // if gender is set it means 2 points
        if (us.getGndr() != null) {
            percentage += 2;
        }

        // different types of questions give different points
        if (!us.getNswrs().isEmpty()) {
            for (UserAnswer ua : us.getNswrs()) {
                switch (ua.getQstn().getTp()) {
                    case IMAGE_MULTIPLE_CHOICE_SINGLE_ANSWER:
                        percentage += 15;
                        break;
                    case SLIDER:
                        percentage += 5;
                        break;
                    case STYLE_SLIDER:
                        percentage += 6;
                        break;
                }
            }
        }

        // social network connections also add to the percentage
        if (us.getScls() != null && !us.getScls().isEmpty()) {
            for (Social social : us.getScls()) {
                if (StringUtils.equals(social.getProviderId(), ApplicationConstants.FACEBOOK)) {

                    // here's facebook integration
                    percentage += 8;
                }
            }
        }

        if (percentage != us.getFrndlvl()) {
            // updated Lela friend level updated
            us.setFrndlvl(percentage);

            saveUserSupplement(us);
        }
    }

    @Override
    public List<String> motivatorLocalizedKeys(String userCode) {
        List<String> motivatorLocalizedKeys = new ArrayList<String>();

        Motivator motivator = findBestMotivator(userCode);

        if (motivator != null) {
            for (String motivatorKey : motivator.getMtvtrs().keySet()) {
                if (StringUtils.equals("B", motivatorKey) ||
                        StringUtils.equals("D", motivatorKey) ||
                        StringUtils.equals("E", motivatorKey) ||
                        StringUtils.equals("F", motivatorKey)) {
                    addLocalizedMotivatorKey(motivatorLocalizedKeys, motivatorKey, motivator.getMtvtrs().get(motivatorKey));
                }
            }

            Collections.sort(motivatorLocalizedKeys, new MotivatorLocalizedKeysComparator());
        }

        return motivatorLocalizedKeys;
    }

    private void addLocalizedMotivatorKey(List<String> motivatorLocalizedKeys, String key, Integer value) {

        if (value < 4) {
            motivatorLocalizedKeys.add(key + "13");
        }
        if (value >= 4 && value < 7) {
            motivatorLocalizedKeys.add(key + "46");
        }
        if (value >= 7 && value < 10) {
            motivatorLocalizedKeys.add(key + "79");
        }
    }

    @Override
    public UserEvent saveUserEvent(String userCode, Event event, String urlName, Map params) throws IllegalAccessException {
        UserSupplement us = findUserSupplement(userCode);
        UserEvent result = null;

        Date now = new Date();
        if (event != null) {
            if (event.getNddt().after(now) && event.getStrtdt().before(now)) {
                boolean alreadySignedUp = false;

                if (us.getVnts() != null && !us.getVnts().isEmpty()) {
                    for (UserEvent ue : us.getVnts()) {
                        if (StringUtils.equals(ue.getRlnm(), event.getRlnm())) {
                            alreadySignedUp = true;
                        }
                    }
                }

                if (!alreadySignedUp) {
                    result = new UserEvent(urlName, new Date(), params);

                    if (us.getVnts() == null) {
                        us.setVnts(new ArrayList<UserEvent>());
                    }

                    us.getVnts().add(result);

                    saveUserSupplement(us);
                } else {
                    throw new IllegalAccessException("User is not allowed to sign up more than once");
                }

            } else {
                throw new IllegalAccessException("User is not allowed to sign up to this event");
            }
        }

        return result;
    }

    @Override
    public List<UserSupplement> findUsersWithMotivators(Integer page, Integer maxResults, List<String> fields) {
        return userSupplementRepository.findUsersWithMotivators(page, maxResults, fields);
    }

    @Override
    public Long findFacebookUserCount(List<String> userCodes) {
        return userSupplementRepository.findFacebookUserCount(userCodes);
    }

    /**
     * Method description
     *
     * @param providerId     providerId
     * @param providerUserId providerUserId
     * @return Return value
     */
    @Override
    public List<UserSupplement> findUserIdsBySocialNetwork(String providerId, String providerUserId) {
        return userSupplementRepository.findUserIdsBySocialNetwork(providerId, providerUserId);
    }

    /**
     * Method description
     *
     * @param providerId      providerId
     * @param providerUserIds providerUserIds
     * @return Return value
     */
    @Override
    public List<UserSupplement> findUserIdsConnectedToSocialNetwork(String providerId, Set<String> providerUserIds) {
        return userSupplementRepository.findUserIdsConnectedToSocialNetwork(providerId, providerUserIds);
    }

    /**
     * Find ALL users for social network
     *
     * @param providerId Social network provider id
     * @return list of users
     */
    @Override
    public List<UserSupplement> findUsersBySocialNetwork(String providerId) {
        return userSupplementRepository.findUsersBySocialNetwork(providerId);
    }

    @Override
    public void removeUserSupplement(String userCode) {
        UserSupplement us = findUserSupplement(userCode);

        removeUserSupplement(us);
    }

    @Override
    public void removeUserSupplement(UserSupplement us) {
        if (us != null && us.getId() != null) {
            userSupplementRepository.delete(us);

            removeFromCache(CacheType.USER_SUPPLEMENT, us.getCd());
        }
    }

    @Override
    public List<UserSupplement> findUsersWithAlerts() {
        return userSupplementRepository.findUsersWithAlerts();
    }

    @Override
    public List<UserSupplement> findByEventUrlName(String urlName) {
        return userSupplementRepository.findByEventUrlName(urlName);
    }

    @Override
    public List<UserSupplement> findByCampaignUrlName(String urlName) {
        return userSupplementRepository.findByCampaignUrlName(urlName);
    }

    @Override
    public List<UserSupplement> findByAffiliateAccountUrlName(String urlName) {
        return userSupplementRepository.findByAffiliateAccountUrlName(urlName);
    }

    @Override
    public void saveGender(String userCode, Gender gender) {
        UserSupplement us = findUserSupplement(userCode);
        if (us == null) {
            throw new IllegalStateException("No UserSupplement found for userCode: " + userCode);
        }

        us.setGndr(gender);
        saveUserSupplement(us);
    }

    @Override
    public void saveAge(String userCode, Integer age) {
        UserSupplement us = findUserSupplement(userCode);
        if (us == null) {
            throw new IllegalStateException("No UserSupplement found for userCode: " + userCode);
        }

        us.setAge(age);
        saveUserSupplement(us);
    }

    @Override
    public Address addOrUpdateAddress(String userCode, Address address) {
        UserSupplement us = findUserSupplement(userCode);
        if (us == null) {
            throw new IllegalStateException("No UserSupplement found for userCode: " + userCode);
        }

        if (address.getTp() == null) {
            throw new IllegalArgumentException("Address must have a type defined");
        }

        us.addAddress(address);

        // If the new address is the default, make sure the other address are not
        if (address.getDflt()) {
            for (Address other : us.getDdrss().values()) {
                if (!address.equals(other)) {
                    other.setDflt(false);
                }
            }
        }

        // Make sure at least one address is the default
        if (us.getDdrss().size() == 1) {
            address.setDflt(true);
        }

        saveUserSupplement(us);

        return address;
    }
    
    @Override
    @Subscribe
    @AllowConcurrentEvents
    public void updateCurrentZip(UpdateZipEvent uze) { 
        Location location = null;
        User user = uze.getUser();
        String remoteIPAddress = uze.getRemoteIPAddress();
        if (remoteIPAddress != null) {
            if (remoteIPAddress.contains(":")) {
                location = geoipLookupServiceIpv6.getLocationV6(remoteIPAddress); 
            } else {
                location = geoipLookupServiceIpv4.getLocation(remoteIPAddress);
            }      
            if (location != null && (!StringUtils.isEmpty(location.postalCode))){
	            UserSupplement us = findUserSupplement(user.getCd());
	            log.debug("Setting zip for user email: " + user.getMl() + " to: " + location.postalCode + ". Old zip: " + us.getCzp());
	            us.setCzp(location.postalCode);
	            this.saveUserSupplement(us);
            } else {
            	log.debug("Could not determine zip for IP: " + remoteIPAddress);
            }
        }
    }

    @Override
    public void trackAffiliateNotified(String userCode, Boolean notified) {
        if (userCode != null && notified != null) {
            userSupplementRepository.trackAffiliateNotified(userCode, notified);
        }
    }
}
