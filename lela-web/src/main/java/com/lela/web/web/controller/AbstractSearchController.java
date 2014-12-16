/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.web.web.controller;

import com.lela.commons.comparator.CategoryCountNameComparator;
import com.lela.commons.service.EventService;
import com.lela.commons.service.ProductEngineService;
import com.lela.domain.dto.search.CategoryCount;
import org.springframework.context.MessageSource;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Created by Bjorn Harvold
 * Date: 3/8/12
 * Time: 9:15 PM
 * Responsibility:
 */
public abstract class AbstractSearchController extends AbstractController {
    protected final ProductEngineService productEngineService;
    protected final EventService eventService;
    protected final MessageSource messageSource;

    public AbstractSearchController(EventService eventService,
                                    ProductEngineService productEngineService,
                                    MessageSource messageSource) {
        this.messageSource = messageSource;
        this.eventService = eventService;
        this.productEngineService = productEngineService;
    }

    protected List<CategoryCount> sortCategoriesFromSearchResult(List<CategoryCount> categories, Locale locale) {
        Collections.sort(categories, new CategoryCountNameComparator(messageSource, locale));

        return categories;
    }

}
