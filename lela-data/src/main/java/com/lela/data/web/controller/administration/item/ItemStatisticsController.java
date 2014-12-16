/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.data.web.controller.administration.item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.lela.commons.comparator.ItemByNameComparator;
import com.lela.commons.service.CategoryService;
import com.lela.commons.service.ItemService;
import com.lela.commons.service.StoreService;
import com.lela.commons.web.utils.WebConstants;
import com.lela.domain.document.Campaign;
import com.lela.domain.document.Category;
import com.lela.domain.document.Foo;
import com.lela.domain.document.Item;
import com.lela.domain.dto.quiz.QuizAnswer;
import com.lela.domain.dto.search.NameValueAggregate;
import com.lela.domain.dto.store.StoreAggregateQuery;

/**
 * Created by Bjorn Harvold
 * Date: 4/25/12
 * Time: 11:28 PM
 * Responsibility:
 */
@Controller
public class ItemStatisticsController {
    private static final Integer DOCUMENT_LOAD = 50;
    private final StoreService storeService;
    private final ItemService itemService;
    private final CategoryService categoryService;

    @Autowired
    public ItemStatisticsController(StoreService storeService, ItemService itemService, CategoryService categoryService) {
        this.storeService = storeService;
        this.itemService = itemService;
        this.categoryService = categoryService;
    }

    /**
     * @param model model
     * @return Tile def
     * @throws Exception
     */
    @RequestMapping(value = "/administration/item/storesperitem", method = RequestMethod.GET)
    public String showStoresPerItem(@RequestParam(value = "categoryUrlName", required = false) String categoryUrlName, Model model) throws Exception {

        List<Category> categories = categoryService.findCategories();

        if (categories != null) {
            model.addAttribute(WebConstants.CATEGORIES, categories);

            for (Category category : categories) {
                if (StringUtils.equals(category.getRlnm(), categoryUrlName)) {
                    model.addAttribute(WebConstants.CATEGORY, category);

                    List<Item> items = itemService.paginateThroughAllItemsPerCategoryIncludingStores(category.getRlnm(), DOCUMENT_LOAD);

                    if (items != null && !items.isEmpty()) {
                        Collections.sort(items, new ItemByNameComparator());
                        model.addAttribute(WebConstants.ITEM_STORES_COUNT, items);
                    }
                }
            }
        }

        return "administration.stores.per.item";
    }

    /**
     * @param model model
     * @return Tile def
     * @throws Exception
     */
    @RequestMapping(value = "/administration/item/itemsperstore", method = RequestMethod.GET)
    public String showItemsPerStore(Model model) throws Exception {

        model.addAttribute(WebConstants.STORE_ITEMS_COUNT, storeService.findStoreAggregates(new StoreAggregateQuery()));

        return "administration.items.per.store";
    }

}
