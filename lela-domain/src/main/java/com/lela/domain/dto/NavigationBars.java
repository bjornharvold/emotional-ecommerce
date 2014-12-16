/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.domain.dto;

import com.lela.domain.document.NavigationBar;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Bjorn Harvold
 * Date: 2/14/12
 * Time: 12:24 AM
 * Responsibility:
 */
public class NavigationBars extends AbstractJSONPayload implements Serializable {
    private static final long serialVersionUID = 6660128608553079464L;

    private List<NavigationBar> list;

    public NavigationBars() {
    }

    public NavigationBars(List<NavigationBar> list) {
        this.list = list;
    }

    public List<NavigationBar> getList() {
        return list;
    }

    public void setList(List<NavigationBar> list) {
        this.list = list;
    }
}
