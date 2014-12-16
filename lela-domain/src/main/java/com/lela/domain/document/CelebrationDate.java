/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.domain.document;

import com.lela.domain.enums.CelebrationType;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Martin Gamboa
 * Date: 11/2/11
 * Time: 7:43 AM
 * To change this template use File | Settings | File Templates.
 */
public class CelebrationDate  implements Serializable {

    private static final long serialVersionUID = 5502371121954549164L;

    private CelebrationType tp;

    private Date dt;

    public CelebrationDate() {
    }

    public CelebrationDate(CelebrationType tp, Date dt) {
        this.tp = tp;
        this.dt = dt;
    }

    public CelebrationType getTp() {
        return tp;
    }

    public void setTp(CelebrationType tp) {
        this.tp = tp;
    }

    public Date getDt() {
        return dt;
    }

    public void setDt(Date dt) {
        this.dt = dt;
    }
}
