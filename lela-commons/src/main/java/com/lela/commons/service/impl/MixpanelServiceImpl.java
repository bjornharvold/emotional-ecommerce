/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */
package com.lela.commons.service.impl;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import com.lela.commons.event.*;
import com.lela.commons.service.*;
import com.lela.commons.web.utils.WebConstants;
import com.lela.domain.document.*;
import com.lela.domain.dto.AffiliateIdentifiers;
import com.lela.domain.dto.RelevantItemQuery;
import com.lela.domain.dto.quiz.QuizAnswer;
import com.lela.domain.dto.quiz.QuizAnswers;
import com.lela.domain.enums.DeviceType;
import com.lela.domain.enums.MixpanelEngagementPropertyType;
import com.lela.domain.enums.MixpanelEventType;
import com.lela.domain.enums.MixpanelPeoplePropertyType;
import com.lela.util.utilities.mixpanel.RequestUtils;
import com.maxmind.geoip.Location;
import com.maxmind.geoip.LookupService;
import com.mixpanel.java.mpmetrics.MPConfig;
import com.mixpanel.java.mpmetrics.MPMetrics;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.DeviceUtils;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User: Chris Tallent
 * Date: 10/22/12
 * Time: 9:34 AM
 */
public class MixpanelServiceImpl implements MixpanelService {
    private static final Logger log = LoggerFactory.getLogger(MixpanelServiceImpl.class);

    private static final String MOTIVATOR_PROPERTY_BASE = "Motivator ";
    private static final String QUESTION_PROPERTY_BASE = "Question ";
    private static final String EVENT_PROPERTY_BASE = "Participated In ";

    private static final String VIEWED_CATEGORY_TOTAL_FORMAT = "Viewed Category %s Total";
    private static final Pattern EMAIL_ISP_REGEX = Pattern.compile("^.*[@](.+)$");

    @Value("${mixpanel.enabled:false}")
    private boolean enabled;

    @Value("${mixpanel.key}")
    private String key;

    @Autowired
    private MPConfig mpConfig;

    @Autowired
    private UserService userService;

    @Autowired
    private UserTrackerService userTrackerService;

    @Autowired
    @Qualifier("geoipLookupServiceIpv4")
    private LookupService lookupService;

    @Autowired
    @Qualifier("geoipLookupServiceIpv6")
    private LookupService lookupServiceIpv6;

    @Autowired
    private ProductEngineService productEngineService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private CategoryService categoryService;

    @Override
    public boolean isEnabled(HttpServletRequest request) {
        return enabled && !RequestUtils.isBot(request);
    }

    @Override
    public String getMixpanelKey() {
        return key;
    }

    /**
     * This event is used for the Mixpanel Stream and will not show up in Funnels / Segmentation
     */
    @Override
    @Subscribe
    @AllowConcurrentEvents
    public void trackPageView(PageViewEvent event) {
        try {
            if (!isEnabled(event.getRequest())) {
                return;
            }

            Map<String, Object> props = RequestUtils.getPageviewInfo(event.getRequest());

            // Handl the country code
            setEngagementProp(props, MixpanelEngagementPropertyType.COUNTRY_CODE, getCountryCode(event.getRequest()));

            // Ensure the distinct id is set
            setEngagementProp(props, MixpanelEngagementPropertyType.DISTINCT_ID, event.getUser().getCd());

            setEngagementProp(props, MixpanelEngagementPropertyType.REFERRER, RequestUtils.getReferrer(event.getRequest()));
            setEngagementProp(props, MixpanelEngagementPropertyType.REFERRING_DOMAIN, RequestUtils.getReferringDomain(event.getRequest()));

            // Set the user full name for the streams feed
            UserSupplement us = userService.findUserSupplement(event.getUser().getCd());
            if (us != null) {
                setEngagementProp(props, MixpanelEngagementPropertyType.STREAMS_NAME, us.getFllnm());
            }

            MPMetrics mixpanel = getMixpanel(event.getUser());
            mixpanel.track(MixpanelEventType.PAGE_VIEW.getValue(), props);

            mixpanel.increment(MixpanelPeoplePropertyType.PAGE_VIEW_TOTAL.getValue(), 1);
        } catch (Exception e) {
            // Tracking should be failsafe
            log.error("Exception while tracking", e);
        }
    }

    @Override
    @Subscribe
    @AllowConcurrentEvents
    public void trackVisit(UserVisitEvent event) {
        try {
            if (!isEnabled(event.getRequest())) {
                return;
            }

            MPMetrics mixpanel = getMixpanel(event.getUser());

            Map<String, Object> props = createEngagementProperties(event.getUser(), event.getRequest());

            // If this is the first visit
            if (userTrackerService.isFirstVisit(event.getUser())) {
                setEngagementProp(props, MixpanelEngagementPropertyType.INITIAL_REFERRER, RequestUtils.getReferrer(event.getRequest()));
                setEngagementProp(props, MixpanelEngagementPropertyType.INITIAL_REFERRING_DOMAIN, RequestUtils.getReferringDomain(event.getRequest()));
            }

            setEngagementProp(props, MixpanelEngagementPropertyType.OBJECT_ID, event.getVisit().getId());
            setEngagementProp(props, MixpanelEngagementPropertyType.VISIT_AFFILIATE_ID, event.getVisit().getFfltccntrlnm());
            setEngagementProp(props, MixpanelEngagementPropertyType.VISIT_CAMPAIGN_ID, event.getVisit().getCmpgnrlnm());

            mixpanel.track(MixpanelEventType.VISIT.getValue(), props);

            mixpanel.increment(MixpanelPeoplePropertyType.USER_VISIT_TOTAL.getValue(), 1);
        } catch (Exception e) {
            // Tracking should be failsafe
            log.error("Exception while tracking", e);
        }
    }

