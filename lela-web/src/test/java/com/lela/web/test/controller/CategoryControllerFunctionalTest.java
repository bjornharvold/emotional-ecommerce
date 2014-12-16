/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.web.test.controller;

import com.google.common.collect.Lists;
import com.lela.commons.event.*;
import com.lela.domain.ApplicationConstants;
import com.lela.commons.service.CategoryService;
import com.lela.commons.service.FunctionalFilterService;
import com.lela.commons.service.ItemService;
import com.lela.commons.service.OwnerService;
import com.lela.commons.service.SeoUrlNameService;
import com.lela.commons.spring.mobile.MockDevice;
import com.lela.commons.spring.mobile.WurflDevice;
import com.lela.domain.document.Attribute;
import com.lela.domain.document.Category;
import com.lela.domain.document.FunctionalFilterOption;
import com.lela.domain.document.Item;
import com.lela.domain.document.Motivator;
import com.lela.domain.document.Owner;
import com.lela.domain.document.PostalCode;
import com.lela.domain.document.RelevantItem;
import com.lela.domain.document.FunctionalFilter;
import com.lela.domain.document.SeoUrlName;
import com.lela.domain.document.User;
import com.lela.domain.dto.FilterEventTrack;
import com.lela.domain.dto.ItemPage;
import com.lela.domain.dto.CategoryItemsQuery;
import com.lela.domain.dto.SimpleCategoryItemsQuery;
import com.lela.domain.enums.FunctionalFilterDomainType;
import com.lela.domain.enums.FunctionalFilterType;
import com.lela.domain.enums.FunctionalSortType;
import com.lela.commons.spring.security.SpringSecurityHelper;
import com.lela.domain.enums.MotivatorSource;
import com.lela.domain.enums.SeoUrlNameType;
import com.lela.web.test.AbstractFunctionalTest;
import com.lela.commons.web.utils.WebConstants;
import com.lela.web.test.controller.event.MockEventHandler;
import com.lela.web.web.controller.CategoryController;
import org.apache.commons.lang.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.mobile.device.Device;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.ui.Model;
import org.springframework.validation.support.BindingAwareModelMap;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.DispatcherServletWebRequest;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

/**
 * Created by Bjorn Harvold
 * Date: 8/3/11
 * Time: 10:05 AM
 * Responsibility:
 */
@SuppressWarnings("unchecked")
public class CategoryControllerFunctionalTest extends AbstractFunctionalTest {
    private static final Logger log = LoggerFactory.getLogger(CategoryControllerFunctionalTest.class);
    private static final String CATEGORY_WITHOUT_SEO_NAME = "CategoryWithoutSEOUrlNameTest";
    private static final String CATEGORY_WITH_SEO_NAME = "CategoryWithSEOUrlNameTest";
    private static final String CATEGORY_WITHOUT_SEO_URL_NAME = "categorywithoutseourlnametest";
    private static final String CATEGORY_WITH_SEO_URL_NAME = "categorywithseourlname";
    private static final String CATEGORY_SEO_URL_NAME = "Category-Controller-Test2";
    private static final String SEO_URL_NAME = "SEO-" + CategoryControllerFunctionalTest.class.getSimpleName();

    @Autowired
    private CategoryController categoryController;

    @Autowired
    private OwnerService ownerService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private FunctionalFilterService functionalFilterService;

    @Autowired
    private SeoUrlNameService seoUrlNameService;

    @Autowired
    private MongoTemplate mongo;

    private Category categoryWithoutSrlnm = null;
    private Category categoryWithSrlnm = null;
    private Item item1 = null;
    private Item item2 = null;
    private FunctionalFilter priceRangeFilter = null;
    private FunctionalFilter brandFilter = null;
    private FunctionalFilter bestForFilter = null;
    private FunctionalFilter typeFilter = null;
    private Owner jeep = null;
    private Owner lotus = null;
    private SeoUrlName seoUrlName;

    private MockDevice mockDevice = new MockDevice(MockDevice.DEVICE_TYPE.NORMAL);
    private Device device = new WurflDevice(mockDevice);

    private MockEventHandler eventHandler = new MockEventHandler();

