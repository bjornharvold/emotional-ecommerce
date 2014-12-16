/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.domain.dto;

import java.io.Serializable;

/**
 * Created by Bjorn Harvold
 * Date: 8/25/12
 * Time: 1:18 PM
 * Responsibility:
 */
public class AbstractApiDto extends AbstractJSONPayload implements Serializable {
    private static final long serialVersionUID = 7673878327268920049L;

    /** User code */
    private String srcd;

    public String getSrcd() {
        return srcd;
    }

    public void setSrcd(String srcd) {
        this.srcd = srcd;
    }
}
