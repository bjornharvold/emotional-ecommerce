/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */
package com.lela.domain.dto.admin;

import java.util.Date;
import java.util.Map;

/**
 * User: Chris Tallent
 * Date: 8/22/12
 * Time: 8:28 AM
 */
public class RegistrationsByAffiliateResult implements Comparable<RegistrationsByAffiliateResult> {
    private Date id;
    private Map<String, Integer> value;

    public Date getId() {
        return id;
    }

    public void setId(Date id) {
        this.id = id;
    }

    public Map<String, Integer> getValue() {
        return value;
    }

    public void setValue(Map<String, Integer> value) {
        this.value = value;
    }

    @Override
    public int compareTo(RegistrationsByAffiliateResult other) {
        return this.id.compareTo(other.getId());
    }
}
