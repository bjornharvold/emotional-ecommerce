/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.commons.bootstrap.impl;

import com.lela.commons.bootstrap.Bootstrapper;
import com.lela.commons.bootstrap.BootstrapperException;
import com.lela.commons.service.*;
import com.lela.domain.document.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
@Component("solrIndexerBootstrapper")
public class SolrIndexerBootstrapper extends AbstractBootstrapper implements Bootstrapper {
    private final static Logger log = LoggerFactory.getLogger(SolrIndexerBootstrapper.class);

    @Value("${bootstrapper.solr.indexer.enabled:true}")
    private Boolean enabled;

    private final CategoryService categoryService;
    private final ItemService itemService;
    private final SearchService searchService;
    private final AffiliateService affiliateService;
    private final NavigationBarService navigationBarService;

    @Autowired
    public SolrIndexerBootstrapper(CategoryService categoryService,
                                   ItemService itemService,
                                   SearchService searchService,
                                   AffiliateService affiliateService,
                                   NavigationBarService navigationBarService) {
        this.categoryService = categoryService;
        this.itemService = itemService;
        this.searchService = searchService;
        this.affiliateService = affiliateService;
        this.navigationBarService = navigationBarService;
    }

    @Override
    public void create() throws BootstrapperException {

        emptyIndex();

        reIndex();

    }

    private void emptyIndex() throws BootstrapperException {
        try {
            searchService.deleteAll();
        } catch (SearchException e) {
            throw new BootstrapperException(e.getMessage(), e);
        }
    }

    private void reIndex() throws BootstrapperException {
        try {
            if (searchService.isItemCoreSolrServerAvailable()) {
                
                log.info("Loading categories for Solr indexing");
                List<Category> categories = categoryService.findCategories();

                List<Category> excludedCategories = affiliateService.findExcludedCategories();

                categories.removeAll(excludedCategories);

                if (categories != null && !categories.isEmpty()) {
                    for (Category category : categories) {
                        
                        log.info(String.format("Loading items from category %s for Solr indexing", category.getSrlnm()));
                        List<Item> items = itemService.findItemsByCategoryUrlName(category.getRlnm());

                        if (items != null && !items.isEmpty()) {
                            log.info("Re-indexing items from category: " + category.getSrlnm());
                            searchService.indexItems(items);
                        }
                    }
                }
            } else {
                if (log.isWarnEnabled()) {
                    log.warn("Could not index items with Solr server. Server not available.");
                }
            }


        } catch (Exception e) {
            throw new BootstrapperException(e.getMessage(), e);
        }
    }

    @Override
    public String toString() {
        return "SolrIndexerBootstrapper";
    }

    @Override
    public Boolean getEnabled() {
        return enabled;
    }
}