// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.domain.entity;

import com.lela.data.domain.entity.AttributeType;
import com.lela.data.domain.entity.Item;
import com.lela.data.domain.entity.ItemAttributeHistory;

privileged aspect ItemAttributeHistory_Roo_JavaBean {
    
    public Item ItemAttributeHistory.getItem() {
        return this.item;
    }
    
    public void ItemAttributeHistory.setItem(Item item) {
        this.item = item;
    }
    
    public AttributeType ItemAttributeHistory.getAttributeType() {
        return this.attributeType;
    }
    
    public void ItemAttributeHistory.setAttributeType(AttributeType attributeType) {
        this.attributeType = attributeType;
    }
    
    public Integer ItemAttributeHistory.getAttributeSequence() {
        return this.attributeSequence;
    }
    
    public void ItemAttributeHistory.setAttributeSequence(Integer attributeSequence) {
        this.attributeSequence = attributeSequence;
    }
    
    public String ItemAttributeHistory.getOldAttributeValue() {
        return this.oldAttributeValue;
    }
    
    public void ItemAttributeHistory.setOldAttributeValue(String oldAttributeValue) {
        this.oldAttributeValue = oldAttributeValue;
    }
    
    public String ItemAttributeHistory.getNewAttributeValue() {
        return this.newAttributeValue;
    }
    
    public void ItemAttributeHistory.setNewAttributeValue(String newAttributeValue) {
        this.newAttributeValue = newAttributeValue;
    }
    
}
