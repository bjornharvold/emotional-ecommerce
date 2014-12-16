package com.lela.data.service;

import com.lela.data.domain.dto.ItemSearchCommand;
import com.lela.data.domain.entity.Brand;
import com.lela.data.domain.entity.Category;
import com.lela.data.domain.entity.Item;
import com.lela.data.domain.entity.ItemStatus;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ballmw
 * Date: 8/3/12
 * Time: 1:08 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ItemSearchCommandService {
    public List<Item> findItems(ItemSearchCommand itemSearchCommand);
}
