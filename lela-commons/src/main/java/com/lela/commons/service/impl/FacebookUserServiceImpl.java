/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */
package com.lela.commons.service.impl;

import com.lela.commons.event.EventHelper;
import com.lela.commons.event.SetAgeEvent;
import com.lela.domain.ApplicationConstants;
import com.lela.commons.repository.FacebookSnapshotRepository;
import com.lela.commons.service.FacebookUserService;
import com.lela.commons.service.TaskService;
import com.lela.commons.service.PostalCodeService;
import com.lela.commons.service.UserService;
import com.lela.domain.document.FacebookSnapshot;
import com.lela.domain.document.Motivator;
import com.lela.domain.document.PostalCode;
import com.lela.domain.document.Social;
import com.lela.domain.document.Task;
import com.lela.domain.document.User;
import com.lela.domain.document.UserSupplement;
import com.lela.domain.dto.facebook.FacebookLocation;
import com.lela.domain.dto.facebook.FacebookReference;
import com.lela.domain.dto.facebook.FrequencyStats;
import com.lela.domain.dto.facebook.PoliticalAffiliation;
import com.lela.domain.dto.user.Address;
import com.lela.domain.enums.AddressType;
import com.lela.domain.enums.Education;
import com.lela.domain.enums.Gender;
import com.lela.domain.enums.MotivatorSource;
import com.lela.domain.enums.TaskType;
import com.maxmind.geoip.Location;
import com.maxmind.geoip.LookupService;
import org.apache.commons.beanutils.BeanUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Months;
import org.joda.time.Weeks;
import org.joda.time.Years;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.social.NotAuthorizedException;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.facebook.api.Album;
import org.springframework.social.facebook.api.EducationEntry;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.FacebookProfile;
import org.springframework.social.facebook.api.Page;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Chris Tallent
 * Date: 3/20/12
 * Time: 9:49 PM
 */
@Service("facebookUserService")
public class FacebookUserServiceImpl implements FacebookUserService {
    private final static Logger log = LoggerFactory.getLogger(FacebookUserServiceImpl.class);

    private static final String GRAPH_API_URL = "https://graph.facebook.com/";

    public static final String EMPLOYER_CATEGORY = "category";
    public static final String EMPLOYER_CATEGORY_GENERIC_COMPANY = "Company";
    public static final int FETCH_LIMIT = 100;
    public static final int FREQUENCY_MAX_MONTHS_BACK = 3;
    private static final int FACEBOOK_TASK_STEPS = 27;
    private static final String TASK_USER_MESSAGE = "task.facebook.message";

    private SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy");

    private static final String HIGH_SCHOOL = "High School";
    private static final String COLLEGE = "College";
    
    private static final Float MOTIVATOR_LOCATION_MAX_SAME_DISTANCE = 25f;
    
    private final UsersConnectionRepository usersConnectionRepository;
    private final FacebookSnapshotRepository facebookSnapshotRepository;
    private final UserService userService;
    private final PostalCodeService postalCodeService;
    private final LookupService lookupService;
    private final LookupService lookupServiceIpv6;
    private final ObjectMapper mapper;
    private final TaskService taskService;
    private final TaskExecutor taskExecutor;

    @Autowired
    public FacebookUserServiceImpl(UsersConnectionRepository usersConnectionRepository,
                                   FacebookSnapshotRepository facebookSnapshotRepository,
                                   UserService userService,
                                   PostalCodeService postalCodeService,
                                   @Qualifier("geoipLookupServiceIpv4") LookupService lookupService,
                                   @Qualifier("geoipLookupServiceIpv6") LookupService lookupServiceIpv6,
                                   @Qualifier("socialObjectMapper") ObjectMapper mapper,
                                   @Qualifier("facebookTaskExecutor") TaskExecutor taskExecutor,
                                   TaskService taskService) {
        this.usersConnectionRepository = usersConnectionRepository;
        this.facebookSnapshotRepository = facebookSnapshotRepository;
        this.userService = userService;
        this.postalCodeService = postalCodeService;
        this.lookupService = lookupService;
        this.lookupServiceIpv6 = lookupServiceIpv6;
        this.mapper = mapper;
        this.taskService = taskService;
        this.taskExecutor = taskExecutor;
    }

