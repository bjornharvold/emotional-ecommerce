package com.lela.data.domain.dto;

import java.util.List;
import com.lela.data.domain.entity.Item;

/**
 * Created with IntelliJ IDEA.
 * User: ballmw
 * Date: 8/2/12
 * Time: 5:14 PM
 * To change this template use File | Settings | File Templates.
 */
public class ItemSearchCommand {

    Long categoryId;
    String itemStatus[];

    public ItemSearchCommand(){}

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String[] getItemStatus() {
        return itemStatus;
    }

    public void setItemStatus(String[] itemStatus) {
        this.itemStatus = itemStatus;
    }
}
