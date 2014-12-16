/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.domain.dto;

//~--- non-JDK imports --------------------------------------------------------

import com.lela.domain.document.User;

//~--- classes ----------------------------------------------------------------

/**
 * Created by Bjorn Harvold
 * Date: 9/7/11
 * Time: 10:37 AM
 * Responsibility:
 */
public class RelevantItemQuery {

    /** Field description */
    private String urlName;

    /** Field description */
    private String userCode;

    //~--- constructors -------------------------------------------------------

    /**
     * Constructs ...
     *
     *
     *
     * @param urlName urlName
     * @param userCode userCode
     */
    public RelevantItemQuery(String urlName, String userCode) {
        this.urlName         = urlName;
        this.userCode            = userCode;
    }

    //~--- get methods --------------------------------------------------------

    /**
     * Method description
     *
     *
     * @return Return value
     */
    public String getUrlName() {
        return urlName;
    }

    //~--- set methods --------------------------------------------------------

    /**
     * Method description
     *
     *
     * @param urlName urlName
     */
    public void setUrlName(String urlName) {
        this.urlName = urlName;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }
}
