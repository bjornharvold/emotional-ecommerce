package com.lela.data.domain.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.dod.RooDataOnDemand;

@RooDataOnDemand(entity = BrandCategoryMotivator.class)
public class BrandCategoryMotivatorDataOnDemand {

    @Autowired
    BrandDataOnDemand brandDataOnDemand;

    @Autowired
    CategoryDataOnDemand categoryDataOnDemand;

    public void setBrand(BrandCategoryMotivator obj, int index) {
        Brand brand = brandDataOnDemand.getRandomBrand();
        obj.setBrand(brand);
    }

    public void setCategory(BrandCategoryMotivator obj, int index) {
        Category category = categoryDataOnDemand.getRandomCategory();
        obj.setCategory(category);
    }
}
