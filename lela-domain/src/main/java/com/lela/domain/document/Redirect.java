/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.domain.document;

import com.lela.domain.enums.RedirectType;
import org.bson.types.ObjectId;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Chris Tallent
 * Date: 10/4/11
 * Time: 9:22 AM
 * To change this template use File | Settings | File Templates.
 */

public class Redirect implements Serializable {

    private ObjectId id;

    /** Create date */
    private Date cdt;

    /** Last Modified Date */
    private Date ldt;

    /**
     * URL redirected to
     */
    private String rl;

    /**
     * Redirect Type
     */
    private RedirectType tp;

    /**
     * Date of the redirection
     */
    private Date rdrctdt;

    /**
     * User Visit ID this Redirect happened in
     */
    private ObjectId vstd;

    /**
     * Additional attributes
     */
    private Map<String, Object> attrs;

    private Double ttlsls = 0.0d;

    private Double ttlcmmssn = 0.0d;

    private List<Sale> sls = new ArrayList<Sale>();

    public void addAttribute(String key, Object value) {
        synchronized (this) {
            if (attrs == null) {
                attrs = new HashMap<String, Object>();
            }
        }

        attrs.put(key, value);
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public Date getCdt() {
        return cdt;
    }

    public void setCdt(Date cdt) {
        this.cdt = cdt;
    }

    public Date getLdt() {
        return ldt;
    }

    public void setLdt(Date ldt) {
        this.ldt = ldt;
    }

    public String getRl() {
        return rl;
    }

    public void setRl(String rl) {
        this.rl = rl;
    }

    public RedirectType getTp() {
        return tp;
    }

    public void setTp(RedirectType tp) {
        this.tp = tp;
    }

    public Date getRdrctdt() {
        return rdrctdt;
    }

    public void setRdrctdt(Date rdrctdt) {
        this.rdrctdt = rdrctdt;
    }

    public Map<String, Object> getAttrs() {
        return attrs;
    }

    public void setAttrs(Map<String, Object> attrs) {
        this.attrs = attrs;
    }

    public ObjectId getVstd() {
        return vstd;
    }

    public void setVstd(ObjectId vstd) {
        this.vstd = vstd;
    }

    public Double getTtlsls() {
        return ttlsls;
    }

    public void setTtlsls(Double ttlSls) {
        this.ttlsls = ttlsls;
    }

    public Double getTtlcmmssn() {
        return ttlcmmssn;
    }

    public void setTtlcmmssn(Double ttlcmmssn) {
        this.ttlcmmssn = ttlcmmssn;
    }

    public List<Sale> getSls() {
        return sls;
    }

    public void setSls(List<Sale> sls) {
        this.sls = sls;
    }
}
