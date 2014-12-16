package com.lela.data.domain.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.dod.RooDataOnDemand;

@RooDataOnDemand(entity = FunctionalFilterAnswer.class)
public class FunctionalFilterAnswerDataOnDemand {

    @Autowired
    FunctionalFilterDataOnDemand functionalFilterDataOnDemand;

    public void setFunctionalFilter(FunctionalFilterAnswer obj, int index) {
        FunctionalFilter functionalFilter = functionalFilterDataOnDemand.getRandomFunctionalFilter();
        obj.setFunctionalFilter(functionalFilter);
    }


}
