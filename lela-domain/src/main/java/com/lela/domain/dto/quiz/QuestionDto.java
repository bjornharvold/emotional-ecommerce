/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.domain.dto.quiz;

import com.lela.domain.enums.QuestionType;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Bjorn Harvold
 * Date: 7/13/12
 * Time: 4:26 PM
 * Responsibility:
 */
public class QuestionDto implements Serializable {
    private static final long serialVersionUID = 710366874281859046L;

    private String nm;
    private String rlnm;
    private QuestionType tp;
    private List<AnswerDto> nswrs;

    public String getNm() {
        return nm;
    }

    public void setNm(String nm) {
        this.nm = nm;
    }

    public String getRlnm() {
        return rlnm;
    }

    public void setRlnm(String rlnm) {
        this.rlnm = rlnm;
    }

    public QuestionType getTp() {
        return tp;
    }

    public void setTp(QuestionType tp) {
        this.tp = tp;
    }

    public List<AnswerDto> getNswrs() {
        return nswrs;
    }

    public void setNswrs(List<AnswerDto> nswrs) {
        this.nswrs = nswrs;
    }
}
