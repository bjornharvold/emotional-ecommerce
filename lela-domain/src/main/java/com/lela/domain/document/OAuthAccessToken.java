/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.domain.document;

//~--- non-JDK imports --------------------------------------------------------

import com.lela.domain.enums.OauthAccessTokenType;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

//~--- JDK imports ------------------------------------------------------------

//~--- classes ----------------------------------------------------------------

/**
 * Created by Bjorn Harvold
 * Date: 9/28/11
 * Time: 9:55 AM
 * Responsibility:
 */
@Document
public class OAuthAccessToken extends AbstractDocument implements Serializable {

    /** Field description */
    private static final long serialVersionUID = -7193618689840746472L;

    //~--- fields -------------------------------------------------------------

    /** Refresh token */
    private String rfrshtkn;

    /** Authentication */
    private byte[] thntctn;

    /** Token */
    private byte[] tkn;

    /** Token ID */
    private String tknd;

    /** Type */
    private OauthAccessTokenType tp;

    //~--- get methods --------------------------------------------------------

    /**
     * Method description
     *
     *
     * @return Return value
     */
    public String getRfrshtkn() {
        return rfrshtkn;
    }

    /**
     * Method description
     *
     *
     * @return Return value
     */
    public byte[] getThntctn() {
        return thntctn;
    }

    /**
     * Method description
     *
     *
     * @return Return value
     */
    public byte[] getTkn() {
        return tkn;
    }

    /**
     * Method description
     *
     *
     * @return Return value
     */
    public String getTknd() {
        return tknd;
    }

    /**
     * Method description
     *
     *
     * @return Return value
     */
    public OauthAccessTokenType getTp() {
        return tp;
    }

    //~--- set methods --------------------------------------------------------

    /**
     * Method description
     *
     *
     * @param rfrshtkn rfrshtkn
     */
    public void setRfrshtkn(String rfrshtkn) {
        this.rfrshtkn = rfrshtkn;
    }

    /**
     * Method description
     *
     *
     * @param thntctn thntctn
     */
    public void setThntctn(byte[] thntctn) {
        this.thntctn = thntctn;
    }

    /**
     * Method description
     *
     *
     * @param tkn tkn
     */
    public void setTkn(byte[] tkn) {
        this.tkn = tkn;
    }

    /**
     * Method description
     *
     *
     * @param tknd tknd
     */
    public void setTknd(String tknd) {
        this.tknd = tknd;
    }

    /**
     * Method description
     *
     *
     * @param tp tp
     */
    public void setTp(OauthAccessTokenType tp) {
        this.tp = tp;
    }
}
