/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.commons.bootstrap.impl;

import com.lela.commons.bootstrap.Bootstrapper;
import com.lela.commons.bootstrap.BootstrapperException;
import com.lela.commons.service.QuizService;
import com.lela.domain.document.Question;
import com.lela.domain.dto.Questions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * User: bjorn
 * Date: Nov 4, 2007
 * Time: 11:19:22 AM
 * Inserts required categories into the system
 */
@SuppressWarnings("unchecked")
@Component("questionBootstrapper")
public class QuestionBootstrapper extends AbstractBootstrapper implements Bootstrapper {
    private final static Logger log = LoggerFactory.getLogger(QuestionBootstrapper.class);
    private static int populated = 0;
    private static int omitted = 0;
    private final Resource file = new ClassPathResource("bootstrap/questions.json");

    @Value("${bootstrapper.question.enabled:true}")
    private Boolean enabled;

    private final QuizService quizService;

    @Autowired
    public QuestionBootstrapper(QuizService quizService) {
        this.quizService = quizService;
    }

    @Override
    public void create() throws BootstrapperException {

        if (file.exists()) {
            processCreation();
        } else {
            throw new BootstrapperException("XML file could not be found");
        }

        log.info("Populated " + populated + " questions in db");
        log.info("Omitted " + omitted + " questions from db. Already exists.");
    }

    private void processCreation() throws BootstrapperException {
        try {

            persist(parseJSON());

        } catch (Exception e) {
            throw new BootstrapperException(e.getMessage(), e);
        }
    }

    private Questions parseJSON() throws Exception {
        return mapper.readValue(file.getInputStream(), Questions.class);
    }

    /**
     * Saves the admin users to the db before the application becomes active
     *
     * @param questions questions
     * @throws com.lela.commons.bootstrap.BootstrapperException
     *
     */
    private void persist(Questions questions) throws BootstrapperException {
        List<Question> dbList = new ArrayList<Question>();

        try {

            for (Question question : questions.getList()) {
                Question tmp = quizService.findQuestionByUrlName(question.getRlnm());

                if (tmp == null) {
                    dbList.add(question);
                    populated++;
                } else {
                    log.info("Question already exists with name: " + question.getNm());
                    omitted++;
                }
            }

            // ready for save
            if (dbList.size() > 0) {
                quizService.saveQuestions(dbList);
            }

        } catch (Exception e) {
            throw new BootstrapperException(e.getMessage(), e);
        }
    }

    @Override
    public String toString() {
        return "QuestionBootstrapper";
    }

    @Override
    public Boolean getEnabled() {
        return enabled;
    }
}