    @Override
    @Subscribe
    @AllowConcurrentEvents
    public void viewedCategory(ViewedCategoryEvent event) {
        try {
            if (!isEnabled(event.getRequest())) {
                return;
            }

            Map<String, Object> props = createEngagementProperties(event.getUser(), event.getRequest());
            setEngagementProp(props, MixpanelEngagementPropertyType.CATEGORY_ID, event.getCategoryUrlName());

            MPMetrics mixpanel = getMixpanel(event.getUser());
            mixpanel.track(MixpanelEventType.VIEWED_CATEGORY.getValue(), props);

            mixpanel.set(MixpanelPeoplePropertyType.LAST_CATEGORY_ID.getValue(), event.getCategoryUrlName());
            mixpanel.increment(String.format(VIEWED_CATEGORY_TOTAL_FORMAT, event.getCategoryUrlName()), 1);
        } catch (Exception e) {
            // Tracking should be failsafe
            log.error("Exception while tracking", e);
        }
    }

    @Override
    @Subscribe
    @AllowConcurrentEvents
    public void filteredCategoryResults(FunctionalFilterEvent event) {
        try {
            if (!isEnabled(event.getRequest())) {
                return;
            }

            MPMetrics mixpanel = getMixpanel(event.getUser());

            Map<String, Object> props = createEngagementProperties(event.getUser(), event.getRequest());
            setEngagementProp(props, MixpanelEngagementPropertyType.CATEGORY_ID, event.getCategoryUrlName());
            setEngagementProp(props, MixpanelEngagementPropertyType.FILTER, event.getFilter());
            setEngagementProp(props, MixpanelEngagementPropertyType.FILTER_OPTION, event.getOption());
            setEngagementProp(props, MixpanelEngagementPropertyType.FILTER_OPTION_VALUE, event.getValue());

            mixpanel.track(MixpanelEventType.FILTERED_RESULTS.getValue(), props);

            mixpanel.increment(MixpanelPeoplePropertyType.FILTER_USAGE_TOTAL.getValue(), 1);
        } catch (Exception e) {
            // Tracking should be failsafe
            log.error("Exception while tracking", e);
        }
    }

    @Override
    @Subscribe
    @AllowConcurrentEvents
    public void sortedCategory(SortEvent event) {
        try {
            if (!isEnabled(event.getRequest())) {
                return;
            }

            MPMetrics mixpanel = getMixpanel(event.getUser());

            Map<String, Object> props = createEngagementProperties(event.getUser(), event.getRequest());
            setEngagementProp(props, MixpanelEngagementPropertyType.CATEGORY_ID, event.getCategoryUrlName());
            setEngagementProp(props, MixpanelEngagementPropertyType.SORT, event.getSort());

            mixpanel.track(MixpanelEventType.SORTED_RESULTS.getValue(), props);

            mixpanel.increment(MixpanelPeoplePropertyType.SORT_USAGE_TOTAL.getValue(), 1);
        } catch (Exception e) {
            // Tracking should be failsafe
            log.error("Exception while tracking", e);
        }
    }

    @Override
    @Subscribe
    @AllowConcurrentEvents
    public void viewedDepartment(ViewedDepartmentEvent event) {
        try {
            if (!isEnabled(event.getRequest())) {
                return;
            }

            Map<String, Object> props = createEngagementProperties(event.getUser(), event.getRequest());
            setEngagementProp(props, MixpanelEngagementPropertyType.DEPARTMENT_ID, event.getDepartmentUrlName());

            MPMetrics mixpanel = getMixpanel(event.getUser());
            mixpanel.track(MixpanelEventType.VIEWED_DEPARTMENT.getValue(), props);
        } catch (Exception e) {
            // Tracking should be failsafe
            log.error("Exception while tracking", e);
        }
    }

    @Override
    @Subscribe
    @AllowConcurrentEvents
    public void viewedItem(ViewedItemEvent event) {
        try {
            if (!isEnabled(event.getRequest())) {
                return;
            }

            Map<String, Object> props = createEngagementProperties(event.getUser(), event.getRequest());
            setEngagementProp(props, MixpanelEngagementPropertyType.ITEM_ID, event.getItemUrlName());
            setEngagementProp(props, MixpanelEngagementPropertyType.ITEM_VIEW_TYPE, event.getItemViewType());

            MPMetrics mixpanel = getMixpanel(event.getUser());
            mixpanel.track(MixpanelEventType.VIEWED_ITEM.getValue(), props);

            mixpanel.set(MixpanelPeoplePropertyType.LAST_ITEM_ID.getValue(), event.getItemUrlName());
        } catch (Exception e) {
            // Tracking should be failsafe
            log.error("Exception while tracking", e);
        }
    }

