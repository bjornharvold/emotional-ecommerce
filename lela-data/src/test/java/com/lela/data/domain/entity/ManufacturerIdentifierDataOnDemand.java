package com.lela.data.domain.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.dod.RooDataOnDemand;

@RooDataOnDemand(entity = ManufacturerIdentifier.class)
public class ManufacturerIdentifierDataOnDemand {

    @Autowired
    IdentifierTypeDataOnDemand identifierTypeDataOnDemand;

    @Autowired
    ManufacturerDataOnDemand manufacturerDataOnDemand;

    public void setIdentifierType(ManufacturerIdentifier obj, int index) {
        IdentifierType identifierType = identifierTypeDataOnDemand.getRandomIdentifierType();
        obj.setIdentifierType(identifierType);
    }

    public void setManufacturer(ManufacturerIdentifier obj, int index) {
        Manufacturer manufacturer = manufacturerDataOnDemand.getRandomManufacturer();
        obj.setManufacturer(manufacturer);
    }
}
