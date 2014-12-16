package com.lela.domain.document;

import java.io.Serializable;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Martin Gamboa
 * Date: 1/20/12
 * Time: 11:57 AM
 * To change this template use File | Settings | File Templates.
 */
public class LocalCategory implements Serializable {

    private static final long serialVersionUID = -3626001870062563047L;

    /** Category Url Name */
    private String ctgryrlnm;

    /** Category Name */
    private String ctgrynm;

    /** Product Owners */
    private List<String> wnrrlnms;

    public String getCtgryrlnm() {
        return ctgryrlnm;
    }

    public void setCtgryrlnm(String ctgryrlnm) {
        this.ctgryrlnm = ctgryrlnm;
    }

    public String getCtgrynm() {
        return ctgrynm;
    }

    public void setCtgrynm(String ctgrynm) {
        this.ctgrynm = ctgrynm;
    }

    public List<String> getWnrrlnms() {
        return wnrrlnms;
    }

    public void setWnrrlnms(List<String> wnrrlnms) {
        this.wnrrlnms = wnrrlnms;
    }
}
