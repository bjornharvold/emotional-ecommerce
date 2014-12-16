/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.commons.test.service;

import com.lela.commons.service.CategoryService;
import com.lela.commons.service.FunctionalFilterService;
import com.lela.commons.service.ItemService;
import com.lela.commons.service.ProductEngineService;
import com.lela.commons.service.UserService;
import com.lela.commons.service.impl.DepartmentServiceImpl;
import com.lela.commons.utilities.UserSessionTrackingHelper;
import com.lela.domain.document.Category;
import com.lela.domain.document.FunctionalFilter;
import com.lela.domain.document.Item;
import com.lela.domain.document.Motivator;
import com.lela.domain.document.RelevantItem;
import com.lela.domain.document.User;
import com.lela.domain.document.UserSupplement;
import com.lela.domain.dto.CategoryItemsQuery;
import com.lela.domain.dto.ItemPage;
import com.lela.domain.dto.department.DepartmentLandingPage;
import com.lela.domain.dto.department.DepartmentLandingPageData;
import com.lela.domain.dto.department.DepartmentQuery;
import com.lela.domain.dto.department.DepartmentSearchResults;
import com.lela.domain.enums.MotivatorSource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.mock.web.MockHttpSession;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DepartmentServiceUnitTest {
    private final static Logger log = LoggerFactory.getLogger(DepartmentServiceUnitTest.class);
    private static final String URL_NAME = "someurl";
    private static final String SOMECATEGORYURLNAME = "somecategoryurlname";
    private static final String USER_CODE = "someusercode";

    @Mock
    private CategoryService categoryService;

    @Mock
    private FunctionalFilterService functionalFilterService;

    @Mock
    private ItemService itemService;

    @Mock
    private ProductEngineService productEngineService;

    @Mock
    private UserService userService;

    @Mock
    private UserSessionTrackingHelper userSessionTrackingHelper;

    @InjectMocks
    private DepartmentServiceImpl departmentService;

    @Test
    public void testFindFunctionalFilterCountByCategoryGroupUrlName() throws Exception {
        log.info("Testing findFunctionalFilterCountByCategoryGroupUrlName()...");

        // configure mocks
        when(functionalFilterService.findFunctionalFilterCountByUrlName(URL_NAME)).thenReturn(1L);

        // execute service method
        Long count = departmentService.findFunctionalFilterCountByCategoryGroupUrlName(URL_NAME);

        // verify
        verify(functionalFilterService, times(1)).findFunctionalFilterCountByUrlName(URL_NAME);
        assertEquals("Count is incorrect", 1L, count, 0);

        log.info("Testing findFunctionalFilterCountByCategoryGroupUrlName() complete");
    }

    @Test
    public void testFindDepartmentLandingPage() throws Exception {
        log.info("Testing findDepartmentLandingPage()...");

        // set up objects
        List<FunctionalFilter> filters = new ArrayList<FunctionalFilter>(1);
        FunctionalFilter ff = new FunctionalFilter();
        filters.add(ff);
        List<Category> categories = new ArrayList<Category>(1);
        Category category = new Category();
        categories.add(category);

        // configure mocks
        when(functionalFilterService.findFunctionalFiltersByUrlName(URL_NAME)).thenReturn(filters);

        // execute service method
        DepartmentLandingPage dlp = departmentService.findDepartmentLandingPage(URL_NAME, null);

        // verify
        verify(functionalFilterService, times(1)).findFunctionalFiltersByUrlName(URL_NAME);

        assertNotNull("Filters are incorrect", dlp.getFilters());

        log.info("Testing findDepartmentLandingPage() complete");
    }

    @Test
    public void testFindDepartmentLandingPageData() {
        log.info("Testing findDepartmentLandingPageData()...");

        List<FunctionalFilter> functionalFilters = new ArrayList<FunctionalFilter>(1);
        FunctionalFilter ff = new FunctionalFilter();
        functionalFilters.add(ff);

        // set up objects
        Map<String, Map<String, String>> filters = new HashMap<String, Map<String, String>>(1);
        List<String> categories = new ArrayList<String>(1);
        categories.add(SOMECATEGORYURLNAME);

        DepartmentQuery query = new DepartmentQuery();
        query.setCategoryUrlNames(categories);
        query.setAvailableCategoryUrlNames(categories);
        query.setFilters(filters);
        query.setRlnm(URL_NAME);

        // configure mocks
        when(functionalFilterService.findFunctionalFiltersByUrlName(URL_NAME)).thenReturn(functionalFilters);
        when(itemService.findItemCountByCategoryAndFilters(SOMECATEGORYURLNAME, functionalFilters, filters)).thenReturn(1L);

        // execute service method
        DepartmentLandingPageData dlpd = departmentService.findDepartmentLandingPageData(query);

        // verify
        assertEquals("Count incorrect", 1L, dlpd.getCount(), 0);
        assertEquals("Count incorrect", 1L, dlpd.getCategoryCount().get(SOMECATEGORYURLNAME), 0);
        assertNotNull("Query is null", dlpd.getQuery());
        verify(itemService, times(1)).findItemCountByCategoryAndFilters(SOMECATEGORYURLNAME, functionalFilters, filters);

        log.info("Testing findDepartmentLandingPageData() complete");
    }

    @Test
    public void testFindDepartmentSearchResults() {
        log.info("Testing findDepartmentSearchResults()...");

        // set up objects
        List<FunctionalFilter> functionalFilters = new ArrayList<FunctionalFilter>(1);
        FunctionalFilter ff = new FunctionalFilter();
        functionalFilters.add(ff);

        Map<String, Map<String, String>> filters = new HashMap<String, Map<String, String>>(1);
        List<String> categories = new ArrayList<String>(1);
        categories.add(SOMECATEGORYURLNAME);

        DepartmentQuery query = new DepartmentQuery();
        query.setCategoryUrlNames(categories);
        query.setAvailableCategoryUrlNames(categories);
        query.setFilters(filters);
        query.setRlnm(URL_NAME);

        List<Item> itemList = new ArrayList<Item>();
        Item item = new Item();
        item.setCtgry(new Category());
        itemList.add(item);

        List<RelevantItem> relevantItemList = new ArrayList<RelevantItem>();
        RelevantItem ri = new RelevantItem();
        ri.setCtgry(new Category());
        relevantItemList.add(ri);

        User user = new User(USER_CODE);
        UserSupplement us = new UserSupplement(USER_CODE);

        ItemPage<Item> items = new ItemPage<Item>(itemList, new PageRequest(0, 1), 1L);
        ItemPage<RelevantItem> relevantItems = new ItemPage<RelevantItem>(relevantItemList, new PageRequest(0, 1), 1L);

        // configure mocks
        when(userSessionTrackingHelper.retrieveUserFromPrincipalOrSession(any(MockHttpSession.class))).thenReturn(user);
        when(userService.findUserSupplement(USER_CODE)).thenReturn(us);
        when(itemService.findItemCountByCategoryAndFilters(SOMECATEGORYURLNAME, functionalFilters, filters)).thenReturn(1L);
        when(productEngineService.findItemsByCategory(any(CategoryItemsQuery.class))).thenReturn(items);
        when(productEngineService.findRelevantItemsByCategory(any(CategoryItemsQuery.class))).thenReturn(relevantItems);

        // execute service method
        log.info("First we retrieve the regular items list");
        DepartmentSearchResults dsr = departmentService.findDepartmentSearchResults(USER_CODE, query);

        // verify
        assertNotNull("Result is null", dsr);
        assertNotNull("Categories are null", dsr.getCategories());
        assertNotNull("ItemPage is null", dsr.getCategories().get(0).getItems());
        assertEquals("Count incorrect", 1L, dsr.getCount(), 0);

        // execute service method
        log.info("Then we retrieve the relevant items list");
        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("A", 4);
        map.put("B", 4);
        map.put("C", 4);
        map.put("D", 4);
        map.put("E", 4);
        map.put("F", 4);
        map.put("G", 4);
        Motivator motivator = new Motivator(MotivatorSource.FACEBOOK, map);
        us.addMotivator(motivator);
        dsr = departmentService.findDepartmentSearchResults(USER_CODE, query);

        // verify
        assertNotNull("Result is null", dsr);
        assertNotNull("Categories are null", dsr.getCategories());
        assertNotNull("RelevantItemPage is null", dsr.getCategories().get(0).getRelevantItems());
        assertEquals("Count incorrect", 1L, dsr.getCount(), 0);

        log.info("Testing findDepartmentSearchResults() complete");
    }

}
