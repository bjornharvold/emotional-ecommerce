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
import com.lela.domain.document.Category;
import org.springframework.core.convert.converter.Converter;

public class CategoryToStringConverter implements Converter<Category, String> {

    @Override
    public String convert(Category source) {
        String result = null;

        if (source != null) {
            result = source.getId().toString();
        }

        return result;
    }

}
