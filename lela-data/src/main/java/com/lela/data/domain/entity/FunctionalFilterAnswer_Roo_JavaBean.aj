// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.domain.entity;

import com.lela.data.domain.entity.FunctionalFilter;
import com.lela.data.domain.entity.FunctionalFilterAnswer;
import com.lela.data.domain.entity.FunctionalFilterAnswerValue;
import java.util.Set;

privileged aspect FunctionalFilterAnswer_Roo_JavaBean {
    
    public FunctionalFilter FunctionalFilterAnswer.getFunctionalFilter() {
        return this.functionalFilter;
    }
    
    public void FunctionalFilterAnswer.setFunctionalFilter(FunctionalFilter functionalFilter) {
        this.functionalFilter = functionalFilter;
    }
    
    public Set<FunctionalFilterAnswerValue> FunctionalFilterAnswer.getFunctionalFilterAnswerValues() {
        return this.functionalFilterAnswerValues;
    }
    
    public void FunctionalFilterAnswer.setFunctionalFilterAnswerValues(Set<FunctionalFilterAnswerValue> functionalFilterAnswerValues) {
        this.functionalFilterAnswerValues = functionalFilterAnswerValues;
    }
    
    public Integer FunctionalFilterAnswer.getAnswer() {
        return this.answer;
    }
    
    public void FunctionalFilterAnswer.setAnswer(Integer answer) {
        this.answer = answer;
    }
    
    public String FunctionalFilterAnswer.getAnswerKey() {
        return this.answerKey;
    }
    
    public void FunctionalFilterAnswer.setAnswerKey(String answerKey) {
        this.answerKey = answerKey;
    }
    
    public String FunctionalFilterAnswer.getAnswerValue() {
        return this.answerValue;
    }
    
    public void FunctionalFilterAnswer.setAnswerValue(String answerValue) {
        this.answerValue = answerValue;
    }
    
    public Integer FunctionalFilterAnswer.getAnswerOrder() {
        return this.answerOrder;
    }
    
    public void FunctionalFilterAnswer.setAnswerOrder(Integer answerOrder) {
        this.answerOrder = answerOrder;
    }
    
    public Boolean FunctionalFilterAnswer.getDefaultt() {
        return this.defaultt;
    }
    
    public void FunctionalFilterAnswer.setDefaultt(Boolean defaultt) {
        this.defaultt = defaultt;
    }
    
    public String FunctionalFilterAnswer.getFunctionalFilterAnswerLabel() {
        return this.functionalFilterAnswerLabel;
    }
    
    public void FunctionalFilterAnswer.setFunctionalFilterAnswerLabel(String functionalFilterAnswerLabel) {
        this.functionalFilterAnswerLabel = functionalFilterAnswerLabel;
    }
    
}