// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.domain.entity;

import com.lela.data.domain.entity.AttributeType;
import com.lela.data.domain.entity.ProductDetailAttributeType;
import com.lela.data.domain.entity.ProductDetailGroupAttribute;

privileged aspect ProductDetailAttributeType_Roo_JavaBean {
    
    public AttributeType ProductDetailAttributeType.getAttributeType() {
        return this.attributeType;
    }
    
    public void ProductDetailAttributeType.setAttributeType(AttributeType attributeType) {
        this.attributeType = attributeType;
    }
    
    public ProductDetailGroupAttribute ProductDetailAttributeType.getProductDetailGroupAttribute() {
        return this.productDetailGroupAttribute;
    }
    
    public void ProductDetailAttributeType.setProductDetailGroupAttribute(ProductDetailGroupAttribute productDetailGroupAttribute) {
        this.productDetailGroupAttribute = productDetailGroupAttribute;
    }
    
}