    @Override
    @Subscribe
    @AllowConcurrentEvents
    public void addedItemToList(AddItemToListEvent event) {
        try {
            if (!isEnabled(event.getRequest())) {
                return;
            }

            User user = userService.findUserByCode(event.getUserCode());
            Map<String, Object> props = buildLelaListEventProperties(event, event, user, event.getUserCode());

            setEngagementProp(props, MixpanelEngagementPropertyType.LELA_LIST_ACTION, "Add");
            MPMetrics mixpanel = getMixpanel(user);
            mixpanel.track(MixpanelEventType.LELA_LIST.getValue(), props);

        } catch (Exception e) {
            // Tracking should be failsafe
            log.error("Exception while tracking", e);
        }
    }


    @Override
    @Subscribe
    @AllowConcurrentEvents
    public void removeItemFromList(RemoveItemFromListEvent event) {
        try {
            if (!isEnabled(event.getRequest())) {
                return;
            }

            User user = userService.findUserByCode(event.getUserCode());

            Map<String, Object> props = buildLelaListEventProperties(event, event, user, event.getUserCode());

            setEngagementProp(props, MixpanelEngagementPropertyType.LELA_LIST_ACTION, "Remove");
            MPMetrics mixpanel = getMixpanel(user);
            mixpanel.track(MixpanelEventType.LELA_LIST.getValue(), props);

        } catch (Exception e) {
            // Tracking should be failsafe
            log.error("Exception while tracking", e);
        }
    }

    private Map<String, Object> buildLelaListEventProperties(ListEvent listEvent, AbstractEvent abstractEvent, User user, String userCode) {
        Map<String, Object> props = createEngagementProperties(user, userCode, abstractEvent.getRequest());

        setEngagementProp(props, MixpanelEngagementPropertyType.ITEM_ID, listEvent.getItemUrlName());
        Item item = itemService.findItemByUrlName(listEvent.getItemUrlName());
        if(item!=null)
        {
            setEngagementProp(props, MixpanelEngagementPropertyType.CATEGORY_ID, item.getCtgry().getRlnm());
            Category category = categoryService.findCategoryByUrlName(item.getCtgry().getRlnm());
            if(category != null)
            {
                setEngagementProp(props, MixpanelEngagementPropertyType.DEPARTMENT_ID, category.getGrps().toArray(new String[category.getGrps().size()]));
            }
        }
        else
        {
            throw new RuntimeException("Item was null, we don't track removal of non item lela list cards.");
        }

        RelevantItemQuery query = new RelevantItemQuery(listEvent.getItemUrlName(), listEvent.getUserCode());
        RelevantItem result = productEngineService.findRelevantItem(query);
        if(result!=null)
        {
            setEngagementProp(props, MixpanelEngagementPropertyType.COMPATIBILITY_SCORE, result.getTtlrlvncynmbr());
        }
        return props;
    }

    @Override
    @Subscribe
    @AllowConcurrentEvents
    public void compareItems(CompareItemsEvent event) {
        try {
            if (!isEnabled(event.getRequest())) {
                return;
            }

            Map<String, Object> props = createEngagementProperties(event.getUser(), event.getRequest());
            setEngagementProp(props, MixpanelEngagementPropertyType.COMPARED_ITEMS, event.getItemUrlNames());

            MPMetrics mixpanel = getMixpanel(event.getUser());
            mixpanel.track(MixpanelEventType.COMPARED_ITEMS.getValue(), props);
        } catch (Exception e) {
            // Tracking should be failsafe
            log.error("Exception while tracking", e);
        }
    }

