// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.domain.entity;

import com.lela.data.domain.entity.Brand;
import com.lela.data.domain.entity.BrandCategoryItemtypeMotivator;
import com.lela.data.domain.entity.Category;

privileged aspect BrandCategoryItemtypeMotivator_Roo_JavaBean {
    
    public Category BrandCategoryItemtypeMotivator.getCategory() {
        return this.category;
    }
    
    public void BrandCategoryItemtypeMotivator.setCategory(Category category) {
        this.category = category;
    }
    
    public Brand BrandCategoryItemtypeMotivator.getBrand() {
        return this.brand;
    }
    
    public void BrandCategoryItemtypeMotivator.setBrand(Brand brand) {
        this.brand = brand;
    }
    
    public String BrandCategoryItemtypeMotivator.getItemType() {
        return this.itemType;
    }
    
    public void BrandCategoryItemtypeMotivator.setItemType(String itemType) {
        this.itemType = itemType;
    }
    
    public Integer BrandCategoryItemtypeMotivator.getMotivator() {
        return this.motivator;
    }
    
    public void BrandCategoryItemtypeMotivator.setMotivator(Integer motivator) {
        this.motivator = motivator;
    }
    
    public Integer BrandCategoryItemtypeMotivator.getMotivatorScore() {
        return this.motivatorScore;
    }
    
    public void BrandCategoryItemtypeMotivator.setMotivatorScore(Integer motivatorScore) {
        this.motivatorScore = motivatorScore;
    }
    
}