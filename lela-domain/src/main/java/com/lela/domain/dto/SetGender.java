/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.domain.dto;

import com.lela.domain.enums.Gender;

/**
 * Created by Bjorn Harvold
 * Date: 10/24/11
 * Time: 3:17 PM
 * Responsibility:
 */
public class SetGender extends AbstractJSONPayload {
    private Gender gndr;

    public Gender getGndr() {
        return gndr;
    }

    public void setGndr(Gender gndr) {
        this.gndr = gndr;
    }
}