    @Before
    public void setUp() {
        eventHandler.clear();
        new EventHelper(eventHandler);

        SpringSecurityHelper.secureChannel();

        log.info("Set up test data for functional test...");
        jeep = new Owner();
        jeep.setNm("Jeep");
        jeep.setRlnm("jeep");

        jeep = ownerService.saveOwner(jeep);

        lotus = new Owner();
        lotus.setNm("Lotus");
        lotus.setRlnm("lotus");

        lotus = ownerService.saveOwner(lotus);

        categoryWithoutSrlnm = new Category();
        categoryWithoutSrlnm.setNm(CATEGORY_WITHOUT_SEO_NAME);
        categoryWithoutSrlnm.setRlnm(CATEGORY_WITHOUT_SEO_URL_NAME);

        categoryWithoutSrlnm = categoryService.saveCategory(categoryWithoutSrlnm);
        assertNotNull("Category 1 is null", categoryWithoutSrlnm);
        assertNotNull("Category 1 ID is null", categoryWithoutSrlnm.getId());

        categoryWithSrlnm = new Category();
        categoryWithSrlnm.setNm(CATEGORY_WITH_SEO_NAME);
        categoryWithSrlnm.setRlnm(CATEGORY_WITH_SEO_URL_NAME);
        categoryWithSrlnm.setSrlnm(CATEGORY_SEO_URL_NAME);

        categoryWithSrlnm = categoryService.saveCategory(categoryWithSrlnm);
        assertNotNull("Category 2 is null", categoryWithSrlnm);
        assertNotNull("Category 2 ID is null", categoryWithSrlnm.getId());

        priceRangeFilter = new FunctionalFilter();
        priceRangeFilter.setRlnm(CATEGORY_WITHOUT_SEO_URL_NAME);
        priceRangeFilter.setKy("price.range");
        priceRangeFilter.setDtky("LowestPrice");
        priceRangeFilter.setTp(FunctionalFilterType.DYNAMIC_RANGE);
        priceRangeFilter.setDtp(FunctionalFilterDomainType.CATEGORY);
        priceRangeFilter.setRdr(1);

        List<FunctionalFilterOption> options = new ArrayList<FunctionalFilterOption>(2);
        FunctionalFilterOption option1 = new FunctionalFilterOption(ApplicationConstants.DYNAMIC_RANGE_LOW, 10, 1, false);
        FunctionalFilterOption option2 = new FunctionalFilterOption(ApplicationConstants.DYNAMIC_RANGE_HIGH, 1000, 3, false);
        options.add(option1);
        options.add(option2);

        priceRangeFilter.setPtns(options);

        priceRangeFilter = functionalFilterService.saveFunctionalFilter(priceRangeFilter);

        brandFilter = new FunctionalFilter();
        brandFilter.setRlnm(CATEGORY_WITHOUT_SEO_URL_NAME);
        brandFilter.setKy("by.brand");
        brandFilter.setTp(FunctionalFilterType.BRAND);
        brandFilter.setDtp(FunctionalFilterDomainType.CATEGORY);
        brandFilter.setRdr(2);

        options = new ArrayList<FunctionalFilterOption>(2);
        option1 = new FunctionalFilterOption(jeep.getRlnm(), jeep.getNm(), null, false);
        option2 = new FunctionalFilterOption(lotus.getRlnm(), lotus.getNm(), null, false);
        options.add(option1);
        options.add(option2);
        brandFilter.setPtns(options);

        brandFilter = functionalFilterService.saveFunctionalFilter(brandFilter);

        bestForFilter = new FunctionalFilter();
        bestForFilter.setRlnm(CATEGORY_WITHOUT_SEO_URL_NAME);
        bestForFilter.setKy("best.for");
        bestForFilter.setTp(FunctionalFilterType.MULTIPLE_CHOICE_MULTIPLE_ANSWER_AND);
        bestForFilter.setDtp(FunctionalFilterDomainType.CATEGORY);
        bestForFilter.setRdr(4);

        options = new ArrayList<FunctionalFilterOption>(2);
        option1 = new FunctionalFilterOption("BestForDriving", "true", 1, false);
        option2 = new FunctionalFilterOption("BestForSleeping", "true", 3, false);
        options.add(option1);
        options.add(option2);

        bestForFilter.setPtns(options);

        bestForFilter = functionalFilterService.saveFunctionalFilter(bestForFilter);

        typeFilter = new FunctionalFilter();
        typeFilter.setRlnm(CATEGORY_WITHOUT_SEO_URL_NAME);
        typeFilter.setKy("stroller.type");
        typeFilter.setTp(FunctionalFilterType.MULTIPLE_CHOICE_MULTIPLE_ANSWER_OR);
        typeFilter.setDtp(FunctionalFilterDomainType.CATEGORY);
        typeFilter.setRdr(5);

        options = new ArrayList<FunctionalFilterOption>(2);
        option1 = new FunctionalFilterOption("LowRider", "true", 1, false);
        option2 = new FunctionalFilterOption("Convertible", "true", 3, false);
        options.add(option1);
        options.add(option2);

        typeFilter.setPtns(options);

        typeFilter = functionalFilterService.saveFunctionalFilter(typeFilter);

        item1 = new Item();
        item1.setCtgry(categoryWithoutSrlnm);
        item1.setWnr(jeep);
        item1.setNm("categorycontrollertestitem1");
        item1.setRlnm("categorycontrollertestitem1");
        List<Attribute> attrs = new ArrayList<Attribute>(1);
        Attribute attr1 = new Attribute("LowestPrice", 1.99d);
        Attribute attr2 = new Attribute("BestForDriving", "true");
        Attribute attr3 = new Attribute("LowRider", "true");
        attrs.add(attr1);
        attrs.add(attr2);
        attrs.add(attr3);
        item1.setAttrs(attrs);

        item1 = itemService.saveItem(item1);
        assertNotNull("Item 1 is null", item1);
        assertNotNull("Item 1 ID is null", item1.getId());

        item2 = new Item();
        item2.setCtgry(categoryWithSrlnm);
        item2.setWnr(lotus);
        item2.setNm("categorycontrollertestitem2");
        item2.setRlnm("categorycontrollertestitem2");
        attrs = new ArrayList<Attribute>(1);
        attr1 = new Attribute("LowestPrice", 10.99d);
        attr2 = new Attribute("BestForSleeping", "true");
        attr3 = new Attribute("Convertible", "true");
        attrs.add(attr1);
        attrs.add(attr2);
        attrs.add(attr3);
        item2.setAttrs(attrs);

        item2 = itemService.saveItem(item2);
        assertNotNull("Item 2 is null", item2);
        assertNotNull("Item 2 ID is null", item2.getId());

        log.info("All test data has been created for this functional test");


        SpringSecurityHelper.unsecureChannel();
    }

