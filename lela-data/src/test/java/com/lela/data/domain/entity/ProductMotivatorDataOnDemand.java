package com.lela.data.domain.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.dod.RooDataOnDemand;

@RooDataOnDemand(entity = ProductMotivator.class)
public class ProductMotivatorDataOnDemand {

    @Autowired
    ItemDataOnDemand itemDataOnDemand;

    @Autowired
    MotivatorDataOnDemand motivatorDataOnDemand;

    public void setItem(ProductMotivator obj, int index) {
        Item item = itemDataOnDemand.getRandomItem();
        obj.setItem(item);
    }

    public void setMotivator(ProductMotivator obj, int index) {
        Motivator motivator = motivatorDataOnDemand.getSpecificMotivator(index);
        obj.setMotivator(motivator);
    }
}
