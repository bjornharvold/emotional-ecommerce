/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.commons.bootstrap.impl;

import com.lela.commons.bootstrap.Bootstrapper;
import com.lela.commons.bootstrap.BootstrapperException;
import com.lela.commons.service.StoreService;
import com.lela.domain.document.Store;
import com.lela.domain.dto.Stores;
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
@Component("storeBootstrapper")
public class StoreBootstrapper extends AbstractBootstrapper implements Bootstrapper {
    private final static Logger log = LoggerFactory.getLogger(StoreBootstrapper.class);
    private static int populated = 0;
    private static int omitted = 0;
    private final Resource file = new ClassPathResource("bootstrap/stores.json");

    @Value("${bootstrapper.store.enabled:true}")
    private Boolean enabled;

    private final StoreService storeService;

    @Autowired
    public StoreBootstrapper(StoreService storeService) {
        this.storeService = storeService;
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

    private Stores parseJSON() throws Exception {
        return mapper.readValue(file.getInputStream(), Stores.class);
    }

    /**
     * Saves the admin users to the db before the application becomes active
     *
     * @param stores categories
     * @throws com.lela.commons.bootstrap.BootstrapperException
     *
     */
    private void persist(Stores stores) throws BootstrapperException {
        List<Store> dbList = new ArrayList<Store>();

        try {

            for (Store store : stores.getList()) {
                Store tmp = storeService.findStoreByUrlName(store.getRlnm());

                if (tmp == null) {
                    dbList.add(store);
                    populated++;
                } else {
                    log.info("Store already exists with name: " + store.getNm());
                    omitted++;
                }
            }

            // ready for save
            if (dbList.size() > 0) {
                storeService.saveStores(dbList);
            }

        } catch (Exception e) {
            throw new BootstrapperException(e.getMessage(), e);
        }
    }

    @Override
    public String toString() {
        return "StoreBootstrapper";
    }

    @Override
    public Boolean getEnabled() {
        return enabled;
    }
}