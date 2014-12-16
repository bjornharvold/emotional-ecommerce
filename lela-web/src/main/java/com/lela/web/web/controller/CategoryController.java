/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */


package com.lela.web.web.controller;

//~--- non-JDK imports --------------------------------------------------------

import com.lela.commons.event.EventHelper;
import com.lela.commons.event.FunctionalFilterEvent;
import com.lela.commons.event.SortEvent;
import com.lela.commons.event.ViewedCategoryEvent;
import com.lela.commons.service.*;
import com.lela.commons.web.utils.AjaxUtils;
import com.lela.commons.web.utils.WebConstants;
import com.lela.domain.document.*;
import com.lela.domain.dto.CategoryItemsQuery;
import com.lela.domain.dto.FilterEventTrack;
import com.lela.domain.dto.ItemPage;
import com.lela.domain.dto.SimpleCategoryItemsQuery;
import com.lela.domain.enums.FunctionalSortType;
import com.lela.domain.enums.SeoUrlNameType;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

//~--- JDK imports ------------------------------------------------------------

//~--- classes ----------------------------------------------------------------

/**
 * <p>Title: CategoryController</p>
 * <p>Description: Dispatches category related pages.</p>
 *
 * @author Bjorn Harvold
 */
@Controller
public class CategoryController extends AbstractController {

    /**
     * Field description
     */
    private final static Logger log = LoggerFactory.getLogger(CategoryController.class);
    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_SIZE = 12;
    private static final String SORT = "sort";

    //~--- fields -------------------------------------------------------------

    /**
     * Field description
     */
    private final ProductEngineService productEngineService;

    private final SeoUrlNameService seoUrlNameService;

    private final CategoryService categoryService;

    private final NavigationBarService navigationBarService;

    private final UserService userService;

    //~--- constructors -------------------------------------------------------

    /**
     * Constructs ...
     *
     * @param productEngineService productEngineService
     * @param seoUrlNameService    seoUrlNameService
     * @param categoryService      categoryService
     * @param userService          userService
     */
    @Autowired
    public CategoryController(ProductEngineService productEngineService,
                              SeoUrlNameService seoUrlNameService,
                              CategoryService categoryService,
                              UserService userService,
                              NavigationBarService navigationBarService) {
        this.productEngineService = productEngineService;
        this.seoUrlNameService = seoUrlNameService;
        this.categoryService = categoryService;
        this.userService = userService;
        this.navigationBarService = navigationBarService;
    }

    //~--- methods ------------------------------------------------------------

    @RequestMapping(value = "/{seoUrlName}/c", method = RequestMethod.GET)
    public String showCategorySEO(@PathVariable("seoUrlName") String seoUrlName,
                                  @RequestParam(value = "rlnm", required = true) String categoryUrlName,
                                  @RequestParam(value = "page", required = false) Integer page,
                                  @RequestParam(value = "size", required = false) Integer size,
                                  @RequestParam(value = "drlnm", required = false) String departmentUrlName,
                                  HttpSession session, Model model, WebRequest request,
                                  HttpServletRequest httpServletRequest,
                                  Device device)
            throws Exception {

        //if the department is blank find the first department that the category belongs to
        departmentUrlName = categoryService.determineDefaultDepartmentUrl(categoryUrlName, departmentUrlName);

        categoryUrlName = StringEscapeUtils.escapeJavaScript(categoryUrlName);
        departmentUrlName = StringEscapeUtils.escapeJavaScript(departmentUrlName);

        // get the user
        User user = retrieveUserFromPrincipalOrSession(session);

        CategoryItemsQuery query = null;

        // Check for incoming flash attributes to see if there is a category query from a prior redirect
        SimpleCategoryItemsQuery flashQuery = (SimpleCategoryItemsQuery) model.asMap().get(WebConstants.CATEGORY_QUERY);
        if (flashQuery == null) {
            query = new CategoryItemsQuery();
        } else {
            query = new CategoryItemsQuery(flashQuery);
        }

        query.setCategoryUrlName(categoryUrlName);
        query.setUserCode(user.getCd());
        query.setPage(page);
        query.setSize(size);

        String view = doCommonCategoryTasks(query, model, device, request, session, httpServletRequest);

        Category category = (Category) model.asMap().get(WebConstants.CATEGORY);
        model.addAttribute(WebConstants.CURRENT_DEPARTMENT, departmentUrlName);

        // TODO we can retire this code and put in in the common method when we retire the non-seo friendly urls
        if (!StringUtils.equals(seoUrlName, category.getSrlnm())) {
            // we need to check to see if there is a seo url name matching the seo url name
            SeoUrlName seo = seoUrlNameService.validateSeoUrlName(seoUrlName, SeoUrlNameType.CATEGORY);

            if (seo == null) {
                view = "resourceNotFound";
            }
        }

        // Track the category view
        EventHelper.post(new ViewedCategoryEvent(user, categoryUrlName));

        return view;
    }

