/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.domain.dto;

//~--- non-JDK imports --------------------------------------------------------

import com.lela.domain.document.Motivator;
import com.lela.domain.document.User;

//~--- JDK imports ------------------------------------------------------------

//~--- classes ----------------------------------------------------------------

/**
 * Created by Bjorn Harvold
 * Date: 8/8/11
 * Time: 11:44 AM
 * Responsibility: Collects all information necessary to retrieve relevant items
 */
public final class RelevantOwnerItemsQuery {

    /** Field description */
    private final String ownerName;

    /** Field description */
    private final String userCode;
    private final Motivator motivator;
    private final Integer page;
    private final Integer size;

    //~--- constructors -------------------------------------------------------

    /**
     * Constructs ...
     *
     *
     * @param ownerName ownerName
     * @param userCode userCode
     * @param motivator motivator
     */
    public RelevantOwnerItemsQuery(String ownerName, String userCode, Motivator motivator, Integer page, Integer size) {
        this.ownerName  = ownerName;
        this.userCode       = userCode;
        this.page = page;
        this.size = size;
        this.motivator = motivator;
    }

    //~--- get methods --------------------------------------------------------

    /**
     * Method description
     *
     *
     * @return Return value
     */
    public String getOwnerName() {
        return ownerName;
    }

    /**
     * Method description
     *
     *
     * @return Return value
     */
    public String getUserCode() {
        return userCode;
    }

    public Integer getPage() {
        return page;
    }

    public Integer getSize() {
        return size;
    }

    public Motivator getMotivator() {
        return motivator;
    }
}
