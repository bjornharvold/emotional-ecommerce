package com.lela.data.domain.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.dod.RooDataOnDemand;

@RooDataOnDemand(entity = ProductDetailGroup.class)
public class ProductDetailGroupDataOnDemand {

    @Autowired
    ProductDetailSectionDataOnDemand productDetailSectionDataOnDemand;


    public void setSection(ProductDetailGroup obj, int index) {
        ProductDetailSection section = productDetailSectionDataOnDemand.getRandomProductDetailSection();
        obj.setSection(section);
    }

}
