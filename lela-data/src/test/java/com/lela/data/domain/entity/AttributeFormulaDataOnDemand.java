package com.lela.data.domain.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.dod.RooDataOnDemand;

@RooDataOnDemand(entity = AttributeFormula.class)
public class AttributeFormulaDataOnDemand {

    @Autowired
    AttributeTypeDataOnDemand attributeTypeDataOnDemand;

    @Autowired
    CategoryDataOnDemand categoryDataOnDemand;

    public void setAttributeType(AttributeFormula obj, int index) {
        AttributeType attributeType = attributeTypeDataOnDemand.getSpecificAttributeType(index);
        obj.setAttributeType(attributeType);
    }

    public void setCategory(AttributeFormula obj, int index) {
        Category category = categoryDataOnDemand.getSpecificCategory(index);
        obj.setCategory(category);
    }
}
