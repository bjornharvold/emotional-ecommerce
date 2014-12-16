/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.domain.document;

//~--- JDK imports ------------------------------------------------------------

import java.io.Serializable;

//~--- classes ----------------------------------------------------------------

/**
 * Created by Bjorn Harvold
 * Date: 8/18/11
 * Time: 3:34 PM
 * Responsibility:
 */
public class Merchant implements Serializable {

    /** Field description */
    private static final long serialVersionUID = -3014705963156919994L;

    //~--- fields -------------------------------------------------------------

    /** Amazon product ID */
    private String asin;

    /** Product id */
    private String prdd;

    //~--- get methods --------------------------------------------------------

    /**
     * Method description
     *
     *
     * @return Return value
     */
    public String getAsin() {
        return asin;
    }

    /**
     * Method description
     *
     *
     * @return Return value
     */
    public String getPrdd() {
        return prdd;
    }

    //~--- set methods --------------------------------------------------------

    /**
     * Method description
     *
     *
     * @param asin asin
     */
    public void setAsin(String asin) {
        this.asin = asin;
    }

    /**
     * Method description
     *
     *
     * @param prdd prdd
     */
    public void setPrdd(String prdd) {
        this.prdd = prdd;
    }
}
