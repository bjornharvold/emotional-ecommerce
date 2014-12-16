/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.commons.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.lela.commons.LelaException;
import com.lela.commons.repository.BlogRepository;
import com.lela.commons.service.BlogService;
import com.lela.commons.service.ItemService;
import com.lela.commons.utilities.blog.CategoryBlogSorter;
import com.lela.domain.document.Blog;
import com.lela.domain.document.BlogItem;
import com.lela.domain.document.Item;
import com.lela.domain.document.QBlog;
import com.lela.domain.dto.CategoryBlogs;
import com.lela.domain.dto.CustomPage;
import com.lela.domain.dto.blog.BlogEntry;
import com.lela.domain.dto.blog.BlogsQuery;
import com.lela.domain.enums.PublishStatus;
import com.lela.util.utilities.number.NumberUtils;
import com.lela.util.utilities.storage.FileStorage;
import com.lela.util.utilities.storage.dto.FileData;

/**
 * Created by Bjorn Harvold
 * Date: 2/10/12
 * Time: 2:24 AM
 * Responsibility:
 */
@Service("blogService")
public class BlogServiceImpl implements BlogService {
    private final BlogRepository blogRepository;
    private final FileStorage fileStorage;
    private final ItemService itemService;

    private static final int SIZE = 8;

    @Autowired
    public BlogServiceImpl(BlogRepository blogRepository,
                           @Qualifier("blogImageFileStorage") FileStorage fileStorage,
                           ItemService itemService
    		) {
        this.blogRepository = blogRepository;
        this.fileStorage = fileStorage;
        this.itemService = itemService;

    }

    /**
     * Retrieves all blogs. Orders by item category first and blog posted date second
     *
     * @return List
     * @param page page
     */
    @Override
    public CustomPage<CategoryBlogs> findBlogPageOrderByCategory(Integer page) {
        CustomPage<CategoryBlogs> result = null;

        List<Blog> blogs = (List<Blog>) blogRepository.findAll(QBlog.blog.stts.eq(PublishStatus.PUBLISHED));
        buildBlogItemMap(blogs);
        if (blogs != null && !blogs.isEmpty()) {
            List<String> sortProps = new ArrayList<String>(1);
            sortProps.add("ldt");
            Sort sort = new Sort(Sort.Order.create(Sort.Direction.DESC, sortProps));
            Pageable pageable = new PageRequest(page, blogs.size(), sort);
            result = new CustomPage<CategoryBlogs>(new CategoryBlogSorter().orderBlogsByCategory(blogs), pageable, blogs.size());
        }

        return result;
    } 

    /**
     *
     * @param page page
     * @return
     */
    @Override
    public CustomPage<CategoryBlogs> findBlogPageOrderByDate(Integer page) {
        CustomPage<CategoryBlogs> result = null;
        List<String> sortProps = new ArrayList<String>(1);
        sortProps.add("ldt");
        Sort sort = new Sort(Sort.Order.create(Sort.Direction.DESC, sortProps));
        Pageable pageable = new PageRequest(page, SIZE, sort);

        Page<Blog> blogs = blogRepository.findAll(QBlog.blog.stts.eq(PublishStatus.PUBLISHED), pageable);
        buildBlogItemMap(blogs.getContent());
        if (blogs != null && blogs.hasContent()) {
            result = new CustomPage<CategoryBlogs>(new CategoryBlogSorter().categoryBlogsByDate(blogs.getContent()), pageable, blogs.getTotalElements());
        }

        return result;
    }

    @Override
    public Blog findPublishedBlogByUrlName(String urlName) {
        Blog blog =  blogRepository.findByUrlNameAndStatus(urlName, PublishStatus.PUBLISHED);
        
        buildBlogItemMap(blog);
        return blog;
    }



    @Override
    public Blog findLatestBlog() {
        List<String> sortProps = new ArrayList<String>(1);
        sortProps.add("ldt");

        Page<Blog> blogs = blogRepository.findAll(QBlog.blog.stts.eq(PublishStatus.PUBLISHED),
                new PageRequest(0, 1, new Sort(Sort.Order.create(Sort.Direction.DESC, sortProps))));
        buildBlogItemMap(blogs.getContent());
        return blogs != null ? blogs.getContent().get(0) : null;
    }

    @Override
    public CustomPage<CategoryBlogs> findBlogs(BlogsQuery query) {
        CustomPage<CategoryBlogs> result = null;

        switch (query.getSort()) {
            case C:
                result = findBlogPageOrderByCategory(query.getPage());
                break;
            case D:
                result = findBlogPageOrderByDate(query.getPage());
                break;
        }

        return result;
    }

    @Override
    public Long findBlogCount() {
        return blogRepository.count();
    }

