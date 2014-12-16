/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.commons.service.impl;

import com.lela.domain.ApplicationConstants;
import com.lela.commons.repository.ApplicationRepository;
import com.lela.commons.service.ApplicationService;
import com.lela.commons.service.CacheService;
import com.lela.domain.document.Application;
import com.lela.domain.document.QApplication;
import com.lela.domain.enums.CacheType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Bjorn Harvold
 * Date: 11/11/11
 * Time: 10:04 PM
 * Responsibility:
 */
@Service("applicationService")
public class ApplicationServiceImpl extends AbstractCacheableService implements ApplicationService {
    private final ApplicationRepository applicationRepository;

    @Autowired
    public ApplicationServiceImpl(CacheService cacheService, ApplicationRepository applicationRepository) {
        super(cacheService);
        this.applicationRepository = applicationRepository;
    }

    @PreAuthorize("hasAnyRole('RIGHT_READ_APPLICATION')")
    @Override
    public List<Application> findApplications(List<String> fields) {
        return applicationRepository.findAll(fields);
    }

    @PreAuthorize("hasAnyRole('RIGHT_READ_APPLICATION')")
    @Override
    public List<Application> findApplications(Integer page, Integer maxResults, List<String> fields) {
        return applicationRepository.findAll(page, maxResults, fields);
    }

    @PreAuthorize("hasAnyRole('RIGHT_READ_APPLICATION')")
    @Override
    public Page<Application> findApplications(Integer page, Integer maxResults) {
        return applicationRepository.findAll(new PageRequest(page, maxResults));
    }

    @Override
    public Application findApplicationByUrlName(String urlName) {
        Application result = null;

        Cache.ValueWrapper wrapper = retrieveFromCache(ApplicationConstants.QUESTION_CACHE, urlName);

        if (wrapper != null && wrapper.get() != null && wrapper.get() instanceof Application) {
            result = (Application) wrapper.get();
        } else {
            result = applicationRepository.findOne(QApplication.application.rlnm.eq(urlName));

            if (result != null) {

                putInCache(ApplicationConstants.APPLICATION_CACHE, urlName, result);
            }
        }

        return result;
    }

    @PreAuthorize("hasAnyRole('RIGHT_INSERT_APPLICATION')")
    @Override
    public Application saveApplication(Application application) {
        application = applicationRepository.save(application);
        removeFromCache(CacheType.APPLICATION, application.getRlnm());

        return application;
    }

    @PreAuthorize("hasAnyRole('RIGHT_DELETE_APPLICATION')")
    @Override
    public void removeApplication(String urlName) {
        Application application = findApplicationByUrlName(urlName);

        if (application != null) {
            applicationRepository.delete(application);

            removeFromCache(CacheType.APPLICATION, urlName);
        }
    }
}
