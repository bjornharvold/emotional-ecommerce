/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.web.web.controller;

import com.lela.commons.service.EventService;
import com.lela.commons.service.ProductEngineService;
import com.lela.commons.service.SearchException;
import com.lela.commons.service.UserService;
import com.lela.commons.web.utils.WebConstants;
import com.lela.domain.document.User;
import com.lela.domain.dto.RelevantItemSearchQuery;
import com.lela.domain.dto.search.SearchResult;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;
import java.util.Locale;

/**
 * Created by Bjorn Harvold
 * Date: 1/17/12
 * Time: 11:58 PM
 * Responsibility:
 */
@Controller
public class SearchController extends AbstractSearchController {
    /**
     * Logger
     */
    private final static Logger log = LoggerFactory.getLogger(SearchController.class);
    private final UserService userService;

    @Autowired
    public SearchController(EventService eventService,
                            ProductEngineService productEngineService,
                            MessageSource messageSource, UserService userService) {
        super(eventService, productEngineService, messageSource);
        this.userService = userService;
    }

    /**
     * Method description
     *
     * @param query   query
     * @param session session
     * @param model   model
     * @param locale  locale
     * @return Return value
     * @throws Exception Exception
     */
    @RequestMapping(value = "/search/item", method = RequestMethod.POST)
    public String searchForItem(RelevantItemSearchQuery query, HttpSession session, Model model, Locale locale)
            throws Exception {
        String view = "search.result";

        // get the user
        User user = retrieveUserFromPrincipalOrSession(session);

        if (query != null && user != null) {
            // add user to the model
            query.setUserCode(user.getCd());

            SearchResult searchResults = null;

            try {
                if (userService.shouldUserSeeRecommendedProducts(user.getCd())) {
                    searchResults = productEngineService.searchForRelevantItemsUsingSolr(query);
                } else {
                    searchResults = productEngineService.searchForItemsUsingSolr(query);
                }

                if (searchResults != null) {
                    // only sort if there is something to sort
                    if (searchResults.getCategories() != null && !searchResults.getCategories().isEmpty()) {
                        sortCategoriesFromSearchResult(searchResults.getCategories(), locale);
                    }
                    model.addAttribute(WebConstants.SEARCH_RESULTS, searchResults);
                }
            } catch (SearchException ex) {
                if (log.isErrorEnabled()) {
                    log.error(ex.getMessage());
                }
            }

        }

        return view;
    }

    /**
     * Method description
     *
     * @param query   query
     * @param session session
     * @param model   model
     * @param locale  locale
     * @return Return value
     * @throws Exception Exception
     */
    @RequestMapping(value = "/search/item", method = RequestMethod.GET)
    public String searchForItemAsGet(RelevantItemSearchQuery query, HttpSession session, Model model, Locale locale)
            throws Exception {
        return searchForItem(query, session, model, locale);
    }

    /**
     * Method description
     *
     * @param terms   terms
     * @param query   query
     * @param session session
     * @param model   model
     * @param locale  locale
     * @return Return value
     * @throws Exception Exception
     */
    @RequestMapping(value = "/search/item/{terms}", method = RequestMethod.GET)
    public String searchForItemAsGetWithTerms(@PathVariable("terms") String terms,
                                              RelevantItemSearchQuery query,
                                              HttpSession session, Model model, Locale locale) throws Exception {

        // Convert SEO dashes to spaces
        if (StringUtils.isNotBlank(terms)) {
            terms = terms.replace("-", " ");
        }

        query.setTerms(terms);
        return searchForItem(query, session, model, locale);
    }

}
