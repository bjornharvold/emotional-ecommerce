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
import com.lela.commons.service.ItemService;
import com.lela.domain.document.Item;
import org.apache.commons.lang.StringUtils;
import org.springframework.core.convert.converter.Converter;

public class UrlNameToItemConverter implements Converter<String, Item> {

    private final ItemService itemService;

    public UrlNameToItemConverter(ItemService itemService) {
        this.itemService = itemService;
    }

    @Override
    public Item convert(String urlName) {
        Item result = null;

        if (StringUtils.isNotBlank(urlName)) {
            result = itemService.findItemByUrlName(urlName);
        }

        return result;
    }

}
