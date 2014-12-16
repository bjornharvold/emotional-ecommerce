/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */


package com.lela.web.web.controller;

//~--- non-JDK imports --------------------------------------------------------

import com.lela.commons.event.CompareItemsEvent;
import com.lela.commons.event.EventHelper;
import com.lela.commons.event.ViewedItemEvent;
import com.lela.commons.service.CategoryService;
import com.lela.commons.service.DealService;
import com.lela.commons.service.ItemService;
import com.lela.commons.service.MerchantService;
import com.lela.commons.service.ProductEngineService;
import com.lela.commons.service.ProductReviewService;
import com.lela.commons.service.UserService;
import com.lela.commons.web.utils.WebConstants;
import com.lela.domain.document.AbstractItem;
import com.lela.domain.document.AvailableInStore;
import com.lela.domain.document.Category;
import com.lela.domain.document.Deal;
import com.lela.domain.document.Item;
import com.lela.domain.document.ProductReview;
import com.lela.domain.document.User;
import com.lela.domain.document.UserSupplement;
import com.lela.domain.dto.BranchSearchResults;
import com.lela.domain.document.ItemDetails;
import com.lela.domain.dto.LocationQuery;
import com.lela.domain.dto.RelevantItemQuery;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

//~--- JDK imports ------------------------------------------------------------

//~--- classes ----------------------------------------------------------------

/**
 * <p>Title: ItemController</p>
 * <p>Description: Dispatches an item details page.</p>
 *
 * @author Bjorn Harvold
 */
@Controller
public class ItemController extends AbstractController {

    private final static Logger log = LoggerFactory.getLogger(ItemController.class);

    /**
     * Field description
     */
    private final ProductEngineService productEngineService;

    /**
     * Field description
     */
    private final MerchantService merchantService;

    /**
     * Field description
     */
    private final UserService userService;

    private final ItemService itemService;
    
    private final ProductReviewService productReviewService;

    private final DealService dealService;

    private final CategoryService categoryService;

    //~--- constructors -------------------------------------------------------

    /**
     * Constructs ...
     *
     * @param productEngineService productEngineService
     * @param merchantService      merchantService
     * @param userService          userService
     * @param itemService          itemService
     * @param productReviewService productReviewService
     * @param dealService          dealService
     */
    @Autowired
    public ItemController(ProductEngineService productEngineService,
                          MerchantService merchantService,
                          UserService userService,
                          ItemService itemService,
                          CategoryService categoryService,
                          ProductReviewService productReviewService,
                          DealService dealService) {
        this.productEngineService = productEngineService;
        this.merchantService = merchantService;
        this.userService = userService;
        this.itemService = itemService;
        this.productReviewService = productReviewService;
        this.dealService = dealService;
        this.categoryService = categoryService;
    }

    //~--- methods ------------------------------------------------------------

