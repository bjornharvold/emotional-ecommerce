/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.domain.document;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Bjorn Harvold
 * Date: 8/23/12
 * Time: 4:21 PM
 * Responsibility:
 */
public class ProductGridFilter implements Serializable {
    private static final long serialVersionUID = 4989250428941144704L;

    /** Filter key */
    private String ky;

    /** Filter value */
    private List<String> vl;

    public ProductGridFilter() {
    }

    public ProductGridFilter(String key, List<String> value) {
        this.ky = key;
        this.vl = value;
    }

    public String getKy() {
        return ky;
    }

    public void setKy(String ky) {
        this.ky = ky;
    }

    public List<String> getVl() {
        return vl;
    }

    public void setVl(List<String> vl) {
        this.vl = vl;
    }
}
