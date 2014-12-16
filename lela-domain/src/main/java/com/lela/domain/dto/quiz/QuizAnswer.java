/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.domain.dto.quiz;

import com.lela.domain.dto.AbstractJSONPayload;

import java.io.Serializable;

/**
 * Created by Bjorn Harvold
 * Date: 8/15/11
 * Time: 3:23 PM
 * Responsibility:
 */
public final class QuizAnswer extends AbstractJSONPayload implements Serializable {
    private static final long serialVersionUID = 3916706993084521614L;

    private String questionUrlName;
    private String answerKey;

    public String getQuestionUrlName() {
        return questionUrlName;
    }

    public void setQuestionUrlName(String questionUrlName) {
        this.questionUrlName = questionUrlName;
    }

    public String getAnswerKey() {
        return answerKey;
    }

    public void setAnswerKey(String answerKey) {
        this.answerKey = answerKey;
    }
}
