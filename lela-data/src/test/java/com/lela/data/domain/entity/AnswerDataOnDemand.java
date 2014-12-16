package com.lela.data.domain.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.dod.RooDataOnDemand;
import org.springframework.stereotype.Component;

@RooDataOnDemand(entity = Answer.class)
public class AnswerDataOnDemand {

    @Autowired
    QuestionDataOnDemand questionDataOnDemand;

    public void setQuestion(Answer obj, int index) {
        Question question = questionDataOnDemand.getRandomQuestion();
        obj.setQuestion(question);
    }
}
