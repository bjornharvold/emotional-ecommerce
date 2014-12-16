/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.commons.service;

import com.lela.domain.document.Answer;
import com.lela.domain.document.Question;
import com.lela.domain.document.Quiz;
import com.lela.domain.document.QuizStep;
import com.lela.domain.document.QuizStepEntry;
import com.lela.domain.document.StaticContent;
import com.lela.domain.dto.quiz.AnswerEntry;
import com.lela.domain.dto.quiz.QuizStaticContentRequest;
import com.lela.domain.enums.QuestionType;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Locale;

/**
 * Created by Bjorn Harvold
 * Date: 6/4/12
 * Time: 7:39 PM
 * Responsibility:
 */
public interface QuizService {
    Question saveQuestion(Question question);
    Question removeQuestion(String rlnm);
    Question findQuestionByUrlName(String urlName);
    List<Question> saveQuestions(List<Question> list);
    List<Question> findAllQuestions();

    Page<Quiz> findQuizzes(Integer page, Integer maxResults);
    List<Quiz> findQuizzes(List<String> fields);
    Quiz findQuizByUrlName(String urlName);
    Quiz saveQuiz(Quiz quiz);
    void removeQuiz(String urlName);
    void saveQuizStep(QuizStep step);
    QuizStep findQuizStep(String quizUrlName, String quizStepId);
    void removeQuizStep(String quizUrlName, String quizStepId);
    QuizStepEntry findQuizStepQuestion(String urlName, String quizStepId, String questionId);
    void removeQuizStepQuestion(String quizUrlName, String quizStepId, String questionId);
    void saveQuizStepQuestion(QuizStepEntry question);

    Page<Question> findQuestions(Integer page, Integer maxResults);
    Answer findAnswer(String urlName, String answerId);
    void saveAnswerEntry(AnswerEntry answerEntry);
    void removeAnswer(String urlName, String answerId);

    Quiz findDefaultQuiz(String language);

    Quiz findPublishedQuizByUrlName(String urlName);

    Quiz findPublishedDefaultQuiz(String language);

    StaticContent renderStaticContent(QuizStaticContentRequest request);
    
    Quiz findQuizByUrlName(String urlName, String affiliateAccountUrl, String applicationUrl);
}
