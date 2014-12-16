/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.commons.bootstrap.impl;

import com.lela.commons.bootstrap.Bootstrapper;
import com.lela.commons.bootstrap.BootstrapperException;
import com.lela.commons.service.BranchService;
import com.lela.domain.document.Branch;
import com.lela.domain.dto.Branches;
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
@Component("branchBootstrapper")
public class BranchBootstrapper extends AbstractBootstrapper implements Bootstrapper {
    private final static Logger log = LoggerFactory.getLogger(BranchBootstrapper.class);
    private static int populated = 0;
    private static int omitted = 0;
    private final Resource file = new ClassPathResource("bootstrap/branches.json");

    @Value("${bootstrapper.branch.enabled:true}")
    private Boolean enabled;

    private final BranchService branchService;

    @Autowired
    public BranchBootstrapper(BranchService branchService) {
        this.branchService = branchService;
    }

    @Override
    public void create() throws BootstrapperException {

        if (file.exists()) {
            processCreation();
        } else {
            throw new BootstrapperException("XML file could not be found");
        }

        log.info("Populated " + populated + " stores in db");
        log.info("Omitted " + omitted + " stores from db. Already exists.");
    }

    private void processCreation() throws BootstrapperException {
        try {

            persist(parseJSON());

        } catch (Exception e) {
            throw new BootstrapperException(e.getMessage(), e);
        }
    }

    private Branches parseJSON() throws Exception {
        return mapper.readValue(file.getInputStream(), Branches.class);
    }

    /**
     * Saves the admin users to the db before the application becomes active
     *
     * @param branches categories
     * @throws com.lela.commons.bootstrap.BootstrapperException
     *
     */
    private void persist(Branches branches) throws BootstrapperException {
        List<Branch> dbList = new ArrayList<Branch>();

        try {

            for (Branch branch : branches.getList()) {
                Branch tmp = branchService.findBranchByUrlName(branch.getRlnm());

                if (tmp == null) {
                    dbList.add(branch);
                    populated++;
                } else {
                    log.info("Store already exists with name: " + branch.getNm());
                    omitted++;
                }
            }

            // ready for save
            if (dbList.size() > 0) {
                branchService.saveBranches(dbList);
            }

        } catch (Exception e) {
            throw new BootstrapperException(e.getMessage(), e);
        }
    }

    @Override
    public String toString() {
        return "BranchBootstrapper";
    }

    @Override
    public Boolean getEnabled() {
        return enabled;
    }
}