    @Override
    @Subscribe
    @AllowConcurrentEvents
    public void registerUser(RegistrationEvent event) {
        try {
            if (!isEnabled(event.getRequest())) {
                return;
            }

            User user = event.getUser();
            HttpServletRequest request = event.getRequest();

            // Track PEOPLE data
            UserSupplement us = userService.findUserSupplement(user.getCd());
            AffiliateIdentifiers ai = userTrackerService.findAffiliateIdentifiers(user.getCd());

            // Registration Event
            Map<String, Object> props = createEngagementProperties(user, request);
            setEngagementProp(props, MixpanelEngagementPropertyType.REGISTRATION_TYPE, event.getRegistrationType());

            MPMetrics mixpanel = getMixpanel(user);
            mixpanel.track(MixpanelEventType.SIGNUP.getValue(), props);

            props = createPeopleProperties(user, request);

            setPeopleProp(props, MixpanelPeoplePropertyType.CREATED, user.getCdt());

            // User info
            if (us != null) {
                setPeopleProp(props, MixpanelPeoplePropertyType.FIRST_NAME, us.getFnm());
                setPeopleProp(props, MixpanelPeoplePropertyType.LAST_NAME, us.getLnm());
                setPeopleProp(props, MixpanelPeoplePropertyType.FULL_NAME, us.getFllnm());
                setPeopleProp(props, MixpanelPeoplePropertyType.GENDER, us.getGndr());
                setPeopleProp(props, MixpanelPeoplePropertyType.EMAIL, us.getMl());
                setPeopleProp(props, MixpanelPeoplePropertyType.USERNAME, us.getMl());
                setPeopleProp(props, MixpanelPeoplePropertyType.EMAIL_OPT_IN, us.getPtn());

                // User Events
                if (us.getVnts() != null) {
                    for (UserEvent userEvent : us.getVnts()) {
                        setIfNotNull(props, EVENT_PROPERTY_BASE + userEvent.getRlnm(), true);
                    }
                }

                if (us.getMl() != null) {
                    Matcher emailMatcher = EMAIL_ISP_REGEX.matcher(us.getMl());
                    if (emailMatcher.matches()) {
                        setPeopleProp(props, MixpanelPeoplePropertyType.EMAIL_DOMAIN, emailMatcher.group(1));
                    }
                }
            }

            // Affiliate info
            if (ai != null) {
                setPeopleProp(props, MixpanelPeoplePropertyType.AFFILIATE_ID, ai.getFfltccntrlnm());
                setPeopleProp(props, MixpanelPeoplePropertyType.CAMPAIGN_ID, ai.getCmpgnrlnm());
                setPeopleProp(props, MixpanelPeoplePropertyType.AFFILIATE_REFERRER_ID, ai.getRfrrlnm());
                setPeopleProp(props, MixpanelPeoplePropertyType.DOMAIN, ai.getDmn());
                setPeopleProp(props, MixpanelPeoplePropertyType.DOMAIN_AFFILIATE_ID, ai.getDmnffltrlnm());
            }

            // Set a random number for a/b testing
            setPeopleProp(props, MixpanelPeoplePropertyType.RANDOM, RandomUtils.nextInt(100)+1);

            mixpanel.set(props);
        } catch (Exception e) {
            // Tracking should be failsafe
            log.error("Exception while tracking", e);
        }
    }

    @Override
    @Subscribe
    @AllowConcurrentEvents
    public void deleteUser(DeleteUserEvent event) {
        try {
            if (!isEnabled(event.getRequest())) {
                return;
            }

            User user = event.getUser();
            HttpServletRequest request = event.getRequest();

            // Registration Event
            Map<String, Object> props = createEngagementProperties(user, request);
            setEngagementProp(props, MixpanelEngagementPropertyType.DELETED_USER, true);

            MPMetrics mixpanel = getMixpanel(user);
            mixpanel.track(MixpanelEventType.DELETE_USER.getValue(), props);

            // Track PEOPLE data
            props = createPeopleProperties(user, request);
            setPeopleProp(props, MixpanelPeoplePropertyType.DELETED_USER, true);
            setPeopleProp(props, MixpanelPeoplePropertyType.EMAIL_OPT_IN, false);

            mixpanel.set(props);
        } catch (Exception e) {
            // Tracking should be failsafe
            log.error("Exception while tracking", e);
        }
    }

    @Override
    @Subscribe
    @AllowConcurrentEvents
    public void loginUser(LoginEvent event) {
        try {
            if (!isEnabled(event.getRequest())) {
                return;
            }

            User user = event.getUser();
            HttpServletRequest request = event.getRequest();

            // Registration Event
            Map<String, Object> props = createEngagementProperties(user, request);
            setEngagementProp(props, MixpanelEngagementPropertyType.AUTHENTICATION_TYPE, event.getAuthenticationType());

            MPMetrics mixpanel = getMixpanel(user);
            mixpanel.track(MixpanelEventType.LOGIN.getValue(), props);

            mixpanel.set(MixpanelPeoplePropertyType.LAST_LOGIN.getValue(), new Date());
        } catch (Exception e) {
            // Tracking should be failsafe
            log.error("Exception while tracking", e);
        }
    }

