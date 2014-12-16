/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.domain.dto.owner;

import com.lela.domain.document.Owner;
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
public class OwnerAggregate extends NameValueAggregate implements Serializable {
    private static final long serialVersionUID = -6810614163213921458L;
    private Owner owner;
    private List<CategoryCount> categories;

    public OwnerAggregate(String rlnm, Long cnt) {
        super(rlnm, cnt);
    }

    public OwnerAggregate(NameValueAggregate sa) {
        super(sa.getRlnm(), sa.getCnt());
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public List<CategoryCount> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryCount> categories) {
        this.categories = categories;
    }
}
