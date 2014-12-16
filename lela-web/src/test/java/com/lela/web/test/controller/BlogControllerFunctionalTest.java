/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.web.test.controller;

import com.lela.commons.service.BlogService;
import com.lela.commons.spring.security.SpringSecurityHelper;
import com.lela.commons.web.ResourceNotFoundException;
import com.lela.commons.web.utils.WebConstants;
import com.lela.domain.document.Blog;
import com.lela.domain.document.Category;
import com.lela.domain.dto.CategoryBlogs;
import com.lela.domain.dto.CustomPage;
import com.lela.domain.dto.blog.BlogEntry;
import com.lela.domain.enums.PublishStatus;
import com.lela.domain.enums.SortBlogBy;
import com.lela.web.test.AbstractFunctionalTest;
import com.lela.web.web.controller.BlogController;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.ui.Model;
import org.springframework.validation.support.BindingAwareModelMap;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.handler.DispatcherServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.junit.Assert.*;

/**
 * Created by Bjorn Harvold
 * Date: 8/3/11
 * Time: 10:05 AM
 * Responsibility:
 */
@SuppressWarnings("unchecked")
public class BlogControllerFunctionalTest extends AbstractFunctionalTest {
    private static final Logger log = LoggerFactory.getLogger(BlogControllerFunctionalTest.class);
    private static final String TESTBLOG1 = "testblog1";
    private static final String TESTBLOG2 = "testblog2";

    private Blog blog1 = null;
    private Blog blog2 = null;

    @Autowired
    private BlogController blogController;

    @Autowired
    private BlogService blogService;
    private static final String CATEGORY = "blogcategory";

    @Before
    public void setUp() {
        SpringSecurityHelper.secureChannel();

        Category category = new Category();
        category.setRlnm(CATEGORY);
        category.setNm(CATEGORY);

        blog1 = new Blog();
        blog1.setNm(TESTBLOG1);
        blog1.setRlnm(TESTBLOG1);
        blog1.setCtgry(category);
        blog1.setStts(PublishStatus.UNPUBLISHED);

        blog1 = blogService.saveBlog(new BlogEntry(blog1));

        blog2 = new Blog();
        blog2.setNm(TESTBLOG2);
        blog2.setRlnm(TESTBLOG2);
        blog2.setCtgry(category);
        blog2.setStts(PublishStatus.PUBLISHED);

        blog2 = blogService.saveBlog(new BlogEntry(blog2));
    }

    @After
    public void tearDown() {

        if (blog1 != null && blog1.getId() != null) {
            blogService.deleteBlog(blog1);
        }

        if (blog2 != null && blog2.getId() != null) {
            blogService.deleteBlog(blog2);
        }

        SpringSecurityHelper.unsecureChannel();
    }

    @Test
    public void testBlogController() {
        log.info("Testing blog1 controller...");
        Model model = new BindingAwareModelMap();
        MockHttpSession session = new MockHttpSession();
        HttpServletRequest request = new MockHttpServletRequest();
        HttpServletResponse response = new MockHttpServletResponse();
        WebRequest webRequest = new DispatcherServletWebRequest(request, response);

        try {
            log.info("First we retrieve all blogs. There should be none but we won't fail the test if there is.");
            String view = blogController.showBlogs(SortBlogBy.D, 0, webRequest, model);
            assertEquals("Tile view is incorrect", "blog.landing", view);
            assertNotNull("Blog list is empty", model.asMap().get(WebConstants.BLOGS));
            CustomPage<CategoryBlogs> page = (CustomPage<CategoryBlogs>) model.asMap().get(WebConstants.BLOGS);
            assertTrue("List size is incorrect", page.hasContent());

            log.info("Then we sort by category. There should be none but we won't fail the test if there is.");
            view = blogController.showBlogs(SortBlogBy.C, 0, webRequest, model);
            assertEquals("Tile view is incorrect", "blog.landing", view);
            assertNotNull("Blog list is empty", model.asMap().get(WebConstants.BLOGS));
            page = (CustomPage<CategoryBlogs>) model.asMap().get(WebConstants.BLOGS);
            assertTrue("List size is incorrect", page.hasContent());
           
            log.info("Trying to retrieve a blog that has not yet been published");
            model = new BindingAwareModelMap();

            view = blogController.showBlog(TESTBLOG1, session, model);
            assertEquals("Tile view is incorrect", "resourceNotFound", view);

            log.info("Let's also preview this blog before we delete it again");
            model = new BindingAwareModelMap();
            view = blogController.previewBlog(TESTBLOG1, null, model);
            assertEquals("Tile view is incorrect", "blog", view);
            assertNotNull("Blog entity is null", model.asMap().get(WebConstants.BLOG));

            log.info("Trying to retrieve a blog that has been published");
            model = new BindingAwareModelMap();
            view = blogController.showBlog(TESTBLOG2, session, model);
            assertEquals("Tile view is incorrect", "blog", view);
            assertNotNull("No blog object found", model.asMap().get(WebConstants.BLOG));
            assertNotNull("No blog objects found", model.asMap().get(WebConstants.BLOGS));
            Blog blog1 = (Blog) model.asMap().get(WebConstants.BLOG);
            assertNotNull("Blog ID is null", blog1.getId());


        } catch (Exception e) {
            log.info(e.getMessage(), e);
            fail("Method threw unexpected exception: " + e.getMessage());
        }
        log.info("Testing blog1 controller complete");
    }

}
