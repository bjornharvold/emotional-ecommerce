/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.domain.dto.productgrid;

import org.springframework.data.mongodb.core.index.Indexed;

import java.io.Serializable;

/**
 * Created by Bjorn Harvold
 * Date: 8/26/12
 * Time: 10:54 AM
 * Responsibility:
 */
public class OwnerDto implements Serializable {
    private static final long serialVersionUID = -7551471204187665655L;

    /** Name */
    private String nm;

    /** URL name */
    private String rlnm;

    /** SEO URL name */
    private String srlnm;

    public String getNm() {
        return nm;
    }

    public void setNm(String nm) {
        this.nm = nm;
    }

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