    @After
    public void tearDown() {
        SpringSecurityHelper.secureChannel();

        if (categoryWithoutSrlnm != null) {
            mongo.remove(query(where("rlnm").is(categoryWithoutSrlnm.getRlnm())), Category.class);
        }

        if (categoryWithSrlnm != null) {
            mongo.remove(query(where("rlnm").is(categoryWithSrlnm.getRlnm())), Category.class);
        }

        if (item1 != null) {
            mongo.remove(query(where("rlnm").is(item1.getRlnm())), Item.class);
        }

        if (item2 != null) {
            mongo.remove(query(where("rlnm").is(item2.getRlnm())), Item.class);
        }

        if (priceRangeFilter != null) {
            mongo.remove(query(where("id").is(priceRangeFilter.getId())), FunctionalFilter.class);
        }

        if (brandFilter != null) {
            mongo.remove(query(where("id").is(brandFilter.getId())), FunctionalFilter.class);
        }

        if (bestForFilter != null) {
            mongo.remove(query(where("id").is(bestForFilter.getId())), FunctionalFilter.class);
        }

        if (typeFilter != null) {
            mongo.remove(query(where("id").is(typeFilter.getId())), FunctionalFilter.class);
        }

        if (jeep != null) {
            mongo.remove(query(where("rlnm").is(jeep.getRlnm())), Owner.class);
        }

        if (lotus != null) {
            mongo.remove(query(where("rlnm").is(lotus.getRlnm())), Owner.class);
        }

        if (seoUrlName != null) {
            mongo.remove(query(where("srlnm").is(seoUrlName.getSrlnm())), SeoUrlName.class);
        }

        SpringSecurityHelper.unsecureChannel();
    }

    @Test
    public void testRetrieveCategory() {
        log.info("Testing category controller...");
        User user = new User();
        Model model = new BindingAwareModelMap();
        HttpSession session = new MockHttpSession();
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        WebRequest webRequest = new DispatcherServletWebRequest(request, response);

        try {
            log.info("Testing categories without any user session...");
            request.addHeader("X-Requested-With", "XMLHttpRequest");
            ModelAndView view = categoryController.showCategory(CATEGORY_WITHOUT_SEO_URL_NAME, null, null, session, model, webRequest, request, device);
            assertNotNull("view is null", view);
            assertEquals("view name is incorrect", "category.page", view.getViewName());
            assertNotNull("page is not null", model.asMap().get(WebConstants.PAGE));

            model = new BindingAwareModelMap();
            view = categoryController.showCategory(CATEGORY_WITHOUT_SEO_URL_NAME, 0, 2, session, model, webRequest, request, device);
            assertNotNull("view is null", view);
            assertEquals("view name is incorrect", "category.data", view.getViewName());
            assertNotNull("page is null", model.asMap().get(WebConstants.PAGE));

            model = new BindingAwareModelMap();
            CategoryItemsQuery query = new CategoryItemsQuery();
            query.setPage(0);
            query.setSize(12);
            String ajaxView = categoryController.showCategoryAjax(CATEGORY_WITHOUT_SEO_URL_NAME, query, session, model, webRequest, request, device);
            assertNotNull("view is null", ajaxView);
            assertEquals("view name is incorrect", "category.data", ajaxView);
            assertNotNull("page is null", model.asMap().get(WebConstants.PAGE));

            log.info("Testing categories without any user session complete");

            log.info("Testing categories without a user session and motivators...");

            Map<String, Integer> userMotivators = new HashMap<String, Integer>();
            userMotivators.put("A", 8);
            userMotivators.put("B", 9);
            userMotivators.put("C", 3);
            userMotivators.put("D", 8);
            userMotivators.put("E", 9);
            userMotivators.put("F", 8);
            userMotivators.put("G", 8);

            Motivator motivator = userService.saveMotivator(user.getCd(), new Motivator(MotivatorSource.QUIZ, userMotivators));
            session.setAttribute(WebConstants.USER, user);

            model = new BindingAwareModelMap();
            view = categoryController.showCategory(CATEGORY_WITHOUT_SEO_URL_NAME, null, null, session, model, webRequest, request, device);
            assertNotNull("view is null", view);
            assertEquals("view name is incorrect", "category.page", view.getViewName());
            assertNotNull("page is not null", model.asMap().get(WebConstants.PAGE));

            model = new BindingAwareModelMap();
            view = categoryController.showCategory(CATEGORY_WITHOUT_SEO_URL_NAME, 0, 2, session, model, webRequest,request, device);
            assertNotNull("view is null", view);
            assertEquals("view name is incorrect", "category.data", view.getViewName());
            assertNotNull("page is null", model.asMap().get(WebConstants.PAGE));

            ItemPage<RelevantItem> page = (ItemPage<RelevantItem>) model.asMap().get(WebConstants.PAGE);
            assertNotNull("tuners are null", page.getFilters());
            assertTrue("tuner size incorrect", page.getFilters().size() > 0);
            FunctionalFilter preset = page.getFilters().get(0);
            assertEquals("tuner question is incorrect", FunctionalFilterType.DYNAMIC_RANGE, preset.getTp());
            assertEquals("Incorrect number of filter answers", 4, preset.getPtns().size(), 0);

            Map<String, Map<String, String>> filter = new HashMap<String, Map<String, String>>();
            Map<String, String> map = new HashMap<String, String>();
            map.put(preset.getFunctionalFilterOption(ApplicationConstants.DYNAMIC_RANGE_LOW).getKy(), preset.getFunctionalFilterOption(ApplicationConstants.DYNAMIC_RANGE_LOW).getVl().toString());
            map.put(preset.getFunctionalFilterOption(ApplicationConstants.DYNAMIC_RANGE_HIGH).getKy(), preset.getFunctionalFilterOption(ApplicationConstants.DYNAMIC_RANGE_HIGH).getVl().toString());
            filter.put("price.range", map);

            query = new CategoryItemsQuery();
            query.setCategoryUrlName(CATEGORY_WITHOUT_SEO_URL_NAME);
            query.setPage(0);
            query.setSize(12);
            query.setFilters(filter);
            ajaxView = categoryController.showCategoryAjax(CATEGORY_WITHOUT_SEO_URL_NAME, query, session, model, webRequest, request, device);
            assertNotNull("view is null", ajaxView);
            assertEquals("view name is incorrect", "category.data", ajaxView);
            assertNotNull("page is not null", model.asMap().get(WebConstants.PAGE));

            assertEquals("There should be 6 events fired", 6, eventHandler.getEvents().size());
            assertNotNull("There should be a UserVisitEvent", eventHandler.find(UserVisitEvent.class));
            assertNotNull("There should be a ViewedCategoryEvent", eventHandler.find(ViewedCategoryEvent.class));
            assertNotNull("There should be a MotivatorEvent", eventHandler.find(MotivatorEvent.class));

            log.info("Testing categories without a user session and motivators complete");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            fail("Method threw unexpected exception: " + e.getMessage());
        } finally {
            SpringSecurityHelper.secureChannel();
            userService.removeUser(user);
            SpringSecurityHelper.unsecureChannel();
        }

        log.info("Testing category controller complete");
    }

