/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.commons.service;

import com.lela.domain.document.Blog;
import com.lela.domain.dto.CategoryBlogs;
import com.lela.domain.dto.CustomPage;
import com.lela.domain.dto.blog.BlogEntry;
import com.lela.domain.dto.blog.BlogsQuery;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Created by Bjorn Harvold
 * Date: 2/10/12
 * Time: 2:25 AM
 * Responsibility:
 */
public interface BlogService {
    CustomPage<CategoryBlogs> findBlogPageOrderByCategory(Integer page);

    CustomPage<CategoryBlogs> findBlogPageOrderByDate(Integer page);

    Blog findPublishedBlogByUrlName(String urlName);

    Blog findLatestBlog();

    Page<CategoryBlogs> findBlogs(BlogsQuery blogsQuery);

    Long findBlogCount();

    Page<Blog> findBlogs(Integer page, Integer maxResults);

    List<Blog> paginateThroughAllBlogs(Integer maxResults);

    Blog saveBlog(BlogEntry blog);

    void deleteBlog(Blog blog);

    void removeEmptyItems(BlogEntry blog);

    Blog findBlogByUrlName(String urlName);

    List<CategoryBlogs> findBlogsOrderByCategory();

    List<CategoryBlogs> findBlogsOrderByDate();
}
