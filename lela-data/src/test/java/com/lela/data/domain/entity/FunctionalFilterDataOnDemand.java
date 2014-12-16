package com.lela.data.domain.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.dod.RooDataOnDemand;

@RooDataOnDemand(entity = FunctionalFilter.class)
public class FunctionalFilterDataOnDemand {
    @Autowired
    FunctionalFilterTypeDataOnDemand functionalFilterTypeDataOnDemand;

    @Autowired
    LocaleDataOnDemand localeDataOnDemand;

    public void setFunctionalFilterType(FunctionalFilter obj, int index) {
        FunctionalFilterType functionalFilterType = functionalFilterTypeDataOnDemand.getRandomFunctionalFilterType();
        obj.setFunctionalFilterType(functionalFilterType);
    }

    public void setLocale(FunctionalFilter obj, int index) {
        Locale locale = localeDataOnDemand.getRandomLocale();
        obj.setLocale(locale);
    }

}
