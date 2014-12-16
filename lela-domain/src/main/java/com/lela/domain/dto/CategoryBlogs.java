/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.domain.dto;

import com.lela.domain.document.Blog;
import com.lela.domain.document.Category;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Bjorn Harvold
 * Date: 1/21/12
 * Time: 2:52 AM
 * Responsibility:
 */
public class CategoryBlogs implements Serializable {
    private Category ctgry;
    private List<Blog> blgs;

    public CategoryBlogs() {
    }

    public CategoryBlogs(List<Blog> blgs) {
        this.blgs = blgs;
    }

    public CategoryBlogs(Category ctgry) {
        this.ctgry = ctgry;
    }

    public Category getCtgry() {
        return ctgry;
    }

    public void setCtgry(Category ctgry) {
        this.ctgry = ctgry;
    }

    public List<Blog> getBlgs() {
        return blgs;
    }

    public void setBlgs(List<Blog> blgs) {
        this.blgs = blgs;
    }
}
