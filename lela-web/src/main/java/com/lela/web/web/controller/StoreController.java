/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.web.web.controller;

import com.lela.commons.service.BranchService;
import com.lela.commons.service.CategoryService;
import com.lela.commons.service.DealService;
import com.lela.commons.service.EventService;
import com.lela.commons.service.ProductEngineService;
import com.lela.commons.service.SearchException;
import com.lela.commons.service.StoreService;
import com.lela.commons.service.UserService;
import com.lela.commons.web.ResourceNotFoundException;
import com.lela.commons.web.utils.WebConstants;
import com.lela.domain.document.AvailableInStore;
import com.lela.domain.document.Category;
import com.lela.domain.document.Deal;
import com.lela.domain.document.Store;
import com.lela.domain.document.User;
import com.lela.domain.document.UserSupplement;
import com.lela.domain.dto.LocationQuery;
import com.lela.domain.dto.RelevantItemsInStoreSearchQuery;
import com.lela.domain.dto.search.AbstractSearchResult;
import com.lela.domain.dto.store.LocalStoresSearchResult;
import com.lela.domain.dto.store.StoreAggregateQuery;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Bjorn Harvold
 * Date: 1/17/12
 * Time: 11:58 PM
 * Responsibility:
 */
@Controller
public class StoreController extends AbstractSearchController {
    /**
     * Logger
     */
    private final static Logger log = LoggerFactory.getLogger(StoreController.class);
    private static final int MAX_RESULT_SIZE = 12;

    private final StoreService storeService;

    private final BranchService branchService;

    private final UserService userService;

    private final CategoryService categoryService;

    private final DealService dealService;

    @Autowired
    public StoreController(EventService eventService,
                           ProductEngineService productEngineService,
                           StoreService storeService,
                           MessageSource messageSource,
                           BranchService branchService,
                           UserService userService,
                           CategoryService categoryService,
                           DealService dealService) {
        super(eventService, productEngineService, messageSource);
        this.storeService = storeService;
        this.branchService = branchService;
        this.userService = userService;
        this.categoryService = categoryService;
        this.dealService = dealService;
    }

    /**
     * This url mapping shows an overview of the products this store carries
     *
     * @param urlName urlName
     * @param session session
     * @param model   model
     * @param locale  locale
     * @return Return value
     * @throws Exception Exception
     */
    @RequestMapping(value = "/{seoUrlName}/s", method = RequestMethod.GET)
    public String showStoreSEO(@PathVariable("seoUrlName") String seoUrlName,
                               @RequestParam(value = "rlnm", required = true) String urlName,
                               HttpSession session, Model model, Locale locale)
            throws Exception {
        String view = "store.index";
        RelevantItemsInStoreSearchQuery query = new RelevantItemsInStoreSearchQuery();
        query.setUrlName(urlName);
        query.setPage(0);
        query.setSize(4);

        doCommonShowStore(query, session, model, locale);

        Store store = (Store) model.asMap().get(WebConstants.STORE);

        if (store == null || StringUtils.isBlank(store.getSrlnm())) {
            view = "resourceNotFound";
        }

        return view;
    }

    /**
     * This url mapping shows an overview of the products this store carries
     *
     * @param urlName urlName
     * @param model   model
     * @return Return value
     * @throws Exception Exception
     */
    @RequestMapping(value = "/{seoUrlName}/s/deals", method = RequestMethod.GET)
    public String showStoreDealsSEO(@PathVariable("seoUrlName") String seoUrlName,
                                    @RequestParam(value = "rlnm", required = true) String urlName,
                                    Model model)
            throws Exception {

        model.addAttribute("rlnm", urlName);

        return "store.deals";
    }

    /**
     * This url mapping shows an overview of the products this store carries
     *
     * @param urlName urlName
     * @param session session
     * @param model   model
     * @param locale  locale
     * @return Return value
     * @throws Exception Exception
     * @deprecated Use showSEOStore above instead
     */
    @RequestMapping(value = "/store/{urlName}", method = RequestMethod.GET)
    public ModelAndView showStore(@PathVariable("urlName") String urlName, HttpSession session, Model model, Locale locale)
            throws Exception {
        ModelAndView view = new ModelAndView("store.index");
        RelevantItemsInStoreSearchQuery query = new RelevantItemsInStoreSearchQuery();
        query.setUrlName(urlName);
        query.setPage(0);
        query.setSize(4);

        Store store = storeService.findStoreByUrlName(query.getUrlName());

        if (store == null) {
            view.setViewName("resourceNotFound");
        } else {
            if (StringUtils.isNotBlank(store.getSrlnm())) {
                // redirect to the url mapping above
                StringBuilder sb = new StringBuilder("/");
                sb.append(store.getSrlnm()).append("/s?rlnm=");
                sb.append(store.getRlnm());
                RedirectView rView = new RedirectView(sb.toString());
                rView.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
                view = new ModelAndView(rView);
            } else {
                doCommonShowStore(query, session, model, locale);
            }
        }

        return view;
    }

