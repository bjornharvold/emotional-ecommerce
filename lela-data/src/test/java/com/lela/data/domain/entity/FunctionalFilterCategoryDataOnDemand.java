package com.lela.data.domain.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.dod.RooDataOnDemand;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@RooDataOnDemand(entity = FunctionalFilterCategory.class)
public class FunctionalFilterCategoryDataOnDemand {

    @Autowired
    FunctionalFilterDataOnDemand functionalFilterDataOnDemand;

    @Autowired
    CategoryDataOnDemand categoryDataOnDemand;

    public void setCategory(FunctionalFilterCategory obj, int index) {
        Category category = categoryDataOnDemand.getSpecificCategory(index);
        obj.setCategory(category);
    }

    public void setFunctionalFilter(FunctionalFilterCategory obj, int index) {
        FunctionalFilter functionalFilter = functionalFilterDataOnDemand.getSpecificFunctionalFilter(index);
        obj.setFunctionalFilter(functionalFilter);
    }
}
