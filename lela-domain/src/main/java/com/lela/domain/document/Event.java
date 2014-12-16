/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.domain.document;

import com.lela.domain.enums.EventType;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Bjorn Harvold
 * Date: 12/8/11
 * Time: 8:31 PM
 * Responsibility: This event object is used for creating temporary offers such as giveaways etc
 */
@Document
public class Event extends AbstractDocument implements Serializable {
    private static final long serialVersionUID = 1679388541308874437L;

    /** Start date **/
    @DateTimeFormat(style = "S-")
    private Date strtdt;

    /** End date */
    @DateTimeFormat(style = "S-")
    private Date nddt;

    /** Name */
    private String nm;
    
    /** Url name */
    @Indexed( unique = true )
    private String rlnm;
    
    /** Type */
    private EventType tp;

    /** Valid fields for this event*/
    private List<EventField> vntFlds = new ArrayList<EventField>();

    /** Terms and Conditions */
    private String trms;

    /**
     * (Optional) Affiliate account of campaign introducing the user
     */
    private String ffltccntrlnm;

    /**
     * (Optional) Affiliate account of campaign introducing the user
     */
    private String cmpgnrlnm;

    /**
     * (Optional) Tiles View Name
     */
    private String vwnm;

    /**
     * (Optional) Expired Tiles View Name
     */
    private String xprdvwnm;

    /**
     * Redirect URL
     */
    private String rdrctrl;

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

    public String getRlnm() {
        return rlnm;
    }

    public void setRlnm(String rlnm) {
        this.rlnm = rlnm;
    }

    public EventType getTp() {
        return tp;
    }

    public void setTp(EventType tp) {
        this.tp = tp;
    }

    public String getTrms() {
        return trms;
    }

    public void setTrms(String trms) {
        this.trms = trms;
    }

    public List<EventField> getVntFlds() {
        return vntFlds;
    }

    public void setVntFlds(List<EventField> vntFlds) {
        this.vntFlds = vntFlds;
    }

    public boolean isValid(Map<String, Object> attrs)
    {
       //if there are no event fields but there are attributes return false
       if(vntFlds.size()==0 && attrs.size()>0)
           return false;

       //if there are missing required fields return false
       for(EventField eventField:vntFlds)
       {
           if(eventField.getRqrd() && (attrs.get(eventField.getFldNm())==null
                   || StringUtils.length(String.valueOf(attrs.get(eventField.getFldNm()))) == 0 ))
           {
               return false;
           }
       }

       //check if there are attributes which are not permitted
       for(String attr:attrs.keySet())
       {
           if(this.getEventField(attr)==null)
               return false;
       }

        return true;

    }

    private EventField getEventField(String field)
    {
        for(EventField vntFld:vntFlds)
        {
            if(StringUtils.equals(field, vntFld.getFldNm()))
            {
                return vntFld;
            };
        }
        return null;
    }

    public String getFfltccntrlnm() {
        return ffltccntrlnm;
    }

    public void setFfltccntrlnm(String ffltccntrlnm) {
        this.ffltccntrlnm = ffltccntrlnm;
    }

    public String getCmpgnrlnm() {
        return cmpgnrlnm;
    }

    public void setCmpgnrlnm(String cmpgnrlnm) {
        this.cmpgnrlnm = cmpgnrlnm;
    }

    public String getVwnm() {
        return vwnm;
    }

    public void setVwnm(String vwnm) {
        this.vwnm = vwnm;
    }

    public String getXprdvwnm() {
        return xprdvwnm;
    }

    public void setXprdvwnm(String xprdvwnm) {
        this.xprdvwnm = xprdvwnm;
    }

    public String getRdrctrl() {
        return rdrctrl;
    }

    public void setRdrctrl(String rdrctrl) {
        this.rdrctrl = rdrctrl;
    }
}
