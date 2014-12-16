/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.domain.document;

import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Transient;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created by Bjorn Harvold
 * Date: 9/20/12
 * Time: 9:09 AM
 * Responsibility:
 */
public class Comment extends AbstractNote implements Serializable {
    private static final long serialVersionUID = -1220246647343309844L;

    /** Flagged */
    private Boolean flg;

    /** User profile */
    private ListCardProfile prfl;

    /** Post to Facebook */
    private Boolean fb = false;

    /** Transient user code for the user who owns this list card */
    @Transient
    @NotNull
    @NotEmpty
    private String wcd;

    public Boolean getFlg() {
        return flg;
    }

    public void setFlg(Boolean flg) {
        this.flg = flg;
    }

    public ListCardProfile getPrfl() {
        return prfl;
    }

    public void setPrfl(ListCardProfile prfl) {
        this.prfl = prfl;
    }

    public Boolean getFb() {
        return fb;
    }

    public void setFb(Boolean fb) {
        this.fb = fb;
    }

    public String getWcd() {
        return wcd;
    }

    public void setWcd(String cd) {
        this.wcd = cd;
    }

    public boolean isProfileEmpty() {
        boolean result = false;

        if (this.prfl == null) {
            result = true;
        } else {
            if (StringUtils.isEmpty(this.prfl.getFnm()) &&
                    StringUtils.isEmpty(this.prfl.getLnm()) &&
                    StringUtils.isEmpty(this.prfl.getMg())) {
                result = true;
            }
        }

        return result;
    }
}
