package com.lela.commons.test.service;

import com.google.common.collect.Lists;
import com.lela.commons.repository.ItemRepository;
import com.lela.commons.service.AffiliateService;
import com.lela.commons.service.CacheService;
import com.lela.commons.service.SearchService;
import com.lela.commons.service.impl.ItemServiceImpl;
import com.lela.domain.document.Category;
import com.lela.domain.document.Item;
import com.mixpanel.java.mpmetrics.MPMetrics;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created with IntelliJ IDEA.
 * User: ballmw
 * Date: 12/13/12
 * Time: 4:16 PM
 * To change this template use File | Settings | File Templates.
 */
@RunWith(PowerMockRunner.class)
public class ItemServiceUnitTest {

    @Mock
    private SearchService searchService;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private AffiliateService affiliateService;

    @Mock
    private CacheService cacheService;

    @InjectMocks
    private ItemServiceImpl itemService;

    @Test
    public void testSaveItems() throws Exception
    {
        List<Category> categories = new ArrayList<Category>();
        when(affiliateService.findExcludedCategories()).thenReturn(categories);

        Item item = new Item();
        Category category = new Category();
        category.setRlnm("abc");
        item.setCtgry(category);
        List<Item> items = Lists.newArrayList(item);
        itemService.saveItems(items);
        verify(itemRepository).save(items);

        verify(searchService).indexItems(any(List.class));
    }

    @Test
    public void testSaveItemsWithExcludedCategory() throws Exception
    {
        List<Category> categories = new ArrayList<Category>();
        Category category = new Category();
        category.setRlnm("abc");
        categories.add(category);
        when(affiliateService.findExcludedCategories()).thenReturn(categories);

        Item item = new Item();
        item.setCtgry(category);
        List<Item> items = Lists.newArrayList(item);
        itemService.saveItems(items);
        verify(itemRepository).save(items);

        verify(searchService, never()).indexItems(any(List.class));
    }
}
