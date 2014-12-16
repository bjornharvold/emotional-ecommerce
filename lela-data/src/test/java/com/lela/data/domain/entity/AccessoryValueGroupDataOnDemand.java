package com.lela.data.domain.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.dod.RooDataOnDemand;

@RooDataOnDemand(entity = AccessoryValueGroup.class)
public class AccessoryValueGroupDataOnDemand {

    @Autowired
    AccessoryGroupDataOnDemand accessoryGroupDataOnDemand;

    @Autowired
    AttributeTypeDataOnDemand attributeTypeDataOnDemand;

    @Autowired
    CategoryDataOnDemand categoryDataOnDemand;

    public void setAccessoryGroup(AccessoryValueGroup obj, int index) {
        AccessoryGroup accessoryGroup = accessoryGroupDataOnDemand.getSpecificAccessoryGroup(index);
        obj.setAccessoryGroup(accessoryGroup);
    }

    public void setAttributeType(AccessoryValueGroup obj, int index) {
        AttributeType attributeType = attributeTypeDataOnDemand.getRandomAttributeType();
        obj.setAttributeType(attributeType);
    }

    public void setCategory(AccessoryValueGroup obj, int index) {
        Category category = categoryDataOnDemand.getSpecificCategory(index);
        obj.setCategory(category);
    }
}
