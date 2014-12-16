package com.lela.data.domain.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.dod.RooDataOnDemand;

@RooDataOnDemand(entity = CategoryAttributeRejectionFilter.class)
public class CategoryAttributeRejectionFilterDataOnDemand {

    @Autowired
    AttributeTypeDataOnDemand attributeTypeDataOnDemand;

    @Autowired
    CategoryDataOnDemand categoryDataOnDemand;

    @Autowired
    RejectionReasonDataOnDemand rejectionReasonDataOnDemand;

    public void setAttributeType(CategoryAttributeRejectionFilter obj, int index) {
        AttributeType attributeType = attributeTypeDataOnDemand.getSpecificAttributeType(index);
        obj.setAttributeType(attributeType);
    }
    public void setCategory(CategoryAttributeRejectionFilter obj, int index) {
        Category category = categoryDataOnDemand.getSpecificCategory(index);
        obj.setCategory(category);
    }
    public void setRejectionReason(CategoryAttributeRejectionFilter obj, int index) {
        RejectionReason rejectionReason = rejectionReasonDataOnDemand.getSpecificRejectionReason(index);
        obj.setRejectionReason(rejectionReason);
    }
}
