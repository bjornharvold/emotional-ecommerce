package com.lela.data.domain.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.dod.RooDataOnDemand;

@RooDataOnDemand(entity = AttributeTypeNormalization.class)
public class AttributeTypeNormalizationDataOnDemand {
    @Autowired
    AttributeTypeDataOnDemand attributeTypeDataOnDemand;

    @Autowired
    CategoryDataOnDemand categoryDataOnDemand;

    public void setAttributeTypeIdNormalized(AttributeTypeNormalization obj, int index) {
        AttributeType attributeTypeIdNormalized = attributeTypeDataOnDemand.getSpecificAttributeType(index);
        obj.setAttributeTypeIdNormalized(attributeTypeIdNormalized);
    }

    public void setAttributeTypeIdSource(AttributeTypeNormalization obj, int index) {
        AttributeType attributeTypeIdSource = attributeTypeDataOnDemand.getSpecificAttributeType(index);
        obj.setAttributeTypeIdSource(attributeTypeIdSource);
    }

    public void setCategoryId(AttributeTypeNormalization obj, int index) {
        Category CategoryId = categoryDataOnDemand.getSpecificCategory(index);
        obj.setCategoryId(CategoryId);
    }
}