    @RequestMapping(value = "/{seoUrlName}/p", method = RequestMethod.GET)
    public String showItemDetailsSEO(@PathVariable("seoUrlName") String seoUrlName,
                                     @RequestParam(value = "rlnm", required = true) String itemUrlName,
                                     @RequestParam(value = "drlnm", required = false) String departmentUrlName,
                                     HttpServletRequest request, HttpSession session, Model model, Device device) throws Exception {
        String view = "item.details";

        // get the user
        User user = retrieveUserFromPrincipalOrSession(session);

        if (user != null) {
            itemUrlName = StringEscapeUtils.escapeJavaScript(itemUrlName);

            AbstractItem item = doCommonItemTasks(user.getCd(), itemUrlName);

            if (item == null || !StringUtils.equals(item.getSrlnm(), seoUrlName)) {
                // throwing a soft 404 if item doesn't exist
                view = "resourceNotFound";
            } else {
                // don't redirect but display the data on the page
                model.addAttribute(WebConstants.ITEM_DETAILS, new ItemDetails(item));
                model.addAttribute(WebConstants.CATEGORY_URL_NAME, item.getCtgry().getRlnm());

                // Get an updated and sorted list of stores
                List<AvailableInStore> stores = merchantService.findOnlineStores(item);
                model.addAttribute(WebConstants.STORES, stores);

                // Grab all the deals for the stores
                if (stores != null && !stores.isEmpty()) {
                    List<String> storeUrlNames = new ArrayList<String>(stores.size());
                    for (AvailableInStore store : stores) {
                        storeUrlNames.add(store.getRlnm());
                    }

                    List<Deal> deals = dealService.findDealsForStores(storeUrlNames);
                    model.addAttribute(WebConstants.DEALS, deals);
                }

                // Perform the local store search
                doLocalStoreSearch(user.getCd(), new LocationQuery(), request, model, item);

                // add recommended items
                addOtherProductRecommendations(user.getCd(), item, model);

                //if the department is blank find the first department that the category belongs to
                departmentUrlName = categoryService.determineDefaultDepartmentUrl(item.getCtgry().getRlnm(), departmentUrlName);
                departmentUrlName = StringEscapeUtils.escapeJavaScript(departmentUrlName);
                model.addAttribute(WebConstants.CURRENT_DEPARTMENT, departmentUrlName);
            }
        }

        EventHelper.post(new ViewedItemEvent(user, itemUrlName, ViewedItemEvent.ItemViewType.DETAILS));
        return returnMobileViewIfNecessary(model, device, session, view);
        //return view;
    }

    /**
     * Shows the home page
     *
     * @param itemUrlName itemUrlName
     * @param request     request
     * @param session     session
     * @param model       model
     * @return Return value
     * @throws Exception Exception
     * @deprecated This is for search engine support while they haven't crawled the new url
     *             TODO only do redirect here when all items have srlnm on them.
     *             TODO we duplicate code here to support data that's not yet ready
     */
    @RequestMapping(value = "/item/{itemUrlName}", method = RequestMethod.GET)
    public ModelAndView showItemDetails(@PathVariable("itemUrlName") String itemUrlName,
                                        HttpServletRequest request, HttpSession session, Model model)
            throws Exception {
        ModelAndView result = new ModelAndView("item.details");

        // get the user
        User user = retrieveUserFromPrincipalOrSession(session);

        if (user != null) {
            itemUrlName = StringEscapeUtils.escapeJavaScript(itemUrlName);
            AbstractItem item = doCommonItemTasks(user.getCd(), itemUrlName);

            // throw a 404 if item doesn't exist
            if (item == null) {
                result.setViewName("resourceNotFound");
            } else {
                // at this point we are going to check whether this item already has
                // an seo-friendly url. if it does, we're going to redirect with 301 to the new url
                // so google gets a chance to re-index the new url
                if (StringUtils.isNotBlank(item.getSrlnm())) {
                    // redirect to the url mapping above
                    StringBuilder sb = new StringBuilder("/");
                    sb.append(item.getSrlnm()).append("/p?rlnm=");
                    sb.append(item.getRlnm());
                    RedirectView rView = new RedirectView(sb.toString());
                    rView.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
                    result = new ModelAndView(rView);
                } else {
                    // don't redirect but display the data on the page
                    model.addAttribute(WebConstants.ITEM_DETAILS, new ItemDetails(item));
                    model.addAttribute(WebConstants.CATEGORY_URL_NAME, item.getCtgry().getRlnm());

                    // Get an updated and sorted list of stores
                    List<AvailableInStore> stores = merchantService.findOnlineStores(item);
                    model.addAttribute(WebConstants.STORES, stores);

                    // Perform the local store search
                    doLocalStoreSearch(user.getCd(), new LocationQuery(), request, model, item);
                }
            }
        }

        EventHelper.post(new ViewedItemEvent(user, itemUrlName, ViewedItemEvent.ItemViewType.DETAILS));
        return result;
    }

