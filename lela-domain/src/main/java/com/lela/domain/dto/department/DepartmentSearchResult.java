/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.domain.dto.department;

import com.lela.domain.document.Category;
import com.lela.domain.document.Item;
import com.lela.domain.document.RelevantItem;
import com.lela.domain.dto.ItemPage;

/**
 * Created by Bjorn Harvold
 * Date: 11/4/12
 * Time: 6:45 PM
 * Responsibility:
 */
public class DepartmentSearchResult {
    private Category category;
    private ItemPage<Item> items;
    private ItemPage<RelevantItem> relevantItems;

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public ItemPage<Item> getItems() {
        return items;
    }

    public void setItems(ItemPage<Item> items) {
        this.items = items;
    }

    public ItemPage<RelevantItem> getRelevantItems() {
        return relevantItems;
    }

    public void setRelevantItems(ItemPage<RelevantItem> relevantItems) {
        this.relevantItems = relevantItems;
    }
}
