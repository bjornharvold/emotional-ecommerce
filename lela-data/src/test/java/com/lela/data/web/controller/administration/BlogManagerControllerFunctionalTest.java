/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.data.web.controller.administration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.support.BindingAwareModelMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import com.lela.commons.service.BlogService;
import com.lela.commons.service.CategoryService;
import com.lela.commons.service.ItemService;
import com.lela.commons.spring.security.SpringSecurityHelper;
import com.lela.commons.web.utils.WebConstants;
import com.lela.data.web.AbstractFunctionalTest;
import com.lela.data.web.controller.administration.blog.BlogManagerController;
import com.lela.domain.document.Blog;
import com.lela.domain.document.BlogItem;
import com.lela.domain.document.Category;
import com.lela.domain.document.Item;
import com.lela.domain.dto.blog.BlogEntry;
import com.lela.domain.enums.PublishStatus;
import com.lela.domain.enums.SortBlogBy;

/**
 * Created by Bjorn Harvold
 * Date: 8/3/11
 * Time: 10:05 AM
 * Responsibility:
 */
@SuppressWarnings("unchecked")
public class BlogManagerControllerFunctionalTest extends AbstractFunctionalTest {
    private static final Logger log = LoggerFactory.getLogger(BlogManagerControllerFunctionalTest.class);
    private static final String TESTBLOG = "BlogManagerControllerFunctionalTest";
    private static final String CATEGORY_URL_NAME = "BlogManagerControllerFunctionalTest";
    private static final String INCORRECT_CATEGORY_URL_NAME = "WRONGAdministrationBlogControllerFunctionalTest";
    private static final String ITEM1_URL_NAME = "AdminBlogItem1";
    private static final String ITEM2_URL_NAME = "AdminBlogItem2";

    private Blog blog = null;

    private ClassPathResource blogImage = new ClassPathResource("testdata/image/blogimage.png");

    @Autowired
    private BlogManagerController blogManagerController;

    @Autowired
    private BlogService blogService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private CategoryService categoryService;

    private Category category;
    private Category wrongCategory;

    private Item item1;
    private Item item2;

    @Before
    public void setUp() {
        SpringSecurityHelper.secureChannel();

        log.info("Creating test data for this functional test...");
        category = new Category();
        category.setRlnm(CATEGORY_URL_NAME);
        category.setNm(CATEGORY_URL_NAME);
        category = categoryService.saveCategory(category);

        wrongCategory = new Category();
        wrongCategory.setRlnm(INCORRECT_CATEGORY_URL_NAME);
        wrongCategory.setNm(INCORRECT_CATEGORY_URL_NAME);
        wrongCategory = categoryService.saveCategory(wrongCategory);

        item1 = new Item();
        item1.setRlnm(ITEM1_URL_NAME);
        item1.setCtgry(category);
        item1.setNm(ITEM1_URL_NAME);
        item1 = itemService.saveItem(item1);

        item2 = new Item();
        item2.setRlnm(ITEM2_URL_NAME);
        item2.setNm(ITEM2_URL_NAME);
        item2.setCtgry(wrongCategory);
        item2 = itemService.saveItem(item2);
        log.info("Created test data for this functional test successfully");
    }

    @After
    public void tearDown() {

        log.info("Deleting test data from test...");

        if (blog != null && blog.getId() != null) {
            blogService.deleteBlog(blog);
        }

        if (category != null) {
            categoryService.removeCategory(category.getRlnm());
        }

        if (wrongCategory != null) {
            categoryService.removeCategory(wrongCategory.getRlnm());
        }

        if (item1 != null) {
            itemService.removeItem(item1.getRlnm());
        }

        if (item2 != null) {
            itemService.removeItem(item2.getRlnm());
        }

        log.info("Deleted test data from test successfully");
        SpringSecurityHelper.unsecureChannel();
    }