    /**
     * Shows the print details page
     *
     * @param itemUrlName itemUrlName
     * @param request     request
     * @param session     session
     * @param model       model
     * @return Return value
     * @throws Exception Exception
     */
    @RequestMapping(value = "/{seoUrlName}/p/print", method = RequestMethod.GET)
    public String showItemPrintDetailsSEO(@PathVariable("seoUrlName") String seoUrlName,
                                          @RequestParam(value = "rlnm", required = true) String itemUrlName,
                                          HttpServletRequest request, HttpSession session, Model model)
            throws Exception {
        String view = "item.details.print";

        // get the user
        User user = retrieveUserFromPrincipalOrSession(session);

        if (user != null) {
            itemUrlName = StringEscapeUtils.escapeJavaScript(itemUrlName);
            AbstractItem item = doCommonItemTasks(user.getCd(), itemUrlName);

            // throw a 404 if item doesn't exist
            if (item == null || !StringUtils.equals(item.getSrlnm(), seoUrlName)) {
                view = "resourceNotFound";
            } else {
                model.addAttribute(WebConstants.ITEM_DETAILS, new ItemDetails(item));
                model.addAttribute(WebConstants.CATEGORY_URL_NAME, item.getCtgry().getRlnm());
                model.addAttribute(WebConstants.PRINT, true);

                // Get an updated and sorted list of stores
                List<AvailableInStore> stores = merchantService.findOnlineStores(item);
                model.addAttribute(WebConstants.STORES, stores);

                // Perform the local store search
                doLocalStoreSearch(user.getCd(), new LocationQuery(), request, model, item);
            }
        }

        EventHelper.post(new ViewedItemEvent(user, itemUrlName, ViewedItemEvent.ItemViewType.PRINT));
        return view;
    }

    /**
     * Shows the print details page
     *
     * @param itemUrlName itemUrlName
     * @param request     request
     * @param session     session
     * @param model       model
     * @return Return value
     * @throws Exception Exception
     * @deprecated This is for search engine support while they haven't crawled the new url
     */
    @RequestMapping(value = "/item/{itemUrlName}/print", method = RequestMethod.GET)
    public ModelAndView showItemPrintDetails(@PathVariable("itemUrlName") String itemUrlName,
                                             HttpServletRequest request, HttpSession session, Model model)
            throws Exception {

        ModelAndView result = new ModelAndView("item.details.print");

        // get the user
        User user = retrieveUserFromPrincipalOrSession(session);

        if (user != null) {
            itemUrlName = StringEscapeUtils.escapeJavaScript(itemUrlName);
            AbstractItem item = doCommonItemTasks(user.getCd(), itemUrlName);

            if (item == null) {
                result.setViewName("resourceNotFound");
            } else {

                // at this point we are going to check whether this item already has
                // an seo-friendly url. if it does, we're going to redirect with 301 to the new url
                // so google gets a chance to re-index the new url
                if (StringUtils.isNotBlank(item.getSrlnm())) {
                    // redirect to the url mapping above
                    StringBuilder sb = new StringBuilder("/");
                    sb.append(item.getSrlnm()).append("/p/print?rlnm=");
                    sb.append(item.getRlnm());
                    RedirectView rView = new RedirectView(sb.toString());
                    rView.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
                    result = new ModelAndView(rView);
                } else {
                    // don't redirect but display the data on the page
                    model.addAttribute(WebConstants.ITEM_DETAILS, new ItemDetails(item));
                    model.addAttribute(WebConstants.CATEGORY_URL_NAME, item.getCtgry().getRlnm());

                    // Get an updated and sorted list of stores
                    List<AvailableInStore> stores = merchantService.findOnlineStores(item);
                    model.addAttribute(WebConstants.STORES, stores);

                    // Perform the local store search
                    doLocalStoreSearch(user.getCd(), new LocationQuery(), request, model, item);
                }
            }
        }

        EventHelper.post(new ViewedItemEvent(user, itemUrlName, ViewedItemEvent.ItemViewType.PRINT));
        return result;
    }