    /**
     * This method must be synchronous so that the Task is created before the user can
     * possibly visit the quiz page, which has logic based on whether a task exists or not
     */
    @Override
    public void startFacebookSnapshotTask(final User user) {

        // ALSO start if FB snapshot are incomplete
        FacebookSnapshot snapshot = facebookSnapshotRepository.findByLelaEmailAddress(user.getMl());
        if (snapshot == null || snapshot.getError() != null) {
            log.info("No facebook snapshot, checking for Facebook social connection");
            List<Social> socials = userService.findSocials(user.getCd());

            // Has this user logged in through Facebook?
            boolean found = false;
            if (socials != null && !socials.isEmpty()) {
                for (Social social : socials) {
                    if (ApplicationConstants.FACEBOOK.equals(social.getProviderId())) {
                        found = true;
                        break;
                    }
                }
            }

            // Facebook social was found, generate a snapshot
            if (found) {

                // Don't create a Task if the user has Quiz Motivators
                boolean createTask = true;
                UserSupplement us = userService.findUserSupplement(user.getCd());
                if (us != null && us.getMotivator() != null && MotivatorSource.QUIZ.equals(us.getMotivator().getTp())) {
                    createTask = false;
                }

                Task task = null;
                if (createTask) {
                    task = new Task();
                    task.setRcpnt(user.getCd());
                    task.setStps(FACEBOOK_TASK_STEPS);
                    task.setCstp(1);
                    task.setTp(TaskType.FACEBOOK_DATA_AGGREGATION);
                    task.setMsg(TASK_USER_MESSAGE);

                    task = taskService.saveTask(task);
                }

                final Task savedTask = task;

                taskExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                        log.info("Generate facebook snapshot for user: " + user.getId());
                        FacebookSnapshot snapshot = internalSnapshot(user, savedTask);

                        log.info("Save facebook snapshot for user: " + user.getId());
                        snapshot = saveFacebookSnapshot(snapshot);

                        log.info("Calculate facebook motivators for user: " + user.getId());
                        calculateFacebookUserMotivators(snapshot);
                        } finally {
                            if (savedTask.getId() != null) {
                                taskService.removeTask(savedTask.getId());
                            }
                        }
                    }
                });
            }
        }
    }

    @Override
    public FacebookSnapshot findOrGenerateFacebookSnapshot(User user) {

        FacebookSnapshot snapshot = facebookSnapshotRepository.findByLelaEmailAddress(user.getMl());

        if (snapshot == null) {
            List<Social> socials = userService.findSocials(user.getCd());

            // Has this user logged in through Facebook?
            if (socials != null && !socials.isEmpty()) {
                for (Social social : socials) {
                    if (ApplicationConstants.FACEBOOK.equals(social.getProviderId())) {
                        snapshot = generateSnapshot(user);
                        break;
                    }
                }
            }
        }

        return snapshot;
    }

    @Override
    public FacebookSnapshot findFacebookSnapshot(User user) {
        return facebookSnapshotRepository.findByLelaEmailAddress(user.getMl());
    }

    @Override
    public FacebookSnapshot generateSnapshot(User user) {
        FacebookSnapshot snapshot = internalSnapshot(user, null);
        snapshot = saveFacebookSnapshot(snapshot);

        return snapshot;
    }

    @Override
    public FacebookSnapshot saveFacebookSnapshot(FacebookSnapshot snapshot) {
        FacebookSnapshot prior = facebookSnapshotRepository.findByLelaEmailAddress(snapshot.getLelaEmail());
        if (prior != null) {
            facebookSnapshotRepository.delete(prior);
        }
        return facebookSnapshotRepository.save(snapshot);
    }

    @Override
    public List<FacebookSnapshot> findFacebookSnapshotsWithoutMotivators() {
        return facebookSnapshotRepository.findValidWithoutMotivators();
    }

    @Override
    public void calculateFacebookUserMotivators(FacebookSnapshot snapshot) {

        User user = userService.findUserByEmail(snapshot.getLelaEmail());

        if (user == null) {
            throw new IllegalArgumentException("No user found for snapshot");
        }

        Map<String, Integer> motivators = calculateMotivators(snapshot);

        Motivator motivator = new Motivator(MotivatorSource.FACEBOOK, motivators);
        motivator.setMtvtrs(motivators);

        userService.saveMotivator(user.getCd(), motivator);
        facebookSnapshotRepository.flagAsMotivatorsDone(snapshot.getId());
    }

    @Override
    public void removeFacebookSnapshot(String lelaEmail) {
        facebookSnapshotRepository.removeByLelaEmail(lelaEmail);
    }

    @Override
    public Map<String, Integer> calculateMotivators(FacebookSnapshot snapshot) {

        // TODO - Tagged - should this be date bound? e.g. - 3 tags within last X months?
        Map<String, Float> motivators = new ConcurrentHashMap<String, Float>();

        if (snapshot != null) {
            motivators.put("A", 3f);
            motivators.put("B", 0f);
            motivators.put("C", 4.5f);
            motivators.put("D", 0f);
            motivators.put("E", 2f);
            motivators.put("F", 4f);

            motivatorA(snapshot, motivators);
            motivatorB(snapshot, motivators);
            motivatorC(snapshot, motivators);
            motivatorD(snapshot, motivators);
            motivatorE(snapshot, motivators);
            motivatorF(snapshot, motivators);
            
            // Constrain motivators 1 through 9
            for (String motivator : motivators.keySet()) {                
                if (motivators.get(motivator) < 1) {
                    motivators.put(motivator, 1f);
                } else if (motivators.get(motivator) > 9) {
                    motivators.put(motivator, 9f);
                }
            }
        }

        // Round to integer
        Map<String, Integer> rounded = new HashMap<String, Integer>();
        for (String key : motivators.keySet()) {
            rounded.put(key, (int)Math.floor(motivators.get(key)));
        }

        return rounded;
    }

    @Override
    public void postOnTimeline(User user, String content) {
        try {
            Facebook fb = getFacebookApi(user);

            fb.feedOperations().updateStatus(content);
        } catch (Exception ex) {
            if (log.isDebugEnabled()) {
                log.debug("User did not give us permission to post to his wall");
            }
        }
    }

    private void motivatorA(FacebookSnapshot snapshot, Map<String, Float> motivators) {

        float val = motivators.get("A");
        
        if (snapshot.getLanguageCount() > 1) {
            val += 1;
        }

        if (snapshot.getDistanceFromCurrentToHometown() != null) {
            if (new Float(0).equals(snapshot.getDistanceFromCurrentToHometown())) {
                val += 2;
            }
        } else if (snapshot.getDistanceFromIpAddressToHometown() != null) {
            if (MOTIVATOR_LOCATION_MAX_SAME_DISTANCE >= snapshot.getDistanceFromIpAddressToHometown()) {
                val += 2;
            }
        }

        if (PoliticalAffiliation.DEMOCRAT.equals(snapshot.getPoliticalAffiliation()) ||
            PoliticalAffiliation.INDEPENDENT.equals(snapshot.getPoliticalAffiliation())) {
            val -= 1;
        } else if (PoliticalAffiliation.REPUBLICAN.equals(snapshot.getPoliticalAffiliation())) {
            val += 1;
        }

        boolean accountedForRelationship = false;
        if (snapshot.getRelationshipStatus() != null) {
            String status = snapshot.getRelationshipStatus().toLowerCase();
            if ("married".equals(status) ||
                "engaged".equals(status) ||
                "in an open relationship".equals(status) ||
                "in a civil union".equals(status) ||
                "in a domestic partnership".equals(status)) {
                val += 1;
                accountedForRelationship = true;
            }
        }

        if (!accountedForRelationship && snapshot.isSignificantOtherSpecified()) {
            val += 1;
        }

        if (snapshot.isReligionAvailable()) {
            val += 1;
        }

        if (snapshot.getCheckinStats() != null && snapshot.getCheckinStats().getPerWeek() >= 1) {
            val += 1;
        }

        if (snapshot.isFamilySpecified()) {
            val += 1;
        }
        
        motivators.put("A", val);
    }

    private void motivatorB(FacebookSnapshot snapshot, Map<String, Float> motivators) {
        float val = motivators.get("B");

        if (snapshot.getDistanceFromCurrentToHometown() != null) {
            if (new Float(0).equals(snapshot.getDistanceFromCurrentToHometown())) {
                val += 2;
            }
        } else if (snapshot.getDistanceFromIpAddressToHometown() != null) {
            if (MOTIVATOR_LOCATION_MAX_SAME_DISTANCE >= snapshot.getDistanceFromIpAddressToHometown()) {
                val += 2;
            }
        }
        
        if (StringUtils.hasText(snapshot.getPolitical())) {
            val += 0.5;
        }

        boolean accountedForRelationship = false;
        if (snapshot.getRelationshipStatus() != null) {
            String status = snapshot.getRelationshipStatus().toLowerCase();
            if ("married".equals(status) ||
                    "engaged".equals(status) ||
                    "in an open relationship".equals(status) ||
                    "in a civil union".equals(status) ||
                    "in a domestic partnership".equals(status)) {
                val += 0.5;
                accountedForRelationship = true;
            }
        }

        if (!accountedForRelationship && snapshot.isSignificantOtherSpecified()) {
            val += 1;
        }

        if (snapshot.isReligionAvailable()) {
            val += 1;
        }

        if (snapshot.getCheckinStats() != null && snapshot.getCheckinStats().getPerWeek() >= 1) {
            val += 1;
        }

        if (snapshot.isFamilySpecified()) {
            val += 1;
        }

        if (snapshot.getFeedStats() != null && snapshot.getFeedStats().getPerDay() >= 1) {
            val += 1;
        }

        if (snapshot.getFriendCount() != null) {
            if (snapshot.getFriendCount() > 300 && snapshot.getFriendCount() <= 1000) {
                val += 0.5;
            } else if (snapshot.getFriendCount() > 1000) {
                val += 1;
            }
        }

        if (snapshot.getGroupCount() != null) {
            if (snapshot.getGroupCount() >= 5) {
                val += 1;
            } else if (snapshot.getGroupCount() >= 1) {
                val += 0.5;
            }
        }

        if (snapshot.getPostStats() != null && snapshot.getPostStats().getPerMonth() >= 5) {
            val += 1;
        }
        
        if (snapshot.getTaggedVideoCount() != null && snapshot.getTaggedVideoCount() > 0) {
            val += 1;
        }

        motivators.put("B", val);
    }

    private void motivatorC(FacebookSnapshot snapshot, Map<String, Float> motivators) {
        float val = motivators.get("C");

        if (Education.COLLEGE.equals(snapshot.getHighestEducationLevel())) {
            val -= 0.5;
        }
        
        if (snapshot.isWorkSpecified()) {
            val -= 0.5;
        }

        motivators.put("C", val);
    }

    private void motivatorD(FacebookSnapshot snapshot, Map<String, Float> motivators) {
        float val = motivators.get("D");

        if (snapshot.getBirthday() != null) {
            val += 1;
        }

        if (snapshot.isWebsiteSpecified()) {
            val += 0.5;
        }

        if (snapshot.getActivityCount() != null && snapshot.getActivityCount() > 0) {
            val += 0.5;
        }

        if (snapshot.getCustomAlbumCount() != null) {
            if (snapshot.getCustomAlbumCount() > 1) {
                val += 2;
            } else if (snapshot.getCustomAlbumCount() == 1) {
                val += 1;
            }
        }

        if (snapshot.getCheckinStats() != null && snapshot.getCheckinStats().getPerWeek() >= 1) {
            val += 1;
        }
        
        if (snapshot.getEventStats() != null) {
            if (snapshot.getEventStats().getPerWeek() >= 1) {
                val += 2;
            } else if (snapshot.getEventStats().getTotal() > 0) {
                val += 0.5;
            }
        }

        if (snapshot.getFeedStats() != null && snapshot.getFeedStats().getPerDay() >= 1) {
            val += 1;
        }

        if (snapshot.getFriendCount() != null) {
            if (snapshot.getFriendCount() > 300 && snapshot.getFriendCount() <= 1000) {
                val += 0.5;
            } else if (snapshot.getFriendCount() > 1000) {
                val += 1;
            }
        }

        if (snapshot.getGameCount() != null && snapshot.getGameCount() > 0) {
            val += 0.5;
        }

        if (snapshot.getInterestCount() != null && snapshot.getInterestCount() > 3) {
            val += 1;
        }

        if (snapshot.getTaggedPhotoCount() != null && snapshot.getTaggedPhotoCount() > 5) {
            val += 0.5;
        }

        if (snapshot.getProfilePhotoCount() != null) {
            if (snapshot.getProfilePhotoCount() >= 10) {
                val += 1;
            } else if (snapshot.getProfilePhotoCount() > 1) {
                val += 0.5;
            }
        }

        if (snapshot.getPostStats() != null && snapshot.getPostStats().getPerMonth() >= 5) {
            val += 1;
        }

        if (snapshot.getTaggedPostCount() != null && snapshot.getTaggedPostCount() > 3) {
            val += 0.5;
        }

        if (snapshot.getTaggedVideoCount() != null && snapshot.getTaggedVideoCount() > 0) {
            val += 1;
        }

        motivators.put("D", val);
    }

    private void motivatorE(FacebookSnapshot snapshot, Map<String, Float> motivators) {
        float val = motivators.get("E");

        if (snapshot.isBioAvailable()) {
            val += 2;
        }

        if (Education.COLLEGE.equals(snapshot.getHighestEducationLevel())) {
            val += 1;
        }

        if (snapshot.isReligionAvailable()) {
            val += 1;
        }

        if (snapshot.isWebsiteSpecified()) {
            val += 0.5;
        }

        if (snapshot.isWorkSpecified()) {
            val += 1;
        }

        if (snapshot.getAchievementCount() != null) {
            val += snapshot.getAchievementCount();
        }

        if (snapshot.getBookCount() != null && snapshot.getBookCount() > 0) {
            val += 0.5;
        }

        if (snapshot.getFriendCount() != null && snapshot.getFriendCount() > 1000) {
            val += 1;
        }

        if (snapshot.getInterestCount() != null && snapshot.getInterestCount() > 3) {
            val += 1;
        }

        if (snapshot.getTaggedPostCount() != null && snapshot.getTaggedPostCount() > 3) {
            val += 0.5;
        }

        motivators.put("E", val);
    }

    private void motivatorF(FacebookSnapshot snapshot, Map<String, Float> motivators) {
        float val = motivators.get("F");

        if (snapshot.getBirthday() != null) {
            val += 1;
        }

        if (snapshot.getCheckinStats() != null && snapshot.getCheckinStats().getPerWeek() >= 1) {
            val += 1;
        }

        if (snapshot.getFriendCount() != null && snapshot.getFriendCount() > 1000) {
            val += 1;
        }

        if (snapshot.getCustomAlbumCount() != null) {
            if (snapshot.getCustomAlbumCount() > 1) {
                val += 2;
            } else if (snapshot.getCustomAlbumCount() == 1) {
                val += 1;
            }
        }

        motivators.put("F", val);
    }

    private FacebookSnapshot internalSnapshot(User user, Task task) {

        FacebookSnapshot snapshot = new FacebookSnapshot();
        snapshot.setLelaUserId(user.getId());
        snapshot.setLelaEmail(user.getMl());
        snapshot.setSnapshotDate(new Date());

        Date start = new Date();

        try {
            log.warn("Start snapshot: " + new Date());

            Facebook fb = getFacebookApi(user);

            FacebookProfile profile = fb.userOperations().getUserProfile();
            increment(task);

            snapshot.setFacebookId(profile.getId());

            // Gender
            if (profile.getGender() != null) {
                if (Gender.MALE.toString().equals(profile.getGender().toUpperCase())) {
                    snapshot.setGender(Gender.MALE);
                } else if (Gender.FEMALE.toString().equals(profile.getGender().toUpperCase())) {
                    snapshot.setGender(Gender.FEMALE);
                }

                // SAVE THIS TO THE USER SUPPLEMENT
                userService.saveGender(user.getCd(), snapshot.getGender());
            }

            // Locale and Timezone (Timezone will only work if the user is currently logged in)
            snapshot.setLocale(profile.getLocale());
            snapshot.setTimezone(profile.getTimezone());

            // Determine hometown location
            JsonNode hometownResponse = fqlQuery(fb, "SELECT hometown_location FROM user WHERE uid=" + profile.getId(), JsonNode.class);
            JsonNode hometown = hometownResponse.path("data").path(0).get("hometown_location");
            if (hometown != null) {
                try {
                    FacebookLocation hometownLocation = mapper.treeToValue(hometown, FacebookLocation.class);
                    snapshot.setHometownLocation(hometownLocation);

                    // Try to locate the lon/lat coordinates
                    if (hometownLocation != null) {
                        PostalCode postalCode = postalCodeService.findPostalCodeByCityAndStateName(hometownLocation.getCity(), hometownLocation.getState());
                        if (postalCode != null) {
                            hometownLocation.setCoordinates(postalCode.getLc());
                        }
                    }
                } catch (IOException e) {
                    log.error("Could not parse hometown location", e);
                }
            }
            increment(task);

            // Determine current location
            JsonNode currentResponse = fqlQuery(fb, "SELECT current_location FROM user WHERE uid=" + profile.getId(), JsonNode.class);
            JsonNode current = currentResponse.path("data").path(0).get("current_location");
            if (current != null) {
                try {
                    FacebookLocation currentLocation = mapper.treeToValue(current, FacebookLocation.class);
                    snapshot.setCurrentLocation(currentLocation);

                    // Try to locate the lon/lat coordinates
                    if (currentLocation != null) {
                        PostalCode postalCode = postalCodeService.findPostalCodeByCityAndStateName(currentLocation.getCity(), currentLocation.getState());
                        if (postalCode != null) {
                            currentLocation.setCoordinates(postalCode.getLc());
                        }

                        // Also set the address on the user supplement object
                        Address address = new Address(AddressType.HOME,
                                currentLocation.getCity(),
                                currentLocation.getState(),
                                postalCode != null ? postalCode.getCd() : null);
                        userService.addOrUpdateAddress(user.getCd(), address);
                    }
                } catch (IOException e) {
                    log.error("Could not parse current location", e);
                }
            }
            increment(task);

            // Find the distance from hometown to current location
            if (snapshot.getHometownLocation() != null && snapshot.getHometownLocation().getCoordinates() != null &&
                snapshot.getCurrentLocation() != null && snapshot.getCurrentLocation().getCoordinates() != null) {

                Float[] home = snapshot.getHometownLocation().getCoordinates();
                Float[] curr = snapshot.getCurrentLocation().getCoordinates();

                double distance = getDistance(home[1], home[0], curr[1], curr[0]);
                snapshot.setDistanceFromCurrentToHometown((float)distance);
            }

            // Find the location from the requester's IP address
            // And determine distance from hometown
            Location location = null;
            if (user.getLgnrmtddrss() != null) {
                if (user.getLgnrmtddrss().contains(":")) {
                    log.debug("IPv6 location query");
                    location = lookupServiceIpv6.getLocationV6(user.getLgnrmtddrss());
                } else {
                    log.debug("IPv4 location query");
                    location = lookupService.getLocation(user.getLgnrmtddrss());
                }
            }

            if (location != null) {
                if (snapshot.getHometownLocation() != null && snapshot.getHometownLocation().getCoordinates() != null) {
                    Float[] coords = snapshot.getHometownLocation().getCoordinates();

                    double distance = getDistance(location.latitude, location.longitude, coords[1], coords[0]);
                    snapshot.setDistanceFromIpAddressToHometown((float)distance);
                }

                if (snapshot.getCurrentLocation() != null && snapshot.getCurrentLocation().getCoordinates() != null) {
                    Float[] coords = snapshot.getCurrentLocation().getCoordinates();

                    double distance = getDistance(location.latitude, location.longitude, coords[1], coords[0]);
                    snapshot.setDistanceFromIpAddressToCurrent((float)distance);
                }
            }

            snapshot.setLanguageCount(profile.getLanguages() != null ? profile.getLanguages().size() : 0);
            snapshot.setBioAvailable(StringUtils.hasText(profile.getBio()));

            // Convert the birthday to a date
            if (StringUtils.hasText(profile.getBirthday())) {
                try {
                    snapshot.setBirthday(DATE_FORMAT.parse(profile.getBirthday()));
                } catch (ParseException e) {
                    log.error(String.format("Could not parse facebook birthdate %s for user %s", profile.getBirthday(), user.getMl()), e);
                }
            }

            // If birthday was available, calculate age
            if (snapshot.getBirthday() != null) {
                DateMidnight birthday = new DateMidnight(snapshot.getBirthday().getTime());
                snapshot.setAge(Years.yearsBetween(birthday, new DateTime()).getYears());
                userService.saveAge(user.getCd(), snapshot.getAge());
            }

            // Determine level of education
            if (profile.getEducation() != null) {
                for (EducationEntry entry : profile.getEducation()) {
                    if (StringUtils.hasText(entry.getType())) {
                        if (COLLEGE.equals(entry.getType())) {
                            snapshot.setHighestEducationLevel(Education.COLLEGE);
                            break;
                        } else if (HIGH_SCHOOL.equals(entry.getType())) {
                            snapshot.setHighestEducationLevel(Education.HIGH_SCHOOL);
                        }
                    }
                }
            }

            // Political affiliation
            snapshot.setPolitical(profile.getPolitical());
            snapshot.setPoliticalAffiliation(PoliticalAffiliation.getPoliticalAffiliation(profile.getPolitical()));

            // Relationship status
            snapshot.setRelationshipStatus(profile.getRelationshipStatus());
            if (profile.getSignificantOther() != null) {
                snapshot.setSignificantOtherSpecified(true);
                snapshot.setSignificantOtherId(profile.getSignificantOther().getId());
            }

            // Religious affiliation
            snapshot.setReligionAvailable(StringUtils.hasText(profile.getReligion()));
            snapshot.setReligion(profile.getReligion());

            // Website
            snapshot.setWebsiteSpecified(StringUtils.hasText(profile.getWebsite()));

            // Evaluate work
            // # of jobs, average length of employment, type, current job
            if (profile.getWork() != null) {

                snapshot.setWorkSpecified(true);
                snapshot.setWorkCount(profile.getWork().size());

                // Manually retrieve work using FQL to get location and position
                JsonNode response = fqlQuery(fb, "SELECT work FROM user where uid=" + profile.getId(), JsonNode.class);
                JsonNode data = response.path("data").path(0);
                for (JsonNode work : data.path("work")) {

                    // Get the position name
                    if (snapshot.getPositionName() == null) {
                        snapshot.setPositionName(work.path("position").path("name").asText());
                    }

                    // Try to fetch the employer record to get the work type
                    String employerId = work.path("employer").path("id").asText();
                    JsonNode employer = fb.fetchObject(employerId, JsonNode.class);

                    // Find the first category
                    String category = employer.path(EMPLOYER_CATEGORY).asText();
                    if (snapshot.getEmployerType() == null && category != null && !EMPLOYER_CATEGORY_GENERIC_COMPANY.equals(category)) {
                        snapshot.setEmployerType(category);
                    }
                }
            }
            increment(task);

            // Achievements
            snapshot.setAchievementCount(connectionCount(fb, profile.getId(), "achievements", 0, 0));
            increment(task);

            // Activities
            snapshot.setActivityCount(connectionCount(fb, profile.getId(), "activities", 0, 0));
            increment(task);

            // Albums / Owned photos
            List<Album> albums = fb.mediaOperations().getAlbums();
            if (albums != null) {
                snapshot.setAlbumCount(albums.size());
                int customAlbumCount = 0;
                boolean allStandardHavePhotos = true;

                // Count owned photos
                int photoCount = 0;
                for (Album album : albums) {
                    photoCount += album.getCount();

                    // If this is the profile album, set the number of profile pics
                    if (Album.Type.PROFILE.equals(album.getType())) {
                        snapshot.setProfilePhotoCount(album.getCount());
                    }

                    // If this is a custom album increment the count
                    if (Album.Type.NORMAL.equals(album.getType()) ||
                        Album.Type.UNKNOWN.equals(album.getType())) {
                        customAlbumCount++;
                    } else {
                        if (album.getCount() == 0) {
                            allStandardHavePhotos = false;
                        }
                    }

                }

                snapshot.setAlbumPhotoCount(photoCount);
                snapshot.setCustomAlbumCount(customAlbumCount);
                snapshot.setAllStandardAlbumsHavePhotos(allStandardHavePhotos);
            }
            increment(task);

            // Books
            snapshot.setBookCount(connectionCount(fb, profile.getId(), "books", 0, 0));
            increment(task);

            // Family
            int familyCount = connectionCount(fb, profile.getId(), "family", 0, 0);
            snapshot.setFamilySpecified(familyCount > 0);
            increment(task);

            // Friend Count
            List<String> friends = fb.friendOperations().getFriendIds(profile.getId());
            snapshot.setFriendCount(friends != null ? friends.size() : 0);
            increment(task);

            // Game count
            snapshot.setGameCount(connectionCount(fb, profile.getId(), "games", 0, 0));
            increment(task);

            // Groups count
            snapshot.setGroupCount(connectionCount(fb, profile.getId(), "groups", 0, 0));
            increment(task);

            // Interests count
            snapshot.setInterestCount(connectionCount(fb, profile.getId(), "interests", 0, 0));
            increment(task);

            // Movies
            List<Page> movies = fb.likeOperations().getMovies(profile.getId());
            if (movies != null) {
                snapshot.setMovieCount(movies.size());

                List<FacebookReference> movieRefs = new ArrayList<FacebookReference>(movies.size());
                for (Page movie: movies) {
                    movieRefs.add(new FacebookReference(movie.getId(), movie.getName(), movie.getCategory()));
                }

                snapshot.setMovies(movieRefs);
            }
            increment(task);

            // Music
            List<Page> music = fb.likeOperations().getMusic(profile.getId());
            if (music != null) {
                snapshot.setMusicCount(music.size());
                List<FacebookReference> musicRefs = new ArrayList<FacebookReference>(music.size());
                for (Page item : music) {
                    musicRefs.add(new FacebookReference(item.getId(), item.getName(), item.getCategory()));
                }

                snapshot.setMusic(musicRefs);
            }
            increment(task);

            // Tagged photos
            snapshot.setTaggedPhotoCount(connectionCount(fb, profile.getId(), "photos", 0, 0));
            increment(task);

            // Tagged posts
            snapshot.setTaggedPostCount(connectionCount(fb, profile.getId(), "tagged", 0, 0));
            increment(task);

            // Tagged videos
            snapshot.setTaggedVideoCount(connectionCount(fb, profile.getId(), "videos", 0, 0));
            increment(task);

            // Subscribers
            snapshot.setSubscriberCount(connectionCount(fb, profile.getId(), "subscribers", 0, 0));
            increment(task);

            // Subscribed To
            snapshot.setSubscribedToCount(connectionCount(fb, profile.getId(), "subscribedto", 0, 0));
            increment(task);

            // Television
            snapshot.setTelevisionCount(connectionCount(fb, profile.getId(), "television", 0, 0));
            increment(task);

            // Status count and frequency
            snapshot.setStatusStats(connectionCountAndFrequency(fb, profile.getId(), "statuses", "updated_time", "yyyy-MM-dd'T'kk:mm:ss'+'SSSS"));
            increment(task);

            // Feed count and frequency
            snapshot.setFeedStats(connectionCountAndFrequency(fb, profile.getId(), "feed", "updated_time", "yyyy-MM-dd'T'kk:mm:ss'+'SSSS"));
            increment(task);

            // Post count and frequency
            snapshot.setPostStats(connectionCountAndFrequency(fb, profile.getId(), "posts", "updated_time", "yyyy-MM-dd'T'kk:mm:ss'+'SSSS"));
            increment(task);

            // Checkin count and frequency
            snapshot.setCheckinStats(connectionCountAndFrequency(fb, profile.getId(), "checkins", "created_time", "yyyy-MM-dd'T'kk:mm:ss'+'SSSS"));
            increment(task);

            // Event count and frequency
            snapshot.setEventStats(connectionCountAndFrequency(fb, profile.getId(), "events", "start_time", "yyyy-MM-dd'T'kk:mm:ss"));
            increment(task);

            // TODO - LIKES ... cannot get a list directly from FB... have to get a list of IDS then
            // query each one individually

            snapshot.setSnapshotTime(new Date().getTime() - start.getTime());

            log.warn("End snapshot: " + new Date());
        } catch (NotAuthorizedException e) {
            snapshot.setError("Cannot access Facebook account.  User has either not logged in using Facebook recently or must agree to new permissions");
            log.error("Facebook Snapshot error - No Authorized", e);
        } catch (OAuth2Exception e) {
            snapshot.setError("Cannot access Facebook API");
            log.error("Facebook Snapshot error - Could not authenticate", e);
        } catch (Exception e) {
            snapshot.setError("Unexpected Exception - " + e.getMessage());
            log.error("Facebook Snapshot error - Unexpected Exception", e);
        }

        return snapshot;
    }

    private void increment(Task task) {
        if (task != null) {
            task.setCstp(task.getCstp()+1);
            taskService.saveTask(task);
        }
    }
    
    private interface Filter<T> {
        public boolean include(T item);
    }

    private FrequencyStats connectionCountAndFrequency(Facebook fb, String userId, String connection, String dateProperty, String dateFormat) {
        return connectionCountAndFrequency(fb, userId, connection, dateProperty, dateFormat, null);        
    }
    
    private FrequencyStats connectionCountAndFrequency(Facebook fb, String userId, String connection, String dateProperty, String dateFormat, Filter filter) {
        SimpleDateFormat format = new SimpleDateFormat(dateFormat);
        FrequencyStats stats = new FrequencyStats();

        // Determine max months to go back to establish frequency
        DateTime minDate = new DateTime();
        minDate = minDate.minusMonths(FREQUENCY_MAX_MONTHS_BACK);
        
        // Gather the date and count data from facebook
        connectionCountAndFrequency(fb, userId, connection, new Date(minDate.getMillis()), dateProperty, format, stats, 0, filter);

        // Calculate frequencies assuming there is a valid start and end date
        if (stats.getStartDate() != null && stats.getEndDate()!= null) {
            DateTime start = new DateTime(stats.getStartDate().getTime());
            DateTime end = new DateTime(stats.getEndDate().getTime());

            int days = Days.daysBetween(start, end).getDays();
            int weeks = Weeks.weeksBetween(start, end).getWeeks();
            int months = Months.monthsBetween(start, end).getMonths();
            
            if (days > 0) {
                stats.setPerDay(stats.getTotal() / (float)days);
            }

            if (weeks > 0) {
                stats.setPerWeek(stats.getTotal() / (float) weeks);
            }

            if (months > 0) {
                stats.setPerMonth(stats.getTotal() / (float)months);
            }
        }

        return stats;
    }

    private void connectionCountAndFrequency(Facebook fb, String userId, String connection, Date minDate, String dateProperty, SimpleDateFormat format, FrequencyStats stats, int offset, Filter<Map> filter) {
        MultiValueMap params = new LinkedMultiValueMap<String, String>();
        params.set("offset", String.valueOf(offset));
        params.set("limit", String.valueOf(FETCH_LIMIT));

        List<Map> connections = fb.fetchConnections(userId, connection, Map.class, params);

        // Evaluate the date property
        if (connections != null) {
            for (Map data : connections) {
                try {
                    String dateString = (String)BeanUtils.getNestedProperty(data, dateProperty);
                    Date date = format.parse(dateString);

                    // Don't count connections before the min date... we can't automatically
                    // terminate the queries because we don't know the items are date ordered
                    if (date.after(minDate)) {
                        
                        // Check against the filter if one is defined otherwise exclude
                        if (filter != null) {
                            if (!filter.include(data)) {
                                continue;
                            }
                        }
                        
                        // Only count connections where we can evaluate the date
                        stats.setTotal(stats.getTotal() + 1);

                        if (stats.getStartDate() == null || stats.getStartDate().after(date)) {
                            stats.setStartDate(date);
                        }

                        if (stats.getEndDate() == null || stats.getEndDate().before(date)) {
                            stats.setEndDate(date);
                        }
                    }
                } catch (Exception e) {
                    log.error(String.format("Could not evaluate date property %s for connection %s for user id: %s", dateProperty, connection, userId));
                }
            }
        }
        
        if (connections != null && connections.size() == FETCH_LIMIT) {
            connectionCountAndFrequency(fb, userId, connection, minDate, dateProperty, format, stats, offset + FETCH_LIMIT, filter);
        }
    }

    private int connectionCount(Facebook fb, String userId, String connection, int count, int offset) {
        MultiValueMap params = new LinkedMultiValueMap<String, String>();
        params.set("offset", String.valueOf(offset));
        params.set("limit", String.valueOf(FETCH_LIMIT));

        List<JsonNode> connections = fb.fetchConnections(userId, connection, JsonNode.class, params);
        count += connections != null ? connections.size() : 0;

        if (connections != null && connections.size() == FETCH_LIMIT) {
            count += connectionCount(fb, userId, connection, count, offset + FETCH_LIMIT);
        }
        
        return count;
    }

    private <T> T fqlQuery(Facebook fb, String fql, Class<T> responseClass) {
        String url = GRAPH_API_URL + "fql?q={query}";
        
        T response = fb.restOperations().<T>getForObject(url, responseClass, fql);

        return response;
    }

    private double getDistance(double lat1, double lon1, double lat2, double lon2) {
        double radius = 3959d; // Radius of Earth: 3959 miles or 6378.1 kilometers
        double dLat = toRadians(lat2 - lat1);
        double dLon = toRadians(lon2 - lon1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
        Math.cos(toRadians(lat1)) *   Math.cos(toRadians(lat2)) *
        Math.sin(dLon/2) * Math.sin(dLon/2);
        double c = 2 * Math.asin(Math.sqrt(a));
        return radius * c;
    }

    private double toRadians(Double degree){
        // Value degree * Pi/180
        Double res = degree * 3.1415926 / 180;
        return res;
    }

    private Facebook getFacebookApi(User user) {
        // how to retrieve a facebook connection
        ConnectionRepository connectionRepository = usersConnectionRepository.createConnectionRepository(user.getIdString());
        Connection<Facebook> connection = connectionRepository.findPrimaryConnection(Facebook.class);
        Facebook facebook = connection != null ? connection.getApi() : new FacebookTemplate();

        return facebook;
    }
}