    @Test
    public void testPriceRangeFilter() {
        log.info("Testing pricing filter tuner category controller...");
        User user = new User();
        Model model = null;
        Long unfilteredSize = null;
        Long filteredSize = null;
        HttpSession session = new MockHttpSession();
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        WebRequest webRequest = new DispatcherServletWebRequest(request, response);

        try {
            log.info("Testing categories without a user session and motivators...");
            request.addHeader("X-Requested-With", "XMLHttpRequest");
            Map<String, Integer> userMotivators = new HashMap<String, Integer>();
            userMotivators.put("A", 8);
            userMotivators.put("B", 9);
            userMotivators.put("C", 3);
            userMotivators.put("D", 8);
            userMotivators.put("E", 9);
            userMotivators.put("F", 8);
            userMotivators.put("G", 8);

            Motivator motivator = userService.saveMotivator(user.getCd(), new Motivator(MotivatorSource.QUIZ, userMotivators));

            session.setAttribute(WebConstants.USER, user);

            model = new BindingAwareModelMap();
            ModelAndView view = categoryController.showCategory(CATEGORY_WITHOUT_SEO_URL_NAME, 0, 12, session, model, webRequest,request,  device);
            assertNotNull("view is null", view);
            assertEquals("view name is incorrect", "category.data", view.getViewName());
            assertNotNull("page is null", model.asMap().get(WebConstants.PAGE));
            unfilteredSize = ((ItemPage<RelevantItem>) model.asMap().get(WebConstants.PAGE)).getTotalElements();
            ItemPage<RelevantItem> page = (ItemPage<RelevantItem>) model.asMap().get(WebConstants.PAGE);
            assertNotNull("tuners are null", page.getFilters());
            assertTrue("tuner size incorrect", page.getFilters().size() > 0);
            FunctionalFilter preset = page.getFilters().get(0);
            assertEquals("tuner question is incorrect", FunctionalFilterType.DYNAMIC_RANGE, preset.getTp());

            log.info("Ok, we have the tuner data and now we want to call the same url again but this time with the price range filter");
            Map<String, Map<String, String>> filter = new HashMap<String, Map<String, String>>();
            Map<String, String> map = new HashMap<String, String>();
            assertEquals("Incorrect number of filter answers", 4, preset.getPtns().size(), 0);
            Integer low = (Integer) preset.getPtns().get(0).getVl();
            Integer high = (Integer) preset.getPtns().get(1).getVl();
            low = low + 100;
            high = high - 100;

            log.info("High is: " + high + ", and low is: " + low);

            log.info("Now we subtract 20 from both sides and we should get less results");
            map.put(ApplicationConstants.DYNAMIC_RANGE_LOW, low.toString());
            map.put(ApplicationConstants.DYNAMIC_RANGE_HIGH, high.toString());
            filter.put("price.range", map);

            model = new BindingAwareModelMap();
            CategoryItemsQuery query = new CategoryItemsQuery();
            query.setCategoryUrlName(CATEGORY_WITHOUT_SEO_URL_NAME);
            query.setPage(0);
            query.setSize(12);
            query.setFilters(filter);
            query.setUpdate(true);

            String ajaxView = categoryController.showCategoryAjax(CATEGORY_WITHOUT_SEO_URL_NAME, query, session, model, webRequest, request, device);
            assertNotNull("view is null", ajaxView);
            assertEquals("view name is incorrect", "category.data", ajaxView);
            assertNotNull("page is null", model.asMap().get(WebConstants.PAGE));
            filteredSize = ((ItemPage<RelevantItem>) model.asMap().get(WebConstants.PAGE)).getTotalElements();

            log.info("Comparing default filter size: " + unfilteredSize + " with filtered size: " + filteredSize);
            assertTrue("filtered size is incorrect", unfilteredSize > filteredSize);

            assertEquals("There should be 2 events fired", 2, eventHandler.getEvents().size());
            assertNotNull("There should be a MotivatorEvent", eventHandler.find(MotivatorEvent.class));
            assertNotNull("There should be a ViewedCategoryEvent", eventHandler.find(ViewedCategoryEvent.class));
            log.info("Testing categories without a user session and motivators complete");


        } catch (Exception e) {
            log.error(e.getMessage(), e);
            fail("Method threw unexpected exception: " + e.getMessage());
        } finally {
            SpringSecurityHelper.secureChannel();
            userService.removeUser(user);
            SpringSecurityHelper.unsecureChannel();
        }

        log.info("Testing pricing filter tuner category controller complete");
    }

