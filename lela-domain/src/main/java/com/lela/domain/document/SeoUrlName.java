/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.domain.document;

import com.lela.domain.enums.SeoUrlNameType;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created by Bjorn Harvold
 * Date: 4/13/12
 * Time: 11:12 PM
 * Responsibility:
 */
@Document
public class SeoUrlName extends AbstractDocument implements Serializable {
    private static final long serialVersionUID = 9105245389832935441L;

    /** Title */
    @NotNull
    @NotEmpty
    private String nm;

    /** Description */
    @NotNull
    @NotEmpty
    private String dsc;

    /** Header */
    @NotNull
    @NotEmpty
    private String hdr;

    /** Intro */
    @NotNull
    @NotEmpty
    private String ntr;

    /** Seo Url name */
    @NotNull
    @NotEmpty
    private String srlnm;

    /** Type */
    @NotNull
    private SeoUrlNameType tp;

    public String getNm() {
        return nm;
    }

    public void setNm(String nm) {
        this.nm = nm;
    }

    public String getSrlnm() {
        return srlnm;
    }

    public void setSrlnm(String srlnm) {
        this.srlnm = srlnm;
    }

    public SeoUrlNameType getTp() {
        return tp;
    }

    public void setTp(SeoUrlNameType tp) {
        this.tp = tp;
    }

    public String getDsc() {
        return dsc;
    }

    public void setDsc(String dsc) {
        this.dsc = dsc;
    }

    public String getHdr() {
        return hdr;
    }

    public void setHdr(String hdr) {
        this.hdr = hdr;
    }

    public String getNtr() {
        return ntr;
    }

    public void setNtr(String ntr) {
        this.ntr = ntr;
    }
}
