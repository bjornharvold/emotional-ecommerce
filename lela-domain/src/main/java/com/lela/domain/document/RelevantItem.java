/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.domain.document;

//~--- non-JDK imports --------------------------------------------------------

import java.util.List;
import java.util.Map;

//~--- JDK imports ------------------------------------------------------------

//~--- classes ----------------------------------------------------------------

/**
 * Created by Bjorn Harvold
 * Date: 7/14/11
 * Time: 11:29 PM
 * Responsibility:
 */
public class RelevantItem extends AbstractItem {

    /** Motivator Relevancy */
    private Map<String, Integer> mtvtrrlvncy;

    /** Total Relevancy */
    private Integer ttlrlvncy;

    /** Total Relevancy number */
    private Integer ttlrlvncynmbr;

    //~--- constructors -------------------------------------------------------

    public RelevantItem() {
    }

    public RelevantItem(AbstractItem item, Integer totalRelevancy, Map<String, Integer> motivatorRelevancy) {
        super(item);
        this.ttlrlvncy   = totalRelevancy;
        this.mtvtrrlvncy = motivatorRelevancy;
    }

    public RelevantItem(AbstractItem item, Integer totalRelevancy, Integer totalRelevancyNumber, Map<String, Integer> motivatorRelevancy) {
        super(item);
        this.ttlrlvncy   = totalRelevancy;
        this.mtvtrrlvncy = motivatorRelevancy;
        this.ttlrlvncynmbr = totalRelevancyNumber;
    }

    //~--- get methods --------------------------------------------------------

    /**
     * Method description
     *
     *
     * @return Return value
     */
    public Map<String, Integer> getMtvtrrlvncy() {
        return mtvtrrlvncy;
    }

    /**
     * Method description
     *
     *
     * @return Return value
     */
    public Integer getTtlrlvncy() {
        return ttlrlvncy;
    }

    /**
     * Method description
     *
     *
     * @return Return value
     */
    public Integer getTtlrlvncynmbr() {
        return ttlrlvncynmbr;
    }

    //~--- set methods --------------------------------------------------------

    /**
     * Method description
     *
     *
     * @param ttlrlvncynmbr ttlrlvncynmbr
     */
    public void setTtlrlvncynmbr(Integer ttlrlvncynmbr) {
        this.ttlrlvncynmbr = ttlrlvncynmbr;
    }
}
