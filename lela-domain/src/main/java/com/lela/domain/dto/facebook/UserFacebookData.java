/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */
package com.lela.domain.dto.facebook;

import org.springframework.social.facebook.api.FacebookProfile;
import org.springframework.social.facebook.api.Page;
import org.springframework.social.facebook.api.Post;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Martin Gamboa
 * Date: 1/30/12
 * Time: 12:30 PM
 */
public class UserFacebookData {
    String userEmail;
    FacebookProfile userProfile;
    List<Page> activities;
    List<Page> books;
    List<Page> interests;
    List<Page> movies;
    List<Page> music;
    List<Page> television;
    List<FacebookProfile> friends;
    List<FacebookProfile> familyMembers;
    Long distanceFromHometownToLocation;
    Integer postCount;
    String postFrequency;
    Boolean postDaily;
    List<Post> posts;
    List<Post> feed;
    Integer photoVideoCount;

    public UserFacebookData() {

    }

    public UserFacebookData(String userEmail, FacebookProfile userProfile, List<Page> activities, List<Page> books,
                            List<Page> interests, List<Page> movies, List<Page> music, List<Page> television,
                            List<FacebookProfile> friends, List<FacebookProfile> familyMembers, Long distanceFromHometownToLocation,
                            Integer postCount, String postFrequency, Boolean postDaily, List<Post> posts, Integer photoVideoCount) {
        this.userEmail = userEmail;
        this.userProfile = userProfile;
        this.activities = activities;
        this.books = books;
        this.interests = interests;
        this.movies = movies;
        this.music = music;
        this.television = television;
        this.friends = friends;
        this.familyMembers = familyMembers;
        this.distanceFromHometownToLocation = distanceFromHometownToLocation;
        this.postCount = postCount;
        this.postFrequency = postFrequency;
        this.postDaily = postDaily;
        this.posts = posts;
        this.photoVideoCount = photoVideoCount;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public FacebookProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(FacebookProfile userProfile) {
        this.userProfile = userProfile;
    }

    public List<Page> getActivities() {
        return activities;
    }

    public void setActivities(List<Page> activities) {
        this.activities = activities;
    }

    public List<Page> getBooks() {
        return books;
    }

    public void setBooks(List<Page> books) {
        this.books = books;
    }

    public List<Page> getInterests() {
        return interests;
    }

    public void setInterests(List<Page> interests) {
        this.interests = interests;
    }

    public List<Page> getMovies() {
        return movies;
    }

    public void setMovies(List<Page> movies) {
        this.movies = movies;
    }

    public List<Page> getMusic() {
        return music;
    }

    public void setMusic(List<Page> music) {
        this.music = music;
    }

    public List<Page> getTelevision() {
        return television;
    }

    public void setTelevision(List<Page> television) {
        this.television = television;
    }

    public List<FacebookProfile> getFriends() {
        return friends;
    }

    public void setFriends(List<FacebookProfile> friends) {
        this.friends = friends;
    }

    public Long getDistanceFromHometownToLocation() {
        return distanceFromHometownToLocation;
    }

    public void setDistanceFromHometownToLocation(Long distanceFromHometownToLocation) {
        this.distanceFromHometownToLocation = distanceFromHometownToLocation;
    }

    public Integer getPhotoVideoCount() {
        return photoVideoCount;
    }

    public void setPhotoVideoCount(Integer photoVideoCount) {
        this.photoVideoCount = photoVideoCount;
    }

    public Integer getPostCount() {
        return postCount;
    }

    public void setPostCount(Integer postCount) {
        this.postCount = postCount;
    }

    public Boolean getPostDaily() {
        return postDaily;
    }

    public void setPostDaily(Boolean postDaily) {
        this.postDaily = postDaily;
    }

    public List<FacebookProfile> getFamilyMembers() {
        return familyMembers;
    }

    public void setFamilyMembers(List<FacebookProfile> familyMembers) {
        this.familyMembers = familyMembers;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public String getPostFrequency() {
        return postFrequency;
    }

    public void setPostFrequency(String postFrequency) {
        this.postFrequency = postFrequency;
    }

    public List<Post> getFeed() {
        return feed;
    }

    public void setFeed(List<Post> feed) {
        this.feed = feed;
    }
}