    /**
     * This method drills down to the category level of the brand
     *
     * @param urlName            urlName
     * @param seoCategoryUrlName seoCategoryUrlName
     * @param page               page
     * @param session            session
     * @param model              model
     * @param locale             locale
     * @return Tile def
     * @throws Exception ex
     */
    @RequestMapping(value = "/{seoUrlName}/{seoCategoryUrlName}/s", method = RequestMethod.GET)
    public String showStoreCategorySEO(@PathVariable("seoUrlName") String seoUrlName,
                                       @PathVariable("seoCategoryUrlName") String seoCategoryUrlName,
                                       @RequestParam(value = "rlnm", required = true) String urlName,
                                       @RequestParam(value = "crlnm", required = true) String categoryUrlName,
                                       @RequestParam(value = "page", defaultValue = "0") Integer page,
                                       HttpSession session, Model model, Locale locale)
            throws Exception {
        String view = "store.category";

        RelevantItemsInStoreSearchQuery query = new RelevantItemsInStoreSearchQuery();
        query.setUrlName(urlName);
        query.setCat(categoryUrlName);
        query.setPage(page);
        query.setSize(12);

        doCommonShowStore(query, session, model, locale);

        Store store = (Store) model.asMap().get(WebConstants.STORE);

        if (store == null || StringUtils.isBlank(store.getSrlnm())) {
            view = "resourceNotFound";
        } else {
            model.addAttribute(WebConstants.URL_NAME, urlName);
            model.addAttribute(WebConstants.CATEGORY_URL_NAME, categoryUrlName);
        }

        return view;
    }

    /**
     * This method drills down to the category level of the store
     *
     * @param urlName         urlName
     * @param categoryUrlName categoryUrlName
     * @param page            page
     * @param session         session
     * @param model           model
     * @param locale          locale
     * @return Tile def
     * @throws Exception ex
     */
    @RequestMapping(value = "/store/{urlName}/{categoryUrlName}", method = RequestMethod.GET)
    public ModelAndView showStoreCategory(@PathVariable("urlName") String urlName,
                                          @PathVariable("categoryUrlName") String categoryUrlName,
                                          @RequestParam(value = "page", defaultValue = "0") Integer page,
                                          HttpSession session, Model model, Locale locale)
            throws Exception {
        ModelAndView view = new ModelAndView("store.category");
        RelevantItemsInStoreSearchQuery query = new RelevantItemsInStoreSearchQuery();
        query.setUrlName(urlName);
        query.setCat(categoryUrlName);
        query.setPage(page);
        query.setSize(12);

        Store store = storeService.findStoreByUrlName(query.getUrlName());

        if (store == null) {
            view.setViewName("resourceNotFound");
        } else {

            if (StringUtils.isNotBlank(store.getSrlnm())) {
                Category category = categoryService.findCategoryByUrlName(categoryUrlName);
                // redirect to the url mapping above
                StringBuilder sb = new StringBuilder("/");
                sb.append(store.getSrlnm());
                sb.append("/").append(category.getSrlnm()).append("/s?rlnm=");
                sb.append(store.getRlnm()).append("&crlnm=").append(category.getRlnm());
                RedirectView rView = new RedirectView(sb.toString());
                rView.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
                view = new ModelAndView(rView);
            } else {
                doCommonShowStore(query, session, model, locale);
            }

        }

        return view;
    }

    /**
     * Returns a page with stores
     *
     * @param model model
     * @return Tile def
     */
    @RequestMapping(value = "/stores", method = RequestMethod.GET)
    public String showStores(Model model) {
        String view = "stores.index";

        StoreAggregateQuery query = new StoreAggregateQuery();
        model.addAttribute(WebConstants.STORES, storeService.findStoreAggregateDetails(query));

        return view;
    }

    /**
     * Returns a page with stores that start with the first letter
     *
     * @param model model
     * @return Tile def
     */
    @RequestMapping(value = "/stores/{letter}", method = RequestMethod.GET)
    public String showStoresByName(@PathVariable("letter") String letter,
                                   Model model) {
        String view = "stores.index";

        StoreAggregateQuery query = new StoreAggregateQuery();
        query.setFilterOnLetter(letter);

        model.addAttribute(WebConstants.SELECTED_LETTER, letter);
        model.addAttribute(WebConstants.STORES, storeService.findStoreAggregateDetails(query));

        return view;
    }

