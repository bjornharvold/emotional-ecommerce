/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.commons.test.service;

import com.lela.domain.ApplicationConstants;
import com.lela.commons.repository.ProductGridRepository;
import com.lela.commons.service.impl.CacheServiceImpl;
import com.lela.commons.service.impl.CategoryServiceImpl;
import com.lela.commons.service.impl.ProductEngineServiceImpl;
import com.lela.commons.service.impl.ProductGridServiceImpl;
import com.lela.commons.service.impl.UserServiceImpl;
import com.lela.domain.document.Category;
import com.lela.domain.document.Item;
import com.lela.domain.document.ProductGrid;
import com.lela.domain.document.RelevantItem;
import com.lela.domain.document.UserSupplement;
import com.lela.domain.dto.CategoryItemsQuery;
import com.lela.domain.dto.CustomPage;
import com.lela.domain.dto.ItemPage;
import com.lela.domain.dto.productgrid.EnrichProductGridQuery;
import com.lela.domain.dto.productgrid.EnrichedProductGrid;
import com.lela.domain.enums.CacheType;
import com.lela.domain.enums.FunctionalSortType;
import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Bjorn Harvold
 * Date: 8/18/12
 * Time: 10:02 PM
 * Responsibility:
 */
@RunWith(MockitoJUnitRunner.class)
public class ProductGridServiceUnitTest {
    private static final Logger log = LoggerFactory.getLogger(ProductGridServiceUnitTest.class);
    private static final String CATEGORY_URL_NAME = ProductGridServiceUnitTest.class.getSimpleName() + "Category";
    private static final String PRODUCT_GRID_URL_NAME = ProductGridServiceUnitTest.class.getSimpleName() + "ProductGrid";
    private static final String FILTER_KEY = "key";
    private static final String EMAIL = "somestupidemail@home.com";
    private static final String SOME_CODE = "somecode";

    @Mock
    private ProductGridRepository productGridRepository;

    @Mock
    private ProductEngineServiceImpl productEngineService;

    @Mock
    private UserServiceImpl userService;

    @Mock
    private CategoryServiceImpl categoryService;

    @Mock
    private CacheServiceImpl cacheService;

    @InjectMocks
    ProductGridServiceImpl productGridService;

    private Category category;

    private ProductGrid productGrid;

    private UserSupplement userSupplement;

    private ItemPage<Item> items;
    private ItemPage<RelevantItem> relevantItems;

    @Before
    public void setUp() {
        category = new Category();
        category.setId(new ObjectId());
        category.setRlnm(PRODUCT_GRID_URL_NAME);
        category.setSrlnm(CATEGORY_URL_NAME);
        category.setNm(CATEGORY_URL_NAME);

        productGrid = new ProductGrid();
        productGrid.setId(new ObjectId());
        productGrid.setRlnm(CATEGORY_URL_NAME);
        productGrid.setCrlnm(CATEGORY_URL_NAME);
        productGrid.setHdr("Some fantastic header");
        productGrid.setNm("Fantastic Name");
        productGrid.setLcl(Locale.US);
        productGrid.setPblshd(true);
        productGrid.setSrt(FunctionalSortType.POPULARITY);
        productGrid.setVrrd(true);

        userSupplement = new UserSupplement(SOME_CODE);

        List<Item> list1 = new ArrayList<Item>(1);
        list1.add(new Item());
        items = new ItemPage<Item>(list1, new PageRequest(0, 1), 1);

        List<RelevantItem> list2 = new ArrayList<RelevantItem>(1);
        list2.add(new RelevantItem());
        relevantItems = new ItemPage<RelevantItem>(list2, new PageRequest(0, 1), 1);

    }

