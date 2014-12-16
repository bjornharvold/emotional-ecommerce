/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */
package com.lela.domain.dto.productgrid;

import com.lela.domain.document.Item;
import com.lela.domain.document.ProductGrid;
import com.lela.domain.document.RelevantItem;
import org.springframework.data.annotation.Transient;

import java.util.List;

/**
 * User: Chris Tallent
 * Date: 9/11/12
 * Time: 3:15 PM
 */
public class EnrichedProductGrid extends ProductGrid {
    @Transient
    private List<RelevantItem> relevantItems;
    @Transient
    private List<Item> items;
    @Transient
    private Long totalElements;

    public EnrichedProductGrid(ProductGrid pg) {
        super(pg);
    }

    public List<RelevantItem> getRelevantItems() {
        return relevantItems;
    }

    public void setRelevantItems(List<RelevantItem> relevantItems) {
        this.relevantItems = relevantItems;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public Long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(Long totalElements) {
        this.totalElements = totalElements;
    }
}