    @Override
    @Subscribe
    @AllowConcurrentEvents
    public void purchase(PurchaseEvent event)
    {
        try {
            if (!isEnabled(event.getRequest())) {
                return;
            }

            User user = event.getUser();
            UserTracker userTracker = event.getUserTracker();

            HttpServletRequest request = event.getRequest();

            Map<String, Object> props = null;

            String userCode = "";

            if(user!=null){
                userCode = user.getCd();
                props = createEngagementProperties(user, request);
            }
            else if (userTracker!=null){
                userCode = userTracker.getSrcd();
                props = createEngagementProperties(userTracker, userCode);
            }
            else{
                userCode = "anon_"+Math.abs(UUID.randomUUID().getLeastSignificantBits());
                props = new HashMap<String, Object>();
            }

            setEngagementProp(props, MixpanelEngagementPropertyType.PRODUCT_CATEGORY, event.getSale().getPrdctCtgry());
            setEngagementProp(props, MixpanelEngagementPropertyType.PRODUCT_NAME, event.getSale().getPrdctNm());
            setEngagementProp(props, MixpanelEngagementPropertyType.PRODUCT_ID, event.getSale().getPrdctd());

            setEngagementProp(props, MixpanelEngagementPropertyType.ADVERTISER, event.getSale().getAdvrtsrNm());
            setEngagementProp(props, MixpanelEngagementPropertyType.NETWORK, event.getSale().getNtwrk());

            setEngagementProp(props, MixpanelEngagementPropertyType.SALES_AMOUNT, event.getSale().getSlmnt());
            setEngagementProp(props, MixpanelEngagementPropertyType.COMMISSION_AMOUNT, event.getSale().getCmmssnmnt());

            setEngagementProp(props, MixpanelEngagementPropertyType.QUANTITY, event.getSale().getQntty());

            setEngagementProp(props, MixpanelEngagementPropertyType.TIMESTAMP, event.getSale().getPrcssdt().getTime() / 1000l);

            MPMetrics mixpanel = null;

            if(user != null){
                mixpanel = getMixpanel(user, true);
            }
            else if (userTracker != null ){
                mixpanel = getMixpanel(true);
                mixpanel.identify(userCode);
            }
            else{
                mixpanel = getMixpanel(true);
                mixpanel.identify(userCode);
            }

            MixpanelEventType eventType = MixpanelEventType.PURCHASE;
            if(event.getSale().getSlmnt() < 0.0)
                eventType = MixpanelEventType.RETURN;

            mixpanel.track(eventType.getValue(), props);

            if(user!=null)
            {
                mixpanel.increment(MixpanelPeoplePropertyType.PURCHASED_ITEMS.getValue(), event.getSale().getQntty());
                BigDecimal roundedSaleValue = new BigDecimal(String.valueOf(event.getSale().getSlmnt())).setScale(0, BigDecimal.ROUND_HALF_DOWN);
                mixpanel.increment(MixpanelPeoplePropertyType.SALES_TOTAL.getValue(), roundedSaleValue.toBigInteger().intValue());
                BigDecimal roundedCommissionValue = new BigDecimal(String.valueOf(event.getSale().getCmmssnmnt())).setScale(0, BigDecimal.ROUND_HALF_DOWN);
                mixpanel.increment(MixpanelPeoplePropertyType.COMMISSION_TOTAL.getValue(), roundedCommissionValue.toBigInteger().intValue());

            }
        } catch (Exception e) {
            // Tracking should be failsafe
            log.error("Exception while tracking", e);
            log.error("Error tracking sale: " + ReflectionToStringBuilder.reflectionToString(event.getSale()));
        }
    }

    @Override
    @Subscribe
    @AllowConcurrentEvents
    public void setGender(SetGenderEvent event) {
        try {
            if (!isEnabled(event.getRequest())) {
                return;
            }

            MPMetrics mixpanel = getMixpanel(event.getUser());
            mixpanel.set(MixpanelPeoplePropertyType.GENDER.getValue(), event.getGender());
        } catch (Exception e) {
            // Tracking should be failsafe
            log.error("Exception while tracking", e);
        }
    }

    @Override
    @Subscribe
    @AllowConcurrentEvents
    public void setAge(SetAgeEvent event) {
        try {
            if (!isEnabled(event.getRequest())) {
                return;
            }

            MPMetrics mixpanel = getMixpanel(event.getUser());
            mixpanel.set(MixpanelPeoplePropertyType.AGE.getValue(), event.getAge());
        } catch (Exception e) {
            // Tracking should be failsafe
            log.error("Exception while tracking", e);
        }
    }

    @Override
    @Subscribe
    @AllowConcurrentEvents
    public void setAddress(SetAddressEvent event) {
        try {
            if (!isEnabled(event.getRequest())) {
                return;
            }

            MPMetrics mixpanel = getMixpanel(event.getUser());
            Map<String, Object> props = createPeopleProperties(event.getUser(), event.getRequest());

            setPeopleProp(props, MixpanelPeoplePropertyType.CITY, event.getAddress().getCt());
            setPeopleProp(props, MixpanelPeoplePropertyType.STATE, event.getAddress().getSt());
            setPeopleProp(props, MixpanelPeoplePropertyType.ZIPCODE, event.getAddress().getZp());

            mixpanel.set(props);
        } catch (Exception e) {
            // Tracking should be failsafe
            log.error("Exception while tracking", e);
        }
    }

    @Override
    @Subscribe
    @AllowConcurrentEvents
    public void subscribe(SubscribeEvent event) {
        try {
            if (!isEnabled(event.getRequest())) {
                return;
            }

            Map<String, Object> props = createEngagementProperties(event.getUser(), event.getRequest());
            setEngagementProp(props, MixpanelEngagementPropertyType.EMAIL_LIST_ID, event.getListId());

            MPMetrics mixpanel = getMixpanel(event.getUser());
            mixpanel.track(MixpanelEventType.SUBSCRIBE.getValue(), props);

            props = createPeopleProperties(event.getUser(), event.getRequest());
            setPeopleProp(props, MixpanelPeoplePropertyType.EMAIL_OPT_IN, true);

            mixpanel.set(props);
        } catch (Exception e) {
            // Tracking should be failsafe
            log.error("Exception while tracking", e);
        }
    }

    @Override
    @Subscribe
    @AllowConcurrentEvents
    public void unsubscribe(UnsubscribeEvent event) {
        try {
            if (!isEnabled(event.getRequest())) {
                return;
            }

            Map<String, Object> props = createEngagementProperties(event.getUser(), event.getRequest());
            setEngagementProp(props, MixpanelEngagementPropertyType.EMAIL_LIST_ID, event.getListId());

            MPMetrics mixpanel = getMixpanel(event.getUser());
            mixpanel.track(MixpanelEventType.UNSUBSCRIBE.getValue(), props);

            props = createPeopleProperties(event.getUser(), event.getRequest());
            setPeopleProp(props, MixpanelPeoplePropertyType.EMAIL_OPT_IN, false);

            mixpanel.set(props);
        } catch (Exception e) {
            // Tracking should be failsafe
            log.error("Exception while tracking", e);
        }
    }

