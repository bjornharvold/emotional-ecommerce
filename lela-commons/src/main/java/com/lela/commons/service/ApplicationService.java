package com.lela.commons.service;

import com.lela.domain.document.Application;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

/**
 * Created by Bjorn Harvold
 * Date: 11/11/11
 * Time: 10:25 PM
 * Responsibility:
 */
public interface ApplicationService {
    Application findApplicationByUrlName(String applicationId);

    List<Application> findApplications(Integer page, Integer maxResults, List<String> fields);

    Page<Application> findApplications(Integer page, Integer maxResults);

    Application saveApplication(Application application);

    void removeApplication(String urlName);

    List<Application> findApplications(List<String> fields);
}
