/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.domain.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by Bjorn Harvold
 * Date: 5/25/12
 * Time: 10:02 PM
 * Responsibility:
 */
public class UserAttributes implements Serializable {
    private static final long serialVersionUID = 7602672952028884236L;

    private Map<String, List<String>> map;

    public Map<String, List<String>> getMap() {
        return map;
    }

    public void setMap(Map<String, List<String>> map) {
        this.map = map;
    }
}
