/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.domain.document;

import java.io.Serializable;

/**
 * Created by Bjorn Harvold
 * Date: 10/15/11
 * Time: 7:57 AM
 * Responsibility:
 */
public class FunctionalFilterPresetOption implements Serializable {
    private static final long serialVersionUID = 5523591486440842L;

    /** Key */
    private String ky;

    /** Value */
    private Object vl;

    public FunctionalFilterPresetOption() {
    }

    public FunctionalFilterPresetOption(String ky, Object vl) {
        this.ky = ky;
        this.vl = vl;
    }

    public String getKy() {
        return ky;
    }

    public void setKy(String ky) {
        this.ky = ky;
    }

    public Object getVl() {
        return vl;
    }

    public void setVl(Object vl) {
        this.vl = vl;
    }
}
