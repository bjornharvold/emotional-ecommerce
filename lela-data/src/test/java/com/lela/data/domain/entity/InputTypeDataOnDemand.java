package com.lela.data.domain.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.dod.RooDataOnDemand;

@RooDataOnDemand(entity = InputType.class)
public class InputTypeDataOnDemand {
    @Autowired
    private MultiValuedTypeDataOnDemand multiValuedTypeDataOnDemand;

    public InputType getNewTransientInputType(int index) {
        InputType obj = new InputType();
        setDateCreated(obj, index);
        setDateModified(obj, index);
        setInputTypeName(obj, index);
        setMultiValuedType(obj, index);
        return obj;
    }

    public void setMultiValuedType(InputType obj, int index) {
        MultiValuedType multiValuedType = multiValuedTypeDataOnDemand.getRandomMultiValuedType();
        obj.setMultiValuedType(multiValuedType);
    }
}
