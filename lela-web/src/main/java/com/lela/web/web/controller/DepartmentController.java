/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.web.web.controller;

import com.lela.commons.event.EventHelper;
import com.lela.commons.event.ViewedDepartmentEvent;
import com.lela.commons.service.DepartmentService;
import com.lela.commons.service.NavigationBarService;
import com.lela.commons.web.utils.WebConstants;
import com.lela.domain.document.CategoryGroup;
import com.lela.domain.document.NavigationBar;
import com.lela.domain.document.User;
import com.lela.domain.dto.department.DepartmentQuery;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Locale;

/**
 * Created by Bjorn Harvold
 * Date: 11/1/12
 * Time: 12:15 AM
 * Responsibility: Handles all functionality for category group pages
 */
@Controller
public class DepartmentController extends AbstractController {

    private final NavigationBarService navigationBarService;
    private final DepartmentService departmentService;

    @Autowired
    public DepartmentController(NavigationBarService navigationBarService,
                                DepartmentService departmentService) {
        this.navigationBarService = navigationBarService;
        this.departmentService = departmentService;
    }


    /**
     * Shows the actual search results from department landing page
     * @param seoUrlName seoUrlName
     * @param urlName urlName
     * @param query query
     * @param session session
     * @param model model
     * @param device device
     * @return Tile def
     */
    @RequestMapping(value = "/{seoUrlName}/d/r", method = RequestMethod.POST,
            consumes = "application/json")
    public String showSEOFriendlyDepartmentSearchResults(@PathVariable("seoUrlName") String seoUrlName,
                                                         @RequestParam(value = "rlnm", required = true) String urlName,
                                                         @RequestBody DepartmentQuery query, HttpSession session,
                                                         Model model, Device device) {
        User user = retrieveUserFromPrincipalOrSession(session);

        String view = "category.departments.search.results";
        query.setRlnm(urlName);

        model.addAttribute(WebConstants.DEPARTMENT_SEARCH_RESULTS, departmentService.findDepartmentSearchResults(user.getCd(), query));

        return returnMobileViewIfNecessary(model, device, session, view);
    }

    /**
     * Displays the department landing page. If the department has functional filters
     * we show the filter landing page. If not, it shows a general landing page.
     * @param seoUrlName seoUrlName
     * @param urlName urlName
     * @param device device
     * @param session session
     * @param model model
     * @return Tile def
     * @throws Exception
     */
    @RequestMapping(value = "/{seoUrlName}/d", method = RequestMethod.GET)
    public String showSEOFriendlyDepartmentLandingPage(@PathVariable("seoUrlName") String seoUrlName,
                                            @RequestParam(value = "rlnm", required = true) String urlName,
                                  Device device, 
                                  HttpServletRequest request,
                                  HttpServletResponse response,
                                  HttpSession session, Model model) throws Exception {

        // get the user
        User user = retrieveUserFromPrincipalOrSession(session);
        EventHelper.post(new ViewedDepartmentEvent(user, seoUrlName));

        // set the department
        String view = "category.departments";
        model.addAttribute(WebConstants.CURRENT_DEPARTMENT, urlName);

        // see if there are any category group functional filters
        Long functionalFilterCount = departmentService.findFunctionalFilterCountByCategoryGroupUrlName(urlName);

        removeSortOrderCookie(request, response);
        
        if (functionalFilterCount > 0) {
            // return department landing page with filters
            view += ".filters";
            model.addAttribute(WebConstants.DEPARTMENT_LANDING_PAGE, departmentService.findDepartmentLandingPage(urlName, user.getCd()));
        }

        return returnMobileViewIfNecessary(model, device, session, view);
    }

    /**
     * Category department landing page
     *
     * @return Return value
     * @throws Exception Exception
     * @deprecated Use seo friendly method instead
     */
    @RequestMapping(value = "/department/{urlName}", method = RequestMethod.GET)
    public ModelAndView showDepartment(@PathVariable("urlName") String categoryGroupUrlName,
                                 Locale locale)
            throws Exception {

        NavigationBar nb = navigationBarService.findDefaultNavigationBar(locale);

        if (nb == null) {
            // TODO hack - retrieve the US one
            nb = navigationBarService.findDefaultNavigationBar(Locale.US);
        }

        CategoryGroup cg = nb.findCategoryGroupByUrlName(categoryGroupUrlName);
        StringBuilder sb = new StringBuilder("/");
        sb.append(cg.getSrlnm()).append("/d?rlnm=");
        sb.append(cg.getRlnm());
        RedirectView rView = new RedirectView(sb.toString());
        rView.setStatusCode(HttpStatus.MOVED_PERMANENTLY);

        return new ModelAndView(rView);
    }

    /**
     * This method will be called when users make a change in their drop down selection
     * @param seoUrlName seoUrlName
     * @param urlName urlName
     * @param query query
     * @param device device
     * @return Tile def
     */
    @RequestMapping(value = "/{seoUrlName}/d", method = RequestMethod.POST,
            consumes = "application/json")
    public String showSEOFriendlyDepartmentLandingPageData(@PathVariable("seoUrlName") String seoUrlName,
                                   @RequestParam(value = "rlnm", required = true) String urlName,
                                   @RequestBody DepartmentQuery query, HttpSession session,
                                   Model model, Device device) {

        String view = "category.departments.filters.data";
        query.setRlnm(urlName);

        model.addAttribute(WebConstants.DEPARTMENT_LANDING_PAGE_DATA, departmentService.findDepartmentLandingPageData(query));

        return returnMobileViewIfNecessary(model, device, session, view);
    }

    private void removeSortOrderCookie(HttpServletRequest request, HttpServletResponse response){
    	
    	Cookie theCookie = null;
        if (request.getCookies() != null) {
            for (int i=0; i<request.getCookies().length; i++) {
                Cookie cookie = request.getCookies()[i];
                if (cookie != null && WebConstants.ITEM_SORT_ORDER_COOKIE.equals(cookie.getName())) {
                	theCookie = cookie;
                    break;
                }
            }
        }
        if (theCookie != null){
        	theCookie.setPath("/");
	        theCookie.setMaxAge(0);
	        response.addCookie(theCookie);
        }
    }

}
