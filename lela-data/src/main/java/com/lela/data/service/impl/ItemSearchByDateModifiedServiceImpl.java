package com.lela.data.service.impl;

import com.lela.data.domain.entity.Brand;
import com.lela.data.domain.entity.Category;
import com.lela.data.domain.entity.Item;
import com.lela.data.domain.entity.ItemStatus;
import com.lela.data.service.ItemSearchService;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ballmw
 * Date: 7/27/12
 * Time: 2:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class ItemSearchByDateModifiedServiceImpl implements ItemSearchService{
    public List<Item> findItems(Category category, Brand brand, ItemStatus itemStatus, Integer itemId, String productModelName, Long[] reviewStatusId, Boolean hasNewImages, int page, int PAGE_SIZE, String sort, String order)
    {
        return Item.findItemsByCategoryByProductImageDateModified(category, brand, itemStatus, itemId, productModelName, reviewStatusId, page, PAGE_SIZE, order);
    }
}
