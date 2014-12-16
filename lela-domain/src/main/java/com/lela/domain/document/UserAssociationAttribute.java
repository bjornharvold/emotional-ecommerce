/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.domain.document;

//~--- JDK imports ------------------------------------------------------------

import org.springframework.data.mongodb.core.index.Indexed;

import java.io.Serializable;

//~--- classes ----------------------------------------------------------------

/**
 * Created by Chris Tallent
 * Date: 10/24/11
 * Time: 2:45 PM
 * Responsibility:
 */
public class UserAssociationAttribute implements Serializable {
    private static final long serialVersionUID = -5666070443500807844L;

    //~--- fields -------------------------------------------------------------

    /** Key */
    @Indexed
    private String ky;

    /** Value */
    private Object vl;

    //~--- constructors -------------------------------------------------------

    /**
     * Constructs ...
     *
     */
    public UserAssociationAttribute() {}

    /**
     * Constructs ...
     *
     *
     * @param key key
     * @param val val
     */
    public UserAssociationAttribute(String key, Object val) {
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