    @Test
    public void testSaveProductGrid() {
        log.info("First we create a productGrid");

        // configure mocks
        when(categoryService.findCategoryByUrlName(CATEGORY_URL_NAME)).thenReturn(category);
        when(productGridRepository.save(productGrid)).thenReturn(productGrid);
        doNothing().when(cacheService).removeFromCache(CacheType.PRODUCT_GRID, productGrid.getRlnm());

        // execute the service method
        productGrid = productGridService.saveProductGrid(productGrid);

        // verify
        assertNotNull("ProductGrid is null", productGrid);
        assertEquals("Product srlnm is incorrect", CATEGORY_URL_NAME, productGrid.getSrlnm());

        verify(categoryService, times(1)).findCategoryByUrlName(productGrid.getCrlnm());
        verify(productGridRepository, times(1)).save(productGrid);
        verify(cacheService, times(1)).removeFromCache(CacheType.PRODUCT_GRID, productGrid.getRlnm());

        log.info("ProductGrid was saved successfully");
    }

    @Test
    public void testQueryProductGrid() {
        log.info("Time to query the service for grids");

        // configure mocks
        List<ProductGrid> list = new ArrayList<ProductGrid>(1);
        list.add(productGrid);
        Page<ProductGrid> page = new CustomPage<ProductGrid>(list, new PageRequest(0, 1), 1);
        when(productGridRepository.findAll(new PageRequest(0, 1))).thenReturn(page);

        // execute the service method
        page = productGridService.findProductGrids(0, 1);

        // verify
        assertNotNull("Page is null", page);
        assertEquals("Page number of elements is incorrect", 1, page.getNumberOfElements(), 0);
        assertEquals("Page total pages is incorrect", 1, page.getTotalPages(), 0);
        assertEquals("Page total elements is incorrect", 1, page.getTotalElements(), 0);
        verify(productGridRepository, times(1)).findAll(new PageRequest(0, 1));

        // configure mocks
        when(cacheService.retrieveFromCache(ApplicationConstants.PRODUCT_GRID_CACHE, PRODUCT_GRID_URL_NAME)).thenReturn(null);
        when(productGridRepository.findByUrlName(PRODUCT_GRID_URL_NAME)).thenReturn(productGrid);
        doNothing().when(cacheService).putInCache(ApplicationConstants.PRODUCT_GRID_CACHE, productGrid.getRlnm(), productGrid);

        // execute service method
        productGrid = productGridService.findProductGridByUrlName(PRODUCT_GRID_URL_NAME);

        // verify
        assertNotNull("ProductGrid is null", productGrid);
        verify(productGridRepository, times(1)).findByUrlName(PRODUCT_GRID_URL_NAME);

        log.info("ProductGrid objects queried successfully");
    }

    @Test
    public void testSaveProductGridFilters() {
        log.info("Time to add some filters...");

        // configure mocks
        when(categoryService.findCategoryByUrlName(CATEGORY_URL_NAME)).thenReturn(category);
        when(productGridRepository.save(productGrid)).thenReturn(productGrid);
        doNothing().when(cacheService).removeFromCache(CacheType.PRODUCT_GRID, productGrid.getRlnm());
        when(productGridRepository.findByUrlName(PRODUCT_GRID_URL_NAME)).thenReturn(productGrid);

        // execute service method
        Map<String, List<String>> map = new HashMap<String, List<String>>(1);
        map.put(FILTER_KEY, new ArrayList<String>());
        productGridService.saveProductGridFilters(PRODUCT_GRID_URL_NAME, map);

        // verify
        verify(categoryService, times(1)).findCategoryByUrlName(productGrid.getCrlnm());
        verify(productGridRepository, times(1)).save(productGrid);
        verify(cacheService, times(1)).removeFromCache(CacheType.PRODUCT_GRID, productGrid.getRlnm());

        log.info("Time to add some filters was successful");

        log.info("Remove the filter again...");

        // configure mocks
        when(productGridRepository.findByUrlName(PRODUCT_GRID_URL_NAME)).thenReturn(productGrid);
        when(productGridRepository.save(productGrid)).thenReturn(productGrid);

        // execute service method
        productGridService.removeProductGridFilter(PRODUCT_GRID_URL_NAME, FILTER_KEY);

        // verify
        verify(categoryService, times(2)).findCategoryByUrlName(productGrid.getCrlnm());
        verify(productGridRepository, times(2)).save(productGrid);
        verify(cacheService, times(2)).removeFromCache(CacheType.PRODUCT_GRID, productGrid.getRlnm());

        log.info("Remove the filter again successful");
    }

