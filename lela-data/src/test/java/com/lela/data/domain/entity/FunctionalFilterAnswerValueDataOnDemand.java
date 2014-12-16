package com.lela.data.domain.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.dod.RooDataOnDemand;

@RooDataOnDemand(entity = FunctionalFilterAnswerValue.class)
public class FunctionalFilterAnswerValueDataOnDemand {

    @Autowired
    FunctionalFilterAnswerDataOnDemand functionalFilterAnswerDataOnDemand;

    @Autowired
    AttributeTypeDataOnDemand attributeTypeDataOnDemand;

    public void setFunctionalFilterAnswer(FunctionalFilterAnswerValue obj, int index) {
        FunctionalFilterAnswer functionalFilterAnswer = functionalFilterAnswerDataOnDemand.getRandomFunctionalFilterAnswer();
        obj.setFunctionalFilterAnswer(functionalFilterAnswer);
    }

    public void setAttributeType(FunctionalFilterAnswerValue obj, int index) {
        AttributeType attributeType = attributeTypeDataOnDemand.getRandomAttributeType();
        obj.setAttributeType(attributeType);
    }
}
