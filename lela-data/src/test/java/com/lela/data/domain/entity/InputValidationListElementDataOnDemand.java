package com.lela.data.domain.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.dod.RooDataOnDemand;

@RooDataOnDemand(entity = InputValidationListElement.class)
public class InputValidationListElementDataOnDemand {
    @Autowired
    InputValidationListDataOnDemand inputValidationListDataOnDemand;

    public void setInputValidationList(InputValidationListElement obj, int index) {
        InputValidationList inputValidationList = inputValidationListDataOnDemand.getRandomInputValidationList();
        obj.setInputValidationList(inputValidationList);
    }

}