    @Test
    public void testMultipleChoiceMultipleAnswersFilter() {
        log.info("Testing multiple choice - multiple answers filter tuner category controller...");
        User user = new User();
        Model model = null;
        Long unfilteredSize = null;
        Long filteredSize = null;
        HttpSession session = new MockHttpSession();
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        WebRequest webRequest = new DispatcherServletWebRequest(request, response);

        try {

            log.info("Testing categories without a user session and motivators...");
            request.addHeader("X-Requested-With", "XMLHttpRequest");

            Map<String, Integer> userMotivators = new HashMap<String, Integer>();
            userMotivators.put("A", 8);
            userMotivators.put("B", 9);
            userMotivators.put("C", 3);
            userMotivators.put("D", 8);
            userMotivators.put("E", 9);
            userMotivators.put("F", 8);
            userMotivators.put("G", 8);
            Motivator motivator = userService.saveMotivator(user.getCd(), new Motivator(MotivatorSource.QUIZ, userMotivators));

            session.setAttribute(WebConstants.USER, user);

            model = new BindingAwareModelMap();
            ModelAndView view = categoryController.showCategory(CATEGORY_WITHOUT_SEO_URL_NAME, 0, 12, session, model, webRequest, request, device);
            assertNotNull("view is null", view);
            assertEquals("view name is incorrect", "category.data", view.getViewName());
            assertNotNull("page is null", model.asMap().get(WebConstants.PAGE));
            unfilteredSize = ((ItemPage<RelevantItem>) model.asMap().get(WebConstants.PAGE)).getTotalElements();
            ItemPage<RelevantItem> page = (ItemPage<RelevantItem>) model.asMap().get(WebConstants.PAGE);
            assertNotNull("tuners are null", page.getFilters());
            assertTrue("tuner size incorrect", page.getFilters().size() > 0);
            FunctionalFilter strollerTypeQuestion = null;
            FunctionalFilter bestForQuestion = null;

            for (FunctionalFilter preset : page.getFilters()) {
                if (StringUtils.equals(preset.getKy(), "best.for")) {
                    bestForQuestion = preset;
                }
                if (StringUtils.equals(preset.getKy(), "stroller.type")) {
                    strollerTypeQuestion = preset;
                }
            }

            assertNotNull("Stroller type functional filter question is null", strollerTypeQuestion);
            assertNotNull("Best for functional filter question is null", bestForQuestion);
            assertEquals("tuner question type is incorrect", FunctionalFilterType.MULTIPLE_CHOICE_MULTIPLE_ANSWER_OR, strollerTypeQuestion.getTp());
            assertEquals("tuner question type is incorrect", FunctionalFilterType.MULTIPLE_CHOICE_MULTIPLE_ANSWER_AND, bestForQuestion.getTp());

            log.info("Ok, we have the functional filter data and now we want to call the same url again but this time with the price range filter");
            Map<String, Map<String, String>> filter = new HashMap<String, Map<String, String>>();
            Map<String, String> map1 = new HashMap<String, String>();
            assertTrue("Incorrect number of filter answers", strollerTypeQuestion.getPtns().size() > 0);
            assertTrue("Incorrect number of filter answers", bestForQuestion.getPtns().size() > 0);

            map1.put(strollerTypeQuestion.getPtns().get(0).getKy(), (String) strollerTypeQuestion.getPtns().get(0).getVl());
            filter.put(strollerTypeQuestion.getKy(), map1);

            Map<String, String> map2 = new HashMap<String, String>();
            map2.put(bestForQuestion.getPtns().get(0).getKy(), (String) bestForQuestion.getPtns().get(0).getVl());
            filter.put(bestForQuestion.getKy(), map2);

            model = new BindingAwareModelMap();
            CategoryItemsQuery query = new CategoryItemsQuery();
            query.setCategoryUrlName(CATEGORY_WITHOUT_SEO_URL_NAME);
            query.setPage(0);
            query.setSize(12);
            query.setFilters(filter);

            String ajaxView = categoryController.showCategoryAjax(CATEGORY_WITHOUT_SEO_URL_NAME, query, session, model, webRequest, request, device);
            assertNotNull("view is null", view);
            assertEquals("view name is incorrect", "category.data", ajaxView);
            assertNotNull("page is null", model.asMap().get(WebConstants.PAGE));
            filteredSize = ((ItemPage<RelevantItem>) model.asMap().get(WebConstants.PAGE)).getTotalElements();

            assertEquals("There should be 2 events fired", 2, eventHandler.getEvents().size());
            assertNotNull("There should be a MotivatorEvent", eventHandler.find(MotivatorEvent.class));
            assertNotNull("There should be a ViewedCategoryEvent", eventHandler.find(ViewedCategoryEvent.class));


            log.info("Comparing default filter size: " + unfilteredSize + " with filtered size: " + filteredSize);

            log.info("Testing categories without a user session and motivators complete");


        } catch (Exception e) {
            log.error(e.getMessage(), e);
            fail("Method threw unexpected exception: " + e.getMessage());
        } finally {
            SpringSecurityHelper.secureChannel();
            userService.removeUser(user);
            SpringSecurityHelper.unsecureChannel();
        }

        log.info("Testing multiple choice - multiple answers filter tuner category controller complete");
    }

