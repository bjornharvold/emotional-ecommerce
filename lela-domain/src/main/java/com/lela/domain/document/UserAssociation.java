/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.domain.document;

//~--- JDK imports ------------------------------------------------------------

import com.lela.domain.enums.AssociationType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//~--- classes ----------------------------------------------------------------

/**
 * Created by Chris Tallent
 * Date: 10/24/11
 * Time: 2:45 PM
 * Responsibility:
 */
public class UserAssociation implements Serializable {
    private static final long serialVersionUID = -4792594479110483190L;

    //~--- fields -------------------------------------------------------------

    private String nm;
    private String rl;
    private AssociationType tp;
    private Date cdt;

    private List<UserAssociationAttribute> attrs = new ArrayList<UserAssociationAttribute>();

    //~--- constructors -------------------------------------------------------

    /**
     * Constructs ...
     *
     */
    public UserAssociation() {}

    /**
     * Constructs ...
     *
     * @param nm
     * @param rl
     * @param tp
     */
    public UserAssociation(String nm, String rl, AssociationType tp) {
        this.nm = nm;
        this.rl = rl;
        this.tp = tp;
    }

    /**
     * Constructs ...
     *
     *
     * @param attrs Association attributes
     */
    public UserAssociation(String nm, String rl, AssociationType tp, List<UserAssociationAttribute> attrs) {
        this.nm = nm;
        this.rl = rl;
        this.tp = tp;
        this.attrs = attrs;
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

    public AssociationType getTp() {
        return tp;
    }

    public void setTp(AssociationType tp) {
        this.tp = tp;
    }

    public List<UserAssociationAttribute> getAttrs() {
        return attrs;
    }

    public void setAttrs(List<UserAssociationAttribute> attrs) {
        this.attrs = attrs;
    }

    public Date getCdt() {
        return cdt;
    }

    public void setCdt(Date cdt) {
        this.cdt = cdt;
    }
}
