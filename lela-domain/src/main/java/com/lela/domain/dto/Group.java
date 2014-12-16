/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.domain.dto;

//~--- non-JDK imports --------------------------------------------------------

import com.lela.domain.document.Attribute;

import java.util.ArrayList;
import java.util.List;

//~--- JDK imports ------------------------------------------------------------

//~--- classes ----------------------------------------------------------------

/**
 * Created by Bjorn Harvold
 * Date: 9/7/11
 * Time: 2:17 PM
 * Responsibility:
 */
public final class Group {

    /** Field description */
    private List<Attribute> attrs = new ArrayList<Attribute>();

    /** Field description */
    private String nm;

    /** Field description */
    private Integer rdr;

    //~--- get methods --------------------------------------------------------

    /**
     * Method description
     *
     *
     * @return Return value
     */
    public List<Attribute> getAttrs() {
        return attrs;
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
    public Integer getRdr() {
        return rdr;
    }

    //~--- set methods --------------------------------------------------------

    /**
     * Method description
     *
     *
     * @param attrs attrs
     */
    public void setAttrs(List<Attribute> attrs) {
        this.attrs = attrs;
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
     * @param rdr rdr
     */
    public void setRdr(Integer rdr) {
        this.rdr = rdr;
    }
}
