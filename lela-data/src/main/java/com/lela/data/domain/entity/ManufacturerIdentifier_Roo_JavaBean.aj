// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.domain.entity;

import com.lela.data.domain.entity.IdentifierType;
import com.lela.data.domain.entity.Manufacturer;
import com.lela.data.domain.entity.ManufacturerIdentifier;

privileged aspect ManufacturerIdentifier_Roo_JavaBean {
    
    public Manufacturer ManufacturerIdentifier.getManufacturer() {
        return this.manufacturer;
    }
    
    public void ManufacturerIdentifier.setManufacturer(Manufacturer manufacturer) {
        this.manufacturer = manufacturer;
    }
    
    public IdentifierType ManufacturerIdentifier.getIdentifierType() {
        return this.identifierType;
    }
    
    public void ManufacturerIdentifier.setIdentifierType(IdentifierType identifierType) {
        this.identifierType = identifierType;
    }
    
    public String ManufacturerIdentifier.getIdentifierValue() {
        return this.identifierValue;
    }
    
    public void ManufacturerIdentifier.setIdentifierValue(String identifierValue) {
        this.identifierValue = identifierValue;
    }
    
}
