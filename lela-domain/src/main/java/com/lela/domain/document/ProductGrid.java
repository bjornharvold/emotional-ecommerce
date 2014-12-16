/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.domain.document;

import com.lela.domain.dto.ItemPage;
import com.lela.domain.enums.FunctionalSortType;
import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Bjorn Harvold
 * Date: 7/31/12
 * Time: 10:01 PM
 * Responsibility:
 */
@Document
public class ProductGrid extends AbstractDocument implements Serializable {
    private static final long serialVersionUID = -9114043651957711857L;

    /** Url name */
    @NotNull
    @NotBlank
    private String rlnm;

    /** Seo friendly url name for category*/
    private String srlnm;

    /** Locale */
    private Locale lcl = Locale.US;

    /** Title */
    @NotNull
    @NotBlank
    private String nm;

    /** Header */
    @NotNull
    @NotBlank
    private String hdr;

    /** Category url name */
    @NotNull
    @NotBlank
    private String crlnm;

    /** How to sort this */
    @NotNull
    private FunctionalSortType srt;

    /** Override desired sort type if we can recommend products to user */
    @NotNull
    private Boolean vrrd = false;

    /** Published and ready for consumption */
    @NotNull
    private Boolean pblshd = false;

    /** Filters to filter category on */
    private List<ProductGridFilter> fltrs;

    public ProductGrid() {

    }

    public ProductGrid(ProductGrid other) {
        super(other);
        this.rlnm = other.rlnm;
        this.srlnm = other.srlnm;
        this.lcl = other.lcl;
        this.nm = other.nm;
        this.hdr = other.hdr;
        this.crlnm = other.crlnm;
        this.srt = other.srt;
        this.vrrd = other.vrrd;
        this.pblshd = other.pblshd;
        this.fltrs = other.fltrs;
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

    public Locale getLcl() {
        return lcl;
    }

    public void setLcl(Locale lcl) {
        this.lcl = lcl;
    }

    public String getNm() {
        return nm;
    }

    public void setNm(String nm) {
        this.nm = nm;
    }

    public String getHdr() {
        return hdr;
    }

    public void setHdr(String hdr) {
        this.hdr = hdr;
    }

    public String getCrlnm() {
        return crlnm;
    }

    public void setCrlnm(String crlnm) {
        this.crlnm = crlnm;
    }

    public FunctionalSortType getSrt() {
        return srt;
    }

    public void setSrt(FunctionalSortType srt) {
        this.srt = srt;
    }

    public Boolean getVrrd() {
        return vrrd;
    }

    public void setVrrd(Boolean vrrd) {
        this.vrrd = vrrd;
    }

    public Boolean getPblshd() {
        return pblshd;
    }

    public void setPblshd(Boolean pblshd) {
        this.pblshd = pblshd;
    }

    public List<ProductGridFilter> getFltrs() {
        return fltrs;
    }

    public void setFltrs(List<ProductGridFilter> fltrs) {
        this.fltrs = fltrs;
    }

    public void addFilters(Map<String, List<String>> map) {
        if (this.fltrs == null) {
            this.fltrs = new ArrayList<ProductGridFilter>();
        }

        List<ProductGridFilter> toRemove = null;

        if (map != null && !map.isEmpty()) {
            // first delete any dupes we might have
            for (ProductGridFilter pgf : this.fltrs) {
                if (map.containsKey(pgf.getKy()) && map.get(pgf.getKy()) != null) {
                    if (toRemove == null) {
                        toRemove = new ArrayList<ProductGridFilter>();
                    }
                    toRemove.add(pgf);
                }
            }

            if (toRemove != null) {
                this.fltrs.removeAll(toRemove);
            }

            // now add the new ones
            for (Map.Entry<String, List<String>> entry : map.entrySet()) {
                if (entry.getValue() != null) {
                    this.fltrs.add(new ProductGridFilter(entry.getKey(), entry.getValue()));
                }
            }
        }
    }

    public void removeFilter(String key) {
        if (this.fltrs != null && !this.fltrs.isEmpty()) {
            ProductGridFilter dupe = null;

            for (ProductGridFilter filter : fltrs) {
                if (StringUtils.equals(filter.getKy(), key)) {
                    dupe = filter;
                }
            }

            if (dupe != null) {
                this.fltrs.remove(dupe);
            }
        }
    }

}
