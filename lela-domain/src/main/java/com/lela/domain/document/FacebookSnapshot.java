/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */
package com.lela.domain.document;

import com.lela.domain.dto.facebook.FacebookLocation;
import com.lela.domain.dto.facebook.FacebookReference;
import com.lela.domain.dto.facebook.FrequencyStats;
import com.lela.domain.dto.facebook.PoliticalAffiliation;
import com.lela.domain.enums.Education;
import com.lela.domain.enums.Gender;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * User: Chris Tallent
 * Date: 3/20/12
 * Time: 3:11 PM
 */
@Document
public class FacebookSnapshot extends AbstractDocument implements Serializable {
    private static final long serialVersionUID = 5207626958118838335L;

    @Indexed(unique = true)
    private String lelaEmail;
    private ObjectId lelaUserId;
    private Date snapshotDate;
    private Long snapshotTime;
    private String error;
    private boolean motivatorsDone = false;

    private String facebookId;
    private String facebookEmail;
    private Gender gender;
    private Locale locale;
    private int languageCount;
    private int timezone;
    private boolean bioAvailable = false;
    private Date birthday;
    private Integer age;
    private Education highestEducationLevel;    

    private FacebookLocation hometownLocation;
    private FacebookLocation currentLocation;
    
    private Float distanceFromCurrentToHometown;
    private Float distanceFromIpAddressToHometown;
    private Float distanceFromIpAddressToCurrent;

    private String relationshipStatus;
    private boolean significantOtherSpecified;
    private String significantOtherId;

    private String political;
    private PoliticalAffiliation politicalAffiliation;

    private boolean religionAvailable;
    private String religion;

    private boolean websiteSpecified;
    
    private boolean workSpecified;
    private Integer workCount;
    private String positionName;
    private String employerType;
    
    private Integer achievementCount;
    private Integer activityCount;
    
    private Integer albumCount;
    private boolean allStandardAlbumsHavePhotos;
    private Integer customAlbumCount;
    private Integer albumPhotoCount;
    private Integer profilePhotoCount;
    
    private Integer bookCount;

    private boolean familySpecified;
    private Integer friendCount;
    private Integer gameCount;
    private Integer groupCount;
    private Integer interestCount;
    
    private Integer movieCount;
    private List<FacebookReference> movies;
    private Integer musicCount;
    private List<FacebookReference> music;
    
    private Integer taggedPhotoCount;
    private Integer taggedPostCount;
    private Integer taggedVideoCount;

    private Integer subscriberCount;
    private Integer subscribedToCount;       

    private Integer televisionCount;

    private FrequencyStats statusStats;
    private FrequencyStats feedStats;
    private FrequencyStats postStats;
    private FrequencyStats eventStats;
    private FrequencyStats checkinStats;

    public Date getSnapshotDate() {
        return snapshotDate;
    }

    public void setSnapshotDate(Date snapshotDate) {
        this.snapshotDate = snapshotDate;
    }

    public Long getSnapshotTime() {
        return snapshotTime;
    }

    public void setSnapshotTime(Long snapshotTime) {
        this.snapshotTime = snapshotTime;
    }

    public ObjectId getLelaUserId() {
        return lelaUserId;
    }

    public void setLelaUserId(ObjectId lelaUserId) {
        this.lelaUserId = lelaUserId;
    }

    public String getLelaEmail() {
        return lelaEmail;
    }

