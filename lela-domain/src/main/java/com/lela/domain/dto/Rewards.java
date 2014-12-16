/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.domain.dto;

import com.lela.domain.document.Reward;
import com.lela.domain.document.UserSupplement;

import java.util.Date;
import java.util.List;

public class Rewards {

    /** Friend level number */
    private Integer frndlvlnmbr;

    /** Rewards */
    private List<Reward> rwrds;

    /** Registration date */
    private Date cdt;

    public Rewards(Integer frndlvlnmbr, List<Reward> rwrds, Date cdt) {
        this.frndlvlnmbr = frndlvlnmbr;
        this.rwrds = rwrds;
        this.cdt = cdt;
    }

    public Rewards(UserSupplement us) {
        this.frndlvlnmbr = us.getFrndlvlnmbr();
        this.rwrds = us.getRwrds();
        this.cdt = us.getCdt();
    }

    public Rewards() {
    }

    public Integer getFrndlvlnmbr() {
        return frndlvlnmbr;
    }

    public void setFrndlvlnmbr(Integer frndlvlnmbr) {
        this.frndlvlnmbr = frndlvlnmbr;
    }

    public List<Reward> getRwrds() {
        return rwrds;
    }

    public void setRwrds(List<Reward> rwrds) {
        this.rwrds = rwrds;
    }

    public Date getCdt() {
        return cdt;
    }

    public void setCdt(Date cdt) {
        this.cdt = cdt;
    }
}
