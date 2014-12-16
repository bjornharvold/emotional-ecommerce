/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.domain.document;

import com.lela.domain.enums.QuizStepType;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Transient;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bjorn Harvold
 * Date: 6/27/12
 * Time: 6:17 PM
 * Responsibility:
 */
public class QuizStep implements Serializable {
    private static final long serialVersionUID = -6682931427699738182L;

    /** Order */
    @Min(1)
    @NotNull
    private Integer rdr;

    /** Question url name*/
    @NotNull
    @NotEmpty
    private String rlnm;

    /** Question name*/
    @NotNull
    @NotEmpty
    private String nm;

    /** Question url name*/
    @NotNull
    @NotEmpty
    private String srlnm;

    @NotNull
    @NotEmpty
    @Transient
    private String qrlnm;

    /** Unique id */
    private String d;

    /** Type of step this is */
    @NotNull
    private QuizStepType tp;

    private List<QuizStepEntry> ntrs;

    public QuizStep() {
    }

    public QuizStep(String qrlnm) {
        this.qrlnm = qrlnm;
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

    public String getQrlnm() {
        return qrlnm;
    }

    public void setQrlnm(String qrlnm) {
        this.qrlnm = qrlnm;
    }

    public String getSrlnm() {
        return srlnm;
    }

    public void setSrlnm(String srlnm) {
        this.srlnm = srlnm;
    }

    public List<QuizStepEntry> getNtrs() {
        return ntrs;
    }

    public void setNtrs(List<QuizStepEntry> ntrs) {
        this.ntrs = ntrs;
    }

    public QuizStepType getTp() {
        return tp;
    }

    public void setTp(QuizStepType tp) {
        this.tp = tp;
    }

    public String getD() {
        return d;
    }

    public void setD(String d) {
        this.d = d;
    }

    public String getNm() {
        return nm;
    }

    public void setNm(String nm) {
        this.nm = nm;
    }

    public void addQuizStepEntry(QuizStepEntry question) {
        if (this.ntrs == null) {
            this.ntrs = new ArrayList<QuizStepEntry>();
        }

        QuizStepEntry dupe = null;

        for (QuizStepEntry qq : ntrs) {
            if (StringUtils.equals(qq.getRlnm(), question.getRlnm())) {
                dupe = qq;
                break;
            }
        }

        // overwrite original
        if (dupe != null) {
            this.ntrs.remove(dupe);
        }

        this.ntrs.add(question);
    }
}