    @Override
    public Page<Blog> findBlogs(Integer page, Integer maxResults) {
        Page<Blog> blogPage =  blogRepository.findAll(new PageRequest(page, maxResults));
        buildBlogItemMap(blogPage.getContent());
        return blogPage;
    }

    @Override
    public List<Blog> paginateThroughAllBlogs(Integer maxResults) {
        List<Blog> result = null;
        Long count = findBlogCount();

        if (count != null && count > 0) {
            result = new ArrayList<Blog>(count.intValue());
            Integer iterations = NumberUtils.calculateIterations(count, maxResults.longValue());

            // load up items in a paginated fashion from mongo
            for (int i = 0; i < iterations; i++) {
                result.addAll(findBlogs(i, maxResults).getContent());
            }
        }

        return result;
    }

    @PreAuthorize("hasAnyRole('RIGHT_READ_BLOG_AS_ADMIN')")
    @Override
    public List<CategoryBlogs> findBlogsOrderByCategory() {
        List<Blog> blogs = (List<Blog>) blogRepository.findAll();
        buildBlogItemMap(blogs);
        return new CategoryBlogSorter().orderBlogsByCategory(blogs);
    }

    @PreAuthorize("hasAnyRole('RIGHT_READ_BLOG_AS_ADMIN')")
    @Override
    public List<CategoryBlogs> findBlogsOrderByDate() {
        List<Blog> blogs = (List<Blog>) blogRepository.findAll();
        buildBlogItemMap(blogs);
        return new CategoryBlogSorter().categoryBlogsByDate(blogs);
    }


    @PreAuthorize("hasAnyRole('RIGHT_READ_BLOG_AS_ADMIN')")
    @Override
    public Blog findBlogByUrlName(String urlName) {
        Blog blog =  blogRepository.findByUrlName(urlName);
        buildBlogItemMap(blog);
        return blog;
    }

    @PreAuthorize("hasAnyRole('RIGHT_INSERT_BLOG')")
    @Override
    public Blog saveBlog(BlogEntry blog) {

        try {
            // if there is a multipart file on the blog it should get uploaded to S3
            if (blog.getMultipartFile() != null && !blog.getMultipartFile().isEmpty()) {
                MultipartFile file = blog.getMultipartFile();
                FileData data = new FileData(blog.getRlnm() + "-" + file.getOriginalFilename(), file.getBytes(), file.getContentType());
                String url = fileStorage.storeFile(data);
                blog.setMgrl(url);
            }
        } catch (IOException e) {
            throw new LelaException(e.getMessage(), e);
        }

        // set the publish date if it hasn't been set before and the blog has been published
        if (blog.getDt() == null && blog.getStts().equals(PublishStatus.PUBLISHED)) {
            blog.setDt(new Date());
        }

        // make sure we clean up a messy url name
        blog.setRlnm(scrubBlogUrlName(blog.getRlnm()));

        return blogRepository.save(new Blog(blog));
    }

    @PreAuthorize("hasAnyRole('RIGHT_DELETE_BLOG')")
    @Override
    public void deleteBlog(Blog blog) {
        blogRepository.delete(blog);
    }

    @Override
    public void removeEmptyItems(BlogEntry blog) {
        List<BlogItem> items = null;

        if (blog.getTms() != null && !blog.getTms().isEmpty()) {
            items = new ArrayList<BlogItem>();
            for (int i = 0; i < blog.getTms().size(); i++) {
                BlogItem blogItem = blog.getTms().get(i);
                if (blogItem != null){	
	                if (blogItem != null && !blogItem.isEmpty()) {
	                    items.add(blogItem);
	                }
                }
            }

            blog.setTms(items);
        }
    }

    /**
     * Removes all whitespace and lower cases strings
     *
     * @param blogName blogName
     */
    private String scrubBlogUrlName(String blogName) {
        blogName = blogName.trim();
        blogName = StringUtils.replaceChars(blogName, " ,|'\"*^%$#@", "-");
        blogName = StringUtils.replace(blogName, "&", "and");
        blogName = StringUtils.replaceChars(blogName, "!.()", "");

        return blogName.trim().toLowerCase();
    }
    
    /**
     * Adds the full item object to each blog entry
     * @param blog
     */
	private void buildBlogItemMap(Blog blog) {
		if (blog != null && blog.getTms() != null && !blog.getTms().isEmpty()) {
            for (BlogItem blogItem : blog.getTms()) {
                Item item = itemService.findItemByUrlName(blogItem.getRlnm());
                blog.getItemMap().put(blogItem.getRlnm(), item);
            }
        }
	}
	
	/**
	 * Adds full item object to each blog in list
	 * @param blogList
	 */
	private void buildBlogItemMap(List<Blog> blogList){
		if (blogList != null){
			for (Blog blog : blogList) {
				buildBlogItemMap(blog);
			}
		}
	}
}