    @Test
    public void testSorting() {
        log.info("Testing sorting on category controller...");
        User user = new User();
        String view = null;
        Model model = null;
        HttpSession session = new MockHttpSession();
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        WebRequest webRequest = new DispatcherServletWebRequest(request, response);

        try {

            log.info("Testing categories without a user session and motivators...");
            request.addHeader("X-Requested-With", "XMLHttpRequest");
            model = new BindingAwareModelMap();
            CategoryItemsQuery query = new CategoryItemsQuery();
            query.setCategoryUrlName(CATEGORY_WITHOUT_SEO_URL_NAME);
            query.setPage(0);
            query.setSize(12);
            query.setSort(true);
            FilterEventTrack filterEventTrack = new FilterEventTrack();
            filterEventTrack.setFilter("sort");
            query.setEventTracking(Lists.newArrayList(filterEventTrack));

            log.info("First we just make a call to see the default sort enum we get back");
            eventHandler.clear();
            view = categoryController.showCategoryAjax(CATEGORY_WITHOUT_SEO_URL_NAME, query, session, model, webRequest,request,  device);
            assertEquals("There should be 2 events fired", 2, eventHandler.getEvents().size());
            assertNotNull("There should be a UserVisitEvent", eventHandler.find(UserVisitEvent.class));
            assertNotNull("There should be a SortEvent", eventHandler.find(SortEvent.class));
            assertNotNull("view is null", view);
            assertEquals("view name is incorrect", "category.data", view);
            assertNotNull("page is null", model.asMap().get(WebConstants.PAGE));
            assertNotNull("sort options is null", model.asMap().get(WebConstants.SORT_OPTIONS));
            ItemPage<Item> page = (ItemPage<Item>) model.asMap().get(WebConstants.PAGE);
            assertEquals("Sort by is incorrect", FunctionalSortType.POPULARITY, page.getSortBy());

            log.info("Then we make a call where we specify that we want a sort enum that is not yet allowed");
            query.setSortBy(FunctionalSortType.RECOMMENDED);
            eventHandler.clear();
            view = categoryController.showCategoryAjax(CATEGORY_WITHOUT_SEO_URL_NAME, query, session, model, webRequest,request,  device);
            assertEquals("There should be 1 events fired", 1, eventHandler.getEvents().size());
            //assertNotNull("There should be a UserVisitEvent", eventHandler.find(UserVisitEvent.class));
            assertNotNull("There should be a SortEvent", eventHandler.find(SortEvent.class));
            assertNotNull("view is null", view);
            assertEquals("view name is incorrect", "category.data", view);
            assertNotNull("page is null", model.asMap().get(WebConstants.PAGE));
            assertNotNull("sort options is null", model.asMap().get(WebConstants.SORT_OPTIONS));
            page = (ItemPage<Item>) model.asMap().get(WebConstants.PAGE);
            assertEquals("Sort by is incorrect", FunctionalSortType.POPULARITY, page.getSortBy());

            log.info("Now we specify a sort enum that is allowed");
            query.setSortBy(FunctionalSortType.PRICE_HIGH_LOW);
            eventHandler.clear();
            view = categoryController.showCategoryAjax(CATEGORY_WITHOUT_SEO_URL_NAME, query, session, model, webRequest, request, device);
            assertEquals("There should be 1 events fired", 1, eventHandler.getEvents().size());
            //assertNotNull("There should be a UserVisitEvent", eventHandler.find(UserVisitEvent.class));
            assertNotNull("There should be a SortEvent", eventHandler.find(SortEvent.class));

            assertNotNull("view is null", view);
            assertEquals("view name is incorrect", "category.data", view);
            assertNotNull("page is null", model.asMap().get(WebConstants.PAGE));
            assertNotNull("sort options is null", model.asMap().get(WebConstants.SORT_OPTIONS));
            page = (ItemPage<Item>) model.asMap().get(WebConstants.PAGE);
            assertEquals("Sort by is incorrect", FunctionalSortType.PRICE_HIGH_LOW, page.getSortBy());

            log.info("Now we add a user to the session that has motivators");

            Map<String, Integer> userMotivators = new HashMap<String, Integer>();
            userMotivators.put("A", 8);
            userMotivators.put("B", 9);
            userMotivators.put("C", 3);
            userMotivators.put("D", 8);
            userMotivators.put("E", 9);
            userMotivators.put("F", 8);
            userMotivators.put("G", 8);


            Motivator motivator = userService.saveMotivator(user.getCd(), new Motivator(MotivatorSource.QUIZ, userMotivators));
            eventHandler.clear();

            session.setAttribute(WebConstants.USER, user);

            log.info("When we make a call to sort by the recommended enum we should get it");
            query.setSortBy(FunctionalSortType.RECOMMENDED);
            view = categoryController.showCategoryAjax(CATEGORY_WITHOUT_SEO_URL_NAME, query, session, model, webRequest,request, device);
            assertNotNull("view is null", view);
            assertEquals("view name is incorrect", "category.data", view);
            assertNotNull("page is null", model.asMap().get(WebConstants.PAGE));
            assertNotNull("sort options is null", model.asMap().get(WebConstants.SORT_OPTIONS));
            page = (ItemPage<Item>) model.asMap().get(WebConstants.PAGE);
            assertEquals("Sort by is incorrect", FunctionalSortType.RECOMMENDED, page.getSortBy());

            assertEquals("There should be 1 events fired", 1, eventHandler.getEvents().size());
            //assertNotNull("There should be a UserVisitEvent", eventHandler.find(UserVisitEvent.class));
            assertNotNull("There should be a SortEvent", eventHandler.find(SortEvent.class));

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            fail("Method threw unexpected exception: " + e.getMessage());
        } finally {
            SpringSecurityHelper.secureChannel();
            userService.removeUser(user);
            SpringSecurityHelper.unsecureChannel();
        }

        log.info("Testing sorting on category controller complete");
    }