    @Test
    public void testRemoveProductGrid() {
        log.info("Remove the ProductGrid again...");

        // configure mocks
        when(productGridRepository.findByUrlName(PRODUCT_GRID_URL_NAME)).thenReturn(productGrid);
        doNothing().when(productGridRepository).delete(productGrid);

        // execute service method
        productGridService.removeProductGrid(PRODUCT_GRID_URL_NAME);

        // verify
        verify(productGridRepository, times(1)).delete(productGrid);

        log.info("Remove the ProductGrid again successful");
    }

    @Test
    public void testFindPublishedProductGrid() {
        log.info("Attempting to find a product grid that has not been published...");

        // configure mocks
        when(productGridRepository.findByUrlName(PRODUCT_GRID_URL_NAME)).thenReturn(productGrid);

        // execute service method
        productGrid = productGridService.findPublishedProductGridByUrlName(PRODUCT_GRID_URL_NAME);

        // verify
        assertNotNull("ProductGrid is null", productGrid);
        verify(productGridRepository, times(1)).findByUrlName(PRODUCT_GRID_URL_NAME);

        log.info("Attempting to find a product grid that has not been published successful");
    }

    @Test
    public void testEnrichProductGrid() {
        log.info("Enriching product grid...");

        log.info("First, we're going to call this method without any email");

        // configure mocks
        when(userService.shouldUserSeeRecommendedProducts(null)).thenReturn(false);
        when(productEngineService.findItemsByCategory(any(CategoryItemsQuery.class))).thenReturn(items);

        EnrichedProductGrid epg = new EnrichedProductGrid(productGrid);
        EnrichProductGridQuery epgq = new EnrichProductGridQuery(null, 1, epg);
        assertNull("Items are not null", epg.getItems());

        // execute service method
        productGridService.enrichProductGrid(epgq);

        // verify
        assertNotNull("Items are null", epg.getItems());
        verify(userService, times(1)).shouldUserSeeRecommendedProducts(null);
        verify(productEngineService, times(1)).findItemsByCategory(any(CategoryItemsQuery.class));

        log.info("This time we want to return relevant items");

        // configure mocks
        when(userService.findUserSupplementByEmail(EMAIL)).thenReturn(userSupplement);
        when(userService.shouldUserSeeRecommendedProducts(SOME_CODE)).thenReturn(true);
        when(productEngineService.findRelevantItemsByCategory(any(CategoryItemsQuery.class))).thenReturn(relevantItems);

        // Ensure that the correct sort was used for popularity
        ArgumentCaptor<CategoryItemsQuery> queryArgument = ArgumentCaptor.forClass(CategoryItemsQuery.class);
        verify(productEngineService).findItemsByCategory(queryArgument.capture());
        assertEquals("Incorrect sort type used for popularity sort", FunctionalSortType.POPULARITY, queryArgument.getValue().getSortBy());
        assertTrue("Item query not set to sort", queryArgument.getValue().getSort());

        // execute service method
        epgq = new EnrichProductGridQuery(SOME_CODE, 1, epg);
        productGridService.enrichProductGrid(epgq);

        // verify
        assertNotNull("Relevant Items are null", epg.getRelevantItems());
        verify(userService, times(1)).shouldUserSeeRecommendedProducts(SOME_CODE);
        verify(productEngineService, times(1)).findRelevantItemsByCategory(any(CategoryItemsQuery.class));

        // Ensure that the correct sort was used for relevancy
        verify(productEngineService).findRelevantItemsByCategory(queryArgument.capture());
        assertEquals("Incorrect sort type used for relevancy sort", FunctionalSortType.RECOMMENDED, queryArgument.getValue().getSortBy());
        assertTrue("Item query not set to sort", queryArgument.getValue().getSort());

        log.info("Enriching product grid successful");
    }
}