    @Override
    @Subscribe
    @AllowConcurrentEvents
    public void motivators(MotivatorEvent event) {
        try {
            if (!isEnabled(event.getRequest())) {
                return;
            }

            User user = event.getUser();
            HttpServletRequest request = event.getRequest();
            Motivator motivator = event.getMotivator();

            if (motivator != null && motivator.getMtvtrs() != null && !motivator.getMtvtrs().isEmpty()) {

                // Engagement Event
                Map<String, Object> engagementProps = createEngagementProperties(user, request);
                setEngagementProp(engagementProps, MixpanelEngagementPropertyType.MOTIVATOR_SOURCE, motivator.getTp().name());

                // People data
                Map<String, Object> peopleProps = createPeopleProperties(user, request);
                setPeopleProp(peopleProps, MixpanelPeoplePropertyType.MOTIVATOR_SOURCE, motivator.getTp().name());

                for (String key : motivator.getMtvtrs().keySet()) {
                    setIfNotNull(engagementProps, MOTIVATOR_PROPERTY_BASE + key, motivator.getMtvtrs().get(key));
                    setIfNotNull(peopleProps, MOTIVATOR_PROPERTY_BASE + key, motivator.getMtvtrs().get(key));
                }

                // Send event
                MPMetrics mixpanel = getMixpanel(user);
                mixpanel.track(MixpanelEventType.MOTIVATORS.getValue(), engagementProps);

                // Set people data
                mixpanel.set(peopleProps);
            }
        } catch (Exception e) {
            // Tracking should be failsafe
            log.error("Exception while tracking", e);
        }
    }

    @Override
    @Subscribe
    @AllowConcurrentEvents
    public void quizStarted(QuizStartEvent event) {
        try {
            if (!isEnabled(event.getRequest())) {
                return;
            }

            User user = event.getUser();
            HttpServletRequest request = event.getRequest();
            AffiliateAccount affiliate = event.getAffiliate();
            Application application = event.getApplication();

            MPMetrics mixpanel = getMixpanel(user);

            // Send event
            Map<String, Object> props = createEngagementProperties(user, request);
            setEngagementProp(props, MixpanelEngagementPropertyType.QUIZ_AFFILIATE_ID, affiliate.getRlnm());
            setEngagementProp(props, MixpanelEngagementPropertyType.APPLICATION_ID, application.getRlnm());
            setEngagementProp(props, MixpanelEngagementPropertyType.QUIZ_ID, application.getTrlnm());

            mixpanel.track(MixpanelEventType.QUIZ_START.getValue(), props);
        } catch (Exception e) {
            // Tracking should be failsafe
            log.error("Exception while tracking", e);
        }
    }

    @Override
    @Subscribe
    @AllowConcurrentEvents
    public void quizFinished(QuizFinishEvent event) {
        try {
            if (!isEnabled(event.getRequest())) {
                return;
            }

            User user = event.getUser();
            HttpServletRequest request = event.getRequest();
            AffiliateAccount affiliate = event.getAffiliate();
            Application application = event.getApplication();

            MPMetrics mixpanel = getMixpanel(user);

            // Send event
            Map<String, Object> props = createEngagementProperties(user, request);
            setEngagementProp(props, MixpanelEngagementPropertyType.QUIZ_AFFILIATE_ID, affiliate.getRlnm());
            setEngagementProp(props, MixpanelEngagementPropertyType.APPLICATION_ID, application.getRlnm());
            setEngagementProp(props, MixpanelEngagementPropertyType.QUIZ_ID, application.getTrlnm());

            mixpanel.track(MixpanelEventType.QUIZ_FINISH.getValue(), props);
        } catch (Exception e) {
            // Tracking should be failsafe
            log.error("Exception while tracking", e);
        }
    }

    @Override
    @Subscribe
    @AllowConcurrentEvents
    public void quizStepFinished(QuizStepFinishEvent event) {
        try {
            if (!isEnabled(event.getRequest())) {
                return;
            }

            User user = event.getUser();
            HttpServletRequest request = event.getRequest();
            AffiliateAccount affiliate = event.getAffiliate();
            Application application = event.getApplication();

            MPMetrics mixpanel = getMixpanel(user);

            // Send event
            Map<String, Object> props = createEngagementProperties(user, request);
            setEngagementProp(props, MixpanelEngagementPropertyType.QUIZ_AFFILIATE_ID, affiliate.getRlnm());
            setEngagementProp(props, MixpanelEngagementPropertyType.APPLICATION_ID, application.getRlnm());
            setEngagementProp(props, MixpanelEngagementPropertyType.QUIZ_ID, application.getTrlnm());
            setEngagementProp(props, MixpanelEngagementPropertyType.QUIZ_STEP_ID, event.getStepId());

            mixpanel.track(MixpanelEventType.QUIZ_STEP_FINISH.getValue(), props);
        } catch (Exception e) {
            // Tracking should be failsafe
            log.error("Exception while tracking", e);
        }
    }

