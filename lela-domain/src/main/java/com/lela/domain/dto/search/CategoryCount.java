/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.domain.dto.search;

import com.lela.domain.document.Category;

import java.io.Serializable;

/**
 * Created by Bjorn Harvold
 * Date: 3/13/12
 * Time: 9:42 PM
 * Responsibility:
 */
public class CategoryCount implements Serializable {
    private static final long serialVersionUID = -9029059686557734186L;
    private Category category;
    private Long count;

    public CategoryCount(Category category, Long count) {
        this.category = category;
        this.count = count;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}
