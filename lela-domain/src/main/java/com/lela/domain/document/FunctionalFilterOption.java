/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.domain.document;

//~--- JDK imports ------------------------------------------------------------

import java.io.Serializable;
import java.util.List;

//~--- classes ----------------------------------------------------------------

/**
 * Created by Bjorn Harvold
 * Date: 8/4/11
 * Time: 12:27 PM
 * Responsibility:
 */
public class FunctionalFilterOption implements Serializable {

    /** Field description */
    private static final long serialVersionUID = -7779100917080177909L;

    //~--- fields -------------------------------------------------------------

    /** Default value */
    private Boolean slctd = false;

    /** Field description */
    private String ky;

    /** Order */
    private Integer rdr;

    /** Field description */
    private Object vl;

    /** Keys to disable if this option is selected */
    private List<String> dsbl;

    //~--- get methods --------------------------------------------------------
    public FunctionalFilterOption() {
    }

    public FunctionalFilterOption(FunctionalFilterOption ffo) {
        this.slctd = ffo.getSlctd();
        this.ky = ffo.getKy();
        this.rdr = ffo.getRdr();
        this.vl = ffo.getVl();
        this.dsbl = ffo.getDsbl();
    }

    public FunctionalFilterOption(String ky, Object vl, Integer rdr, Boolean slctd) {
        this.slctd = slctd;
        this.ky = ky;
        this.rdr = rdr;
        this.vl = vl;
    }

    /**
     * Method description
     *
     *
     * @return Return value
     */
    public Boolean getSlctd() {
        return slctd;
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
    public Object getVl() {
        return vl;
    }

    //~--- set methods --------------------------------------------------------

    /**
     * Method description
     *
     *
     * @param slctd dflt
     */
    public void setSlctd(Boolean slctd) {
        this.slctd = slctd;
    }

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
     * @param rdr rdr
     */
    public void setRdr(Integer rdr) {
        this.rdr = rdr;
    }

    /**
     * Method description
     *
     *
     * @param vl vl
     */
    public void setVl(Object vl) {
        this.vl = vl;
    }

    public List<String> getDsbl() {
        return dsbl;
    }

    public void setDsbl(List<String> dsbl) {
        this.dsbl = dsbl;
    }
}