    @RequestMapping(value = "/local", method = RequestMethod.GET)
    public String showLocalStores(LocationQuery locationQuery, HttpServletRequest request,
                                  HttpSession session, Model model) {
        String view = "local.stores.index";

        // get the user
        User user = retrieveUserFromPrincipalOrSession(session);
        UserSupplement us = userService.findUserSupplement(user.getCd());

        // If zip is not provided on the request, attempt to derive from
        // the logged in or transient user
        if (StringUtils.isBlank(locationQuery.getIpAddress())) {
            locationQuery.setIpAddress(request.getRemoteAddr());
        }

        if (StringUtils.isBlank(locationQuery.getZipcode()) && us != null
                && StringUtils.isNotBlank(us.getCzp())) {
            locationQuery.setZipcode(us.getCzp());
        }

        // If there was a real user, then update the zip code if its different
        if (StringUtils.isNotBlank(locationQuery.getZipcode()) &&
                us != null && !StringUtils.equals(locationQuery.getZipcode(), us.getCzp())) {
            us.setCzp(locationQuery.getZipcode());
            userService.saveUserSupplement(us);
        }

        // Perform the local store search
        LocalStoresSearchResult result = branchService.findLocalStoreAggregateDetails(locationQuery);

        model.addAttribute(WebConstants.BRANCHES, result);

        // Set a zipcode
        String zipcode = locationQuery.getZipcode();
        if (result.getSearchedForZipcode() != null) {
            zipcode = result.getSearchedForZipcode();
        }

        model.addAttribute(WebConstants.ZIPCODE, zipcode);

        return view;
    }

    @RequestMapping(value = "/local/{area}", method = RequestMethod.GET)
    public String showLocalStores(@PathVariable("area") String area,
                                  HttpServletRequest request,
                                  HttpSession session, Model model) {
        String view = "local.stores.index";

        LocationQuery locationQuery = new LocationQuery();

        // area can be either a zip code or a city name
        boolean isAreaCode = NumberUtils.isDigits(area);

        if (isAreaCode) {
            locationQuery.setZipcode(area);
        } else {
            locationQuery.setCity(area);
        }

        // Perform the local store search
        LocalStoresSearchResult result = branchService.findLocalStoreAggregateDetails(locationQuery);

        model.addAttribute(WebConstants.BRANCHES, result);

        // Set a zipcode
        String zipcode = locationQuery.getZipcode();
        if (StringUtils.isNotBlank(result.getSearchedForZipcode())) {

            // prioritize the returned zip code
            zipcode = result.getSearchedForZipcode();
        }

        model.addAttribute(WebConstants.ZIPCODE, zipcode);

        return view;
    }

    private void doCommonShowStore(RelevantItemsInStoreSearchQuery query, HttpSession session, Model model, Locale locale) {
        // see if user is already logged in
        // get the user
        User user = retrieveUserFromPrincipalOrSession(session);

        if (user != null) {
            // add user to the model
            query.setUserCode(user.getCd());

            // load up this store
            Store store = storeService.findStoreByUrlName(query.getUrlName());

            if (store != null) {

                model.addAttribute(WebConstants.STORE, store);

                // Grab all the deals for the store
                List<String> storeUrlNames = new ArrayList<String>(1);
                storeUrlNames.add(store.getRlnm());

                List<Deal> deals = dealService.findDealsForStores(storeUrlNames);
                model.addAttribute(WebConstants.DEALS, deals);

                AbstractSearchResult searchResults;

                try {
                    if (userService.shouldUserSeeRecommendedProducts(user.getCd())) {
                        // if a category is specified, we'll return a SearchResult object
                        if (StringUtils.isNotBlank(query.getCat())) {
                            searchResults = productEngineService.searchForRelevantCategoryItemsInStoreUsingSolr(query);
                        } else {
                            searchResults = productEngineService.searchForRelevantItemsInStoreUsingSolr(query);
                        }
                    } else {
                        // if a category is not specified, we'll return a GroupedSearchResult object
                        if (StringUtils.isNotBlank(query.getCat())) {
                            searchResults = productEngineService.searchForCategoryItemsInStoreUsingSolr(query);
                        } else {
                            searchResults = productEngineService.searchForItemsInStoreUsingSolr(query);
                        }
                    }

                    if (searchResults != null && searchResults.getCategories() != null) {
                        sortCategoriesFromSearchResult(searchResults.getCategories(), locale);
                        model.addAttribute(WebConstants.SEARCH_RESULTS, searchResults);
                    }
                } catch (SearchException ex) {
                    if (log.isErrorEnabled()) {
                        log.error(ex.getMessage());
                    }
                }
            } else {
                if (log.isWarnEnabled()) {
                    log.warn(String.format("Store: %s does not exist or is not available", query.getUrlName()));
                }
            }
        }
    }
}
