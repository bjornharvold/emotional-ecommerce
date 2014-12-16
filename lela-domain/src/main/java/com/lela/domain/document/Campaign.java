/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.domain.document;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by IntelliJ IDEA.
 * User: Chris Tallent
 * Date: 11/3/11
 * Time: 2:49 PM
 */
@Document
public class Campaign extends AbstractDocument implements Serializable {

    private static final long serialVersionUID = 4507638106493087335L;

    /**
     * Campaign name
     */
    private String nm;

    /**
     * Campaign Description
     */
    private String dscrptn;

    /**
     * Campain url name
     */
    @Indexed( unique = true)
    private String rlnm;

    /**
     * Redirect URL
     */
    private String rdrctrl;

    /**
     * Tiles View Name
     */
    private String vwnm;

    /**
     * Affiliate account that owns this campaign
     */
    private String ffltrlnm;

    /**
     * Valid referring affiliate url names for this campaign
     */
    private List<String> vldffltrlnms = new ArrayList<String>();

    /**
     * Is the campaign active
     */
    private Boolean ctv;
    
    /**
     * Campaign start date
     */
    private Date strtdt;

    /**
     * Campaign end date
     */
    private Date nddt;
    
    /**
     * Static content associated to the campaign
     */
    private String sttccntnt;

    /**
     * Is this campaign active and is today's date between the start and end dates?
     * @return
     */
    public boolean isActiveAndCurrent(){
    	boolean boo = false;
    	if (this.getStrtdt() != null && this.getNddt() != null){
    	    Date now = new Date();
    	    boo =   now.after(this.getStrtdt()) && now.before(this.getNddt());
    	} 
    	return boo;
    }
    

    public String getSttccntnt() {
		return sttccntnt;
	}

	public void setSttccntnt(String sttccntnt) {
		this.sttccntnt = sttccntnt;
	}

	public String getNm() {
        return nm;
    }

    public void setNm(String nm) {
        this.nm = nm;
    }

    public String getDscrptn() {
        return dscrptn;
    }

    public void setDscrptn(String dscrptn) {
        this.dscrptn = dscrptn;
    }

    public String getRlnm() {
        return rlnm;
    }

    public void setRlnm(String rlnm) {
        this.rlnm = rlnm;
    }

    public String getRdrctrl() {
        return rdrctrl;
    }

    public void setRdrctrl(String rdrctrl) {
        this.rdrctrl = rdrctrl;
    }

    public String getVwnm() {
        return vwnm;
    }

    public void setVwnm(String vwnm) {
        this.vwnm = vwnm;
    }

    public String getFfltrlnm() {
        return ffltrlnm;
    }

    public void setFfltrlnm(String ffltrlnm) {
        this.ffltrlnm = ffltrlnm;
    }

    public List<String> getVldffltrlnms() {
        return vldffltrlnms;
    }

    public void setVldffltrlnms(List<String> vldffltrlnms) {
        this.vldffltrlnms = vldffltrlnms;
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

    public Boolean getCtv() {
        return ctv;
    }

    public void setCtv(Boolean ctv) {
        this.ctv = ctv;
    }
}
