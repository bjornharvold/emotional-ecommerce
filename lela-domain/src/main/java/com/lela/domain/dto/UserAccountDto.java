/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.domain.dto;

//~--- non-JDK imports --------------------------------------------------------

import com.lela.domain.document.UserSupplement;

import java.util.Date;

//~--- classes ----------------------------------------------------------------

public class UserAccountDto {

    /** Email */
    private String ml;

    /** First name */
    private String fnm;

    /** Last name */
    private String lnm;

    /** Password */
    private String psswrd;

    /** Confirm Password */
    private String cnfrmpsswrd;

    /** Friend level number */
    private Integer frndlvlnmbr;

    /** Registration date */
    private Date cdt;
    
    /**
     * Does this user want newsletter subscription?
     */
    private Boolean optin = false;

    public UserAccountDto() {

    }

    public UserAccountDto(UserSupplement us) {
        this.ml = us.getMl();
        this.fnm = us.getFnm();
        this.lnm = us.getLnm();
        this.frndlvlnmbr = us.getFrndlvlnmbr();
        this.cdt = us.getCdt();
        this.optin = us.getPtn();
    }

    public String getMl() {
        return ml;
    }

    public void setMl(String ml) {
        this.ml = ml;
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

    public String getPsswrd() {
        return psswrd;
    }

    public void setPsswrd(String psswrd) {
        this.psswrd = psswrd;
    }

    public String getCnfrmpsswrd() {
        return cnfrmpsswrd;
    }

    public void setCnfrmpsswrd(String cnfrmpsswrd) {
        this.cnfrmpsswrd = cnfrmpsswrd;
    }

    public Integer getFrndlvlnmbr() {
        return frndlvlnmbr;
    }

    public void setFrndlvlnmbr(Integer frndlvlnmbr) {
        this.frndlvlnmbr = frndlvlnmbr;
    }

    public Date getCdt() {
        return cdt;
    }

    public void setCdt(Date cdt) {
        this.cdt = cdt;
    }

	public Boolean getOptin() {
		return optin;
	}

	public void setOptin(Boolean optin) {
		this.optin = optin;
	}
}
