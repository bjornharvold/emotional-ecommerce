/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.domain.dto.productgrid;

import java.io.Serializable;

/**
 * Created by Bjorn Harvold
 * Date: 8/26/12
 * Time: 1:33 PM
 * Responsibility:
 */
public class CategoryDto implements Serializable {
    private static final long serialVersionUID = -1475192644309485903L;

    private String rlnm;
    private String srlnm;

    public String getRlnm() {
        return rlnm;
    }

    public void setRlnm(String rlnm) {
        this.rlnm = rlnm;
    }

    public String getSrlnm() {
        return srlnm;
    }

    public void setSrlnm(String srlnm) {
        this.srlnm = srlnm;
    }
}
