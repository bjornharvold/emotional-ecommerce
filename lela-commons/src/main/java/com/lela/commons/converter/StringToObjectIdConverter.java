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
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.core.convert.converter.Converter;

public class StringToObjectIdConverter implements Converter<String, ObjectId> {

    @Override
    public ObjectId convert(String id) {
        ObjectId result = null;

        if (StringUtils.isNotBlank(id)) {
            result = new ObjectId(id);
        }

        return result;
    }

}
