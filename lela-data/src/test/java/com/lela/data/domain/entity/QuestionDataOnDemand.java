package com.lela.data.domain.entity;

import org.springframework.roo.addon.dod.RooDataOnDemand;

@RooDataOnDemand(entity = Question.class)
public class QuestionDataOnDemand {
    public void setLocalization(Question obj, int index) {
        String localization = "loc" + index;
        obj.setLocalization(localization);
    }
}
