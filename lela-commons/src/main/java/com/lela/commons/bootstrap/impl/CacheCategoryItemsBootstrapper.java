/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.commons.bootstrap.impl;

import com.lela.commons.bootstrap.Bootstrapper;
import com.lela.commons.bootstrap.BootstrapperException;
import com.lela.commons.service.CategoryService;
import com.lela.commons.service.ItemService;
import com.lela.domain.document.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * User: bjorn
 * Date: Nov 4, 2007
 * Time: 11:19:22 AM
 * Inserts required categories into the system
 */
@SuppressWarnings("unchecked")
@Component("cacheCategoryItemsBootstrapper")
public class CacheCategoryItemsBootstrapper extends AbstractBootstrapper implements Bootstrapper {
    private final static Logger log = LoggerFactory.getLogger(CacheCategoryItemsBootstrapper.class);
    private static int populated = 0;
    private static int omitted = 0;

    @Value("${bootstrapper.cache.categories.enabled:true}")
    private Boolean enabled;

    private final CategoryService categoryService;
    private final ItemService itemService;

    @Autowired
    public CacheCategoryItemsBootstrapper(CategoryService categoryService, ItemService itemService) {
        this.categoryService = categoryService;
        this.itemService = itemService;
    }

    @Override
    public void create() throws BootstrapperException {
        log.info("Loading categories to populate cache");
        List<Category> categories = categoryService.findCategories();
        for (Category category : categories) {
            log.info(String.format("Loading items for category %s to populate cache", category.getRlnm()));
            itemService.findItemsByCategoryUrlName(category.getRlnm());
        }
    }

    @Override
    public String toString() {
        return "CacheCategoryItemsBootstrapper";
    }

    @Override
    public Boolean getEnabled() {
        return enabled;
    }
}