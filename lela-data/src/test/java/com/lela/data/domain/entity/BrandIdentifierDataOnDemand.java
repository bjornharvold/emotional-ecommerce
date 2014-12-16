package com.lela.data.domain.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.dod.RooDataOnDemand;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@RooDataOnDemand(entity = BrandIdentifier.class)
public class BrandIdentifierDataOnDemand {
    @Autowired
    BrandDataOnDemand brandDataOnDemand;

    @Autowired
    IdentifierTypeDataOnDemand identifierTypeDataOnDemand;

    public void setBrand(BrandIdentifier obj, int index) {
        Brand brand = brandDataOnDemand.getRandomBrand();
        obj.setBrand(brand);
    }

    public void setIdentifierType(BrandIdentifier obj, int index) {
        IdentifierType identifierType = identifierTypeDataOnDemand.getSpecificIdentifierType(index);
        obj.setIdentifierType(identifierType);
    }
}
