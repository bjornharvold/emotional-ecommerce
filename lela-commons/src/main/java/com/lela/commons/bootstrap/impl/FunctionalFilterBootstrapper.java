/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.commons.bootstrap.impl;

import com.lela.commons.bootstrap.Bootstrapper;
import com.lela.commons.bootstrap.BootstrapperException;
import com.lela.commons.service.FunctionalFilterService;
import com.lela.domain.document.FunctionalFilter;
import com.lela.domain.dto.FunctionalFilters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * User: bjorn
 * Date: Nov 4, 2007
 * Time: 11:19:22 AM
 * Inserts required categories into the system
 */
@SuppressWarnings("unchecked")
@Component("functionalFilterBootstrapper")
public class FunctionalFilterBootstrapper extends AbstractBootstrapper implements Bootstrapper {
    private final static Logger log = LoggerFactory.getLogger(FunctionalFilterBootstrapper.class);
    private static int populated = 0;
    private static int omitted = 0;
    private final Resource file = new ClassPathResource("bootstrap/functional_filters.json");
    private final FunctionalFilterService functionalFilterService;

    @Value("${bootstrapper.functionalfilter.enabled:true}")
    private Boolean enabled;

    @Autowired
    public FunctionalFilterBootstrapper(FunctionalFilterService functionalFilterService) {
        this.functionalFilterService = functionalFilterService;
    }

    @Override
    public void create() throws BootstrapperException {

        if (file.exists()) {
            processCreation();
        } else {
            throw new BootstrapperException("File could not be found");
        }

        log.info("Populated " + populated + " functional filters in db");
        log.info("Omitted " + omitted + " functional filters from db. Already exists.");
    }

    private void processCreation() throws BootstrapperException {
        try {

            persist(parseJSON());

        } catch (Exception e) {
            throw new BootstrapperException(e.getMessage(), e);
        }
    }

    private FunctionalFilters parseJSON() throws Exception {
        return mapper.readValue(file.getInputStream(), FunctionalFilters.class);
    }

    /**
     * Saves the admin users to the db before the application becomes active
     *
     * @param functionalFilters categories
     * @throws com.lela.commons.bootstrap.BootstrapperException
     *
     */
    private void persist(FunctionalFilters functionalFilters) throws BootstrapperException {
        List<FunctionalFilter> dbList = new ArrayList<FunctionalFilter>();

        try {

            for (FunctionalFilter ff : functionalFilters.getList()) {
                FunctionalFilter tmp = functionalFilterService.findFunctionalFilterByUrlNameAndKey(ff.getRlnm(), ff.getKy());

                if (tmp == null) {
                    dbList.add(ff);
                    populated++;
                } else {
                    log.info("Functional filter already exists with key: " + ff.getKy());
                    omitted++;
                }
            }

            // ready for save
            if (dbList.size() > 0) {
                functionalFilterService.saveFunctionalFilters(dbList);
            }

        } catch (Exception e) {
            throw new BootstrapperException(e.getMessage(), e);
        }
    }

    @Override
    public String toString() {
        return "FunctionalFilterBootstrapper";
    }

    @Override
    public Boolean getEnabled() {
        return enabled;
    }
}