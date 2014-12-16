/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.domain.document;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Transient;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created by Bjorn Harvold
 * Date: 6/28/12
 * Time: 7:01 PM
 * Responsibility:
 */
public class QuizStepEntry implements Serializable {
    private static final long serialVersionUID = 8627251774613010738L;

    /** Order */
    @Min(1)
    @NotNull
    private Integer rdr;

    /** Question url name */
    @NotNull
    @NotEmpty
    private String rlnm;

    @Transient
    private Question question;

    @Transient
    private StaticContent staticContent;

    @NotNull
    @NotEmpty
    @Transient
    private String qrlnm;

    @NotNull
    @NotEmpty
    @Transient
    private String qd;

    private String d;

    public QuizStepEntry() {
    }

    public QuizStepEntry(String qrlnm, String qd) {
        this.qrlnm = qrlnm;
        this.qd = qd;
    }

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

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public String getQrlnm() {
        return qrlnm;
    }

    public void setQrlnm(String qrlnm) {
        this.qrlnm = qrlnm;
    }

    public String getQd() {
        return qd;
    }

    public void setQd(String qd) {
        this.qd = qd;
    }

    public String getD() {
        return d;
    }

    public void setD(String d) {
        this.d = d;
    }

    public StaticContent getStaticContent() {
        return staticContent;
    }

    public void setStaticContent(StaticContent staticContent) {
        this.staticContent = staticContent;
    }
}
