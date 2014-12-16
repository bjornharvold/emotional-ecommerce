/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.domain.dto.quiz;

import com.lela.domain.dto.AbstractJSONPayload;

import java.io.Serializable;

/**
 * Created by Bjorn Harvold
 * Date: 7/13/12
 * Time: 4:28 PM
 * Responsibility:
 */
public class StaticContentDto extends AbstractJSONPayload implements Serializable {
    private static final long serialVersionUID = -186779749953127340L;

    private String rlnm;

    /** SEO friendly url name */
    private String srlnm;

    private String bdy;

    private String nm;

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

    public String getBdy() {
        return bdy;
    }

    public void setBdy(String bdy) {
        this.bdy = bdy;
    }

    public String getNm() {
        return nm;
    }

    public void setNm(String nm) {
        this.nm = nm;
    }
}
