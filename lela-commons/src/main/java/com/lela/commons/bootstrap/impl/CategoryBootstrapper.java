/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.commons.bootstrap.impl;

import com.lela.commons.bootstrap.Bootstrapper;
import com.lela.commons.bootstrap.BootstrapperException;
import com.lela.domain.document.Category;
import com.lela.domain.dto.Categories;
import com.lela.commons.service.CategoryService;
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
@Component("categoryBootstrapper")
public class CategoryBootstrapper extends AbstractBootstrapper implements Bootstrapper {
    private final static Logger log = LoggerFactory.getLogger(CategoryBootstrapper.class);
    private static int populated = 0;
    private static int omitted = 0;
    private final Resource file = new ClassPathResource("bootstrap/categories.json");
    private final CategoryService categoryService;

    @Value("${bootstrapper.category.enabled:true}")
    private Boolean enabled;

    @Autowired
    public CategoryBootstrapper(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Override
    public void create() throws BootstrapperException {

        if (file.exists()) {
            processCreation();
        } else {
            throw new BootstrapperException("JSON file could not be found");
        }

        log.info("Populated " + populated + " categories in db");
        log.info("Omitted " + omitted + " categories from db. Already exists.");
    }

    private void processCreation() throws BootstrapperException {
        try {

            persist(parseJSON());

        } catch (Exception e) {
            throw new BootstrapperException(e.getMessage(), e);
        }
    }

    private Categories parseJSON() throws Exception {
        return mapper.readValue(file.getInputStream(), Categories.class);
    }

    /**
     * Saves the admin users to the db before the application becomes active
     *
     * @param categories categories
     * @throws com.lela.commons.bootstrap.BootstrapperException
     *
     */
    private void persist(Categories categories) throws BootstrapperException {
        List<Category> dbList = new ArrayList<Category>();

        try {

            for (Category category : categories.getList()) {
                Category tmp = categoryService.findCategoryByUrlName(category.getRlnm());

                if (tmp == null) {
                    dbList.add(category);
                    populated++;
                } else {
                    log.info("Category already exists with name: " + category.getNm());
                    omitted++;
                }
            }

            // ready for save
            if (dbList.size() > 0) {
                categoryService.saveCategories(dbList);
            }

        } catch (Exception e) {
            throw new BootstrapperException(e.getMessage(), e);
        }
    }

    @Override
    public String toString() {
        return "CategoryBootstrapper";
    }

    @Override
    public Boolean getEnabled() {
        return enabled;
    }
}