    @Test
    public void testBrandFilter() {
        log.info("Testing brand filter on category controller...");

        String view = null;
        Model model = null;
        HttpSession session = new MockHttpSession();
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        WebRequest webRequest = new DispatcherServletWebRequest(request, response);

        try {

            log.info("Testing categories without a user session and motivators...");
            request.addHeader("X-Requested-With", "XMLHttpRequest");
            model = new BindingAwareModelMap();
            CategoryItemsQuery query = new CategoryItemsQuery();
            query.setCategoryUrlName(CATEGORY_WITHOUT_SEO_URL_NAME);
            query.setPage(0);
            query.setSize(12);
            query.setSort(true);
            FilterEventTrack filterEventTrack = new FilterEventTrack();
            filterEventTrack.setFilter("filter");
            query.setEventTracking(Lists.newArrayList(filterEventTrack));

            log.info("First we just make a call to see the default sort enum we get back");
            view = categoryController.showCategoryAjax(CATEGORY_WITHOUT_SEO_URL_NAME, query, session, model, webRequest, request, device);
            assertNotNull("view is null", view);
            assertEquals("view name is incorrect", "category.data", view);
            assertNotNull("page is null", model.asMap().get(WebConstants.PAGE));
            assertNotNull("sort options is null", model.asMap().get(WebConstants.SORT_OPTIONS));
            ItemPage<Item> page = (ItemPage<Item>) model.asMap().get(WebConstants.PAGE);
            assertNotNull("Functional filters are null", page.getFilters());
            List<FunctionalFilter> functionalFilters = page.getFilters();

            log.info("Retrieved functional filters successfully. Now we try to find the brand filter");
            FunctionalFilter brandFilter = null;

            for (FunctionalFilter filter : functionalFilters) {
                if (filter.getTp().equals(FunctionalFilterType.BRAND)) {
                    brandFilter = filter;
                    break;
                }
            }

            assertNotNull("Brand filter is null", brandFilter);

            log.info("Let's discover the brand filter a bit. We have " + brandFilter.getPtns().size() + " brand filter options");

            assertNotNull("Brands in brand filter is empty", brandFilter.getPtns());
            assertEquals("Brand filter option size is incorrect", 2, brandFilter.getPtns().size(), 0);

            Map<String, Map<String, String>> filters = new HashMap<String, Map<String, String>>(1);
            Map<String, String> filterProperties = new HashMap<String, String>(brandFilter.getPtns().size());

            for (int i = 0; i < 1; i++) {
                FunctionalFilterOption ffo = brandFilter.getPtns().get(i);
                log.info("Now let's filter on a specific owner: " + ffo.getKy());
                filterProperties.put(ffo.getKy(), (String) ffo.getVl());
            }

            filters.put(brandFilter.getKy(), filterProperties);
            query.setFilters(filters);
            query.setUpdate(true);

            eventHandler.clear();
            view = categoryController.showCategoryAjax(CATEGORY_WITHOUT_SEO_URL_NAME, query, session, model, webRequest,request, device);
            assertNotNull("view is null", view);
            assertEquals("view name is incorrect", "category.data", view);
            assertNotNull("page is null", model.asMap().get(WebConstants.PAGE));
            assertNotNull("sort options is null", model.asMap().get(WebConstants.SORT_OPTIONS));
            page = (ItemPage<Item>) model.asMap().get(WebConstants.PAGE);
            assertNotNull("Functional filters are null", page.getFilters());

            for (Item item : page) {
                log.info("The results should now only show items with the correct owner: " + item.getWnr().getRlnm());
                assertTrue("Owner is incorrect", filterProperties.containsKey(item.getWnr().getRlnm()));
            }

            for (FunctionalFilter filter : page.getFilters()) {
                if (filter.getTp().equals(FunctionalFilterType.BRAND)) {
                    brandFilter = filter;
                    break;
                }
            }

            assertNotNull("Brand filter is null", brandFilter);

            log.info("Let's discover the brand filter a bit. We have " + brandFilter.getPtns().size() + " brand filter options");

            log.info("Now our brand filter presets should have a size of 1 since we selected 1 brands on our last call.");
            int selectedCounter = 0;
            for (FunctionalFilterOption ffo : brandFilter.getPtns()) {
                if (ffo.getSlctd()) {
                    selectedCounter++;
                }
            }

            assertEquals("Brand filter preset selected count is incorrect", 1, selectedCounter, 0);
            assertEquals("There should be 1 events fired", 1, eventHandler.getEvents().size());
            assertNotNull("There should be a FunctionalFilterEvent", eventHandler.find(FunctionalFilterEvent.class));

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            fail("Method threw unexpected exception: " + e.getMessage());
        } finally {
            // we've been working with a transient user all this time
            // but in order to delete the persisted user supplement, we need to find the user code
            User user = (User) session.getAttribute(WebConstants.USER);
            SpringSecurityHelper.secureChannel();
            userService.removeUser(user);
            SpringSecurityHelper.unsecureChannel();
        }

        log.info("Testing brand filter on category controller complete");
    }

