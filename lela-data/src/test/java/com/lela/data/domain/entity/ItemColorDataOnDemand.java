package com.lela.data.domain.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.dod.RooDataOnDemand;

@RooDataOnDemand(entity = ItemColor.class)
public class ItemColorDataOnDemand {

    @Autowired
    ColorDataOnDemand colorDataOnDemand;

    @Autowired
    ItemDataOnDemand itemDataOnDemand;

    public void setColor(ItemColor obj, int index) {
        Color color = colorDataOnDemand.getSpecificColor(index);
        obj.setColor(color);
    }

    public void setItem(ItemColor obj, int index)
    {
        Item item = itemDataOnDemand.getRandomItem();
        obj.setItem(item);
    }
}
