package com.lela.data.domain.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.dod.RooDataOnDemand;

@RooDataOnDemand(entity = ColorPrimaryColor.class)
public class ColorPrimaryColorDataOnDemand {
    @Autowired
    CategoryDataOnDemand categoryDataOnDemand;

    @Autowired
    ColorDataOnDemand colorDataOnDemand;

    @Autowired
    PrimaryColorDataOnDemand primaryColorDataOnDemand;

    public void setCategory(ColorPrimaryColor obj, int index) {
        Category category = categoryDataOnDemand.getSpecificCategory(index);
        obj.setCategory(category);
    }

    public void setColor(ColorPrimaryColor obj, int index) {
        Color color = colorDataOnDemand.getSpecificColor(index);
        obj.setColor(color);
    }

    public void setPrimaryColor(ColorPrimaryColor obj, int index) {
        PrimaryColor primaryColor = primaryColorDataOnDemand.getSpecificPrimaryColor(index);
        obj.setPrimaryColor(primaryColor);
    }
}
