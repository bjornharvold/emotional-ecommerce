/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */


package com.lela.data.web.controller.administration.blog;

//~--- non-JDK imports --------------------------------------------------------

import java.util.HashMap;
import java.util.Locale;

import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lela.commons.service.BlogService;
import com.lela.commons.service.CategoryService;
import com.lela.commons.service.ItemService;
import com.lela.commons.service.ProductEngineService;
import com.lela.commons.service.UserService;
import com.lela.commons.web.utils.WebConstants;
import com.lela.data.web.validator.BlogValidator;
import com.lela.domain.document.Blog;
import com.lela.domain.document.BlogItem;
import com.lela.domain.document.RelevantItem;
import com.lela.domain.document.User;
import com.lela.domain.dto.RelevantItemQuery;
import com.lela.domain.dto.blog.BlogEntry;
import com.lela.domain.enums.PublishStatus;
import com.lela.domain.enums.SortBlogBy;

//~--- JDK imports ------------------------------------------------------------

//~--- classes ----------------------------------------------------------------

/**
 * <p>Title: BlogManagerController</p>
 * <p>Description: Admin blog pages.</p>
 *
 * @author Bjorn Harvold
 */
@Controller("administrationBlogController")
@SessionAttributes(types = BlogEntry.class)
public class BlogManagerController {

    /**
     * Field description
     */
    private final static Logger log = LoggerFactory.getLogger(BlogManagerController.class);

    private final BlogService blogService;

    private final BlogValidator blogValidator;

    private final MessageSource messageSource;

    private final UserService userService;

    private final ProductEngineService productEngineService;

    private final CategoryService categoryService;
    
    private final ItemService itemService;
    
    @Value("${preview.url:http://www.lela.com}")
    private String previewUrl;

    @Autowired
    public BlogManagerController(BlogService blogService,
                                 MessageSource messageSource,
                                 BlogValidator blogValidator,
                                 UserService userService, ProductEngineService productEngineService, 
                                 CategoryService categoryService,
                                 ItemService itemService                     
    		) {
        this.blogService = blogService;
        this.messageSource = messageSource;
        this.blogValidator = blogValidator;
        this.userService = userService;
        this.productEngineService = productEngineService;
        this.categoryService = categoryService;
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
    @RequestMapping(value = "/administration/blog/list", method = RequestMethod.GET)
    public String showBlogs(@RequestParam(value = "sort", required = false, defaultValue = "D") String sort, Model model) throws Exception {
        SortBlogBy sortBy = null;

        try {
            sortBy = SortBlogBy.valueOf(sort);
        } catch (IllegalArgumentException ex) {
            sortBy = SortBlogBy.D;
        }

        switch (sortBy) {
            case C:
                model.addAttribute(WebConstants.BLOGS, blogService.findBlogsOrderByCategory());
                break;
            case D:
                model.addAttribute(WebConstants.BLOGS, blogService.findBlogsOrderByDate());
                break;
        }
        model.addAttribute(WebConstants.PREVIEW_URL, previewUrl);

        return "administration.blogs";
    }

    /**
     * Shows a form to either insert or update a blog entry
     *
     * @param urlName urlName
     * @param model   model
     * @return Tile definition
     * @throws Exception ex
     */
    @RequestMapping(value = "/administration/blog", method = RequestMethod.GET)
    public String showBlog(@RequestParam(value = "urlName", required = false) String urlName, Model model) throws Exception {

        populateReferenceData(model);

        if (StringUtils.isNotBlank(urlName)) {
            model.addAttribute(WebConstants.BLOG_ENTRY, new BlogEntry(blogService.findBlogByUrlName(urlName)));
        } else {
            model.addAttribute(WebConstants.BLOG_ENTRY, new BlogEntry());
        }

        return "administration.blog";
    }

    @RequestMapping(value = "/administration/blog/{urlName}", method = RequestMethod.DELETE)
    public String deleteBlog(@PathVariable("urlName") String urlName,
                             RedirectAttributes redirectAttributes, Locale locale) throws Exception {

        Blog blog = blogService.findBlogByUrlName(urlName);

        if (blog != null) {
            blogService.deleteBlog(blog);

            // add a success message that can be seen on the redirected page
            String message = messageSource.getMessage("blog.deleted.successfully", new String[]{blog.getNm()}, locale);
            redirectAttributes.addFlashAttribute(WebConstants.MESSAGE, message);
        }

        return "redirect:/administration/blog/list";
    }



    private void populateReferenceData(Model model) {
        model.addAttribute(WebConstants.CATEGORIES, categoryService.findCategories());
        model.addAttribute(WebConstants.PUBLISH_STATUSES, PublishStatus.values());
    }

    /**
     * Submits a form to either insert or update a blog entry
     *
     * @param blog               blogEntry
     * @param errors             errors
     * @param redirectAttributes redirectAttributes
     * @param model              model
     * @param locale             locale
     * @return Tile definition
     * @throws Exception ex
     */
    @RequestMapping(value = "/administration/blog", method = RequestMethod.POST)
    public String submitBlog(@Valid BlogEntry blog, BindingResult errors,
                             RedirectAttributes redirectAttributes, Model model, Locale locale) throws Exception {
        String view = "redirect:/administration/blog/list";

        if (errors.hasErrors()) {
            model.addAttribute(WebConstants.BLOG_ENTRY, blog);
            populateReferenceData(model);
            return "administration.blog";
        }

        // remove empty array items
        blogService.removeEmptyItems(blog);

        // validate on business rules
        blogValidator.validate(blog, errors);

        if (errors.hasErrors()) {
            model.addAttribute(WebConstants.BLOG_ENTRY, blog);
            populateReferenceData(model);
            return "administration.blog";
        }

        blogService.saveBlog(blog);

        // add a success message that can be seen on the redirected page
        String message = messageSource.getMessage("blog.saved.successfully", new String[]{blog.getNm()}, locale);
        redirectAttributes.addFlashAttribute(WebConstants.MESSAGE, message);

        return view;
    }
}