    @Override
    @Subscribe
    @AllowConcurrentEvents
    public void quizAnswers(QuizAnswersEvent event) {
        try {
            if (!isEnabled(event.getRequest())) {
                return;
            }

            User user = event.getUser();
            HttpServletRequest request = event.getRequest();
            QuizAnswers answers = event.getAnswers();

            MPMetrics mixpanel = getMixpanel(user);

            Map<String, Object> props = createEngagementProperties(user, request);
            setEngagementProp(props, MixpanelEngagementPropertyType.QUIZ_AFFILIATE_ID, answers.getAffiliateId());
            setEngagementProp(props, MixpanelEngagementPropertyType.APPLICATION_ID, answers.getApplicationId());
            setEngagementProp(props, MixpanelEngagementPropertyType.QUIZ_ID, answers.getQuizUrlName());

            for (QuizAnswer answer : answers.getList()) {
                setIfNotNull(props, QUESTION_PROPERTY_BASE + answer.getQuestionUrlName(), answer.getAnswerKey());
            }

            mixpanel.track(MixpanelEventType.QUIZ_ANSWERS.getValue(), props);

            // Send event
            props = createPeopleProperties(user, request);
            setPeopleProp(props, MixpanelPeoplePropertyType.QUIZ_AFFILIATE_ID, answers.getAffiliateId());
            setPeopleProp(props, MixpanelPeoplePropertyType.QUIZ_APPLICATION_ID, answers.getApplicationId());
            setPeopleProp(props, MixpanelPeoplePropertyType.QUIZ_ID, answers.getQuizUrlName());

            for (QuizAnswer answer : answers.getList()) {
                setIfNotNull(props, QUESTION_PROPERTY_BASE + answer.getQuestionUrlName(), answer.getAnswerKey());
            }

            mixpanel.set(props);
        } catch (Exception e) {
            // Tracking should be failsafe
            log.error("Exception while tracking", e);
        }
    }

    @Override
    @Subscribe
    @AllowConcurrentEvents
    public void eventParticipant(EventParticipantEvent event) {
        try {
            if (!isEnabled(event.getRequest())) {
                return;
            }

            String userCode = event.getUserCode();
            User user = userService.findUserByCode(event.getUserCode());
            UserTracker userTracker = null;
            HttpServletRequest request = event.getRequest();

            if (user == null) {
                userTracker = userTrackerService.findByUserCode(event.getUserCode());
            }

            MPMetrics mixpanel = null;
            if(user != null){
                mixpanel = getMixpanel(user, false);
            }
            else {
                mixpanel = getMixpanel(false);
                mixpanel.identify(userCode);
            }

            Map<String, Object> props = null;
            if(user!=null){
                props = createEngagementProperties(user, request);
            }
            else if (userTracker!=null){
                props = createEngagementProperties(userTracker, userCode);
            } else {
                props = new HashMap<String, Object>();
            }

            // Registration Event
            setEngagementProp(props, MixpanelEngagementPropertyType.EVENT_ID, event.getEvent().getRlnm());
            setEngagementProp(props, MixpanelEngagementPropertyType.EVENT_AFFILIATE_ID, event.getEvent().getFfltccntrlnm());
            setEngagementProp(props, MixpanelEngagementPropertyType.EVENT_CAMPAIGN_ID, event.getEvent().getCmpgnrlnm());

            mixpanel.track(MixpanelEventType.EVENT_PARTICIPANT.getValue(), props);

            // Track PEOPLE data
            if (user != null) {
                props = createPeopleProperties(user, request);
                setIfNotNull(props, EVENT_PROPERTY_BASE + event.getEvent().getRlnm(), true);
                mixpanel.set(props);
            }
        } catch (Exception e) {
            // Tracking should be failsafe
            log.error("Exception while tracking", e);
        }
    }

    private MPMetrics getMixpanel(User user) {
        return getMixpanel(user, false);
    }

    private MPMetrics getMixpanel(User user, boolean importMode) {
        MPMetrics mixpanel = MPMetrics.getInstance(mpConfig, importMode);
        mixpanel.identify(user.getCd());
        return mixpanel;
    }

    private MPMetrics getMixpanel() {
        return getMixpanel(false);
    }

    private MPMetrics getMixpanel(boolean importMode) {
        MPMetrics mixpanel = MPMetrics.getInstance(mpConfig, importMode);
        return mixpanel;
    }

    private Map<String, Object> createEngagementProperties(User user, HttpServletRequest request) {
        return createEngagementProperties(user, user.getCd(), request);
    }

    private Map<String, Object> createEngagementProperties(User user, String userCode, HttpServletRequest request) {
        Map<String, Object> props = RequestUtils.getProperties(request);

        // Add all super properties
        UserSupplement us = userService.findUserSupplement(userCode);
        AffiliateIdentifiers ai = userTrackerService.findAffiliateIdentifiers(userCode);
        props.putAll(getSuperProperties(user, userCode, us, ai, request));

        return props;
    }

    private Map<String, Object> createEngagementProperties(UserTracker userTracker, String userCode) {
        Map<String, Object> props = new HashMap<String, Object>();
        // Add all super properties
        UserSupplement us = userService.findUserSupplement(userTracker.getSrcd());
        AffiliateIdentifiers ai = userTrackerService.findAffiliateIdentifiers(userTracker.getSrcd());
        props.putAll(getSuperProperties(null, userCode, us, ai, null));

        return props;
    }

