/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.domain.document;

import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Chris Tallent
 * Date: 11/3/11
 * Time: 2:49 PM
 */
@Document
public class Branch extends AbstractDocument implements Serializable {
    private static final long serialVersionUID = -2124981473380638111L;

    /**
     * Merchant ID
     */
    private String mrchntd;

    /**
     * Merchant Name
     */
    private String mrchntnm;

    /**
     * Local only merchant
     */
    private Boolean lclnly;

    /**
     * Affiliate Url Name
     */
    private String ffltrlnm;

    /**
     * Store number
     */
    private Long strnmbr;

    /**
     * Approved
     */
    private Boolean pprvd;

    /**
     * Store name
     */
    private String nm;

    /**
     * SEO URL Name
     */
    private String rlnm;

    /**
     * Address
     */
    private String ddrss;

    /**
     * City
     */
    private String cty;

    /**
     * State
     */
    private String st;

    /**
     * Zip (Postal Code)
     */
    private String zp;

    /**
     * Phone
     */
    private String phn;

    /**
     * Email
     */
    private String ml;

    /**
     * Hours
     */
    private String hrs;

    /**
     * Local Code
     */
    private String lclcd;

    /**
     * Location.  2 element array of Double in order [ longitude, latitude ]
     * The ordering is important if the location is to be used for MongoDB Spherical calculations
     */
    @GeoSpatialIndexed
    private Float[] lc;

    /**
     * Additional Attributes
     */
    private List<BranchAttribute> attrs;

    /**
     * Branch Contact
     */
    private BranchContact cntct;

    /**
     * Branch Details
     */
    private List<BranchDetails> dtls;

    /**
     * Product Categories
     */
    private List<LocalCategory> lclctgrs;

    //~--- get methods --------------------------------------------------------

    public Map<String, Object> getAttributes() {
        Map<String, Object> attributes = null;

        if ((attrs != null) && !attrs.isEmpty()) {
            attributes = new HashMap<String, Object>();

            for (BranchAttribute attr : attrs) {
                attributes.put(attr.getKy(), attr.getVl());
            }
        }

        return attributes;
    }

    public Float getLongitude() {
        if (lc != null && lc.length > 0) {
            return lc[0];
        }

        return null;
    }

    public Float getLatitude() {
        if (lc != null && lc.length > 1) {
            return lc[1];
        }

        return null;
    }

    public String getMrchntd() {
        return mrchntd;
    }

    public Long getStrnmbr() {
        return strnmbr;
    }

    public String getNm() {
        return nm;
    }

    public String getDdrss() {
        return ddrss;
    }

    public String getCty() {
        return cty;
    }

    public String getSt() {
        return st;
    }

    public String getZp() {
        return zp;
    }

    public String getLclcd() {
        return lclcd;
    }

    public Float[] getLc() {
        return lc;
    }

    public List<BranchAttribute> getAttrs() {
        return attrs;
    }

    public String getRlnm() {
        return rlnm;
    }

    public String getMrchntnm() {
        return mrchntnm;
    }

    public Boolean getPprvd() {
        return pprvd;
    }

    public Boolean getLclnly() {
        return lclnly;
    }

    public String getFfltrlnm() {
        return ffltrlnm;
    }

    public BranchContact getCntct() {
        return cntct;
    }

    public List<BranchDetails> getDtls() {
        return dtls;
    }

    public String getPhn() {
        return phn;
    }

    public String getMl() {
        return ml;
    }

    public String getHrs() {
        return hrs;
    }

    public List<LocalCategory> getLclctgrs() {
        return lclctgrs;
    }

    //~--- set methods --------------------------------------------------------

    public void setMrchntd(String mrchntd) {
        this.mrchntd = mrchntd;
    }

    public void setStrnmbr(Long strnmbr) {
        this.strnmbr = strnmbr;
    }

    public void setNm(String nm) {
        this.nm = nm;
    }

    public void setDdrss(String ddrss) {
        this.ddrss = ddrss;
    }

    public void setCty(String cty) {
        this.cty = cty;
    }

    public void setSt(String st) {
        this.st = st;
    }

    public void setZp(String zp) {
        this.zp = zp;
    }

    public void setLclcd(String lclcd) {
        this.lclcd = lclcd;
    }

    public void setLc(Float[] lc) {
        this.lc = lc;
    }

    public void setAttrs(List<BranchAttribute> attrs) {
        this.attrs = attrs;
    }

    public void setRlnm(String rlnm) {
        this.rlnm = rlnm;
    }

    public void setMrchntnm(String mrchntnm) {
        this.mrchntnm = mrchntnm;
    }

    public void setPprvd(Boolean pprvd) {
        this.pprvd = pprvd;
    }

    public void setLclnly(Boolean lclnly) {
        this.lclnly = lclnly;
    }

    public void setFfltrlnm(String ffltrlnm) {
        this.ffltrlnm = ffltrlnm;
    }

    public void setCntct(BranchContact cntct) {
        this.cntct = cntct;
    }

    public void setDtls(List<BranchDetails> dtls) {
        this.dtls = dtls;
    }

    public void setPhn(String phn) {
        this.phn = phn;
    }

    public void setMl(String ml) {
        this.ml = ml;
    }

    public void setHrs(String hrs) {
        this.hrs = hrs;
    }

    public void setLclctgrs(List<LocalCategory> lclctgrs) {
        this.lclctgrs = lclctgrs;
    }
}
