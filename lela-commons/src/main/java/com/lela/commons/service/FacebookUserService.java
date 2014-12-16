/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */
package com.lela.commons.service;

import com.lela.domain.document.FacebookSnapshot;
import com.lela.domain.document.User;
import org.springframework.scheduling.annotation.Async;

import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Chris Tallent
 * Date: 3/20/12
 * Time: 9:47 PM
 */
public interface FacebookUserService {
    Map<String, Integer> calculateMotivators(FacebookSnapshot snapshot);

    FacebookSnapshot findOrGenerateFacebookSnapshot(User user);

    FacebookSnapshot saveFacebookSnapshot(FacebookSnapshot snapshot);

    FacebookSnapshot generateSnapshot(User user);

    FacebookSnapshot findFacebookSnapshot(User user);

    List<FacebookSnapshot> findFacebookSnapshotsWithoutMotivators();

    void calculateFacebookUserMotivators(FacebookSnapshot snapshot);

    void startFacebookSnapshotTask(User user);

    void removeFacebookSnapshot(String ml);

    void postOnTimeline(User user, String content);
}
