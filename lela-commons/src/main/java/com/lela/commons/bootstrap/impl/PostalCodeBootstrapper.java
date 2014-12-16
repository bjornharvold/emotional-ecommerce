/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.commons.bootstrap.impl;

import com.lela.commons.bootstrap.Bootstrapper;
import com.lela.commons.bootstrap.BootstrapperException;
import com.lela.commons.service.PostalCodeService;
import com.lela.domain.document.PostalCode;
import com.lela.domain.dto.PostalCodes;
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
@Component("postalCodeBootstrapper")
public class PostalCodeBootstrapper extends AbstractBootstrapper implements Bootstrapper {
    private final static Logger log = LoggerFactory.getLogger(PostalCodeBootstrapper.class);
    private static int populated = 0;
    private static int omitted = 0;
    private final Resource file = new ClassPathResource("bootstrap/postal_codes.json");

    @Value("${bootstrapper.postalCode.enabled:true}")
    private Boolean enabled;

    private final PostalCodeService postalCodeService;

    @Autowired
    public PostalCodeBootstrapper(PostalCodeService postalCodeService) {
        this.postalCodeService = postalCodeService;
    }

    @Override
    public void create() throws BootstrapperException {

        if (file.exists()) {
            processCreation();
        } else {
            throw new BootstrapperException("XML file could not be found");
        }

        log.info("Populated " + populated + " postal codes in db");
        log.info("Omitted " + omitted + " postal codes from db. Already exists.");
    }

    private void processCreation() throws BootstrapperException {
        try {

            persist(parseJSON());

        } catch (Exception e) {
            throw new BootstrapperException(e.getMessage(), e);
        }
    }

    private PostalCodes parseJSON() throws Exception {
        return mapper.readValue(file.getInputStream(), PostalCodes.class);
    }

    /**
     * Saves the admin users to the db before the application becomes active
     *
     * @param postalCodes categories
     * @throws com.lela.commons.bootstrap.BootstrapperException
     *
     */
    private void persist(PostalCodes postalCodes) throws BootstrapperException {
        List<PostalCode> dbList = new ArrayList<PostalCode>();

        try {

            for (PostalCode postalCode : postalCodes.getList()) {
                PostalCode tmp = postalCodeService.findPostalCodeByCode(postalCode.getCd());

                if (tmp == null) {
                    dbList.add(postalCode);
                    populated++;
                } else {
                    log.info("Postal Code already exists with code: " + postalCode.getCd());
                    omitted++;
                }
            }

            // ready for save
            if (dbList.size() > 0) {
                postalCodeService.savePostalCodes(dbList);
            }

        } catch (Exception e) {
            throw new BootstrapperException(e.getMessage(), e);
        }
    }

    @Override
    public String toString() {
        return "PostalCodeBootstrapper";
    }

    @Override
    public Boolean getEnabled() {
        return enabled;
    }
}