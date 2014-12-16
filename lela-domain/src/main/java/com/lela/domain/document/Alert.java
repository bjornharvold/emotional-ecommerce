/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.domain.document;

import com.fasterxml.uuid.EthernetAddress;
import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedGenerator;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Transient;
import org.springframework.format.annotation.NumberFormat;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Bjorn Harvold
 * Date: 5/22/12
 * Time: 8:38 PM
 * Responsibility:
 */
public class Alert implements Serializable {
    private static final long serialVersionUID = 6588418375465558447L;

    /** Price alert is enabled */
    private Boolean prclrt = false;

    /** Pricing floor. User wants to be notified when item price is lower than this price */
    @NumberFormat(style = NumberFormat.Style.NUMBER, pattern = "#,###.##")
    private Double prc;

    /** Alerts via Email */
    private Boolean mllrt = true;

    /** Alert when manufacturer issues a recall */
    private Boolean mrlrt;

    /** Email to notify */
    @NotNull
    @NotEmpty
    @Email
    private String ml;

    /** Mobile number to SMS to */
    private String phn;

    /** Date alert was created */
    private Date dt;

    @Transient
    private final static EthernetAddress nic = EthernetAddress.fromInterface();

    @Transient
    private final static TimeBasedGenerator uuidGenerator = Generators.timeBasedGenerator(nic);

    /**
     * Checksum that we can use to uniquely identify object
     */
    private String cd = uuidGenerator.generate().toString();

    public Boolean getMllrt() {
        return mllrt;
    }

    public void setMllrt(Boolean mllrt) {
        this.mllrt = mllrt;
    }

    public Boolean getPrclrt() {
        return prclrt;
    }

    public void setPrclrt(Boolean prclrt) {
        this.prclrt = prclrt;
    }

    public Double getPrc() {
        return prc;
    }

    public void setPrc(Double prc) {
        this.prc = prc;
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

    public String getCd() {
        return cd;
    }

    public void setCd(String cd) {
        this.cd = cd;
    }

    public Date getDt() {
        return dt;
    }

    public void setDt(Date dt) {
        this.dt = dt;
    }

    public Boolean getMrlrt() {
        return mrlrt;
    }

    public void setMrlrt(Boolean mrlrt) {
        this.mrlrt = mrlrt;
    }
}
