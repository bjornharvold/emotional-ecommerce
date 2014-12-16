/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.domain.dto;

import com.lela.domain.document.Role;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Bjorn Harvold
 * Date: 11/17/11
 * Time: 10:02 PM
 * Responsibility:
 */
public class Roles extends AbstractJSONPayload implements Serializable {
    private static final long serialVersionUID = 1353361554414634955L;

    private List<Role> list;

    public Roles() {
    }

    public Roles(List<Role> list) {
        this.list = list;
    }

    public List<Role> getList() {
        return list;
    }

    public void setList(List<Role> list) {
        this.list = list;
    }
}
