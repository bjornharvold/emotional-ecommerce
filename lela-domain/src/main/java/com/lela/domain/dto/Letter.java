/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.domain.dto;

import java.io.Serializable;

/**
 * Created by Bjorn Harvold
 * Date: 4/27/12
 * Time: 12:59 AM
 * Responsibility:
 */
public class Letter implements Serializable {
    private static final long serialVersionUID = -8586261228981276072L;
    private final String nm;
    private final Boolean selected;

    public Letter(String nm, Boolean selected) {
        this.nm = nm;
        this.selected = selected;
    }

    public String getNm() {
        return nm;
    }

    public Boolean getSelected() {
        return selected;
    }
}