    /**
     * This url should be used for the user list pages in case the item no longer exists with us
     * we still have the option of retrieving the item from one of the user's lists
     *
     * @param itemUrlName itemUrlName
     * @param request     request
     * @param session     session
     * @param model       model
     * @return Tile def
     * @throws Exception ex
     */
    @RequestMapping(value = "/item/{itemUrlName}/snapshot", method = RequestMethod.GET)
    public String showItemSnapshot(@PathVariable("itemUrlName") String itemUrlName, HttpServletRequest request,
                                   HttpSession session, Model model)
            throws Exception {

        String view = "item.details";

        model.addAttribute(WebConstants.SNAPSHOT, true);

        // get the user
        User user = retrieveUserFromPrincipalOrSession(session);

        if (user != null) {
            itemUrlName = StringEscapeUtils.escapeJavaScript(itemUrlName);
            // first we try to retrieve the original item
            AbstractItem item = doCommonItemTasks(user.getCd(), itemUrlName);

            // if it no longer exists we go to the user's lists to see if it's there
            if (item != null) {
                model.addAttribute(WebConstants.ITEM_DETAILS, new ItemDetails(item));
                model.addAttribute(WebConstants.CATEGORY_URL_NAME, item.getCtgry().getRlnm());

                // Get an updated and sorted list of stores
                List<AvailableInStore> stores = merchantService.findOnlineStores(item);
                model.addAttribute(WebConstants.STORES, stores);

                // Perform the local store search
                doLocalStoreSearch(user.getCd(), new LocationQuery(), request, model, item);

                // add recommended items
                addOtherProductRecommendations(user.getCd(), item, model);
            } else {
                if (log.isWarnEnabled()) {
                    log.warn("Item: " + itemUrlName + " is not available but it should be");
                }
            }
        }

        EventHelper.post(new ViewedItemEvent(user, itemUrlName, ViewedItemEvent.ItemViewType.SNAPSHOT));
        return view;
    }


    /**
     * @param itemUrlName   itemUrlName
     * @param locationQuery locationQuery
     * @param request       request
     * @param session       session
     * @param model         model
     * @return Tile definition name
     * @throws Exception ex
     */
    @RequestMapping(value = "/item/{itemUrlName}/stores", method = RequestMethod.POST,
            consumes = {"application/json"})
    public String showLocalStores(@PathVariable("itemUrlName") String itemUrlName,
                                  @RequestBody LocationQuery locationQuery,
                                  HttpServletRequest request,
                                  HttpSession session,
                                  Model model) throws Exception {
        User user = retrieveUserFromPrincipalOrSession(session);

        // Find the item
        Item item = itemService.findItemByUrlName(itemUrlName);

        if (user != null) {
            // Perform the local store search
            doLocalStoreSearch(user.getCd(), locationQuery, request, model, item);
        }

        return "merchant.stores";
    }

