/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.domain.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: Chris Tallent
 * Date: 2/7/12
 * Time: 10:55 AM
 */
public class MapList extends AbstractJSONPayload {
    private List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

    public List<HashMap<String, String>> getList() {
        return list;
    }

    public void setList(List<HashMap<String, String>> list) {
        this.list = list;
    }
}