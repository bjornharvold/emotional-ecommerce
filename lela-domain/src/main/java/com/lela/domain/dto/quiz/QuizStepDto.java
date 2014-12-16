/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.domain.dto.quiz;

import com.lela.domain.document.QuizStepEntry;
import com.lela.domain.enums.QuizStepType;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Bjorn Harvold
 * Date: 7/13/12
 * Time: 3:32 PM
 * Responsibility:
 */
public class QuizStepDto implements Serializable {
    private static final long serialVersionUID = -6959391017295060663L;

    private Integer rdr;

    /** Question url name*/
    private String rlnm;

    /** Question name*/
    private String nm;

    /** Question url name*/
    private String srlnm;

    private QuizStepType tp;

    private List<QuizStepEntryDto> ntrs;

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

    public String getNm() {
        return nm;
    }

    public void setNm(String nm) {
        this.nm = nm;
    }

    public String getSrlnm() {
        return srlnm;
    }

    public void setSrlnm(String srlnm) {
        this.srlnm = srlnm;
    }

    public QuizStepType getTp() {
        return tp;
    }

    public void setTp(QuizStepType tp) {
        this.tp = tp;
    }

    public List<QuizStepEntryDto> getNtrs() {
        return ntrs;
    }

    public void setNtrs(List<QuizStepEntryDto> ntrs) {
        this.ntrs = ntrs;
    }
}
