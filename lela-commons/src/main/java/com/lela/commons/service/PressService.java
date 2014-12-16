/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.commons.service;

import com.lela.domain.document.Press;
import com.lela.domain.document.PressImage;
import com.lela.domain.document.Testimonial;
import com.lela.domain.document.Tweet;
import com.lela.domain.dto.press.PressImageEntry;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

/**
 * Created by Bjorn Harvold
 */
public interface PressService {
    Page<Press> findPressPages(Integer size, Integer maxResults);

    Press savePress(Press press);

    void removePress(String urlName);

    Press findPressByUrlName(String urlName);

    void savePressImage(PressImageEntry pressImage);

    PressImage findPressImageEntry(String urlName, String id);

    Testimonial findTestimonial(String urlName, String id);

    Tweet findTweet(String urlName, String id);

    void saveTestimonial(Testimonial testimonial);

    void saveTweet(Tweet tweet);

    void removeTweet(String urlName, String id);

    void removeTestimonial(String urlName, String id);

    void removePressImage(String urlName, String id);

    List<Press> findAllPressUrlNames();

    @PreAuthorize("hasAnyRole('RIGHT_READ_PRESS')")
    Press findWholePressByUrlName(String urlName);

    Press findPressByUrlName(String urlName, List<String> fields);
}
