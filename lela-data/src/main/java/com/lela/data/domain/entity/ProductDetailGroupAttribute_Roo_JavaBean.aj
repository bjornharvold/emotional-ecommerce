// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.domain.entity;

import com.lela.data.domain.entity.ProductDetailAttributeType;
import com.lela.data.domain.entity.ProductDetailAttributeValueType;
import com.lela.data.domain.entity.ProductDetailGroup;
import com.lela.data.domain.entity.ProductDetailGroupAttribute;
import com.lela.data.domain.entity.ProductDetailPart;
import java.util.Set;

privileged aspect ProductDetailGroupAttribute_Roo_JavaBean {
    
    public ProductDetailGroup ProductDetailGroupAttribute.getProductDetailGroup() {
        return this.productDetailGroup;
    }
    
    public void ProductDetailGroupAttribute.setProductDetailGroup(ProductDetailGroup productDetailGroup) {
        this.productDetailGroup = productDetailGroup;
    }
    
    public Set<ProductDetailAttributeType> ProductDetailGroupAttribute.getProductDetailAttributeTypes() {
        return this.productDetailAttributeTypes;
    }
    
    public void ProductDetailGroupAttribute.setProductDetailAttributeTypes(Set<ProductDetailAttributeType> productDetailAttributeTypes) {
        this.productDetailAttributeTypes = productDetailAttributeTypes;
    }
    
    public Set<ProductDetailPart> ProductDetailGroupAttribute.getProductDetailParts() {
        return this.productDetailParts;
    }
    
    public void ProductDetailGroupAttribute.setProductDetailParts(Set<ProductDetailPart> productDetailParts) {
        this.productDetailParts = productDetailParts;
    }
    
    public Integer ProductDetailGroupAttribute.getOrderInGroup() {
        return this.orderInGroup;
    }
    
    public void ProductDetailGroupAttribute.setOrderInGroup(Integer orderInGroup) {
        this.orderInGroup = orderInGroup;
    }
    
    public String ProductDetailGroupAttribute.getAttrLabel() {
        return this.attrLabel;
    }
    
    public void ProductDetailGroupAttribute.setAttrLabel(String attrLabel) {
        this.attrLabel = attrLabel;
    }
    
    public String ProductDetailGroupAttribute.getAttrName() {
        return this.attrName;
    }
    
    public void ProductDetailGroupAttribute.setAttrName(String attrName) {
        this.attrName = attrName;
    }
    
    public ProductDetailAttributeValueType ProductDetailGroupAttribute.getProductDetailAttributeValueTypeId() {
        return this.productDetailAttributeValueTypeId;
    }
    
    public void ProductDetailGroupAttribute.setProductDetailAttributeValueTypeId(ProductDetailAttributeValueType productDetailAttributeValueTypeId) {
        this.productDetailAttributeValueTypeId = productDetailAttributeValueTypeId;
    }
    
}
