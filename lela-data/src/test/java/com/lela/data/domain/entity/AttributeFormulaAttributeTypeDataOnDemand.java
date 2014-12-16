package com.lela.data.domain.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.dod.RooDataOnDemand;

@RooDataOnDemand(entity = AttributeFormulaAttributeType.class)
public class AttributeFormulaAttributeTypeDataOnDemand {

    @Autowired
    AttributeFormulaDataOnDemand attributeFormulaDataOnDemand;

    @Autowired
    AttributeTypeDataOnDemand attributeTypeDataOnDemand;

    public void setAttributeFormula(AttributeFormulaAttributeType obj, int index) {
        AttributeFormula attributeFormula = attributeFormulaDataOnDemand.getSpecificAttributeFormula(index);
        obj.setAttributeFormula(attributeFormula);
    }

    public void setAttributeType(AttributeFormulaAttributeType obj, int index) {
        AttributeType attributeType = attributeTypeDataOnDemand.getSpecificAttributeType(index);
        obj.setAttributeType(attributeType);
    }
}
