// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.domain.entity;

import com.lela.data.domain.entity.Item;
import com.lela.data.domain.entity.Motivator;
import com.lela.data.domain.entity.ProductMotivatorError;

privileged aspect ProductMotivatorError_Roo_JavaBean {
    
    public Item ProductMotivatorError.getItem() {
        return this.item;
    }
    
    public void ProductMotivatorError.setItem(Item item) {
        this.item = item;
    }
    
    public Motivator ProductMotivatorError.getMotivator() {
        return this.motivator;
    }
    
    public void ProductMotivatorError.setMotivator(Motivator motivator) {
        this.motivator = motivator;
    }
    
    public String ProductMotivatorError.getError() {
        return this.error;
    }
    
    public void ProductMotivatorError.setError(String error) {
        this.error = error;
    }
    
    public String ProductMotivatorError.getStackTrace() {
        return this.stackTrace;
    }
    
    public void ProductMotivatorError.setStackTrace(String stackTrace) {
        this.stackTrace = stackTrace;
    }
    
}
