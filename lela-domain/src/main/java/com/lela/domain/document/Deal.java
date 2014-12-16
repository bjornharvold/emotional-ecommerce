/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.domain.document;

import com.fasterxml.uuid.EthernetAddress;
import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedGenerator;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Bjorn Harvold
 * Date: 9/17/11
 * Time: 2:05 PM
 * Responsibility:
 */
@Document
public class Deal extends AbstractDocument implements Serializable {
    private static final long serialVersionUID = -1251725428295919771L;

    /** Store url name */
    private String rlnm;

    /** Start date */
    private Date strtdt;

    /** End date */
    private Date nddt;

    /** Name */
    private String nm;

    /** Deal url */
    private String rl;

    @Transient
    private final static EthernetAddress nic = EthernetAddress.fromInterface();

    @Transient
    private final static TimeBasedGenerator uuidGenerator = Generators.timeBasedGenerator(nic);

    /**
     * Checksum that we can use to uniquely identify object
     */
    @Indexed(unique = true)
    private String cd = uuidGenerator.generate().toString();

    public String getRlnm() {
        return rlnm;
    }

    public void setRlnm(String rlnm) {
        this.rlnm = rlnm;
    }

    public Date getStrtdt() {
        return strtdt;
    }

    public void setStrtdt(Date strtdt) {
        this.strtdt = strtdt;
    }

    public Date getNddt() {
        return nddt;
    }

    public void setNddt(Date nddt) {
        this.nddt = nddt;
    }

    public String getNm() {
        return nm;
    }

    public void setNm(String nm) {
        this.nm = nm;
    }

    public String getRl() {
        return rl;
    }

    public void setRl(String rl) {
        this.rl = rl;
    }

    public String getCd() {
        return cd;
    }

    public void setCd(String cd) {
        this.cd = cd;
    }
}
