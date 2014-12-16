/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.domain.dto.quiz;

import com.lela.domain.document.Question;
import com.lela.domain.document.StaticContent;
import java.io.Serializable;

/**
 * Created by Bjorn Harvold
 * Date: 7/13/12
 * Time: 3:49 PM
 * Responsibility:
 */
public class QuizStepEntryDto implements Serializable {
    private static final long serialVersionUID = -3707248475518143573L;

    private Integer rdr;

    /** Question url name */
    private String rlnm;

    private QuestionDto question;

    private StaticContentDto staticContent;

    public Integer getRdr() {
        return rdr;
    }

    public void setRdr(Integer rdr) {
        this.rdr = rdr;
    }

    public String getRlnm() {
        return rlnm;
    }

    public void setRlnm(String rlnm) {
        this.rlnm = rlnm;
    }

    public QuestionDto getQuestion() {
        return question;
    }

    public void setQuestion(QuestionDto question) {
        this.question = question;
    }

    public StaticContentDto getStaticContent() {
        return staticContent;
    }

    public void setStaticContent(StaticContentDto staticContent) {
        this.staticContent = staticContent;
    }
}
