/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.domain.dto.list;

import org.hibernate.validator.constraints.NotEmpty;

import javax.annotation.RegEx;
import javax.validation.constraints.NotNull;

/**
 * Created by Bjorn Harvold
 * Date: 11/7/12
 * Time: 1:03 AM
 * Responsibility:
 */
public class RenameListCard {
    @NotNull
    @NotEmpty
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
