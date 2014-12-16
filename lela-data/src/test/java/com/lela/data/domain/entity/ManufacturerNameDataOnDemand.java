package com.lela.data.domain.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.dod.RooDataOnDemand;

import java.text.SimpleDateFormat;
import java.util.Date;

@RooDataOnDemand(entity = ManufacturerName.class)
public class ManufacturerNameDataOnDemand {

    @Autowired
    LocaleDataOnDemand localeDataOnDemand;

    @Autowired
    ManufacturerDataOnDemand manufacturerDataOnDemand;

    public void setLocale(ManufacturerName obj, int index)
    {
        Locale locale = localeDataOnDemand.getSpecificLocale(index);
        obj.setLocale(locale);
    }

    public void setManufacturer(ManufacturerName obj, int index)
    {
        obj.setManufacturer(manufacturerDataOnDemand.getSpecificManufacturer(index));
    }



}
