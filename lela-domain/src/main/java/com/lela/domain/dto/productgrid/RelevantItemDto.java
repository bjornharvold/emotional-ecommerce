/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.domain.dto.productgrid;

import java.io.Serializable;

/**
 * Created by Bjorn Harvold
 * Date: 8/26/12
 * Time: 10:41 AM
 * Responsibility:
 */
public class RelevantItemDto extends AbstractItemDto implements Serializable {
    private static final long serialVersionUID = -5694606270077395700L;

    /** Total Relevancy */
    private Integer ttlrlvncy;

    /** Total Relevancy number */
    private Integer ttlrlvncynmbr;

    public Integer getTtlrlvncy() {
        return ttlrlvncy;
    }

    public void setTtlrlvncy(Integer ttlrlvncy) {
        this.ttlrlvncy = ttlrlvncy;
    }

    public Integer getTtlrlvncynmbr() {
        return ttlrlvncynmbr;
    }

    public void setTtlrlvncynmbr(Integer ttlrlvncynmbr) {
        this.ttlrlvncynmbr = ttlrlvncynmbr;
    }
}
