/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.commons.service.impl;

import com.lela.domain.ApplicationConstants;
import com.lela.commons.repository.CategoryRepository;
import com.lela.commons.service.CacheService;
import com.lela.commons.service.CategoryService;
import com.lela.domain.document.Category;
import com.lela.domain.document.NavigationBar;
import com.lela.domain.document.QCategory;
import com.lela.domain.enums.CacheType;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bjorn Harvold
 * Date: 6/1/12
 * Time: 8:18 PM
 * Responsibility:
 */
@Service("categoryService")
public class CategoryServiceImpl extends AbstractCacheableService implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(CacheService cacheService, CategoryRepository categoryRepository) {
        super(cacheService);
        this.categoryRepository = categoryRepository;
    }

    /**
     * Method description
     *
     * @return Return value
     */
    @Override
    public List<Category> findCategories() {
        List<Category>     result  = null;
        Cache.ValueWrapper wrapper = retrieveFromCache(ApplicationConstants.CATEGORY_CACHE, ApplicationConstants.CATEGORIES_KEY);

        if ((wrapper != null) && (wrapper.get() instanceof List)) {
            result = (List<Category>) wrapper.get();
        }

        // retrieve from db then put in cache
        if (result == null) {
            List<String> sortProps = new ArrayList<String>();

            sortProps.add("rdr");
            result = (List<Category>) categoryRepository.findAll(new Sort(Sort.Order.create(Sort.Direction.ASC,
                    sortProps)));

            if (result != null) {
                putInCache(ApplicationConstants.CATEGORY_CACHE, ApplicationConstants.CATEGORIES_KEY, result);
            }
        }

        return result;
    }

    /**
     * Method description
     *
     * @param categoryId categoryId
     * @return Return value
     */
    @Override
    public Category findCategoryById(ObjectId categoryId) {
        return categoryRepository.findOne(categoryId);
    }

    /**
     * Return the category with the correct ancestors and url name
     *
     * @param categoryUrlName categoryUrlName
     * @return Category
     */
    @Override
    public Category findCategoryByUrlName(String categoryUrlName) {
        Category result = null;

        Cache.ValueWrapper wrapper = retrieveFromCache(ApplicationConstants.CATEGORY_CACHE, categoryUrlName);

        if (wrapper != null && wrapper.get() != null && wrapper.get() instanceof Category) {
            result = (Category) wrapper.get();
        } else {
            result = categoryRepository.findByUrlName(categoryUrlName);

            if (result != null) {
                putInCache(ApplicationConstants.CATEGORY_CACHE, categoryUrlName, result);
            }
        }

        return result;
    }

    /**
     * Method description
     *
     * @param rlnm category
     */
    @PreAuthorize("hasAnyRole('RIGHT_CONTENT_INGEST')")
    @Override
    public Category removeCategory(String rlnm) {
        Category category = categoryRepository.findByUrlName(rlnm);
        if (category != null) {
            categoryRepository.delete(category);

            // Remove from cache
            List<String> cacheKeys = new ArrayList<String>(2);
            cacheKeys.add(category.getRlnm());
            cacheKeys.add(ApplicationConstants.CATEGORIES_KEY);

            removeFromCache(CacheType.CATEGORY, cacheKeys);
        }

        return category;
    }

    /**
     *
     * @param list list
     * @return Return value
     */
    @PreAuthorize("hasAnyRole('RIGHT_CONTENT_INGEST')")
    @Override
    public List<Category> saveCategories(List<Category> list) {
        List<Category> result = (List<Category>) categoryRepository.save(list);

        // Remove from cache
        if (list != null && !list.isEmpty()) {
            List<String> cacheKeys = new ArrayList<String>(list.size() + 1);

            cacheKeys.add(ApplicationConstants.CATEGORIES_KEY);
            for (Category item : list) {
                cacheKeys.add(item.getRlnm());
            }

            removeFromCache(CacheType.CATEGORY, cacheKeys);
        }

        return result;
    }

    /**
     * Method description
     *
     * @param category category
     * @return Return value
     */
    @PreAuthorize("hasAnyRole('RIGHT_CONTENT_INGEST')")
    @Override
    public Category saveCategory(Category category) {
        Category result = categoryRepository.save(category);

        // Remove from cache
        List<String> cacheKeys = new ArrayList<String>(2);
        cacheKeys.add(category.getRlnm());
        cacheKeys.add(ApplicationConstants.CATEGORIES_KEY);

        removeFromCache(CacheType.CATEGORY, cacheKeys);

        return result;
    }

    /**
     * This is just to update the lastUpdate date property on category
     * @param categoryUrlName categoryUrlName
     */
    @Override
    public void refreshCategory(String categoryUrlName) {
        Category category = findCategoryByUrlName(categoryUrlName);

        if (category != null) {
            saveCategory(category);
        }
    }

    @Override
    public List<Category> findCategoriesByUrlName(List<String> urlNames) {
        return (List<Category>) categoryRepository.findAll(QCategory.category.rlnm.in(urlNames));
    }

    /**
     * Returns all categories that are part of a category group
     * @param urlName urlName
     * @return list
     */
    @Override
    public List<Category> findCategoriesByCategoryGroupUrlName(String urlName) {
        return (List<Category>) categoryRepository.findAll(QCategory.category.grps.contains(urlName));
    }

    @Override
    public String determineDefaultDepartmentUrl(String categoryUrlName, String departmentUrlName) {
        if (StringUtils.isBlank(departmentUrlName)) {
            Category category = findCategoryByUrlName(categoryUrlName);
            if(category != null && category.getDprtmnts() != null && category.getDprtmnts().size() > 0)
            {
                departmentUrlName = category.getDprtmnts().get(0);
            }
        }
        return departmentUrlName;
    }

    @Override
    public String determineDefaultDepartmentUrl(Category category) {
        String departmentUrlName = null;
        if(category != null && category.getDprtmnts() != null && category.getDprtmnts().size() > 0)
        {
            departmentUrlName = category.getDprtmnts().get(0);
        }

        return departmentUrlName;
    }
}
