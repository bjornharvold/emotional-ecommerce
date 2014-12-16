package com.lela.data.domain.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.dod.RooDataOnDemand;

@RooDataOnDemand(entity = ItemIdentifier.class)
public class ItemIdentifierDataOnDemand {

    @Autowired
    ItemDataOnDemand itemDataOnDemand;

    @Autowired
    IdentifierTypeDataOnDemand identifierTypeDataOnDemand;

    public void setIdentifierType(ItemIdentifier obj, int index) {
        IdentifierType identifierType = identifierTypeDataOnDemand.getRandomIdentifierType();
        obj.setIdentifierType(identifierType);
    }

    public void setItem(ItemIdentifier obj, int index) {
        Item item = itemDataOnDemand.getRandomItem();
        obj.setItem(item);
    }
}
