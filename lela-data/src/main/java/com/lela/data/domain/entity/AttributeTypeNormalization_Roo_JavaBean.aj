// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.domain.entity;

import com.lela.data.domain.entity.AttributeType;
import com.lela.data.domain.entity.AttributeTypeNormalization;
import com.lela.data.domain.entity.Category;

privileged aspect AttributeTypeNormalization_Roo_JavaBean {
    
    public AttributeType AttributeTypeNormalization.getAttributeTypeIdNormalized() {
        return this.attributeTypeIdNormalized;
    }
    
    public void AttributeTypeNormalization.setAttributeTypeIdNormalized(AttributeType attributeTypeIdNormalized) {
        this.attributeTypeIdNormalized = attributeTypeIdNormalized;
    }
    
    public AttributeType AttributeTypeNormalization.getAttributeTypeIdSource() {
        return this.attributeTypeIdSource;
    }
    
    public void AttributeTypeNormalization.setAttributeTypeIdSource(AttributeType attributeTypeIdSource) {
        this.attributeTypeIdSource = attributeTypeIdSource;
    }
    
    public Category AttributeTypeNormalization.getCategoryId() {
        return this.categoryId;
    }
    
    public void AttributeTypeNormalization.setCategoryId(Category categoryId) {
        this.categoryId = categoryId;
    }
    
    public Integer AttributeTypeNormalization.getAttributeTypeOrder() {
        return this.attributeTypeOrder;
    }
    
    public void AttributeTypeNormalization.setAttributeTypeOrder(Integer attributeTypeOrder) {
        this.attributeTypeOrder = attributeTypeOrder;
    }
    
}
