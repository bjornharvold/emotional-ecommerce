package com.lela.domain.document;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: Martin Gamboa
 * Date: 1/13/12
 * Time: 1:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class BranchDetails implements Serializable {

    private static final long serialVersionUID = 3805976366597607092L;

    /** Name */
    private String nm;

    /** Terms */
    private String trms;

    /** Price */
    private Double prc;

    public String getNm() {
        return nm;
    }

    public void setNm(String nm) {
        this.nm = nm;
    }

    public String getTrms() {
        return trms;
    }

    public void setTrms(String trms) {
        this.trms = trms;
    }

    public Double getPrc() {
        return prc;
    }

    public void setPrc(Double prc) {
        this.prc = prc;
    }
}
