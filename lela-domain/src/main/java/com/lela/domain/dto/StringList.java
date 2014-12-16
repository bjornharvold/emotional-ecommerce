/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.domain.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Chris Tallent
 * Date: 2/7/12
 * Time: 10:55 AM
 */
public class StringList extends AbstractJSONPayload {
    private List<String> list = new ArrayList<String>();

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }
}