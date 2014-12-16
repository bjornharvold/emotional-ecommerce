package com.lela.domain.dto;

/**
 * Created by IntelliJ IDEA.
 * User: Cruiser1
 * Date: 1/17/12
 * Time: 11:43 AM
 * To change this template use File | Settings | File Templates.
 */
public class OnBoardData implements Comparable<OnBoardData> {

    /** Url Name */
    private String rlnm;

    /** Name */
    private String nm;

    public OnBoardData() {
    }

    public OnBoardData(String rlnm, String nm) {
        this.rlnm = rlnm;
        this.nm = nm;
    }

    public String getRlnm() {
        return rlnm;
    }

    public void setRlnm(String rlnm) {
        this.rlnm = rlnm;
    }

    public String getNm() {
        return nm;
    }

    public void setNm(String nm) {
        this.nm = nm;
    }

    @Override
    public int compareTo(OnBoardData o) {
        return this.nm.compareTo(o.nm);
    }
}
