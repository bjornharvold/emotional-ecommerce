/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.domain.dto.search;

import com.lela.domain.document.Category;
import com.lela.domain.document.Item;
import com.lela.domain.document.RelevantItem;
import com.lela.domain.dto.ItemPage;
import org.apache.solr.client.solrj.response.QueryResponse;

import java.util.List;
import java.util.Map;

/**
 * Created by Bjorn Harvold
 * Date: 3/13/12
 * Time: 9:41 PM
 * Responsibility:
 */
public class GroupedSearchResult extends AbstractSearchResult {
    private Map<String, ItemPage<Item>> items;
    private Map<String, ItemPage<RelevantItem>> relevantItems;

    public GroupedSearchResult(Integer page, Integer size) {
        super(page, size);
    }

    public Map<String, ItemPage<Item>> getItems() {
        return items;
    }

    public void setItems(Map<String, ItemPage<Item>> content) {
        this.items = content;
    }

    public Map<String, ItemPage<RelevantItem>> getRelevantItems() {
        return relevantItems;
    }

    public void setRelevantItems(Map<String, ItemPage<RelevantItem>> relevantContent) {
        this.relevantItems = relevantContent;
    }

    public long getTotalElements() {
        long result = 0;

        if (items != null && !items.isEmpty()) {
            for (ItemPage<Item> page : items.values()) {
                result += page.getTotalElements();
            }
        }

        return result;
    }
}
