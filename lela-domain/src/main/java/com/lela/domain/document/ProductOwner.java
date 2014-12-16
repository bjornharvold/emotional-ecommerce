package com.lela.domain.document;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: Martin Gamboa
 * Date: 1/20/12
 * Time: 11:57 AM
 * To change this template use File | Settings | File Templates.
 */
public class ProductOwner implements Serializable {

    private static final long serialVersionUID = 9037299758073831127L;

    /** Owner Url Name */
    private String wnrrlnm;

    /** Owner Name */
    private String wnrnm;

    public String getWnrrlnm() {
        return wnrrlnm;
    }

    public void setWnrrlnm(String wnrrlnm) {
        this.wnrrlnm = wnrrlnm;
    }

    public String getWnrnm() {
        return wnrnm;
    }

    public void setWnrnm(String wnrnm) {
        this.wnrnm = wnrnm;
    }
}
