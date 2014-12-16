/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.commons.jobs.interceptor;

import com.lela.commons.jobs.IngestJobInterceptor;
import com.lela.commons.jobs.ExecutionContext;
import com.lela.commons.service.CategoryService;
import com.lela.commons.service.FunctionalFilterService;
import com.lela.commons.service.ItemService;
import com.lela.domain.document.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Bjorn Harvold
 * Date: 3/27/12
 * Time: 11:08 PM
 * Responsibility: Processes all functional filters that need to be populated
 */
public class FunctionalFiltersIngestJobInterceptor extends AbstractFunctionalFiltersIngestJobInterceptor implements IngestJobInterceptor {
    /**
     * Logger
     */
    private final static Logger log = LoggerFactory.getLogger(FunctionalFiltersIngestJobInterceptor.class);

    public FunctionalFiltersIngestJobInterceptor(ItemService itemService,
                                                 CategoryService categoryService,
                                                 FunctionalFilterService functionalFilterService) {
        super(itemService, categoryService, functionalFilterService);
    }

    @Override
    public boolean ingestCompleted(ExecutionContext context) {

        log.info(context.message("Running FunctionalFiltersIngestJobInterceptor"));

        // Get the list of objects that was processed by the ingest job
        List<Object> processed = context.getProcessed();
        if (processed != null && !processed.isEmpty()) {

            // Get the distinct set of categories represented by the processed filters
            Set<String> categoryUrlNames = new HashSet<String>();
            for (Object obj : processed) {

                // Check the first item in the list to verify it is an FunctionalFilter as expected
                if (obj instanceof Item) {

                    // Get the category from the first
                    Item item = (Item)obj;
                    categoryUrlNames.add(item.getCtgry().getRlnm());
                } else {
                    if (log.isErrorEnabled()) {
                        log.error(context.message("Object processed must be an Item but is instead of class: " + obj != null ? obj.getClass().getCanonicalName() : "null"));
                    }
                }
            }

            processCategories(categoryUrlNames, context);
        } else {
            if (log.isInfoEnabled()) {
                log.info(context.message("Skipping Functional Filter processing, no Items were ingested"));
            }
        }

        return true;
    }
}