    private Map<String, Object> getSuperProperties(User user, String userCode, UserSupplement us, AffiliateIdentifiers ai, HttpServletRequest request) {
        Map<String, Object> props = new HashMap<String, Object>();

        // Ensure the distinct id is set
        setEngagementProp(props, MixpanelEngagementPropertyType.DISTINCT_ID, userCode);

        // Request based attributes
        if (request != null) {
            // Set the IP address
            setEngagementProp(props, MixpanelEngagementPropertyType.IP, request.getRemoteAddr());
            setEngagementProp(props, MixpanelEngagementPropertyType.IP_ADDRESS, request.getRemoteAddr());

            // Country code from IP address
            setEngagementProp(props, MixpanelEngagementPropertyType.COUNTRY_CODE, getCountryCode(request));

            // Search engine and keywords
            setEngagementProp(props, MixpanelEngagementPropertyType.SEARCH_ENGINE, RequestUtils.getSearchEngine(request));
            setEngagementProp(props, MixpanelEngagementPropertyType.SEARCH_KEYWORD, RequestUtils.getSearchKeyword(request));

            // User Agent
            setEngagementProp(props, MixpanelEngagementPropertyType.USER_AGENT, request.getHeader(WebConstants.USER_AGENT));

            // Device type
            Device device = DeviceUtils.getCurrentDevice(request);
            DeviceType type = DeviceType.NORMAL;
            if (device != null) {
                if (device.isMobile()) {
                    type = DeviceType.MOBILE;
                } else if (device.isTablet()) {
                    type = DeviceType.TABLET;
                }
            }
            setEngagementProp(props, MixpanelEngagementPropertyType.DEVICE_TYPE, type);
        }

        // Set the user full name for the streams feed
        String name = null;
        if (us != null) {
            name = us.getFllnm();
        }
        setEngagementProp(props, MixpanelEngagementPropertyType.STREAMS_NAME, name != null ? name : userCode);

        // User info
        if (us != null) {
            setEngagementProp(props, MixpanelEngagementPropertyType.FIRST_NAME, us.getFnm());
            setEngagementProp(props, MixpanelEngagementPropertyType.LAST_NAME, us.getLnm());
            setEngagementProp(props, MixpanelEngagementPropertyType.FULL_NAME, us.getFllnm());
            setEngagementProp(props, MixpanelEngagementPropertyType.GENDER, us.getGndr());
            setEngagementProp(props, MixpanelEngagementPropertyType.EMAIL, us.getMl());

            Motivator motivator = us.getMotivator();
            if (motivator != null && motivator.getMtvtrs() != null && !motivator.getMtvtrs().isEmpty()) {
                for (String key : motivator.getMtvtrs().keySet()) {
                    setIfNotNull(props, MOTIVATOR_PROPERTY_BASE + key, motivator.getMtvtrs().get(key));
                }
            }
        }

        // Affiliate info
        if (ai != null) {
            setEngagementProp(props, MixpanelEngagementPropertyType.AFFILIATE_ID, ai.getFfltccntrlnm());
            setEngagementProp(props, MixpanelEngagementPropertyType.CAMPAIGN_ID, ai.getCmpgnrlnm());
            setEngagementProp(props, MixpanelEngagementPropertyType.AFFILIATE_REFERRER_ID, ai.getRfrrlnm());
            setEngagementProp(props, MixpanelEngagementPropertyType.DOMAIN, ai.getDmn());
            setEngagementProp(props, MixpanelEngagementPropertyType.DOMAIN_AFFILIATE_ID, ai.getDmnffltrlnm());
        }

        return props;
    }

    private Map<String, Object> createPeopleProperties(User user, HttpServletRequest request) {
        Map<String, Object> props = new HashMap<String, Object>();

        setPeopleProp(props, MixpanelPeoplePropertyType.DISTINCT_ID, user.getCd());
        setPeopleProp(props, MixpanelPeoplePropertyType.LAST_SEEN, new Date());

        // Country code from IP address
        setEngagementProp(props, MixpanelEngagementPropertyType.COUNTRY_CODE, getCountryCode(request));

        return props;
    }

    private String getCountryCode(HttpServletRequest request) {
        if (request != null) {
            Location location = null;
            String remoteAddr = request.getRemoteAddr();
            if (remoteAddr.contains(":")) {
                log.debug("IPv6 location query");
                location = lookupServiceIpv6.getLocationV6(remoteAddr);
            } else {
                log.debug("IPv4 location query");
                location = lookupService.getLocation(remoteAddr);
            }

            if (location != null) {
                return location.countryCode;
            }
        }

        return null;
    }

    private void setEngagementProp(Map<String, Object> props, MixpanelEngagementPropertyType key, Object value) {
        setIfNotNull(props, key.getValue(), value);
    }

    private void setPeopleProp(Map<String, Object> props, MixpanelPeoplePropertyType key, Object value) {
        setIfNotNull(props, key.getValue(), value);
    }

    private void setIfNotNull(Map<String, Object> props, String key, Object value) {
        if (value != null) {
            props.put(key, value);
        }
    }
}