    /**
     * Display category page with initial data. This is the url is for a first level category with no parent.
     * e.g. /category/baby
     *
     * @param categoryUrlName categoryUrlName
     * @param page            page
     * @param size            size
     * @param session         session
     * @param model           model
     * @param request         request
     * @return Return value
     * @throws Exception Exception
     * @deprecated This is for search engine support while they haven't crawled the new url
     */
    @RequestMapping(value = "/category/{categoryUrlName}", method = RequestMethod.GET)
    public ModelAndView showCategory(@PathVariable("categoryUrlName") String categoryUrlName,
                                     @RequestParam(value = "page", required = false) Integer page,
                                     @RequestParam(value = "size", required = false) Integer size,
                                     HttpSession session, Model model, WebRequest request, 
                                     HttpServletRequest httpServletRequest,
                                     Device device)
            throws Exception {
        ModelAndView result = null;
        categoryUrlName = StringEscapeUtils.escapeJavaScript(categoryUrlName);

        // get the user
        User user = retrieveUserFromPrincipalOrSession(session);

        // retrieve the category before doing anything else so we can verify it exists before querying its items
        Category category = categoryService.findCategoryByUrlName(categoryUrlName);

        if (StringUtils.isNotBlank(category.getSrlnm())) {
            // redirect to the url mapping above
            StringBuilder sb = new StringBuilder("/");
            sb.append(category.getSrlnm()).append("/c?rlnm=");
            sb.append(category.getRlnm());
            RedirectView rView = new RedirectView(sb.toString());
            rView.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
            result = new ModelAndView(rView);
        } else {
            CategoryItemsQuery query = new CategoryItemsQuery();
            query.setCategoryUrlName(categoryUrlName);
            query.setUserCode(user.getCd());
            query.setPage(page);
            query.setSize(size);

            result = new ModelAndView(doCommonCategoryTasks(query, model, device, request, session, httpServletRequest));
        }

        // Track the category view
        EventHelper.post(new ViewedCategoryEvent(user, categoryUrlName));

        return result;
    }


    @RequestMapping(value = "/{seoUrlName}/cad/{categoryUrlName}", method = RequestMethod.GET)
    public String showCategorySEOAd(@PathVariable("seoUrlName") String seoUrlName,
                                    @PathVariable("categoryUrlName") String categoryUrlName,
                                    SimpleCategoryItemsQuery query,
                                    HttpSession session, Model model, WebRequest request, 
                                    HttpServletRequest httpServletRequest,
                                    Device device) throws Exception {
        String view = null;

        query.setRlnm(categoryUrlName);
        CategoryItemsQuery newQuery = new CategoryItemsQuery(query);

        // get the user
        User user = retrieveUserFromPrincipalOrSession(session);

        SeoUrlName seo = seoUrlNameService.validateSeoUrlName(seoUrlName, SeoUrlNameType.FUNCTIONAL_FILTER_PRESET);

        // validate on the seo url name so random characters cannot be freely entered
        if (seo == null) {
            view = "resourceNotFound";
        } else {
            model.addAttribute(WebConstants.SEO, seo);
            view = showCategoryAjax(newQuery.getCategoryUrlName(), newQuery, session, model, request, httpServletRequest,  device);
        }

        // Track the category view
        EventHelper.post(new ViewedCategoryEvent(user, categoryUrlName));

        return view;
    }

