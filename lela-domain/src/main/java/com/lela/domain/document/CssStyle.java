/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */
package com.lela.domain.document;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

/**
 * User: Chris Tallent
 * Date: 12/3/12
 * Time: 4:20 PM
 */
@Document
public class CssStyle extends AbstractDocument implements Serializable {
    private static final long serialVersionUID = -6766481131453200861L;
    /**
     * CSS Key used to reference this Style in domain definitions
     */
    @Indexed
    private String rlnm;

    /**
     * Readable Name
     */
    private String nm;

    /**
     * Description of style (Optional)
     */
    private String dsc;

    /**
     * CSS Selector
     */
    private String slctr;

    /**
     * Default value (Optional)
     */
    private String dflt;

    public String getRlnm() {
        return rlnm;
    }

    public void setRlnm(String rlnm) {
        this.rlnm = rlnm;
    }

    public String getNm() {
        return nm;
    }

    public void setNm(String nm) {
        this.nm = nm;
    }

    public String getDsc() {
        return dsc;
    }

    public void setDsc(String dsc) {
        this.dsc = dsc;
    }

    public String getSlctr() {
        return slctr;
    }

    public void setSlctr(String slctr) {
        this.slctr = slctr;
    }

    public String getDflt() {
        return dflt;
    }

    public void setDflt(String dflt) {
        this.dflt = dflt;
    }
}
