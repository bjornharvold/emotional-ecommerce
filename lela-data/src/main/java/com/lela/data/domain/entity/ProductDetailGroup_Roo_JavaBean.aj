// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.domain.entity;

import com.lela.data.domain.entity.ProductDetailGroup;
import com.lela.data.domain.entity.ProductDetailGroupAttribute;
import com.lela.data.domain.entity.ProductDetailSection;
import java.util.Set;

privileged aspect ProductDetailGroup_Roo_JavaBean {
    
    public ProductDetailSection ProductDetailGroup.getSection() {
        return this.section;
    }
    
    public void ProductDetailGroup.setSection(ProductDetailSection section) {
        this.section = section;
    }
    
    public Set<ProductDetailGroupAttribute> ProductDetailGroup.getProductDetailGroupAttributes() {
        return this.productDetailGroupAttributes;
    }
    
    public void ProductDetailGroup.setProductDetailGroupAttributes(Set<ProductDetailGroupAttribute> productDetailGroupAttributes) {
        this.productDetailGroupAttributes = productDetailGroupAttributes;
    }
    
    public Integer ProductDetailGroup.getGroupId() {
        return this.groupId;
    }
    
    public void ProductDetailGroup.setGroupId(Integer groupId) {
        this.groupId = groupId;
    }
    
    public String ProductDetailGroup.getGroupName() {
        return this.groupName;
    }
    
    public void ProductDetailGroup.setGroupName(String groupName) {
        this.groupName = groupName;
    }
    
    public Integer ProductDetailGroup.getGroupOrder() {
        return this.groupOrder;
    }
    
    public void ProductDetailGroup.setGroupOrder(Integer groupOrder) {
        this.groupOrder = groupOrder;
    }
    
    public String ProductDetailGroup.getProductDetailGroupLabel() {
        return this.productDetailGroupLabel;
    }
    
    public void ProductDetailGroup.setProductDetailGroupLabel(String productDetailGroupLabel) {
        this.productDetailGroupLabel = productDetailGroupLabel;
    }
    
}
