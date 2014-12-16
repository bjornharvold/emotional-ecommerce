/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.domain.dto.productgrid;

/**
 * Created by Bjorn Harvold
 * Date: 8/25/12
 * Time: 9:58 PM
 * Responsibility:
 */
public class EnrichProductGridQuery {
    private final String userCode;
    private final Integer size;
    private final EnrichedProductGrid pg;

    public EnrichProductGridQuery(String userCode, Integer size, EnrichedProductGrid pg) {
        this.userCode = userCode;
        this.size = size;
        this.pg = pg;
    }

    public Integer getSize() {
        return size;
    }

    public String getUserCode() {
        return userCode;
    }

    public EnrichedProductGrid getPg() {
        return pg;
    }
}
