/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.commons.jobs.interceptor;

import com.lela.commons.jobs.IngestJobInterceptor;
import com.lela.commons.jobs.ExecutionContext;
import com.lela.commons.service.CategoryService;
import com.lela.commons.service.FunctionalFilterService;
import com.lela.commons.service.ItemService;
import com.lela.domain.document.FunctionalFilter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Bjorn Harvold
 * Date: 3/27/12
 * Time: 11:08 PM
 * Responsibility: Processes all functional filters that need to be populated
 */
public class AllFunctionalFiltersIngestJobInterceptor extends AbstractFunctionalFiltersIngestJobInterceptor implements IngestJobInterceptor {

    public AllFunctionalFiltersIngestJobInterceptor(ItemService itemService,
                                                    CategoryService categoryService,
                                                    FunctionalFilterService functionalFilterService) {
        super(itemService, categoryService, functionalFilterService);
    }

    @Override
    public boolean ingestCompleted(ExecutionContext context) {

        log.info(context.message("Running AllFunctionalFiltersIngestJobInterceptor"));

        // Get the list of objects that was processed by the ingest job
        List<Object> processed = context.getProcessed();
        if (processed != null && !processed.isEmpty()) {

            // Get the distinct set of categories represented by the processed filters
            Set<String> categoryUrlNames = new HashSet<String>();
            Set<String> categoryGroupUrlNames = new HashSet<String>();
            for (Object obj : processed) {

                // Check the first item in the list to verify it is an FunctionalFilter as expected
                if (obj instanceof FunctionalFilter) {

                    // Get the category from the first
                    FunctionalFilter ff = (FunctionalFilter)obj;

                    switch(ff.getDtp()) {
                        case CATEGORY:
                            categoryUrlNames.add(ff.getRlnm());
                            break;
                        case DEPARTMENT:
                            categoryGroupUrlNames.add(ff.getRlnm());
                            break;
                        default:
                            if (log.isWarnEnabled()) {
                                log.warn(context.message("Currently do not support the filter domain type: " + ff.getDtp().name()));
                            }
                    }
                } else {
                    if (log.isErrorEnabled()) {
                        log.error(context.message("Object processed must be an FunctionalFilter but is instead of class: " + obj != null ? obj.getClass().getCanonicalName() : "null"));
                    }
                }
            }

            // process department level filters
            processDepartments(categoryGroupUrlNames, context);

            // process category level filters
            processCategories(categoryUrlNames, context);
        } else {
            if (log.isInfoEnabled()) {
                log.info(context.message("No FunctionalFilters were ingested"));
            }
        }

        return true;
    }
}
