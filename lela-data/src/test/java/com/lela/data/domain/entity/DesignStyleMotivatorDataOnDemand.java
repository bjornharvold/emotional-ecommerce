package com.lela.data.domain.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.dod.RooDataOnDemand;

@RooDataOnDemand(entity = DesignStyleMotivator.class)
public class DesignStyleMotivatorDataOnDemand {
    @Autowired
    CategoryDataOnDemand categoryDataOnDemand;

    public void setCategory(DesignStyleMotivator obj, int index) {
        Category category = categoryDataOnDemand.getRandomCategory();
        obj.setCategory(category);
    }
}
