package com.lela.data.domain.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.dod.RooDataOnDemand;

@RooDataOnDemand(entity = ProductMotivatorError.class)
public class ProductMotivatorErrorDataOnDemand {

    @Autowired
    ItemDataOnDemand itemDataOnDemand;

    @Autowired
    MotivatorDataOnDemand motivatorDataOnDemand;

    public void setItem(ProductMotivatorError obj, int index) {
        Item item = itemDataOnDemand.getRandomItem();
        obj.setItem(item);
    }

    public void setMotivator(ProductMotivatorError obj, int index) {
        Motivator motivator = motivatorDataOnDemand.getSpecificMotivator(index);
        obj.setMotivator(motivator);
    }
}
