/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.commons.converter;

/**
 * Created by Bjorn Harvold
 * Date: 1/24/12
 * Time: 11:38 PM
 * Responsibility:
 */
import com.lela.commons.service.CategoryService;
import com.lela.domain.document.Category;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.core.convert.converter.Converter;

public class StringToCategoryConverter implements Converter<String, Category> {

    private final CategoryService categoryService;

    public StringToCategoryConverter(CategoryService categoryService) {
        this.categoryService = categoryService;
    }


    @Override
    public Category convert(String id) {
        Category result = null;

        if (StringUtils.isNotBlank(id)) {
            result = categoryService.findCategoryById(new ObjectId(id));
        }

        return result;
    }

}
