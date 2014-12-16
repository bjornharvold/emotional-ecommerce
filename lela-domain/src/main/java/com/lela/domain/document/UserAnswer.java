/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.domain.document;

//~--- non-JDK imports --------------------------------------------------------

import java.io.Serializable;
import java.util.Date;
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
public class UserAnswer implements Serializable {

    /** Field description */
    private static final long serialVersionUID = 4209524738835826562L;

    //~--- fields -------------------------------------------------------------

    /**
     * This is a map of all answer values the user selected. It supports multiple answers.
     *  The key is the emotional key e.g. A, B, C and the value is value from 0 - 10
     */
    private Map<String, Integer> mtvtrs;

    /** Field description */
    private String ky;

    /** Image */
    private String mg;

    /** Field description */
    private String nm;

    /** UserQuestion for this answer */
    private UserQuestion qstn;

    /** Date question was answered */
    private Date dt;

    //~--- get methods --------------------------------------------------------

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

    /**
     * Method description
     *
     *
     * @return Return value
     */
    public UserQuestion getQstn() {
        return qstn;
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

    /**
     * Method description
     *
     *
     * @param qstn qstn
     */
    public void setQstn(UserQuestion qstn) {
        this.qstn = qstn;
    }

    public Date getDt() {
        return dt;
    }

    public void setDt(Date dt) {
        this.dt = dt;
    }
}
