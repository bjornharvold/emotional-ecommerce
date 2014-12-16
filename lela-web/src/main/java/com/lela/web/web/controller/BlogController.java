/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */


package com.lela.web.web.controller;

//~--- non-JDK imports --------------------------------------------------------

import com.lela.commons.service.BlogService;
import com.lela.commons.service.ItemService;
import com.lela.commons.service.ProductEngineService;
import com.lela.commons.service.UserService;
import com.lela.commons.spring.security.SpringSecurityHelper;
import com.lela.commons.web.ResourceNotFoundException;
import com.lela.commons.web.utils.AjaxUtils;
import com.lela.commons.web.utils.WebConstants;
import com.lela.domain.document.Blog;
import com.lela.domain.document.BlogItem;
import com.lela.domain.document.Item;
import com.lela.domain.document.ItemDetails;
import com.lela.domain.document.RelevantItem;
import com.lela.domain.document.User;
import com.lela.domain.dto.Principal;
import com.lela.domain.dto.RelevantItemQuery;
import com.lela.domain.dto.blog.BlogsQuery;
import com.lela.domain.enums.SortBlogBy;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//~--- JDK imports ------------------------------------------------------------

//~--- classes ----------------------------------------------------------------

/**
 * <p>Title: BlogController</p>
 * <p>Description: Displays a blog to the user.</p>
 *
 * @author Bjorn Harvold
 */
@Controller("blogController")
public class BlogController {

    /**
     * Field description
     */
    private final static Logger log = LoggerFactory.getLogger(BlogController.class);

    private final BlogService blogService;

    private final UserService userService;

    private final ProductEngineService productEngineService;
    
    private final ItemService itemService;

    @Autowired
    public BlogController(BlogService blogService, UserService userService,
                          ProductEngineService productEngineService,
                          ItemService itemService
    		) {
        this.blogService = blogService;
        this.userService = userService;
        this.productEngineService = productEngineService;
        this.itemService = itemService;
    }

    //~--- methods ------------------------------------------------------------

    /**
     * Displays a list of all blogs in the system
     *
     * @param sort  sort
     * @param model model
     * @return Return value
     * @throws Exception Exception
     */
    @RequestMapping(value = "/blog", method = RequestMethod.GET)
    public String showBlogs(@RequestParam(value = "sort", required = false, defaultValue = "D") SortBlogBy sort,
                            @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                            WebRequest request, Model model) throws Exception {

        model.addAttribute(WebConstants.BLOGS, blogService.findBlogs(new BlogsQuery(sort, page)));

        return AjaxUtils.isAjaxRequest(request) ? "blogs" : "blog.landing";
    }

    /**
     * Shows a single blog page
     *
     * @param urlName urlName
     * @param session session
     * @param model   model
     * @return Tile definition
     * @throws Exception ex
     */
    @RequestMapping(value = "/blog/{urlName}", method = RequestMethod.GET)
    public String showBlog(@PathVariable("urlName") String urlName, HttpSession session, Model model) throws Exception {
        Blog blog = blogService.findPublishedBlogByUrlName(urlName);

        return doCommonBlog(session, model, blog);
    }

    /*
      Preview must be done on the lela app b/c it has all the static artifacts.
      //TODO move tests from data app
     */
    @RequestMapping(value = "/administration/blog/{urlName}/preview", method = RequestMethod.POST)
    public String previewBlog(@PathVariable("urlName") String urlName,
                              @RequestParam(value = "ml", required = false) String email,
                              Model model) throws Exception {
        String view = "blog";

        Blog blog = blogService.findBlogByUrlName(urlName);

        if (blog == null) {
            view = "resourceNotFound";
        } else {

            if (StringUtils.isNotBlank(email)) {
                User user = userService.findUserByEmail(email);

                // determine if we should compute motivator relevancy here
                if (user != null && userService.shouldUserSeeRecommendedProducts(user.getCd())) {
                    if (blog.getTms() != null && !blog.getTms().isEmpty()) {
                        for (BlogItem blogItem : blog.getTms()) {

                            // let's try to retrieve the item from the db first
                            RelevantItemQuery query = new RelevantItemQuery(blogItem.getRlnm(), user.getCd());
                            RelevantItem ri = productEngineService.findRelevantItem(query);

                            if (blog.getRelevantItems() == null) {
                                blog.setRelevantItems(new HashMap<String, RelevantItem>());
                            }

                            blog.getRelevantItems().put(blogItem.getRlnm(), ri);
                        }
                    }
                }
            }

            model.addAttribute(WebConstants.BLOG, blog);
        }

        return view;
    }


    private String doCommonBlog(HttpSession session, Model model, Blog blog) {
        String result = "blog";

        if (blog == null) {
            result = "resourceNotFound";
        } else {

            model.addAttribute(WebConstants.BLOGS, blogService.findBlogs(new BlogsQuery(SortBlogBy.D, 0)));

            // see if user is already logged in
            Principal principal = SpringSecurityHelper.getSecurityContextPrincipal();
            User user;

            if (principal != null) {
                user = principal.getUser();
            } else {
                user = (User) session.getAttribute(WebConstants.USER);
            }

            // determine if we should compute motivator relevancy here
            if (user != null && userService.shouldUserSeeRecommendedProducts(user.getCd())) {
            	
            	
                if (blog.getTms() != null && !blog.getTms().isEmpty()) {
                    for (BlogItem blogItem : blog.getTms()) {

                        // let's try to retrieve the item from the db first
                        RelevantItemQuery query = new RelevantItemQuery(blogItem.getRlnm(), user.getCd());
                        RelevantItem ri = productEngineService.findRelevantItem(query);

                        if (blog.getRelevantItems() == null) {
                            blog.setRelevantItems(new HashMap<String, RelevantItem>());
                        }

                        blog.getRelevantItems().put(blogItem.getRlnm(), ri);
                    }
                }
            }


            model.addAttribute(WebConstants.BLOG, blog);
        }

        return result;
    }

}
