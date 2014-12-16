/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.domain.document;

//~--- non-JDK imports --------------------------------------------------------

import com.lela.domain.dto.quiz.AnswerEntry;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Transient;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

//~--- JDK imports ------------------------------------------------------------

//~--- classes ----------------------------------------------------------------

/**
 * Created by Bjorn Harvold
 * Date: 7/6/11
 * Time: 11:59 AM
 * Responsibility:
 */
public class Answer implements Serializable {

    /** Field description */
    private static final long serialVersionUID = -5554526172306161536L;

    //~--- fields -------------------------------------------------------------

    /** Key */
    private String ky;

    /** Image */
    private String mg;

    /**
     * This is a map of all answer values the user selected. It supports multiple answers.
     *  The key is the emotional key e.g. A, B, C and the value is value from 0 - 10
     *  We don't ever want to expose motivator values via json so we add jsonignore for
     *  our jackson marshaller
     */
    private Map<String, Integer> mtvtrs = new ConcurrentHashMap<String, Integer>();

    /** Name */
    private String nm;

    /** unique id */
    private String d;

    /** question url name */
    @Transient
    private String qrlnm;

    //~--- get methods --------------------------------------------------------

    public Answer() {
    }

    public Answer(AnswerEntry ae) {
        this.d = ae.getD();
        this.ky = ae.getRdr().toString();
        this.mg = ae.getMg();
        this.mtvtrs = ae.getMtvtrs();
        this.nm = ae.getNm();
        this.qrlnm = ae.getQrlnm();
    }

    /**
     * Method description
     *
     *
     * @return Return value
     */
    public String getKy() {
        return ky;
    }

    /**
     * Method description
     *
     *
     * @return Return value
     */
    public String getMg() {
        return mg;
    }

    /**
     * Method description
     *
     *
     * @return Return value
     */
    public Map<String, Integer> getMtvtrs() {
        return mtvtrs;
    }

    /**
     * Method description
     *
     *
     * @return Return value
     */
    public String getNm() {
        return nm;
    }

    //~--- set methods --------------------------------------------------------

    /**
     * Method description
     *
     *
     * @param ky ky
     */
    public void setKy(String ky) {
        this.ky = ky;
    }

    /**
     * Method description
     *
     *
     * @param mg mg
     */
    public void setMg(String mg) {
        this.mg = mg;
    }

    /**
     * Method description
     *
     *
     * @param mtvtrs nswr
     */
    public void setMtvtrs(Map<String, Integer> mtvtrs) {
        this.mtvtrs = mtvtrs;
    }

    /**
     * Method description
     *
     *
     * @param nm nm
     */
    public void setNm(String nm) {
        this.nm = nm;
    }

    public String getD() {
        return d;
    }

    public void setD(String d) {
        this.d = d;
    }

    public String getQrlnm() {
        return qrlnm;
    }

    public void setQrlnm(String qrlnm) {
        this.qrlnm = qrlnm;
    }
}
