/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.commons.bootstrap.impl;

import com.lela.commons.bootstrap.Bootstrapper;
import com.lela.commons.bootstrap.BootstrapperException;
import com.lela.commons.service.OwnerService;
import com.lela.domain.document.Owner;
import com.lela.domain.dto.Owners;
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
@Component("ownerBootstrapper")
public class OwnerBootstrapper extends AbstractBootstrapper implements Bootstrapper {
    private final static Logger log = LoggerFactory.getLogger(OwnerBootstrapper.class);
    private static int populated = 0;
    private static int omitted = 0;
    private final Resource file = new ClassPathResource("bootstrap/owners.json");

    @Value("${bootstrapper.owner.enabled:true}")
    private Boolean enabled;

    private final OwnerService ownerService;

    @Autowired
    public OwnerBootstrapper(OwnerService ownerService) {
        this.ownerService = ownerService;
    }

    @Override
    public void create() throws BootstrapperException {

        if (file.exists()) {
            processCreation();
        } else {
            throw new BootstrapperException("XML file could not be found");
        }

        log.info("Populated " + populated + " owners in db");
        log.info("Omitted " + omitted + " owners from db. Already exists.");
    }

    private void processCreation() throws BootstrapperException {
        try {

            persist(parseJSON());

        } catch (Exception e) {
            throw new BootstrapperException(e.getMessage(), e);
        }
    }

    private Owners parseJSON() throws Exception {
        return mapper.readValue(file.getInputStream(), Owners.class);
    }

    /**
     * Saves the admin users to the db before the application becomes active
     *
     * @param owners categories
     * @throws com.lela.commons.bootstrap.BootstrapperException
     *
     */
    private void persist(Owners owners) throws BootstrapperException {
        List<Owner> dbList = new ArrayList<Owner>();

        try {

            for (Owner owner : owners.getList()) {
                Owner tmp = ownerService.findOwnerByUrlName(owner.getRlnm());

                if (tmp == null) {
                    dbList.add(owner);
                    populated++;
                } else {
                    log.info("Owner already exists with name: " + owner.getNm());
                    omitted++;
                }
            }

            // ready for save
            if (dbList.size() > 0) {
                ownerService.saveOwners(dbList);
            }

        } catch (Exception e) {
            throw new BootstrapperException(e.getMessage(), e);
        }
    }

    @Override
    public String toString() {
        return "OwnerBootstrapper";
    }

    @Override
    public Boolean getEnabled() {
        return enabled;
    }
}