package com.lela.data.domain.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.dod.RooDataOnDemand;

@RooDataOnDemand(entity = ItemAttribute.class)
public class ItemAttributeDataOnDemand {
    @Autowired
    ItemDataOnDemand itemDataOnDemand;

    @Autowired
    AttributeTypeDataOnDemand attributeTypeOnDemand;

    public void setItem(ItemAttribute obj, int index)
    {
        Item item = itemDataOnDemand.getRandomItem();
        obj.setItem(item);
    }

    public void setAttributeType(ItemAttribute obj, int index) {
        AttributeType attributeType = attributeTypeOnDemand.getRandomAttributeType();
        obj.setAttributeType(attributeType);
    }
}
