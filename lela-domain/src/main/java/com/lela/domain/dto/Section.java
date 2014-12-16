/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.domain.dto;

//~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;
import java.util.List;

//~--- classes ----------------------------------------------------------------

/**
 * Created by Bjorn Harvold
 * Date: 9/7/11
 * Time: 2:16 PM
 * Responsibility:
 */
public final class Section {

    /** Field description */
    private List<Group> grps = new ArrayList<Group>();

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
    public List<Group> getGrps() {
        return grps;
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
     * @param grps grps
     */
    public void setGrps(List<Group> grps) {
        this.grps = grps;
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
