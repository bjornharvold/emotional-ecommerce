package com.lela.data.domain.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.dod.RooDataOnDemand;

@RooDataOnDemand(entity = BrandName.class)
public class BrandNameDataOnDemand {

    @Autowired
    BrandDataOnDemand brandDataOnDemand;

    @Autowired
    LocaleDataOnDemand localeDataOnDemand;

    public void setBrand(BrandName obj, int index) {
        Brand brand = brandDataOnDemand.getRandomBrand();
        obj.setBrand(brand);
    }

    public void setLocale(BrandName obj, int index) {
        Locale locale = localeDataOnDemand.getRandomLocale();
        obj.setLocale(locale);
    }
}
