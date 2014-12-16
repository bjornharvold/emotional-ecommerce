/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.domain.document;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: Chris Tallent
 * Date: 1/11/12
 * Time: 2:41 PM
 */
public class AnalyticAttribute implements Serializable {
    private static final long serialVersionUID = 317244102708576125L;

    //~--- fields -------------------------------------------------------------

    /** Key */
    private String ky;

    /** Value */
    private Object vl;

    //~--- constructors -------------------------------------------------------

    /**
     * Constructs ...
     *
     */
    public AnalyticAttribute() {}

    /**
     * Constructs ...
     *
     *
     * @param key key
     * @param val val
     */
    public AnalyticAttribute(String key, Object val) {
        this.ky = key;
        this.vl = val;
    }

    //~--- get methods --------------------------------------------------------

    /**
     * Method description
     *
     *
     * @return Return value
     */
    public String getKy() {
        return ky;
    }

    /**
     * Method description
     *
     *
     * @return Return value
     */
    public Object getVl() {
        return vl;
    }

    //~--- set methods --------------------------------------------------------

    /**
     * Method description
     *
     *
     * @param ky key
     */
    public void setKy(String ky) {
        this.ky = ky;
    }

    /**
     * Method description
     *
     *
     * @param vl val
     */
    public void setVl(Object vl) {
        this.vl = vl;
    }
}
