/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.domain.document;

import com.lela.domain.enums.TestimonialType;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Transient;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Bjorn Harvold
 * Date: 6/8/12
 * Time: 5:01 PM
 * Responsibility:
 */
public class Testimonial implements Serializable {
    private static final long serialVersionUID = 3125742243568823448L;

    /** Publish date */
    @NotNull
    private Date pblshdt;

    /** Testimonial date */
    @NotNull
    private Date dt;

    /** Creation date */
    private Date cdt;

    /** testimonial id */
    private String tid;

    /** Testimonial type */
    @NotNull
    private TestimonialType tp;

    /** Header */
    @NotNull
    @NotEmpty
    String hdr;

    /** Text */
    @NotNull
    @NotEmpty
    private String txt;

    /** Posted by */
    @NotNull
    @NotEmpty
    private String pstdb;

    /** Order */
    @Min(1)
    @NotNull
    private Integer rdr;

    @Transient
    @NotNull
    @NotEmpty
    private String rlnm;

    public Testimonial() {
    }

    public Testimonial(String rlnm) {
        this.rlnm = rlnm;
    }

    public Testimonial(Testimonial testimonial) {
        this.dt = testimonial.getDt();
        this.cdt = testimonial.getCdt();
        this.hdr = testimonial.getHdr();
        this.pblshdt = testimonial.getPblshdt();
        this.tp = testimonial.getTp();
        this.pstdb = testimonial.getPstdb();
        this.rdr = testimonial.getRdr();
        this.rlnm = testimonial.getRlnm();
        this.txt = testimonial.getTxt();
        this.tid = testimonial.getTid();
    }

    public String getHdr() {
        return hdr;
    }

    public void setHdr(String hdr) {
        this.hdr = hdr;
    }

    public Date getPblshdt() {
        return pblshdt;
    }

    public void setPblshdt(Date pblshdt) {
        this.pblshdt = pblshdt;
    }

    public Date getDt() {
        return dt;
    }

    public void setDt(Date dt) {
        this.dt = dt;
    }

    public TestimonialType getTp() {
        return tp;
    }

    public void setTp(TestimonialType tp) {
        this.tp = tp;
    }

    public String getTxt() {
        return txt;
    }

    public void setTxt(String txt) {
        this.txt = txt;
    }

    public String getPstdb() {
        return pstdb;
    }

    public void setPstdb(String pstdb) {
        this.pstdb = pstdb;
    }

    public Integer getRdr() {
        return rdr;
    }

    public void setRdr(Integer rdr) {
        this.rdr = rdr;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getRlnm() {
        return rlnm;
    }

    public void setRlnm(String rlnm) {
        this.rlnm = rlnm;
    }

    public Date getCdt() {
        return cdt;
    }

    public void setCdt(Date cdt) {
        this.cdt = cdt;
    }
}
