/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.domain.dto.search;

import java.io.Serializable;

/**
 * Created by Bjorn Harvold
 * Date: 4/26/12
 * Time: 9:43 PM
 * Responsibility:
 */
public class NameValueAggregate implements Serializable {
    private static final long serialVersionUID = -2510965182984883694L;
    private final String rlnm;
    private final Long cnt;

    public NameValueAggregate(String rlnm, Long cnt) {
        this.rlnm = rlnm;
        this.cnt = cnt;
    }

    public String getRlnm() {
        return rlnm;
    }

    public Long getCnt() {
        return cnt;
    }
}