    /**
     * Method description
     *
     * @param categoryUrlName categoryUrlName
     * @param query           query
     * @param session         session
     * @param model           model
     * @param request         request
     * @return Return value
     * @throws Exception Exception
     */
    @RequestMapping(value = "/category/{categoryUrlName}", method = RequestMethod.POST, consumes = "application/json")
    public String showCategoryAjax(@PathVariable("categoryUrlName") String categoryUrlName,
                                   @RequestBody CategoryItemsQuery query, HttpSession session,
                                   Model model, WebRequest request, 
                                   HttpServletRequest httpServletRequest,
                                   Device device)
            throws Exception {
        categoryUrlName = StringEscapeUtils.escapeJavaScript(categoryUrlName);
        String result = null;

        // get the user
        User user = retrieveUserFromPrincipalOrSession(session);

        query.setCategoryUrlName(categoryUrlName);
        query.setUserCode(user.getCd());
        result = doCommonCategoryTasks(query, model, device, request, session, httpServletRequest);

        // Event handling
        if (query.getEventTracking() != null) {
            for (FilterEventTrack track : query.getEventTracking()) {
                if (SORT.equals(track.getFilter())) {
                    EventHelper.post(new SortEvent(user, categoryUrlName, track.getValue()));
                } else {
                    EventHelper.post(new FunctionalFilterEvent(user, categoryUrlName, track.getFilter(), track.getOption(), track.getValue()));
                }
            }
        }

        return result;
    }

    /**
     * Method description
     *
     * @param query   query
     * @param model   model
     * @param request request
     * @return Return value
     */
    private String doCommonCategoryTasks(CategoryItemsQuery query, Model model, Device device, WebRequest request, HttpSession session, HttpServletRequest httpServletRequest) {
        String view = null;
        boolean recommendProducts = false;

        // retrieve the category before doing anything else so we can verify it exists before querying its items
        Category category = categoryService.findCategoryByUrlName(query.getCategoryUrlName());

        // throw a 404 if category doesn't exist
        if (category == null) {
            view = "resourceNotFound";
        } else {

            model.addAttribute(WebConstants.CATEGORY, category);
            model.addAttribute(WebConstants.CATEGORY_URL_NAME, query.getCategoryUrlName());

            if (StringUtils.isNotBlank(query.getUserCode())) {
                recommendProducts = userService.shouldUserSeeRecommendedProducts(query.getUserCode());
            }

            view = determineView(query, request, model);

            // it's important that this snippet of code comes after determineView()
            if ((query.getPage() == null)) {
                query.setPage(DEFAULT_PAGE);
            }

            if ((query.getSize() == null)) {
                query.setSize(DEFAULT_SIZE);
            }
            setSortOrderFromCookie(query, httpServletRequest);

            if (recommendProducts) {

                ItemPage<RelevantItem> relevantItemPage = productEngineService.findRelevantItemsByCategory(query);
                model.addAttribute(WebConstants.PAGE, relevantItemPage);
                model.addAttribute(WebConstants.SORT_OPTIONS, FunctionalSortType.values());

            } else {

                ItemPage<Item> itemPage = productEngineService.findItemsByCategory(query);

                // we need to remove the relevant sort option as it can't exist in this context
                FunctionalSortType[] sortTypes = FunctionalSortType.values();
                List<FunctionalSortType> list = new ArrayList<FunctionalSortType>(sortTypes.length - 1);
                for (FunctionalSortType st : sortTypes) {
                    if (!st.equals(FunctionalSortType.RECOMMENDED)) {
                        list.add(st);
                    }
                }

                model.addAttribute(WebConstants.SORT_OPTIONS, list);
                model.addAttribute(WebConstants.PAGE, itemPage);

            }
        }

        return returnMobileViewIfNecessary(model, device, session, view);
    }

    private String determineView(CategoryItemsQuery query, WebRequest wRequest, Model model) {
        String view;

        if ((query.getPage() != null)) {
            if (AjaxUtils.isAjaxRequest(wRequest)) {
                view = "category.data";
                model.addAttribute(WebConstants.IS_AJAX, true);
            } else {
                view = "category.print";
            }
        } else {
            view = "category.page";
        }

        return view;
    }
    
    private void setSortOrderFromCookie(CategoryItemsQuery query, HttpServletRequest request){
        String sortOrder = null;
        if (request.getCookies() != null) {
            for (int i=0; i<request.getCookies().length; i++) {
                Cookie cookie = request.getCookies()[i];
                if (cookie != null && WebConstants.ITEM_SORT_ORDER_COOKIE.equals(cookie.getName())) {
                    sortOrder = cookie.getValue();
                    break;
                }
            }
        }
        if (sortOrder != null){
        	query.setSortBy(FunctionalSortType.getEnumByValue(sortOrder));
        }
    }

}
