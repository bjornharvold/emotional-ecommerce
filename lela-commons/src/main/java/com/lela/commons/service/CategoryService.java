/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.commons.service;

import com.lela.domain.document.Category;
import org.bson.types.ObjectId;

import java.util.List;

/**
 * Created by Bjorn Harvold
 * Date: 6/1/12
 * Time: 8:15 PM
 * Responsibility:
 */
public interface CategoryService {
    Category findCategoryByUrlName(String categoryUrlName);
    Category saveCategory(Category category);
    List<Category> saveCategories(List<Category> list);
    Category removeCategory(String rlnm);
    List<Category> findCategories();
    Category findCategoryById(ObjectId categoryId);
    List<Category> findCategoriesByUrlName(List<String> urlNames);
    void refreshCategory(String categoryUrlName);
    List<Category> findCategoriesByCategoryGroupUrlName(String urlName);

    String determineDefaultDepartmentUrl(String categoryUrlName, String departmentUrlName);

    String determineDefaultDepartmentUrl(Category category);
}
