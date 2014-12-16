/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.commons.bootstrap.impl;

import com.lela.commons.bootstrap.Bootstrapper;
import com.lela.commons.bootstrap.BootstrapperException;
import com.lela.commons.service.ComputedCategoryService;
import com.lela.commons.service.ItemService;
import com.lela.commons.service.UserService;
import com.lela.domain.document.Item;
import com.lela.domain.dto.Items;
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
@Component("itemBootstrapper")
public class ItemBootstrapper extends AbstractBootstrapper implements Bootstrapper {
    private final static Logger log = LoggerFactory.getLogger(ItemBootstrapper.class);
    private static int populated = 0;
    private static int omitted = 0;
    private final Resource file = new ClassPathResource("bootstrap/items.json");

    @Value("${bootstrapper.item.enabled:true}")
    private Boolean enabled;

    private final ItemService itemService;
    private final ComputedCategoryService computedCategoryService;

    @Autowired
    public ItemBootstrapper(ItemService itemService, ComputedCategoryService computedCategoryService) {
        this.itemService = itemService;
        this.computedCategoryService = computedCategoryService;
    }

    @Override
    public void create() throws BootstrapperException {

        if (file.exists()) {
            processCreation();
        } else {
            throw new BootstrapperException("XML file could not be found");
        }

        log.info("Populated " + populated + " items in db");
        log.info("Omitted " + omitted + " items from db. Already exists.");
    }

    private void processCreation() throws BootstrapperException {
        try {

            persist(parseJSON());

        } catch (Exception e) {
            throw new BootstrapperException(e.getMessage(), e);
        }
    }

    private Items parseJSON() throws Exception {
        return mapper.readValue(file.getInputStream(), Items.class);
    }

    /**
     * Saves the admin users to the db before the application becomes active
     *
     * @param items categories
     * @throws com.lela.commons.bootstrap.BootstrapperException
     *
     */
    private void persist(Items items) throws BootstrapperException {
        List<Item> dbList = new ArrayList<Item>();

        try {

            for (Item item : items.getList()) {
                Item tmp = itemService.findItemByUrlName(item.getRlnm());

                if (tmp == null) {
                    dbList.add(item);
                    populated++;
                } else {
                    log.info("Item already exists with name: " + item.getNm());
                    omitted++;
                }
            }

            // ready for save
            if (dbList.size() > 0) {
                // persist items to db
                itemService.saveItems(dbList);

                // clear out all previous user / item computed values
                computedCategoryService.setAllComputedCategoriesToDirty();
            }

        } catch (Exception e) {
            throw new BootstrapperException(e.getMessage(), e);
        }
    }

    @Override
    public String toString() {
        return "ItemBootstrapper";
    }

    @Override
    public Boolean getEnabled() {
        return enabled;
    }
}