    /**
     * Compare items
     *
     * @param itemUrlNames itemUrlNames
     * @param session      session
     * @param model        model
     * @return Return value
     * @throws Exception Exception
     */
    @RequestMapping(value = "/item/comparison", method = RequestMethod.GET)
    public String showItemComparison(@RequestParam(value = "nm", required = true) String[] itemUrlNames,
                                     HttpSession session, Model model)
            throws Exception {

        String view = "item.comparison";

        // get the user
        User user = retrieveUserFromPrincipalOrSession(session);

        if (user != null) {
            if ((itemUrlNames != null) && (itemUrlNames.length > 0)) {
                List<AbstractItem> items = new ArrayList<AbstractItem>();
                List<Category> listCategories = new ArrayList<Category>();
                List<ItemDetails> listItemDetails = new ArrayList<ItemDetails>();

                for (String itemUrlName : itemUrlNames) {

                    itemUrlName = StringEscapeUtils.escapeJavaScript(itemUrlName);

                    AbstractItem item = doCommonItemTasks(user.getCd(), itemUrlName);
                    items.add(item);

                    Category cat = item.getCtgry();
                    ItemDetails id = new ItemDetails(item);
                    listItemDetails.add(id);

                    if (!listCategories.contains(cat)) {
                        listCategories.add(cat);
                    }

                }

                model.addAttribute(WebConstants.ITEM_DETAILS_LIST, listItemDetails);
                model.addAttribute(WebConstants.ITEMS, items);
                model.addAttribute(WebConstants.LIST_CATEGORIES, listCategories);
            }
        }

        EventHelper.post(new CompareItemsEvent(user, itemUrlNames));
        return view;
    }
    
    
    @RequestMapping(value = "/item/{itemUrlName}/reviews", method = RequestMethod.GET)
    public String showReviewsForItem(@PathVariable("itemUrlName") String itemUrlName, Model model)
            throws Exception {
    	ProductReview productReview = productReviewService.findProductReviewsByItemUrlName(itemUrlName);
    	if (productReview != null){
    		model.addAttribute(WebConstants.ITEM_PRODUCT_REVIEW, productReview);
    	}
        return "item.reviews";
    }

    /**
     * Method description
     *
     * @param itemUrlName itemUrlName
     * @return Return value
     */
    private AbstractItem doCommonItemTasks(String userCode, String itemUrlName) {
        boolean recommendProducts = userService.shouldUserSeeRecommendedProducts(userCode);
        AbstractItem result;

        if (recommendProducts) {
            RelevantItemQuery query = new RelevantItemQuery(itemUrlName, userCode);
            result = productEngineService.findRelevantItem(query);
        } else {
            result = itemService.findItemByUrlName(itemUrlName);
        }

        return result;
    }

    /**
     * Perform the local store search
     *
     * @param locationQuery locationQuery
     * @param request       request
     * @param model         model
     * @param item          item
     */
    private void doLocalStoreSearch(String userCode, LocationQuery locationQuery, HttpServletRequest request,
                                    Model model, AbstractItem item) {

        // Do nothing if there is no item
        if (item == null) {
            return;
        }

        // Supply defaults if not provided for IP
        if (StringUtils.isEmpty(locationQuery.getIpAddress())) {
            locationQuery.setIpAddress(request.getRemoteAddr());
        }

        UserSupplement us = userService.findUserSupplement(userCode);

        // If zip is not provided on the request, attempt to derive from
        // the logged in or transient user
        if (StringUtils.isEmpty(locationQuery.getZipcode()) && us != null) {
            locationQuery.setZipcode(us.getCzp());
        }

        // If there was a real user, then update the zip code if its different
        if (!StringUtils.isEmpty(locationQuery.getZipcode()) &&
                us != null &&
                !locationQuery.getZipcode().equals(us.getCzp())) {
            us.setCzp(locationQuery.getZipcode());
            userService.saveUserSupplement(us);
        }

        // Perform the local store search
        BranchSearchResults results = merchantService.findLocalStoresForItem(item, locationQuery, item.getStrs());

        model.addAttribute(WebConstants.BRANCHES, results);
        model.addAttribute(WebConstants.ITEM, item);

        // Set a zipcode
        String zipcode = locationQuery.getZipcode();
        if (results.getSearchedForZipcode() != null) {
            zipcode = results.getSearchedForZipcode();
        }

        model.addAttribute(WebConstants.ZIPCODE, zipcode);
    }

    /**
     * This method will search for products that match this one
     *
     * @param userCode userCode
     * @param item     item
     * @param model    model
     */
    private void addOtherProductRecommendations(String userCode, AbstractItem item, Model model) {
        model.addAttribute(WebConstants.SUPPLEMENTARY_ITEMS, productEngineService.findSupplementaryItemsByItem(userCode, item, 5));
    }
}
