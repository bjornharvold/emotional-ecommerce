/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.domain.dto.blog;

import com.lela.domain.enums.SortBlogBy;

/**
 * Created by Bjorn Harvold
 * Date: 3/30/12
 * Time: 10:17 PM
 * Responsibility:
 */
public class BlogsQuery {
    private final SortBlogBy sort;
    private final Integer page;

    public BlogsQuery(SortBlogBy sort, Integer page) {
        this.sort = sort;
        this.page = page;
    }

    public SortBlogBy getSort() {
        return sort;
    }

    public Integer getPage() {
        return page;
    }
}
