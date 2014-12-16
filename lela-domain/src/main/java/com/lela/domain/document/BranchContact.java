package com.lela.domain.document;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: Martin Gamboa
 * Date: 1/13/12
 * Time: 1:19 PM
 * To change this template use File | Settings | File Templates.
 */
public class BranchContact implements Serializable {

    private static final long serialVersionUID = -6696370187721327672L;

    /** First Name */
    private String fn;

    /** Last Name */
    private String ln;

    /** Email */
    private String ml;

    /** Phone */
    private String phn;

    public String getFn() {
        return fn;
    }

    public void setFn(String fn) {
        this.fn = fn;
    }

    public String getLn() {
        return ln;
    }

    public void setLn(String ln) {
        this.ln = ln;
    }

    public String getMl() {
        return ml;
    }

    public void setMl(String ml) {
        this.ml = ml;
    }

    public String getPhn() {
        return phn;
    }

    public void setPhn(String phn) {
        this.phn = phn;
    }
}
