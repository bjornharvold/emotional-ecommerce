package com.lela.data.domain.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.dod.RooDataOnDemand;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@RooDataOnDemand(entity = AttributeTypeCategory.class)
public class AttributeTypeCategoryDataOnDemand {

    @Autowired
    AttributeTypeDataOnDemand attributeTypeDataOnDemand;

    @Autowired
    CategoryDataOnDemand categoryDataOnDemand;

    @Autowired
    InputTypeDataOnDemand inputTypeDataOnDemand;

    @Autowired
    InputValidationListDataOnDemand inputValidationListDataOnDemand;

    public void setAttributeType(AttributeTypeCategory obj, int index) {
        AttributeType attributeType = attributeTypeDataOnDemand.getSpecificAttributeType(index);
        obj.setAttributeType(attributeType);
    }

    public void setCategory(AttributeTypeCategory obj, int index) {
        Category category = categoryDataOnDemand.getSpecificCategory(index);
        obj.setCategory(category);
    }

    public void setInputType(AttributeTypeCategory obj, int index) {
        InputType inputType = inputTypeDataOnDemand.getRandomInputType();
        obj.setInputType(inputType);
    }

    public void setInputValidationList(AttributeTypeCategory obj, int index) {
        InputValidationList inputValidationList = inputValidationListDataOnDemand.getRandomInputValidationList();
        obj.setInputValidationList(inputValidationList);
    }

}