    @Test
    public void testSEOFriendlyPages() {
        log.info("Testing the SEO friendly url mappings...");

        Model model = new BindingAwareModelMap();
        HttpSession session = new MockHttpSession();
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        WebRequest webRequest = new DispatcherServletWebRequest(request, response);

        try {
            log.info("First we look at a category that has a srlnm property on it");
            ModelAndView view = categoryController.showCategory(CATEGORY_WITH_SEO_URL_NAME, null, null, session, model, webRequest, request, device);
            assertNotNull("view is null", view);
            assertEquals("view name is incorrect", "/Category-Controller-Test2/c?rlnm=categorywithseourlname", ((RedirectView) view.getView()).getUrl());
            assertNull("page is not null", model.asMap().get(WebConstants.PAGE));

            log.info("Then we go directly to the seo url mapping");
            String theView = categoryController.showCategorySEO(CATEGORY_SEO_URL_NAME, CATEGORY_WITH_SEO_URL_NAME, 0, 12, null, session, model, webRequest, request, device);
            assertNotNull("view is null", theView);
            assertEquals("view name is incorrect", "category.print", theView);
            assertNotNull("page is null", model.asMap().get(WebConstants.PAGE));

            log.info("Let's test that we can pass an arbitrary seo url name, as long as it is registered with our list of seo url names...");
            theView = categoryController.showCategorySEO(SEO_URL_NAME, CATEGORY_WITH_SEO_URL_NAME, 0, 12, null, session, model, webRequest, request, device);
            assertNotNull("view is null", theView);
            assertEquals("view name is incorrect", "resourceNotFound", theView);

            log.info("Save a new seo url name...");
            seoUrlName = new SeoUrlName();
            seoUrlName.setSrlnm(SEO_URL_NAME);
            seoUrlName.setNm(SEO_URL_NAME);
            seoUrlName.setDsc(SEO_URL_NAME);
            seoUrlName.setHdr(SEO_URL_NAME);
            seoUrlName.setNtr(SEO_URL_NAME);
            seoUrlName.setTp(SeoUrlNameType.CATEGORY);

            SpringSecurityHelper.secureChannel();
            seoUrlName = seoUrlNameService.saveSeoUrlName(seoUrlName);
            assertNotNull("Seo url name id is null", seoUrlName.getId());
            SpringSecurityHelper.unsecureChannel();

            log.info("Now we've added the seo url name and we can safely call the category method again without getting an error.");
            log.info("Let's test that we can pass an arbitrary seo url name, as long as it is registered with our list of seo url names...");
            log.info("First we make it throw an exception");
            theView = categoryController.showCategorySEO(SEO_URL_NAME, CATEGORY_WITH_SEO_URL_NAME, 0, 12, null, session, model, webRequest, request, device);
            assertNotNull("view is null", theView);
            assertEquals("view name is incorrect", "category.print", theView);
            assertNotNull("page is null", model.asMap().get(WebConstants.PAGE));

            log.info("Now we're calling the request mapping that supports pre-defined functional filters");
            SimpleCategoryItemsQuery query = new SimpleCategoryItemsQuery();
            theView = categoryController.showCategorySEOAd(CATEGORY_SEO_URL_NAME, CATEGORY_WITH_SEO_URL_NAME, query, session, model, webRequest,request,  device);
            assertNotNull("view is null", theView);
            assertEquals("view name is incorrect", "resourceNotFound", theView);

            assertEquals("There should be 2 events fired", 6, eventHandler.getEvents().size());
            assertNotNull("There should be a UserVisitEvent", eventHandler.find(UserVisitEvent.class));
            assertNotNull("There should be a ViewedCategoryEvent", eventHandler.find(ViewedCategoryEvent.class));

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            fail("Method threw unexpected exception: " + e.getMessage());
        }

        log.info("Testing the SEO friendly url mappings complete");
    }
}
