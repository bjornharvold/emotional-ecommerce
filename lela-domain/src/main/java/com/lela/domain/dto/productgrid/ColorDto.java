/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */
package com.lela.domain.dto.productgrid;

import java.io.Serializable;
import java.util.Map;

/**
 * User: Chris Tallent
 * Date: 9/6/12
 * Time: 9:44 AM
 */
public class ColorDto implements Serializable {
    private static final long serialVersionUID = 8741969086279961797L;

    //~--- fields -------------------------------------------------------------

    /** Field description */
    private String hx;

    /** Name */
    private String nm;

    /** Sizes */
    private Map<String, String> sz;

    /** Preferred */
    private Boolean prfrrd;

    //~--- get methods --------------------------------------------------------

    /**
     * Method description
     *
     *
     * @return Return value
     */
    public String getHx() {
        return hx;
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
    public Map<String, String> getSz() {
        return sz;
    }

    //~--- set methods --------------------------------------------------------

    /**
     * Method description
     *
     *
     * @param hx hex
     */
    public void setHx(String hx) {
        this.hx = hx;
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
     * @param sz sz
     */
    public void setSz(Map<String, String> sz) {
        this.sz = sz;
    }

    public Boolean getPrfrrd() {
        return prfrrd;
    }

    public void setPrfrrd(Boolean prfrrd) {
        this.prfrrd = prfrrd;
    }

}
