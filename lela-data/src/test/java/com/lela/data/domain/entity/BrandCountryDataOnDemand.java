package com.lela.data.domain.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.dod.RooDataOnDemand;

@RooDataOnDemand(entity = BrandCountry.class)
public class BrandCountryDataOnDemand {
    @Autowired
    BrandDataOnDemand brandDataOnDemand;

    @Autowired
    CountryDataOnDemand countryDataOnDemand;

    public void setBrand(BrandCountry obj, int index) {
        Brand brand = brandDataOnDemand.getRandomBrand();
        obj.setBrand(brand);
    }

    public void setCountry(BrandCountry obj, int index) {
        Country country = countryDataOnDemand.getRandomCountry();
        obj.setCountry(country);
    }
}
