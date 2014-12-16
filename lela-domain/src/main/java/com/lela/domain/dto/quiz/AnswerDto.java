/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.domain.dto.quiz;

import java.io.Serializable;

/**
 * Created by Bjorn Harvold
 * Date: 7/13/12
 * Time: 4:29 PM
 * Responsibility:
 */
public class AnswerDto implements Serializable {
    private static final long serialVersionUID = 5023399135019852138L;

    /** Key */
    private String ky;

    /** Image url */
    private String mg;

    /** name */
    private String nm;

    public String getKy() {
        return ky;
    }

    public void setKy(String ky) {
        this.ky = ky;
    }

    public String getMg() {
        return mg;
    }

    public void setMg(String mg) {
        this.mg = mg;
    }

    public String getNm() {
        return nm;
    }

    public void setNm(String nm) {
        this.nm = nm;
    }
}
