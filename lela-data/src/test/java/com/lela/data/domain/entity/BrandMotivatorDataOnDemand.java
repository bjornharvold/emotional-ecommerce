package com.lela.data.domain.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.dod.RooDataOnDemand;

@RooDataOnDemand(entity = BrandMotivator.class)
public class BrandMotivatorDataOnDemand {
    @Autowired
    BrandDataOnDemand brandDataOnDemand;

    public void setBrand(BrandMotivator obj, int index) {
        Brand brand = brandDataOnDemand.getRandomBrand();
        obj.setBrand(brand);
    }

}
