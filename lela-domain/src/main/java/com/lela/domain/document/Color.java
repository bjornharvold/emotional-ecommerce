/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.domain.document;

//~--- JDK imports ------------------------------------------------------------

import java.io.Serializable;
import java.util.Map;

//~--- classes ----------------------------------------------------------------

/**
 * Created by Bjorn Harvold
 * Date: 8/18/11
 * Time: 3:34 PM
 * Responsibility:
 */
public class Color extends ProductImage implements Serializable {

    /** Field description */

    /** Primary color name */
    private String prmryNm;

    /** url name */
    private String rlnm;

    /** SEO rlnm */
    private String srlnm;

    /**
     *
     * @return primary color name
     */
    public String getPrmryNm() {
        return prmryNm;
    }

    /**
     *
     * @param prmryNm color name
     */
    public void setPrmryNm(String prmryNm) {
        this.prmryNm = prmryNm;
    }

    /**
     *
     * @return urlname of product
     */
    public String getRlnm() {
        return rlnm;
    }

    /**
     *
     * @param rlnm urlname of product
     */
    public void setRlnm(String rlnm) {
        this.rlnm = rlnm;
    }

    public String getSrlnm() {
        return srlnm;
    }

    public void setSrlnm(String srlnm) {
        this.srlnm = srlnm;
    }
}
