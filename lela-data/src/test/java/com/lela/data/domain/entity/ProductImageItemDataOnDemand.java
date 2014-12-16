package com.lela.data.domain.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.dod.RooDataOnDemand;

@RooDataOnDemand(entity = ProductImageItem.class)
public class ProductImageItemDataOnDemand {

    @Autowired
    ItemDataOnDemand itemDataOnDemand;

    @Autowired
    ProductImageDataOnDemand productImageDataOnDemand;

    @Autowired
    ProductImageItemStatusDataOnDemand productImageItemStatusDataOnDemand;

    public void setItem(ProductImageItem obj, int index) {
        Item item = itemDataOnDemand.getRandomItem();
        obj.setItem(item);
    }

    public void setProductImage(ProductImageItem obj, int index) {
        ProductImage productImage = productImageDataOnDemand.getSpecificProductImage(index);
        obj.setProductImage(productImage);
    }

    public void setProductImageItemStatus(ProductImageItem obj, int index) {
        ProductImageItemStatus productImageItemStatus = productImageItemStatusDataOnDemand.getSpecificProductImageItemStatus(index);
        obj.setProductImageItemStatus(productImageItemStatus);
    }
}
