// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.domain.entity;

import com.lela.data.domain.entity.Category;
import com.lela.data.domain.entity.FunctionalFilter;
import com.lela.data.domain.entity.FunctionalFilterCategory;

privileged aspect FunctionalFilterCategory_Roo_JavaBean {
    
    public FunctionalFilter FunctionalFilterCategory.getFunctionalFilter() {
        return this.functionalFilter;
    }
    
    public void FunctionalFilterCategory.setFunctionalFilter(FunctionalFilter functionalFilter) {
        this.functionalFilter = functionalFilter;
    }
    
    public Category FunctionalFilterCategory.getCategory() {
        return this.category;
    }
    
    public void FunctionalFilterCategory.setCategory(Category category) {
        this.category = category;
    }
    
    public String FunctionalFilterCategory.getObjectId() {
        return this.objectId;
    }
    
    public void FunctionalFilterCategory.setObjectId(String objectId) {
        this.objectId = objectId;
    }
    
}
