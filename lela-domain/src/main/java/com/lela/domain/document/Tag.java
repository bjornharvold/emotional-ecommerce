/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.domain.document;

//~--- JDK imports ------------------------------------------------------------

import java.io.Serializable;

//~--- classes ----------------------------------------------------------------

/**
 * Created by Chris Tallent
 * Date: 5/7/2012
 * Time: 3:34 PM
 * Responsibility:
 */
public class Tag implements Serializable {
    private static final long serialVersionUID = 7477310114741063965L;

    public String tg;

    public String getTg() {
        return tg;
    }

    public void setTg(String tg) {
        this.tg = tg;
    }
}
