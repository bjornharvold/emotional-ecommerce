/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.domain.document;

import com.lela.domain.enums.AnalyticsSubType;
import com.lela.domain.enums.AnalyticsType;
import com.lela.domain.enums.DeviceType;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bjorn Harvold
 * Date: 11/5/11
 * Time: 4:19 AM
 * Responsibility:
 */
@Document
public class Analytics extends AbstractDocument implements Serializable {
    private static final long serialVersionUID = 2395659264492887845L;

    /**
     * Registered USER ID
     */
    @Indexed
    private ObjectId srid;

    /**
     * User "cd" of the user INCLUDING transient users
     */
    private String srcd;

    /**
     * Device Type
     */
    private DeviceType dvctp;

    /** Device ID */
    private String dvcd;

    /** Application ID */
    private String ppd;

    /** Endpoint */
    private String ndpnt;

    /** Referring page **/
    private String rfr;

    /** Device Model as specified by wurfl */
    private String wrfld;

    /** User agent */
    private String srgnt;

    /** Request method */
    private String mthd;

    /** Type */
    private AnalyticsType tp;

    /** Sub-Type */
    @Indexed
    private AnalyticsSubType sbtp;
    
    private List<AnalyticAttribute> ttrbts;

    public Analytics() {
    }

    public Analytics(AnalyticsType tp, String dvcd, String ppd, String ndpnt, String mthd, String wrfld, String srgnt) {
        this.tp = tp;
        this.dvcd = dvcd;
        this.ppd = ppd;
        this.ndpnt = ndpnt;
        this.mthd = mthd;
        this.wrfld = wrfld;
        this.srgnt = srgnt;
    }
    
    public Analytics(AnalyticsType tp, String mthd, String ndpnt, String srgnt) {
        this.tp = tp;
        this.mthd = mthd;
        this.ndpnt = ndpnt;
        this.srgnt = srgnt;
    }
    
    public void addAttribute(String key, Object value) {
        synchronized (this) {
            if (ttrbts == null) {
                ttrbts = new ArrayList<AnalyticAttribute>();
            }
        }

        ttrbts.add(new AnalyticAttribute(key, value));
    }

    public String getNdpnt() {
        return ndpnt;
    }

    public void setNdpnt(String ndpnt) {
        this.ndpnt = ndpnt;
    }

    public DeviceType getDvctp() {
        return dvctp;
    }

    public void setDvctp(DeviceType dvctp) {
        this.dvctp = dvctp;
    }

    public String getDvcd() {
        return dvcd;
    }

    public void setDvcd(String dvcd) {
        this.dvcd = dvcd;
    }

    public String getPpd() {
        return ppd;
    }

    public void setPpd(String ppd) {
        this.ppd = ppd;
    }

    public String getWrfld() {
        return wrfld;
    }

    public void setWrfld(String wrfld) {
        this.wrfld = wrfld;
    }

    public String getSrgnt() {
        return srgnt;
    }

    public void setSrgnt(String srgnt) {
        this.srgnt = srgnt;
    }

    public String getMthd() {
        return mthd;
    }

    public void setMthd(String mthd) {
        this.mthd = mthd;
    }

    public AnalyticsType getTp() {
        return tp;
    }

    public void setTp(AnalyticsType tp) {
        this.tp = tp;
    }

    public List<AnalyticAttribute> getTtrbts() {
        return ttrbts;
    }

    public void setTtrbts(List<AnalyticAttribute> ttrbts) {
        this.ttrbts = ttrbts;
    }

    public ObjectId getSrid() {
        return srid;
    }

    public void setSrid(ObjectId srid) {
        this.srid = srid;
    }

    public String getSrcd() {
        return srcd;
    }

    public void setSrcd(String srcd) {
        this.srcd = srcd;
    }

    public String getRfr() {
        return rfr;
    }

    public void setRfr(String rfr) {
        this.rfr = rfr;
    }

    public AnalyticsSubType getSbtp() {
        return sbtp;
    }

    public void setSbtp(AnalyticsSubType sbtp) {
        this.sbtp = sbtp;
    }
}
