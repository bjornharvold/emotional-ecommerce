/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.domain.document;

import com.lela.domain.enums.PublishStatus;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Transient;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Bjorn Harvold
 * Date: 2/10/12
 * Time: 1:36 AM
 * Responsibility:
 */
public class CategoryGroup implements Serializable {
    private static final long serialVersionUID = 2990753126609399362L;

    /** Name */
    @NotNull
    @NotEmpty
    private String nm;

    /** Url name */
    @NotNull
    @NotEmpty
    private String rlnm;

    /** Url name */
    @NotNull
    @NotEmpty
    private String srlnm;

    /** Order */
    @NotNull
    private Integer rdr;

    /** Publish Status */
    @NotNull
    private PublishStatus stts;

    /** Unique id */
    private String d;

    /** Sub groups */
    private List<CategoryGroup> chldrn;

    /** Categories - These get loaded on a second call */
    @Transient
    private List<Category> ctgrs;

    /** Navigation bar url name */
    @Transient
    private String navigationBarUrlName;

    @Transient
    private String parentCategoryGroupId;

    public CategoryGroup() {
    }

    public CategoryGroup(String navigationBarUrlName) {
        this.navigationBarUrlName = navigationBarUrlName;
    }

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

    public Integer getRdr() {
        return rdr;
    }

    public void setRdr(Integer rdr) {
        this.rdr = rdr;
    }

    public PublishStatus getStts() {
        return stts;
    }

    public void setStts(PublishStatus stts) {
        this.stts = stts;
    }

    public String getSrlnm() {
        return srlnm;
    }

    public void setSrlnm(String srlnm) {
        this.srlnm = srlnm;
    }

    public List<Category> getCtgrs() {
        return ctgrs;
    }

    public void setCtgrs(List<Category> ctgrs) {
        this.ctgrs = ctgrs;
    }

    public List<CategoryGroup> getChldrn() {
        return chldrn;
    }

    public void setChldrn(List<CategoryGroup> chldrn) {
        this.chldrn = chldrn;
    }

    public String getD() {
        return d;
    }

    public void setD(String d) {
        this.d = d;
    }

    public String getNavigationBarUrlName() {
        return navigationBarUrlName;
    }

    public void setNavigationBarUrlName(String navigationBarUrlName) {
        this.navigationBarUrlName = navigationBarUrlName;
    }

    public String getParentCategoryGroupId() {
        return parentCategoryGroupId;
    }

    public void setParentCategoryGroupId(String parentCategoryGroupId) {
        this.parentCategoryGroupId = parentCategoryGroupId;
    }
}
