/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.domain.dto;

import com.lela.domain.document.User;
import com.lela.domain.document.UserSupplement;
import com.lela.domain.enums.Gender;
import com.lela.domain.enums.MetricType;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created by Bjorn Harvold
 * Date: 9/5/11
 * Time: 9:19 AM
 * Responsibility:
 */
public class Profile extends AbstractJSONPayload implements Serializable {

    private static final long serialVersionUID = 546928655929422289L;

    private String fnm;
    private String lnm;
    private Date db;
    private Gender gndr;
    private String hzp;
    private String czp;
    private String cntry;
    private Locale lcl;
    private String ndstry;
    private String cmpnysz;
    private String jbttl;
    private String ncm;

    private Integer dbd = -1;
    private Integer dbm = -1;
    private Integer dby = -1;

    public Profile() {
    }

    public Profile(UserSupplement us) {
        this.fnm = us.getFnm();
        this.lnm = us.getLnm();
        this.gndr = us.getGndr();
        this.hzp = us.getHzp();
        this.czp = us.getCzp();
        this.lcl = us.getLcl();
        this.cntry = us.getCntry();

        if (us.getMtrcs() != null) {
            if (us.getMtrcs().containsKey(MetricType.INDUSTRY)) {
                this.ndstry = us.getMtrcs().get(MetricType.INDUSTRY);
            }
            if (us.getMtrcs().containsKey(MetricType.COMPANY_SIZE)) {
                this.cmpnysz = us.getMtrcs().get(MetricType.COMPANY_SIZE);
            }
            if (us.getMtrcs().containsKey(MetricType.JOB_TITLE)) {
                this.jbttl = us.getMtrcs().get(MetricType.JOB_TITLE);
            }
            if (us.getMtrcs().containsKey(MetricType.ANNUAL_HOUSEHOLD_INCOME)) {
                this.ncm = us.getMtrcs().get(MetricType.ANNUAL_HOUSEHOLD_INCOME);
            }
        }

        this.db = us.getDb();
        if (this.db != null) {
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(this.db);
            this.dbd = calendar.get(Calendar.DAY_OF_MONTH);
            this.dbm = calendar.get(Calendar.MONTH);
            this.dby = calendar.get(Calendar.YEAR);
        }
    }

    public String getFnm() {
        return fnm;
    }

    public void setFnm(String fnm) {
        this.fnm = fnm;
    }

    public String getLnm() {
        return lnm;
    }

    public void setLnm(String lnm) {
        this.lnm = lnm;
    }

    public Date getDb() {
        Calendar calendar = null;
        if ((dby != -1) && (dbm != -1) && (dby != -1)) {
            calendar = new GregorianCalendar(dby, dbm, dbd);
            db = calendar.getTime();
        } else {
            db = null;
        }

        return db;
    }

    public void setDb(Date db) {
        this.db = db;
        if (db != null) {
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(db);

            dbd = calendar.get(Calendar.DAY_OF_MONTH);
            dbm = calendar.get(Calendar.MONTH);
            dby = calendar.get(Calendar.YEAR);
        } else {
            dbd = -1;
            dbm = -1;
            dby = -1;
        }
    }

    public Gender getGndr() {
        return gndr;
    }

    public void setGndr(Gender gndr) {
        this.gndr = gndr;
    }

    public String getHzp() {
        return hzp;
    }

    public void setHzp(String hzp) {
        this.hzp = hzp;
    }

    public String getCzp() {
        return czp;
    }

    public void setCzp(String czp) {
        this.czp = czp;
    }

    public String getCntry() {
        return cntry;
    }

    public void setCntry(String cntry) {
        this.cntry = cntry;
    }

    public Locale getLcl() {
        return lcl;
    }

    public void setLcl(Locale lcl) {
        this.lcl = lcl;
    }

    public String getNdstry() {
        return ndstry;
    }

    public void setNdstry(String ndstry) {
        this.ndstry = ndstry;
    }

    public String getCmpnysz() {
        return cmpnysz;
    }

    public void setCmpnysz(String cmpnysz) {
        this.cmpnysz = cmpnysz;
    }

    public String getJbttl() {
        return jbttl;
    }

    public void setJbttl(String jbttl) {
        this.jbttl = jbttl;
    }

    public String getNcm() {
        return ncm;
    }

    public void setNcm(String ncm) {
        this.ncm = ncm;
    }

    public Integer getDbd() {
        return dbd;
    }

    public void setDbd(Integer dbd) {
        this.dbd = dbd;
    }

    public Integer getDbm() {
        return dbm;
    }

    public void setDbm(Integer dbm) {
        this.dbm = dbm;
    }

    public Integer getDby() {
        return dby;
    }

    public void setDby(Integer dby) {
        this.dby = dby;
    }
}
