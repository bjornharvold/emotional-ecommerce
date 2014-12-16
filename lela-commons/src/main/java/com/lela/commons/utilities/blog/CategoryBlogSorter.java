/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.commons.utilities.blog;

import com.lela.commons.comparator.BlogLastUpdateComparator;
import com.lela.commons.comparator.CategoryBlogsComparator;
import com.lela.domain.document.Blog;
import com.lela.domain.dto.CategoryBlogs;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Bjorn Harvold
 * Date: 2/11/12
 * Time: 2:55 AM
 * Responsibility:
 */
public class CategoryBlogSorter {
    public List<CategoryBlogs> orderBlogsByCategory(List<Blog> blogs) {
        Map<String, CategoryBlogs> catMap = null;

        List<CategoryBlogs> result = null;

        if (blogs != null && !blogs.isEmpty()) {

            // first we sort by last update
            Collections.sort(blogs, new BlogLastUpdateComparator());

            // group by category
            result = new ArrayList<CategoryBlogs>();

            for (Blog blog : blogs) {
                if (catMap == null) {
                    catMap = new HashMap<String, CategoryBlogs>();
                }

                // retrieve first blog item and determine category
                catMap.put(blog.getCtgry().getRlnm(), new CategoryBlogs(blog.getCtgry()));
            }

            if (catMap != null) {
                // now we have all categories and we can loop through blogs and add items
                // since we already sorted the blogs, they should be sorted correctly
                for (CategoryBlogs cb : catMap.values()) {
                    for (Blog blog : blogs) {
                        if (StringUtils.equals(cb.getCtgry().getRlnm(), blog.getCtgry().getRlnm())) {

                            if (cb.getBlgs() == null) {
                                cb.setBlgs(new ArrayList<Blog>());
                            }
                            cb.getBlgs().add(blog);
                        }
                    }
                }

                // now everything is filled in and we just want to finish by ordering
                result = new ArrayList<CategoryBlogs>(catMap.values());

                for (CategoryBlogs cb : result) {
                    Collections.sort(cb.getBlgs(), new BlogLastUpdateComparator());
                }

                Collections.sort(result, new CategoryBlogsComparator());
            }
        }
        return result;
    }

    public List<CategoryBlogs> categoryBlogsByDate(List<Blog> blogs) {
        List<CategoryBlogs> result = null;

        if (blogs != null && !blogs.isEmpty()) {
            result = new ArrayList<CategoryBlogs>(1);
            CategoryBlogs cb = new CategoryBlogs(blogs);

            result.add(cb);
        }

        return result;
    }
}
