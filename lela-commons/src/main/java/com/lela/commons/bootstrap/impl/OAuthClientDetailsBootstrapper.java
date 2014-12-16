/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.commons.bootstrap.impl;

import com.lela.commons.bootstrap.Bootstrapper;
import com.lela.commons.bootstrap.BootstrapperException;
import com.lela.commons.service.OAuthService;
import com.lela.domain.document.OAuthClientDetail;
import com.lela.domain.dto.OAuthClientDetails;
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
@Component("oAuthClientDetailsBootstrapper")
public class OAuthClientDetailsBootstrapper extends AbstractBootstrapper implements Bootstrapper {
    private final static Logger log = LoggerFactory.getLogger(OAuthClientDetailsBootstrapper.class);
    private static int populated = 0;
    private static int omitted = 0;
    private final Resource file = new ClassPathResource("bootstrap/oauth_client_details.json");
    private final OAuthService oAuthService;

    @Value("${bootstrapper.oauth.client.details.enabled:true}")
    private Boolean enabled;

    @Autowired
    public OAuthClientDetailsBootstrapper(OAuthService oAuthService) {
        this.oAuthService = oAuthService;
    }

    @Override
    public void create() throws BootstrapperException {

        if (file.exists()) {
            processCreation();
        } else {
            throw new BootstrapperException("File could not be found");
        }

        log.info("Populated " + populated + " oauth client details in db");
        log.info("Omitted " + omitted + " oauth client details from db. Already exists.");
    }

    private void processCreation() throws BootstrapperException {
        try {

            persist(parseJSON());

        } catch (Exception e) {
            throw new BootstrapperException(e.getMessage(), e);
        }
    }

    private OAuthClientDetails parseJSON() throws Exception {
        return mapper.readValue(file.getInputStream(), OAuthClientDetails.class);
    }

    /**
     * Saves the admin users to the db before the application becomes active
     *
     * @param details details
     * @throws com.lela.commons.bootstrap.BootstrapperException ex
     *
     */
    private void persist(OAuthClientDetails details) throws BootstrapperException {
        List<OAuthClientDetail> dbList = new ArrayList<OAuthClientDetail>();

        try {
            for (OAuthClientDetail detail : details.getList()) {

                OAuthClientDetail tmp = oAuthService.findOAuthClientDetailsByClientId(detail.getClientId());

                if (tmp == null) {
                    dbList.add(detail);
                    populated++;
                } else {
                    log.info("Client details already exists with clientId: " + detail.getClientId());
                    omitted++;
                }
            }

            // ready for save
            if (dbList.size() > 0) {
                oAuthService.saveOAuthClientDetails(dbList);
            }

        } catch (Exception e) {
            throw new BootstrapperException(e.getMessage(), e);
        }
    }

    @Override
    public String toString() {
        return "OAuthClientDetailsBootstrapper";
    }

    @Override
    public Boolean getEnabled() {
        return enabled;
    }
}