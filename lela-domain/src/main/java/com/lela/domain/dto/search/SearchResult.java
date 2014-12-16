/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.domain.dto.search;

import com.lela.domain.document.Item;
import com.lela.domain.document.RelevantItem;
import com.lela.domain.dto.ItemPage;

import java.util.List;
import java.util.Map;

/**
 * Created by Bjorn Harvold
 * Date: 2/24/12
 * Time: 1:31 AM
 * Responsibility:
 */
public class SearchResult extends AbstractSearchResult {
    private ItemPage<Item> items;
    private ItemPage<RelevantItem> relevantItems;

    public SearchResult() {
    }

    public SearchResult(Integer page, Integer size) {
        super(page, size);
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

    public long getTotalElements() {
        long result = 0l;

        if (items != null) {
            result = items.getTotalElements();
        }

        return result;
    }
}
