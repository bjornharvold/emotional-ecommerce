/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.domain.dto.list;

/**
 * Created by Bjorn Harvold
 * Date: 11/5/12
 * Time: 10:47 PM
 * Responsibility:
 */
public class ListCardBoardName {
    private String cd;
    private String nm;

    public ListCardBoardName(String cd, String nm) {
        this.cd = cd;
        this.nm = nm;
    }

    public String getCd() {
        return cd;
    }

    public String getNm() {
        return nm;
    }

    public void setCd(String cd) {
        this.cd = cd;
    }

    public void setNm(String nm) {
        this.nm = nm;
    }
}
