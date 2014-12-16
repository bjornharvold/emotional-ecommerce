/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.domain.document;

import com.fasterxml.uuid.EthernetAddress;
import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedGenerator;
import com.lela.domain.enums.PictureType;
import org.springframework.data.annotation.Transient;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * Created by Bjorn Harvold
 * Date: 8/31/12
 * Time: 4:06 PM
 * Responsibility:
 */
public class Picture implements Serializable {
    private static final long serialVersionUID = -7446952690761627507L;

    /** Images size keyed on size for internal images*/
    private Map<String, String> mg;

    /** Url of external image */
    private String rl;

    /** Width of external image */
    private String wdth;

    /** Date added */
    private Date dt;

    /** Type */
    private PictureType tp;

    @Transient
    private final static EthernetAddress nic = EthernetAddress.fromInterface();

    @Transient
    private final static TimeBasedGenerator uuidGenerator = Generators.timeBasedGenerator(nic);

    /**
     * Checksum that we can use to uniquely identify object
     */
    private String cd = uuidGenerator.generate().toString();

    public Picture() {
    }

    public Picture(String url, String width) {
        this.tp = PictureType.EXTERNAL;
        this.rl = url;
        this.wdth = width;
    }

    public Picture(Map<String, String> mg) {
        tp = PictureType.INTERNAL;
        this.mg = mg;
        this.dt = new Date();
    }

    public Map<String, String> getMg() {
        return mg;
    }

    public void setMg(Map<String, String> mg) {
        this.mg = mg;
    }

    public Date getDt() {
        return dt;
    }

    public void setDt(Date dt) {
        this.dt = dt;
    }

    public String getCd() {
        return cd;
    }

    public void setCd(String cd) {
        this.cd = cd;
    }

    public PictureType getTp() {
        return tp;
    }

    public void setTp(PictureType tp) {
        this.tp = tp;
    }

    public String getRl() {
        return rl;
    }

    public void setRl(String rl) {
        this.rl = rl;
    }

    public String getWdth() {
        return wdth;
    }

    public void setWdth(String wdth) {
        this.wdth = wdth;
    }
}
