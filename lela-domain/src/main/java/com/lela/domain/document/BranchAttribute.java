/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.domain.document;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: Chris Tallent
 * Date: 11/3/11
 * Time: 2:49 PM
 */
public class BranchAttribute implements Serializable {

    private static final long serialVersionUID = -5972532062757072425L;

    /**
     * Key
     */
    private String ky;

    /**
     * Value
     */
    private String vl;

    public String getKy() {
        return ky;
    }

    public void setKy(String ky) {
        this.ky = ky;
    }

    public String getVl() {
        return vl;
    }

    public void setVl(String vl) {
        this.vl = vl;
    }
}
