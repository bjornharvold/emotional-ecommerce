// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.domain.entity;

import com.lela.data.domain.entity.Category;
import com.lela.data.domain.entity.ProductDetailGroup;
import com.lela.data.domain.entity.ProductDetailSection;
import java.util.Set;

privileged aspect ProductDetailSection_Roo_JavaBean {
    
    public Set<ProductDetailGroup> ProductDetailSection.getProductDetailGroups() {
        return this.productDetailGroups;
    }
    
    public void ProductDetailSection.setProductDetailGroups(Set<ProductDetailGroup> productDetailGroups) {
        this.productDetailGroups = productDetailGroups;
    }
    
    public Category ProductDetailSection.getCategory() {
        return this.category;
    }
    
    public void ProductDetailSection.setCategory(Category category) {
        this.category = category;
    }
    
    public String ProductDetailSection.getSectionName() {
        return this.sectionName;
    }
    
    public void ProductDetailSection.setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }
    
    public Integer ProductDetailSection.getSectionOrder() {
        return this.sectionOrder;
    }
    
    public void ProductDetailSection.setSectionOrder(Integer sectionOrder) {
        this.sectionOrder = sectionOrder;
    }
    
    public Boolean ProductDetailSection.getDirty() {
        return this.dirty;
    }
    
    public void ProductDetailSection.setDirty(Boolean dirty) {
        this.dirty = dirty;
    }
    
    public String ProductDetailSection.getObjectId() {
        return this.objectId;
    }
    
    public void ProductDetailSection.setObjectId(String objectId) {
        this.objectId = objectId;
    }
    
    public String ProductDetailSection.getProductDetailSectionLabel() {
        return this.productDetailSectionLabel;
    }
    
    public void ProductDetailSection.setProductDetailSectionLabel(String productDetailSectionLabel) {
        this.productDetailSectionLabel = productDetailSectionLabel;
    }
    
}
