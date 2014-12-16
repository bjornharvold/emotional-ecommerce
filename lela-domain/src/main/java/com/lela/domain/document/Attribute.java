/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.domain.document;

//~--- JDK imports ------------------------------------------------------------

import java.io.Serializable;

//~--- classes ----------------------------------------------------------------

/**
 * Created by Bjorn Harvold
 * Date: 6/13/11
 * Time: 2:45 PM
 * Responsibility:
 */
public class Attribute implements Serializable {

    /** Field description */
    private static final long serialVersionUID = -1973607969135664131L;

    //~--- fields -------------------------------------------------------------

    /** group name */
    private String grp;

    /** Group order */
    private Integer grprdr;

    /** Key */
    private String ky;

    /** Attribute order */
    private Integer rdr;

    /** Section */
    private String sctn;

    /** Section order */
    private Integer sctnrdr;

    /** Value */
    private Object vl;

    //~--- constructors -------------------------------------------------------

    /**
     * Constructs ...
     *
     */
    public Attribute() {}

    /**
     * Constructs ...
     *
     *
     * @param key key
     * @param val val
     */
    public Attribute(String key, Object val) {
        this.ky = key;
        this.vl = val;
    }

    /**
     * Constructs ...
     *
     *
     * @param grp grp
     * @param grprdr grprdr
     * @param rdr rdr
     * @param ky ky
     * @param vl vl
     */
    public Attribute(String grp, Integer grprdr, Integer rdr, String ky, Object vl) {
        this.grp    = grp;
        this.grprdr = grprdr;
        this.ky     = ky;
        this.rdr    = rdr;
        this.vl     = vl;
    }

    /**
     * Constructs ...
     *
     *
     * @param sctn sctn
     * @param sctnrdr sctnrdr
     * @param grp grp
     * @param grprdr grprdr
     * @param rdr rdr
     * @param ky ky
     * @param vl vl
     */
    public Attribute(String sctn, Integer sctnrdr, String grp, Integer grprdr, Integer rdr, String ky, Object vl) {
        this.sctnrdr = sctnrdr;
        this.sctn   = sctn;
        this.grp    = grp;
        this.grprdr = grprdr;
        this.ky     = ky;
        this.rdr    = rdr;
        this.vl     = vl;
    }

    //~--- get methods --------------------------------------------------------

    /**
     * Method description
     *
     *
     * @return Return value
     */
    public String getGrp() {
        return grp;
    }

    /**
     * Method description
     *
     *
     * @return Return value
     */
    public Integer getGrprdr() {
        return grprdr;
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
    public Integer getRdr() {
        return rdr;
    }

    /**
     * Method description
     *
     *
     * @return Return value
     */
    public String getSctn() {
        return sctn;
    }

    /**
     * Method description
     *
     *
     * @return Return value
     */
    public Integer getSctnrdr() {
        return sctnrdr;
    }

    /**
     * Method description
     *
     *
     * @return Return value
     */
    public Object getVl() {
        return vl;
    }

    //~--- set methods --------------------------------------------------------

    /**
     * Method description
     *
     *
     * @param grp grp
     */
    public void setGrp(String grp) {
        this.grp = grp;
    }

    /**
     * Method description
     *
     *
     * @param grprdr grprdr
     */
    public void setGrprdr(Integer grprdr) {
        this.grprdr = grprdr;
    }

    /**
     * Method description
     *
     *
     * @param ky key
     */
    public void setKy(String ky) {
        this.ky = ky;
    }

    /**
     * Method description
     *
     *
     * @param rdr rdr
     */
    public void setRdr(Integer rdr) {
        this.rdr = rdr;
    }

    /**
     * Method description
     *
     *
     * @param sctn sctn
     */
    public void setSctn(String sctn) {
        this.sctn = sctn;
    }

    /**
     * Method description
     *
     *
     * @param sctnrdr sctnrdr
     */
    public void setSctnrdr(Integer sctnrdr) {
        this.sctnrdr = sctnrdr;
    }

    /**
     * Method description
     *
     *
     * @param vl val
     */
    public void setVl(Object vl) {
        this.vl = vl;
    }
}
