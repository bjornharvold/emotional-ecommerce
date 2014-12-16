/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.commons.comparator;

import com.lela.domain.dto.CategoryBlogs;

import java.util.Comparator;

/**
 * Created by Bjorn Harvold
 * Date: 10/10/11
 * Time: 9:56 PM
 * Responsibility:
 */
public class CategoryBlogsComparator implements Comparator<CategoryBlogs> {

    /**
     * Method description
     *
     * @param c1 c1
     * @param c2 c2
     * @return Return value
     */
    @Override
    public int compare(CategoryBlogs c1, CategoryBlogs c2) {
        return c1.getCtgry().getNm().compareTo(c2.getCtgry().getNm());
    }
}
