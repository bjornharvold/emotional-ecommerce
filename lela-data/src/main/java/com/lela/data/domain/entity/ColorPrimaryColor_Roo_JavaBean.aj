// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.domain.entity;

import com.lela.data.domain.entity.Category;
import com.lela.data.domain.entity.Color;
import com.lela.data.domain.entity.ColorPrimaryColor;
import com.lela.data.domain.entity.PrimaryColor;

privileged aspect ColorPrimaryColor_Roo_JavaBean {
    
    public Category ColorPrimaryColor.getCategory() {
        return this.category;
    }
    
    public void ColorPrimaryColor.setCategory(Category category) {
        this.category = category;
    }
    
    public Color ColorPrimaryColor.getColor() {
        return this.color;
    }
    
    public void ColorPrimaryColor.setColor(Color color) {
        this.color = color;
    }
    
    public PrimaryColor ColorPrimaryColor.getPrimaryColor() {
        return this.primaryColor;
    }
    
    public void ColorPrimaryColor.setPrimaryColor(PrimaryColor primaryColor) {
        this.primaryColor = primaryColor;
    }
    
}