/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.domain.dto.productgrid;

import java.io.Serializable;

/**
 * Created by Bjorn Harvold
 * Date: 8/26/12
 * Time: 12:40 PM
 * Responsibility:
 */
public class AttributeDto implements Serializable {
    private static final long serialVersionUID = -8131395032144505998L;

    private String ky;

    private Object vl;

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
