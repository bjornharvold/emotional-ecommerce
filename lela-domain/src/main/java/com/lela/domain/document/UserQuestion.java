/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.domain.document;

//~--- non-JDK imports --------------------------------------------------------

import com.lela.domain.enums.QuestionType;

import java.io.Serializable;

//~--- JDK imports ------------------------------------------------------------

//~--- classes ----------------------------------------------------------------

/**
 * Created by Bjorn Harvold
 * Date: 7/6/11
 * Time: 12:11 PM
 * Responsibility:
 */
public class UserQuestion implements Serializable {

    /** Field description */
    private static final long serialVersionUID = -869266803095951133L;

    //~--- fields -------------------------------------------------------------

    /** Name */
    private String nm;

    /** Question ID */
    private String rlnm;

    /** Question type */
    private QuestionType tp;

    /** Quiz url name */
    private String qrlnm;

    /** Affiliate ID */
    private String ffltd;

    /** Application ID */
    private String pplctnd;

    //~--- constructors -------------------------------------------------------

    /**
     * Constructs ...
     *
     */
    public UserQuestion() {}

    /**
     * Constructs ...
     *
     *
     * @param nm nm
     * @param rlnm qstnd
     * @param tp tp
     */
    public UserQuestion(String affiliateId, String applicationId, String quizUrlName, String rlnm, String nm, QuestionType tp) {
        this.ffltd = affiliateId;
        this.pplctnd = applicationId;
        this.qrlnm = quizUrlName;
        this.nm    = nm;
        this.rlnm = rlnm;
        this.tp    = tp;
    }

    //~--- get methods --------------------------------------------------------

    /**
     * Method description
     *
     *
     * @return Return value
     */
    public String getNm() {
        return nm;
    }

    /**
     * Method description
     *
     *
     * @return Return value
     */
    public String getRlnm() {
        return rlnm;
    }

    /**
     * Method description
     *
     *
     * @return Return value
     */
    public QuestionType getTp() {
        return tp;
    }

    //~--- set methods --------------------------------------------------------

    /**
     * Method description
     *
     *
     * @param nm nm
     */
    public void setNm(String nm) {
        this.nm = nm;
    }

    /**
     * Method description
     *
     *
     * @param rlnm qstnId
     */
    public void setRlnm(String rlnm) {
        this.rlnm = rlnm;
    }

    /**
     * Method description
     *
     *
     * @param tp tp
     */
    public void setTp(QuestionType tp) {
        this.tp = tp;
    }

    public String getQrlnm() {
        return qrlnm;
    }

    public void setQrlnm(String qrlnm) {
        this.qrlnm = qrlnm;
    }

    public String getFfltd() {
        return ffltd;
    }

    public void setFfltd(String ffltd) {
        this.ffltd = ffltd;
    }

    public String getPplctnd() {
        return pplctnd;
    }

    public void setPplctnd(String pplctnd) {
        this.pplctnd = pplctnd;
    }
}
