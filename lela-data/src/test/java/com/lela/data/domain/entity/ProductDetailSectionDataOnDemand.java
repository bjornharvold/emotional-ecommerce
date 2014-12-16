package com.lela.data.domain.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.dod.RooDataOnDemand;

@RooDataOnDemand(entity = ProductDetailSection.class)
public class ProductDetailSectionDataOnDemand {
    @Autowired
    CategoryDataOnDemand categoryDataOnDemand;

    public void setCategory(ProductDetailSection obj, int index) {
        Category category = categoryDataOnDemand.getRandomCategory();
        obj.setCategory(category);
    }
}
