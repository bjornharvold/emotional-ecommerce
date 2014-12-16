/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.domain.dto.store;

import com.lela.domain.document.Store;
import com.lela.domain.dto.search.CategoryCount;
import com.lela.domain.dto.search.NameValueAggregate;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Bjorn Harvold
 * Date: 4/27/12
 * Time: 12:42 AM
 * Responsibility:
 */
public class StoreAggregate extends NameValueAggregate implements Serializable {
    private static final long serialVersionUID = -6810614163213921458L;
    private Store store;
    private List<CategoryCount> categories;

    public StoreAggregate(String rlnm, Long cnt) {
        super(rlnm, cnt);
    }

    public StoreAggregate(NameValueAggregate sa) {
        super(sa.getRlnm(), sa.getCnt());
    }

    public StoreAggregate(StoreAggregate sa) {
        super(sa.getRlnm(), sa.getCnt());
        this.store = sa.getStore();
        this.categories = sa.getCategories();
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public List<CategoryCount> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryCount> categories) {
        this.categories = categories;
    }
}