    public void setLelaEmail(String lelaEmail) {
        this.lelaEmail = lelaEmail;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public int getLanguageCount() {
        return languageCount;
    }

    public void setLanguageCount(int languageCount) {
        this.languageCount = languageCount;
    }

    public int getTimezone() {
        return timezone;
    }

    public void setTimezone(int timezone) {
        this.timezone = timezone;
    }

    public boolean isBioAvailable() {
        return bioAvailable;
    }

    public void setBioAvailable(boolean bioAvailable) {
        this.bioAvailable = bioAvailable;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Education getHighestEducationLevel() {
        return highestEducationLevel;
    }

    public void setHighestEducationLevel(Education highestEducationLevel) {
        this.highestEducationLevel = highestEducationLevel;
    }

    public String getPolitical() {
        return political;
    }

    public void setPolitical(String political) {
        this.political = political;
    }

    public PoliticalAffiliation getPoliticalAffiliation() {
        return politicalAffiliation;
    }

    public void setPoliticalAffiliation(PoliticalAffiliation politicalAffiliation) {
        this.politicalAffiliation = politicalAffiliation;
    }

    public String getRelationshipStatus() {
        return relationshipStatus;
    }

    public void setRelationshipStatus(String relationshipStatus) {
        this.relationshipStatus = relationshipStatus;
    }

    public boolean isReligionAvailable() {
        return religionAvailable;
    }

    public void setReligionAvailable(boolean religionAvailable) {
        this.religionAvailable = religionAvailable;
    }

    public String getReligion() {
        return religion;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }

    public String getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
    }

    public String getFacebookEmail() {
        return facebookEmail;
    }

    public void setFacebookEmail(String facebookEmail) {
        this.facebookEmail = facebookEmail;
    }

    public boolean isSignificantOtherSpecified() {
        return significantOtherSpecified;
    }

    public void setSignificantOtherSpecified(boolean significantOtherSpecified) {
        this.significantOtherSpecified = significantOtherSpecified;
    }

    public String getSignificantOtherId() {
        return significantOtherId;
    }

    public void setSignificantOtherId(String significantOtherId) {
        this.significantOtherId = significantOtherId;
    }

    public boolean isWebsiteSpecified() {
        return websiteSpecified;
    }

    public void setWebsiteSpecified(boolean websiteSpecified) {
        this.websiteSpecified = websiteSpecified;
    }

    public boolean isWorkSpecified() {
        return workSpecified;
    }

    public void setWorkSpecified(boolean workSpecified) {
        this.workSpecified = workSpecified;
    }

    public Integer getWorkCount() {
        return workCount;
    }

    public void setWorkCount(Integer workCount) {
        this.workCount = workCount;
    }

    public String getEmployerType() {
        return employerType;
    }

    public void setEmployerType(String employerType) {
        this.employerType = employerType;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public FacebookLocation getHometownLocation() {
        return hometownLocation;
    }

    public void setHometownLocation(FacebookLocation hometownLocation) {
        this.hometownLocation = hometownLocation;
    }

    public FacebookLocation getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(FacebookLocation currentLocation) {
        this.currentLocation = currentLocation;
    }

    public Float getDistanceFromCurrentToHometown() {
        return distanceFromCurrentToHometown;
    }

    public void setDistanceFromCurrentToHometown(Float distanceFromCurrentToHometown) {
        this.distanceFromCurrentToHometown = distanceFromCurrentToHometown;
    }

    public Float getDistanceFromIpAddressToHometown() {
        return distanceFromIpAddressToHometown;
    }

    public void setDistanceFromIpAddressToHometown(Float distanceFromIpAddressToHometown) {
        this.distanceFromIpAddressToHometown = distanceFromIpAddressToHometown;
    }

    public Float getDistanceFromIpAddressToCurrent() {
        return distanceFromIpAddressToCurrent;
    }

    public void setDistanceFromIpAddressToCurrent(Float distanceFromIpAddressToCurrent) {
        this.distanceFromIpAddressToCurrent = distanceFromIpAddressToCurrent;
    }

    public Integer getAchievementCount() {
        return achievementCount;
    }

    public void setAchievementCount(Integer achievementCount) {
        this.achievementCount = achievementCount;
    }

    public Integer getActivityCount() {
        return activityCount;
    }

    public void setActivityCount(Integer activityCount) {
        this.activityCount = activityCount;
    }

    public Integer getAlbumCount() {
        return albumCount;
    }

    public void setAlbumCount(Integer albumCount) {
        this.albumCount = albumCount;
    }

    public boolean isAllStandardAlbumsHavePhotos() {
        return allStandardAlbumsHavePhotos;
    }

    public void setAllStandardAlbumsHavePhotos(boolean allStandardAlbumsHavePhotos) {
        this.allStandardAlbumsHavePhotos = allStandardAlbumsHavePhotos;
    }

    public Integer getCustomAlbumCount() {
        return customAlbumCount;
    }

    public void setCustomAlbumCount(Integer customAlbumCount) {
        this.customAlbumCount = customAlbumCount;
    }

    public Integer getAlbumPhotoCount() {
        return albumPhotoCount;
    }

    public void setAlbumPhotoCount(Integer albumPhotoCount) {
        this.albumPhotoCount = albumPhotoCount;
    }

    public Integer getBookCount() {
        return bookCount;
    }

    public void setBookCount(Integer bookCount) {
        this.bookCount = bookCount;
    }

    public boolean isFamilySpecified() {
        return familySpecified;
    }

    public void setFamilySpecified(boolean familySpecified) {
        this.familySpecified = familySpecified;
    }

    public Integer getFriendCount() {
        return friendCount;
    }

    public void setFriendCount(Integer friendCount) {
        this.friendCount = friendCount;
    }

    public Integer getGameCount() {
        return gameCount;
    }

    public void setGameCount(Integer gameCount) {
        this.gameCount = gameCount;
    }

    public Integer getGroupCount() {
        return groupCount;
    }

    public void setGroupCount(Integer groupCount) {
        this.groupCount = groupCount;
    }

    public Integer getInterestCount() {
        return interestCount;
    }

    public void setInterestCount(Integer interestCount) {
        this.interestCount = interestCount;
    }

    public Integer getMovieCount() {
        return movieCount;
    }

    public void setMovieCount(Integer movieCount) {
        this.movieCount = movieCount;
    }

    public Integer getMusicCount() {
        return musicCount;
    }

    public void setMusicCount(Integer musicCount) {
        this.musicCount = musicCount;
    }

    public List<FacebookReference> getMovies() {
        return movies;
    }

    public void setMovies(List<FacebookReference> movies) {
        this.movies = movies;
    }

    public List<FacebookReference> getMusic() {
        return music;
    }

    public void setMusic(List<FacebookReference> music) {
        this.music = music;
    }

    public Integer getTaggedPhotoCount() {
        return taggedPhotoCount;
    }

    public void setTaggedPhotoCount(Integer taggedPhotoCount) {
        this.taggedPhotoCount = taggedPhotoCount;
    }

    public Integer getProfilePhotoCount() {
        return profilePhotoCount;
    }

    public void setProfilePhotoCount(Integer profilePhotoCount) {
        this.profilePhotoCount = profilePhotoCount;
    }

    public Integer getSubscriberCount() {
        return subscriberCount;
    }

    public void setSubscriberCount(Integer subscriberCount) {
        this.subscriberCount = subscriberCount;
    }

    public Integer getSubscribedToCount() {
        return subscribedToCount;
    }

    public void setSubscribedToCount(Integer subscribedToCount) {
        this.subscribedToCount = subscribedToCount;
    }

    public Integer getTaggedPostCount() {
        return taggedPostCount;
    }

    public void setTaggedPostCount(Integer taggedPostCount) {
        this.taggedPostCount = taggedPostCount;
    }

    public Integer getTaggedVideoCount() {
        return taggedVideoCount;
    }

    public void setTaggedVideoCount(Integer taggedVideoCount) {
        this.taggedVideoCount = taggedVideoCount;
    }

    public Integer getTelevisionCount() {
        return televisionCount;
    }

    public void setTelevisionCount(Integer televisionCount) {
        this.televisionCount = televisionCount;
    }

    public FrequencyStats getStatusStats() {
        return statusStats;
    }

    public void setStatusStats(FrequencyStats statusStats) {
        this.statusStats = statusStats;
    }

    public FrequencyStats getFeedStats() {
        return feedStats;
    }

    public void setFeedStats(FrequencyStats feedStats) {
        this.feedStats = feedStats;
    }

    public FrequencyStats getPostStats() {
        return postStats;
    }

    public void setPostStats(FrequencyStats postStats) {
        this.postStats = postStats;
    }

    public FrequencyStats getEventStats() {
        return eventStats;
    }

    public void setEventStats(FrequencyStats eventStats) {
        this.eventStats = eventStats;
    }

    public FrequencyStats getCheckinStats() {
        return checkinStats;
    }

    public void setCheckinStats(FrequencyStats checkinStats) {
        this.checkinStats = checkinStats;
    }

    public boolean isMotivatorsDone() {
        return motivatorsDone;
    }

    public void setMotivatorsDone(boolean motivatorsDone) {
        this.motivatorsDone = motivatorsDone;
    }
}
