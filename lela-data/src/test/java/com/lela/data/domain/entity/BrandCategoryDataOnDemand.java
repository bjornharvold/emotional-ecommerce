package com.lela.data.domain.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.dod.RooDataOnDemand;

@RooDataOnDemand(entity = BrandCategory.class)
public class BrandCategoryDataOnDemand {
    @Autowired
    BrandDataOnDemand brandDataOnDemand;

    @Autowired
    CategoryDataOnDemand categoryDataOnDemand;

    public void setBrand(BrandCategory obj, int index) {
        Brand brand = brandDataOnDemand.getSpecificBrand(index);
        obj.setBrand(brand);
    }

    public void setCategory(BrandCategory obj, int index) {
        Category category = categoryDataOnDemand.getSpecificCategory(index);
        obj.setCategory(category);
    }

}
