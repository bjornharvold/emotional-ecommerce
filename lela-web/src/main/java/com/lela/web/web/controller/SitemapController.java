/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.web.web.controller;

import com.lela.commons.service.BlogService;
import com.lela.commons.service.CategoryService;
import com.lela.commons.service.ItemService;
import com.lela.commons.service.NavigationBarService;
import com.lela.commons.service.OwnerService;
import com.lela.commons.service.StaticContentService;
import com.lela.commons.service.StoreService;
import com.lela.commons.web.utils.WebConstants;
import com.lela.domain.document.AffiliateAccount;
import com.lela.domain.document.Category;
import com.lela.domain.document.NavigationBar;
import com.lela.domain.dto.Sitemap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bjorn Harvold
 * Date: 4/12/12
 * Time: 10:18 PM
 * Responsibility:
 */
@Controller
public class SitemapController {

    private static final Integer DOCUMENT_LOAD = 100;
    private final BlogService blogService;
    private final StoreService storeService;
    private final OwnerService ownerService;
    private final CategoryService categoryService;
    private final ItemService itemService;
    private final StaticContentService staticContentService;
    private final NavigationBarService navigationBarService;

    @Autowired
    public SitemapController(BlogService blogService,
                             StoreService storeService,
                             OwnerService ownerService,
                             CategoryService categoryService,
                             ItemService itemService,
                             StaticContentService staticContentService,
                             NavigationBarService navigationBarService) {
        this.blogService = blogService;
        this.storeService = storeService;
        this.ownerService = ownerService;
        this.categoryService = categoryService;
        this.itemService = itemService;
        this.staticContentService = staticContentService;
        this.navigationBarService = navigationBarService;
    }

    /**
     * Loads up all the data necessary to populate the Sitemap.xml that is read by robots
     *
     * @param model model
     * @return Tile def                                                                                N
     */
    @RequestMapping(value = "/Sitemap.xml", method = RequestMethod.GET)
    public String showSitemap(Model model,
                              HttpServletRequest request) {
        Sitemap sitemap = new Sitemap();

        NavigationBar navbar = null;

        // If there is an affiliate domain, determine if it has a navbar override
        AffiliateAccount domainAffiliate = (AffiliateAccount)request.getAttribute(WebConstants.DOMAIN_AFFILIATE);
        if (domainAffiliate != null && domainAffiliate.getDmnnvbr() != null) {
            navbar = navigationBarService.findNavigationBarByUrlName(domainAffiliate.getDmnnvbr());
        }
        if (navbar == null) {
            navbar = navigationBarService.findNavigationBarByUrlName(WebConstants.CONSUMER_NAVIGATION_BAR_URL_NAME);
        }

        // load up all categories
        if (navbar != null) {
            List<Category> categories = navbar.getCtgrs();

            if (categories != null && !categories.isEmpty()) {
                sitemap.setCtgrs(categories);
                List<String> categoryUrlNames = new ArrayList<String>(categories.size());

                for (Category category : categories) {
                    categoryUrlNames.add(category.getRlnm());
                }

                sitemap.setTms(itemService.paginateThroughAllItemsPerCategories(categoryUrlNames, DOCUMENT_LOAD));
            }

            // static content
            List<String> fields = new ArrayList<String>(1);
            fields.add("rlnm");
            sitemap.setSttc(staticContentService.paginateThroughAllStaticContent(DOCUMENT_LOAD, fields));

            // blog
            sitemap.setBlgs(blogService.paginateThroughAllBlogs(DOCUMENT_LOAD));

            // stores
            sitemap.setStrs(storeService.findAllStores(DOCUMENT_LOAD));

            // owners
            sitemap.setWnrs(ownerService.findAllOwners(DOCUMENT_LOAD));
        }

        model.addAttribute(WebConstants.SITEMAP, sitemap);

        return "sitemap";
    }



}
