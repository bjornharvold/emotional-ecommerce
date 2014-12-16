/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.domain.dto.quiz;

import com.lela.domain.dto.AbstractApiMetricsDto;
import com.lela.domain.dto.AbstractJSONPayload;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Bjorn Harvold
 * Date: 7/13/12
 * Time: 3:27 PM
 * Responsibility:
 */
public class QuizDto extends AbstractApiMetricsDto implements Serializable {
    private static final long serialVersionUID = -969195564148467479L;

    /**
     * Name
     */
    private String nm;

    /**
     * Url name
     */
    private String rlnm;

    /**
     * Seo url name
     */
    private String srlnm;

    /**
     * Default theme that may be overriden by the host site
     */
    private String thm;

    /**
     * Optional URL to redirect to when the quiz is completed
     */
    private String cmpltrl;

    /**
     * Optional static content displayed when quiz is completed
     */
    private String cmpltcntnt;

    /**
     * Optional static content displayed when returning to a completed quiz
     */
    private String rtrncntnt;

    /** Quiz steps */
    private List<QuizStepDto> stps;

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

    public String getSrlnm() {
        return srlnm;
    }

    public void setSrlnm(String srlnm) {
        this.srlnm = srlnm;
    }

    public String getThm() {
        return thm;
    }

    public void setThm(String thm) {
        this.thm = thm;
    }

    public String getCmpltrl() {
        return cmpltrl;
    }

    public void setCmpltrl(String cmpltrl) {
        this.cmpltrl = cmpltrl;
    }

    public String getCmpltcntnt() {
        return cmpltcntnt;
    }

    public void setCmpltcntnt(String cmpltcntnt) {
        this.cmpltcntnt = cmpltcntnt;
    }

    public String getRtrncntnt() {
        return rtrncntnt;
    }

    public void setRtrncntnt(String rtrncntnt) {
        this.rtrncntnt = rtrncntnt;
    }

    public List<QuizStepDto> getStps() {
        return stps;
    }

    public void setStps(List<QuizStepDto> stps) {
        this.stps = stps;
    }
}
