/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.domain.dto.dom;

import java.util.List;

/**
 * Created by Bjorn Harvold
 * Date: 10/2/12
 * Time: 9:58 PM
 * Responsibility:
 */
public class DOMElements {
    private final List<DOMElement> list;

    public DOMElements(List<DOMElement> list) {
        this.list = list;
    }

    public List<DOMElement> getList() {
        return list;
    }
}
