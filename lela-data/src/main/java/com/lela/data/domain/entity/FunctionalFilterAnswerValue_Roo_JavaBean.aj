// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.domain.entity;

import com.lela.data.domain.entity.AttributeType;
import com.lela.data.domain.entity.FunctionalFilterAnswer;
import com.lela.data.domain.entity.FunctionalFilterAnswerValue;

privileged aspect FunctionalFilterAnswerValue_Roo_JavaBean {
    
    public FunctionalFilterAnswer FunctionalFilterAnswerValue.getFunctionalFilterAnswer() {
        return this.functionalFilterAnswer;
    }
    
    public void FunctionalFilterAnswerValue.setFunctionalFilterAnswer(FunctionalFilterAnswer functionalFilterAnswer) {
        this.functionalFilterAnswer = functionalFilterAnswer;
    }
    
    public AttributeType FunctionalFilterAnswerValue.getAttributeType() {
        return this.attributeType;
    }
    
    public void FunctionalFilterAnswerValue.setAttributeType(AttributeType attributeType) {
        this.attributeType = attributeType;
    }
    
    public String FunctionalFilterAnswerValue.getAttributeValue() {
        return this.attributeValue;
    }
    
    public void FunctionalFilterAnswerValue.setAttributeValue(String attributeValue) {
        this.attributeValue = attributeValue;
    }
    
    public String FunctionalFilterAnswerValue.getAnswerValue() {
        return this.answerValue;
    }
    
    public void FunctionalFilterAnswerValue.setAnswerValue(String answerValue) {
        this.answerValue = answerValue;
    }
    
    public Boolean FunctionalFilterAnswerValue.getRequiredValue() {
        return this.requiredValue;
    }
    
    public void FunctionalFilterAnswerValue.setRequiredValue(Boolean requiredValue) {
        this.requiredValue = requiredValue;
    }
    
}