    @Test
    public void testBlogController() {
        log.info("Testing admin blog controller...");
        Model model = new BindingAwareModelMap();

        try {
            log.info("First we retrieve all blogs. There should be none but we won't fail the test if there is.");
            String view = blogManagerController.showBlogs(SortBlogBy.D.name(), model);
            assertEquals("Tile view is incorrect", "administration.blogs", view);
           
            log.info("Now we get ready to start creating a new blog by going to the blog form page");
            model = new BindingAwareModelMap();
            view = blogManagerController.showBlog(null, model);
            assertEquals("Tile view is incorrect", "administration.blog", view);
            assertNotNull("No blog object found", model.asMap().get(WebConstants.BLOG_ENTRY));
            BlogEntry blogEntry = (BlogEntry) model.asMap().get(WebConstants.BLOG_ENTRY);
            assertNull("Blog ID is not null", blogEntry.getId());
            
            log.info("Ok, the blog object was created successfully. Let's populate the object.");
            blogEntry.setNm(TESTBLOG);
            blogEntry.setRlnm(TESTBLOG);
            blogEntry.setStts(PublishStatus.UNPUBLISHED);

            // retrieve a category we can populate the blog with
            blogEntry.setCtgry(category);

            model = new BindingAwareModelMap();
            BindingResult errors = new BindException(blogEntry, "blog");
            RedirectAttributes ra = new RedirectAttributesModelMap();

            // let's also add a multipart image to the mix
            MultipartFile file = new MockMultipartFile(blogImage.getFilename(), blogImage.getFilename(), "image/png", blogImage.getInputStream());
            blogEntry.setMultipartFile(file);

            log.info("Let's save the most basic blog first");
            view = blogManagerController.submitBlog(blogEntry, errors, ra, model, Locale.US);
            assertEquals("Tile view is incorrect", "redirect:/administration/blog/list", view);
            assertNotNull("Redirect flash attributes are empty", ra.getFlashAttributes());
            assertEquals("Redirect flash attributes are empty", 1, ra.getFlashAttributes().size());
            assertNotNull("Redirect flash attribute is empty", ra.getFlashAttributes().get(WebConstants.MESSAGE));
            
            log.info("Now we load up this blog again");
            model = new BindingAwareModelMap();
            view = blogManagerController.showBlog(blogEntry.getRlnm(), model);
            assertEquals("Tile view is incorrect", "administration.blog", view);
            assertNotNull("No blog object found", model.asMap().get(WebConstants.BLOG_ENTRY));
            blogEntry = (BlogEntry) model.asMap().get(WebConstants.BLOG_ENTRY);
            assertNotNull("Blog ID is null", blogEntry.getId());
            assertNotNull("Blog Image is null", blogEntry.getMgrl());

            log.info("Let's trigger some validation errors");
            
            log.info("Let's first introduce some items on this blog that does not match the category selected");
            List<BlogItem> items = new ArrayList<BlogItem>(1);
            items.add(new BlogItem(item2.getRlnm()));
            blogEntry.setTms(items);

            model = new BindingAwareModelMap();
            errors = new BindException(blogEntry, "blog");
            ra = new RedirectAttributesModelMap();

            log.info("When we save now we should get an error");
            view = blogManagerController.submitBlog(blogEntry, errors, ra, model, Locale.US);
            assertEquals("Tile view is incorrect", "administration.blog", view);
            assertEquals("Redirect flash attributes are not empty", 0, ra.getFlashAttributes().size(), 0);
            assertTrue("There should be form errors", errors.hasErrors());
            assertEquals("Error count is incorrect", 1, errors.getErrorCount(), 0);
            
            log.info("Let's fix that error by adding a valid item");
            items = new ArrayList<BlogItem>(1);
            items.add(new BlogItem(item1.getRlnm()));
            blogEntry.setTms(items);

            model = new BindingAwareModelMap();
            errors = new BindException(blogEntry, "blog");
            ra = new RedirectAttributesModelMap();

            log.info("When we save now we should not get an error");
            view = blogManagerController.submitBlog(blogEntry, errors, ra, model, Locale.US);
            assertEquals("Tile view is incorrect", "redirect:/administration/blog/list", view);
            assertNotNull("Redirect flash attributes are empty", ra.getFlashAttributes());
            assertEquals("Redirect flash attributes are empty", 1, ra.getFlashAttributes().size());
            assertNotNull("Redirect flash attribute is empty", ra.getFlashAttributes().get(WebConstants.MESSAGE));
            assertFalse("There should be no form errors", errors.hasErrors());
            assertEquals("Error count is incorrect", 0, errors.getErrorCount(), 0);

            log.info("Time to delete this blog and verify that it's been deleted");
            ra = new RedirectAttributesModelMap();
            view = blogManagerController.deleteBlog(blogEntry.getRlnm(),ra, Locale.US);
            assertEquals("Tile view is incorrect", "redirect:/administration/blog/list", view);
            assertNotNull("Redirect flash attributes are empty", ra.getFlashAttributes());
            assertNotNull("Redirect flash attribute is empty", ra.getFlashAttributes().get(WebConstants.MESSAGE));
            
            blog = blogService.findPublishedBlogByUrlName(blogEntry.getRlnm());
            assertNull("Blog didn't get deleted", blog);
        } catch (Exception e) {
            log.info(e.getMessage(), e);
            fail("Method threw unexpected exception: " + e.getMessage());
        }
        log.info("Testing admin blog controller complete");
    }

}
