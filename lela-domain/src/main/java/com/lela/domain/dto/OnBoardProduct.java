package com.lela.domain.dto;

/**
 * Created by IntelliJ IDEA.
 * User: Martin Gamboa
 * Date: 1/18/12
 * Time: 10:02 AM
 * To change this template use File | Settings | File Templates.
 */
public class OnBoardProduct {

    private String ctgryrlnm;

    private String wnrrlnm;

    public OnBoardProduct() {
    }

    public OnBoardProduct(String ctgryrlnm, String wnrrlnm) {
        this.ctgryrlnm = ctgryrlnm;
        this.wnrrlnm = wnrrlnm;
    }

    public String getCtgryrlnm() {
        return ctgryrlnm;
    }

    public void setCtgryrlnm(String ctgryrlnm) {
        this.ctgryrlnm = ctgryrlnm;
    }

    public String getWnrrlnm() {
        return wnrrlnm;
    }

    public void setWnrrlnm(String wnrrlnm) {
        this.wnrrlnm = wnrrlnm;
    }
}
