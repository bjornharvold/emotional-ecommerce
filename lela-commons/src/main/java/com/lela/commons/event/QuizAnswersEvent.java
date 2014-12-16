/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */
package com.lela.commons.event;

import com.lela.domain.document.AffiliateAccount;
import com.lela.domain.document.Application;
import com.lela.domain.document.User;
import com.lela.domain.dto.quiz.QuizAnswers;

import java.util.List;

/**
 * User: Chris Tallent
 * Date: 10/29/12
 * Time: 2:48 PM
 */
public class QuizAnswersEvent extends AbstractUserEvent {
    private final QuizAnswers answers;

    public QuizAnswersEvent(User user, QuizAnswers answers) {
        super(user);
        this.answers = answers;
    }

    public QuizAnswers getAnswers() {
        return answers;
    }
}
